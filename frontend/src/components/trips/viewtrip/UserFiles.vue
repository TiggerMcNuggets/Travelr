<template>
  <div>
    <h2>User Files</h2>
    <v-container grid-list-md class="no-padding">
      <v-layout row wrap>
        <v-flex
          v-for="file in files" 
          :key="file.id" 
          xs6 sm4 md3 lg4
        >
          <UserFile 
            :file="file"
            :getFile="getFile"
            :hasWritePermissions="hasWritePermissions"
          />
        </v-flex>
      </v-layout>
    </v-container>
  </div>
</template>

<style>
</style>

<script>
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  import { download } from "../trips_destinations_util";
  import UserFile from "./UserFile";
  
  let fileRepository = RepositoryFactory.get("file");

  export default {
    name: "UserFiles",

    props: {
      hasWritePermissions: Boolean
    },

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
        const res = await fileRepository.getFile(this.userId, file.id);
        download(res, file.name);
      }
    },

    mounted() {
      this.getFiles();
    }
  };
</script>
