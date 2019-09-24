<template>
  <div>
    <h2>User Files</h2>
    <UserFile 
      v-for="file in files"
      :key="file.id"
      :file="file"
      @click.native="getFile(file)"
    />
  </div>
</template>

<style>
</style>

<script>
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  import UserFile from "./UserFile";
  
  let fileRepository = RepositoryFactory.get("file");

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
        const res = await fileRepository.getFilesForTrip(this.userId, this.tripId);
        this.files = res.data;
      },

      async getFile(file) {
        console.log("here");
        const res = await fileRepository.getFile(this.userId, file.id);
        this.download(res, file.name);
      },

      /**
       * Downloads the file returned by a http response
       */
      download(res, filename) {
        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", filename);
        document.body.appendChild(link);
        link.click();
      }
    },

    mounted() {
      this.getFiles();
    }
  };
</script>
