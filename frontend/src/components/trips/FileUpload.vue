<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Upload File</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <form enctype="multipart/form-data">
        <div class="dropbox">
          <input type="file" multiple :name="uploadFieldName"
                 @change="filesChange($event.target.files)"
                 class="input-file">
          <p>
            Drag your file(s) here to begin<br> or click to browse
          </p>
        </div>
      </form>
      <!--<v-flex mt-3>-->
      <!--<FileDisplayer :files="files"/>-->
      <!--</v-flex>-->
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat v-on:click="closeUploadDialog()">Cancel</v-btn>
      <v-btn
        ma-3
        color="primary"
        flat
        v-on:click="uploadFilesToTrip()"
      >Upload Files
      </v-btn>
    </v-card-actions>
  </v-card>
</template>


<style lang="scss">

  .dropbox {
    background: white;
    border-radius: 5px;
    border: 2px dashed rgb(0, 135, 247);
    border-image: none;
    height: 180px
  }

  .input-file {
    opacity: 0; /* invisible */
    width: 96%;
    height: 180px;
    position: absolute;
    cursor: pointer;
  }

  .dropbox:hover {
    background: lightblue; /* when mouse over to the drop zone, change color */
  }

  .dropbox p {
    font-size: 1.2em;
    text-align: center;
    padding: 50px 0;
  }

</style>


<script>
  import FileDisplayer from "./FileDisplayer"

  import {RepositoryFactory} from "../../repository/RepositoryFactory";

  let fileRepository = RepositoryFactory.get("file");

  export default {

    components: {
      FileDisplayer
    },

    props: {
      openUploadDialog: Function,
      closeUploadDialog: Function,
      userId: String,
      tripId: String
    },

    // local variables
    data() {
      return {
        uploadedFiles: [],
        uploadError: null,
        currentStatus: null,
        uploadFieldName: 'files',
        filesFormData: null,
        fileNames: [],
        fileList: []
      };
    },


    methods: {
      reset() {
        // reset form to initial state
        this.currentStatus = STATUS_INITIAL;
        this.uploadedFiles = [];
        this.uploadError = null;
      },

      filesChange(fileList) {

        if (!fileList.length) return;

        // append the files to list
        Array
          .from(Array(fileList.length).keys())
          .map(x => {
            this.fileList.push(fileList[x]);
          });

      },

      uploadFilesToTrip() {

        const formData = new FormData();
        // append the files to FormData
        Array
          .from(Array(this.fileList.length).keys())
          .map(x => {
            console.log(x);
            console.log(this.fileList[x].name);
            formData.append(this.uploadFieldName, this.fileList[x], this.fileList[x].name);
          });

        fileRepository
          .uploadFiles(Number(this.userId), Number(this.tripId), formData)
          .then(() => {
            console.log("successfully uploaded files");
          })
          .catch(error => {
            console.log(error);
          });

      }
    },
    mounted() {
      this.reset();
    },
  };
</script>
