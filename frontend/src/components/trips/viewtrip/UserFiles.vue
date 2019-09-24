<template>
  <div>
    <h2>User Files</h2>
    <UserFile />
  </div>
</template>

<style>
</style>

<script>
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  import UserFile from "./UserFile";
  
  let tripRepository = RepositoryFactory.get("trip");

  export default {
    name: "UserFiles",

    props: {},

    components: {
      UserFile
    },

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
