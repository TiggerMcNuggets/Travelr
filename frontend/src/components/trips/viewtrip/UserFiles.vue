<template>
  <v-container grid-list-md pt-3 pb-3 pl-0 pr-0>
    <h2 class="user-files">User Files</h2>
    <v-layout row wrap>
      <v-flex
        v-for="file in files" 
        :key="file.id" 
        xs4
      >
        <UserFile 
          :file="file"
          :getFile="getFile"
          :deleteFile="deleteFile"
          :hasWritePermissions="hasWritePermissions"
        />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  import { download } from "../trips_destinations_util";
  import UserFile from "./UserFile";
  
  let fileRepository = RepositoryFactory.get("file");

  export default {
    name: "UserFiles",

    props: {
      hasWritePermissions: Boolean,
      pushStack: Function,
      getFiles: Function,
      files: Array
    },

    components: {
      UserFile
    },

    data() {
      return {
        userId: this.$route.params.id,
        tripId: this.$route.params.trip_id
      };
    },

    methods: {


      /**
       * Downloades the file
       * @param file the file
       */
      async getFile(file) {
        const res = await fileRepository.getFile(this.userId, file.id);
        download(res, file.name);
      },

      /**
       * Deletes the file and updates the undo redo stack
       * @param file the file
       */
      async deleteFile(file) {
        let checkpoint = {
          action: async () => await this.deleteAndPopulate(file),
          reaction: async() => await this.deleteAndPopulate(file)
        };
        this.pushStack(checkpoint);
        this.deleteAndPopulate(file);
      },

      /**
       * Deletes the file and repopulates the component state
       * @param file the file
       */
      async deleteAndPopulate(file) {
        await fileRepository.deleteFile(this.userId, this.tripId, file.id);
        await this.getFiles();
      }
    },

    mounted() {
      this.getFiles();
    }
  };
</script>
