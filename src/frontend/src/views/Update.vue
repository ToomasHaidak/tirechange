<template>
  <div class="updateData">
    <h1>Muuda töökoja andmeid</h1>
      <div id="dropdownlist" style="margin:50px auto 0; width:250px;">
        <br>
        <ejs-dropdownlist :dataSource="workshopList" :fields="localField" placeholder="Vali töökoda" v-model="workshop"></ejs-dropdownlist>
        <br>
        <input placeholder="Sisesta uus nimi" v-model="workshopData.workshopName"> <br>
        <input placeholder="Sisesta uus aadress" v-model="workshopData.workshopAddress"> <br>
        <Button v-on:click="updateWorkshopData">Sisesta</Button>
      </div>
  </div>
</template>

<style>
@import url(https://cdn.syncfusion.com/ej2/material.css);
</style>

<script>
export default {
  name: 'UpdateWorkshopData',
  data: function () {
    return {
      workshopData: {},
      workshopList: [],
      localField: {text: "Name"},
      workshop: ''
    }
  },
  methods: {
    getWorkshopData: function () {
      this.$http.get("/api/backend/getWorkshopData/" + this.workshop)
      .then(response => {
        this.workshopData = response.data
      }).catch(function (response) {
      })
    },

    updateWorkshopData: function () {
      this.$http.post("/api/backend/updateWorkshopData/" + this.workshop, this.workshopData)
      .then((response) => {
        alert("Andmed muudetud")
        document.location.reload(true)
      }).catch((error) => {
        alert("Ei saa uuendada. Puudub ühendus serveriga")
      })
    },
  },

  beforeMount() {
    this.$http.get("/api/backend/getWorkshopList")
        .then(response => {
          this.workshopList = response.data;
          this.workshopList.shift();
        }).catch((error) => {
          alert("Ei leia töökodasid. Puudub ühendus serveriga")
    })
  }
}
</script>