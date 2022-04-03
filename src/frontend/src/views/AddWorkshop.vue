<template>
  <div class="addWorkshop">
    <h1>Lisa uus töökoda</h1>
    <input placeholder="Sisesta töökoja nimi" v-model="workshopData.workshopName"> <br>
    <input placeholder="Sisesta töökoja aadress" v-model="workshopData.workshopAddress"> <br>
    <Button v-on:click="addNewWorkshop">Sisesta</Button>
  </div>
</template>

<style>

</style>

<script>
export default {
  name: 'addWorkshop',
  data: function () {
    return {
      workshopData: {}
    }
  },
  methods: {
    addNewWorkshop: function () {
      this.$http.post("/api/backend/addNewWorkshop", this.workshopData)
      .then((response) => {
        alert("Uus töökoda on lisatud");
      }).catch((error) => {
        alert("Töökoda ei saa lisada. Puudub ühendus serveriga");
      })
    }
  }
}
</script>
