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
                 @click="closeErrorMessage()"
                 class="input-file">
          <p>
            Drag your file(s) here to begin<br> or click to browse
          </p>
        </div>
      </form>
      <v-layout row wrap>
        <v-flex
          v-for="(file, index) in fileList"
          :key="file.id"
          xs4
        >
          <div class="file-element">
            <v-img src="http://images.clipartpanda.com/file-clipart-xigKMeMjT.png" height="100px" :contain="true">
            </v-img>
            <v-layout pa-1 column align-center>
              <span class="file-name">{{file.name}}</span>
              <v-card-actions>
                <v-layout>
                  <v-flex>
                    <v-tooltip bottom>
                      <template v-slot:activator="{ on }">
                        <v-btn v-on="on" @click="removeFile(index)" color="error" class="delete-button" flat fab small>
                          <v-icon dark>delete</v-icon>
                        </v-btn>
                      </template>
                      <span>Delete file</span>
                    </v-tooltip>
                  </v-flex>
                </v-layout>
              </v-card-actions>
            </v-layout>
          </div>
        </v-flex>
      </v-layout>
    </v-card-text>

    <v-divider></v-divider>

    <div>
      <v-alert
        v-model="fileToBig"
        type="error">
        File must be less than 12MB!
      </v-alert>
    </div>

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

  .file-name {
    text-overflow: ellipsis !important;
    white-space: nowrap;
    overflow: hidden;
  }

  .file-element {
    padding-top: 20px;
  }

</style>


<script>
  import FileDisplayer from "./FileDisplayer"

  import {RepositoryFactory} from "../../repository/RepositoryFactory";

  let fileRepository = RepositoryFactory.get("file");

  export default {

    name: "FileUpload",

    components: {
      FileDisplayer
    },

    props: {
      closeUploadDialog: Function,
      userId: String,
      tripId: String,
      getFiles: Function
    },

    // local variables
    data() {
      return {
        uploadedFiles: [],
        uploadError: null,
        currentStatus: null,
        uploadFieldName: 'files',
        fileList: [],
        fileToBig: false
      };
    },


    methods: {

      /**
       * removes a file from the list of files user wants to upload
       */
      removeFile(index) {
        this.fileList.splice(index,1);
      },

      /**
       * closes error message for files that are too large
       */
      closeErrorMessage() {
        this.fileToBig = false;
      },

      /**
       * updates list of files we want to add to trip
       */
      filesChange(fileList) {
        if (!fileList.length) return;


        // append the files to list
        Array
          .from(Array(fileList.length).keys())
          .map(x => {

            // check is file less than 12MB
            if (fileList[x].size > 12000000) {
              this.fileToBig = true;
              fileList.splice(x);
            } else {
              this.fileList.push(fileList[x]);
            }

          });

      },

      /**
       * uploads files to trip
       */
      uploadFilesToTrip() {

        const formData = new FormData();
        // append the files to FormData
        Array
          .from(Array(this.fileList.length).keys())
          .map(x => {
            formData.append(this.uploadFieldName, this.fileList[x], this.fileList[x].name);
          });

        fileRepository
          .uploadFiles(Number(this.userId), Number(this.tripId), formData)
          .then(() => {
            this.fileList = [];
            this.getFiles();
            this.closeUploadDialog();
          })
          .catch(error => {
            console.log(error);
          });

      }
    },
  };
</script>
