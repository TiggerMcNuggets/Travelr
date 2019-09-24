<template>
  <h2>User Files</h2>
</template>

<style>
</style>

<script>
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  
  let tripRepository = RepositoryFactory.get("trip");

  export default {
    name: "UserFiles",

    props: {},

    components: {},

    data() {
      return {
        userId: this.$route.params.id,
        tripId: this.$route.params.trip_id,
        files: []
      };
    },

    methods: {
      async getFiles() {
        const res = await tripRepository.getTripFiles(this.userId, this.tripId);
        this.files = res.data;
      }
    },

    mounted() {
      this.getFiles();
    }
  };
</script>
