<template>

  <div class="home">

      <div id="pickWorkshop" style="margin:1px auto 0; width:250px;">
        <br>
        <ejs-dropdownlist :dataSource="workshopList" placeholder="Vali töökoda" v-model="selectedWorkshop"></ejs-dropdownlist>
      </div>

      <div id="pickVehicleType" style="margin:1px auto 0; width:250px;">
        <br>
        <ejs-dropdownlist :dataSource="vehicleTypeList" placeholder="Vali sõiduki tüüp" v-model="selectedVehicleType"></ejs-dropdownlist>
      </div>

      <VueDatePicker v-model="dateFrom" placeholder="Vali algusaeg"></VueDatePicker>
      <VueDatePicker v-model="dateUntil" placeholder="Vali lõpu aeg"></VueDatePicker>

      <button v-on:click="getAvailableTimes">Otsi vabasid aegu</button>

      <v-table class="avaiableTimesTable" :data="availableTimesList" selectedClass="table-info" @selectionChanged="selectedRows = $event">
        <thead slot="head">
        <th>Töökoda</th>
        <th>Aadress</th>
        <th>Vaba aeg</th>
        <th>Teenindatavad sõidukid</th>
        </thead>
        <tbody slot="body" slot-scope="{displayData}">
        <v-tr v-for="row in displayData" :key="row.guid" :row="row">
            <td>{{ row.workshopName }}</td>
            <td>{{ row.workshopAdress }}</td>
            <td>{{ row.time }}</td>
            <td>{{ row.vehicleTypeServiced }}</td>
        </v-tr>
        </tbody>
      </v-table>

      <p>Valitud:</p>

      <div v-if="selectedRows.length === 0" class="text-muted"></div>
      <ul>
        <li v-for="selected in selectedRows">
          {{selected.workshopName}}{{"    "}}{{selected.time}}
        </li>
      </ul>
      <br>

      <button v-on:click="bookTime">Broneeri aeg</button>

  </div>

</template>

<style>
@import url(https://cdn.syncfusion.com/ej2/material.css);

.avaiableTimesTable {
  border: solid;
  margin: auto;
}

th, td {
  border: 1px solid;
}

p {
  margin-top: 10px;
}

button {
  margin-bottom: 30px;
}

</style>

<script>

export default {
  name: 'Register',
  data: function () {
    return {
      workshopList: [],
      vehicleTypeList: [],
      availableTimesList: [],
      localField: {text: "time"},
      selectedRows: [],
      selectedWorkshop: "",
      selectedVehicleType: "",
      waterMark : 'Select a Range',
      dateFrom: null,
      dateUntil: null
    }
  },
  methods: {
    getAvailableTimes: function () {
      if(this.selectedWorkshop && this.selectedVehicleType && this.dateFrom && this.dateUntil) {
        this.$http.get("/api/backend/getAvailableTimes/" + this.selectedWorkshop + "/" + this.selectedVehicleType + "/" + this.dateFrom + "/" + this.dateUntil)
            .then(response => {
              this.availableTimesList = response.data
              if(this.availableTimesList == "") {
                alert("Vabasid aegu ei ole")
              }
            }).catch(function (response) {
        })
      } else {
        alert("Vali töökoda, sõiduki tüüp ning algus ja lõpu aeg.")
      }
    },

    bookTime: function () {
      this.$http.post("/api/backend/bookTime", this.selectedRows[0])
          .then(response => {
          alert(response.data)
          }).catch(function(response) {
      })
    }
  },
  beforeMount() {
    this.$http.get("/api/backend/getWorkshopList")
        .then(response => {
          this.workshopList = response.data
        }).catch((error) => {
        alert("Ei leia töökodasid. Puudub ühendus serveriga")
    }),

        this.$http.get("/api/backend/getVehicleTypeList")
            .then(response => {
              this.vehicleTypeList = response.data
            }).catch(function(response) {

        })
  }
}
</script>
