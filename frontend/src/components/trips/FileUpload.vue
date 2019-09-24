<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Upload File</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <form enctype="multipart/form-data" novalidate v-if="isInitial || isSaving">


        <div class="dropbox">
          <input type="file" multiple :name="uploadFieldName" :disabled="isSaving"
                 @change="filesChange($event.target.name, $event.target.files); fileCount = $event.target.files.length"
                 class="input-file">
          <p v-if="isInitial">
            Drag your file(s) here to begin<br> or click to browse
          </p>
          <p v-if="isSaving">
            Uploading {{ fileCount }} files...
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
      >Upload Files
      </v-btn>
    </v-card-actions>
  </v-card>
</template>


<style lang="scss">

  /*#dropzone {*/
    /*background: white;*/
    /*border-radius: 5px;*/
    /*border: 2px dashed rgb(0, 135, 247);*/
    /*border-image: none;*/
    /*padding: 50px;*/
  /*}*/

  /*#dropzone.dragover {*/
    /*background: rgba(0, 135, 247, 0.4);*/
  /*}*/
  .dropbox {
    background: white;
    border-radius: 5px;
    border: 2px dashed rgb(0, 135, 247);
    border-image: none;
    padding: 50px;
  }

  .input-file {
    opacity: 0; /* invisible but it's there! */
    width: 100%;
    height: 200px;
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

  const STATUS_INITIAL = 0, STATUS_SAVING = 1, STATUS_SUCCESS = 2, STATUS_FAILED = 3;

  export default {

    components: {
      FileDisplayer
    },

    props: {
      openUploadDialog: Function,
      closeUploadDialog: Function,
    },

    // local variables
    data() {
      return {
        uploadedFiles: [],
        uploadError: null,
        currentStatus: null,
        uploadFieldName: 'files',
        formDataExample: null,
        fileNames: []
      };
    },

    computed: {
      isInitial() {
        return this.currentStatus === STATUS_INITIAL;
      },
      isSaving() {
        return this.currentStatus === STATUS_SAVING;
      },
      isSuccess() {
        return this.currentStatus === STATUS_SUCCESS;
      },
      isFailed() {
        return this.currentStatus === STATUS_FAILED;
      }
    },

    // methods: {
    //
    //   /**
    //    * Deselects the image and removes from the list of selected images.
    //    * @param selectedImage The image to remove from the selected images.
    //    */
    //   deleteFile(selectedImage) {
    //     for (let i = 0; i <= this.files.length; i++) {
    //       if (selectedImage === this.files[i]) {
    //         this.files.splice(i, 1);
    //         this.rawFiles.splice(i, 1);
    //       }
    //     }
    //   },
    //
    //   triggerCallback(e, callback) {
    //     let files;
    //     if (e.dataTransfer) {
    //       files = e.dataTransfer.files;
    //     } else if (e.target) {
    //       files = e.target.files;
    //     }
    //     callback.call(null, files);
    //   },
    //
    //   makeDroppable(ele, callback) {
    //     let input = document.createElement("input");
    //     input.setAttribute("type", "file");
    //     input.setAttribute("multiple", true);
    //     input.style.display = "none";
    //     // input.accept = "image/*";
    //     input.addEventListener("change", e => {
    //       this.triggerCallback(e, callback);
    //     });
    //     ele.appendChild(input);
    //
    //     ele.addEventListener("dragover", function (e) {
    //       e.preventDefault();
    //       e.stopPropagation();
    //       ele.classList.add("dragover");
    //     });
    //
    //     ele.addEventListener("dragleave", function (e) {
    //       e.preventDefault();
    //       e.stopPropagation();
    //       ele.classList.remove("dragover");
    //     });
    //
    //     ele.addEventListener("drop", e => {
    //       e.preventDefault();
    //       e.stopPropagation();
    //       ele.classList.remove("dragover");
    //       this.triggerCallback(e, callback);
    //     });
    //
    //     ele.addEventListener("click", function () {
    //       input.value = null;
    //       input.click();
    //     });
    //   },
    //
    //   /**
    //    * Uploads media to the selected albums if viewing the media page.
    //    * Uploads media to the currently active album if viewing an individual album.
    //    */
    //   uploadFilesAndClearFileBox(rawFiles, selectedAlbums) {
    //     if (this.activeAlbumMetadata) {
    //       this.uploadMedia(rawFiles, [this.activeAlbumMetadata]);
    //     } else {
    //       this.uploadMedia(rawFiles, selectedAlbums);
    //     }
    //     this.rawFiles = [];
    //     this.files = [];
    //     this.selectedAlbumNames = [];
    //   }
    // },

    // mounted: function () {
    //   let dropzone = document.getElementById("dropzone");
    //
    //   this.makeDroppable(dropzone, files => {
    //     this.rawFiles = Array.from(files);
    //     for (let i = 0; i < files.length; i++) {
    //       let file = files[i];
    //       let reader = new FileReader();
    //
    //       reader.onload = e => {
    //         this.files.push(e.target.result);
    //       };
    //       reader.readAsDataURL(file);
    //     }
    //   });
    // },
    //
    // /**
    //  * Initialises the component with the`image data.
    //  */
    // created: function () {
    //   this.files = [];
    // },

    methods: {
      reset() {
        // reset form to initial state
        this.currentStatus = STATUS_INITIAL;
        this.uploadedFiles = [];
        this.uploadError = null;
      },
      // save(formData) {
      //   // upload data to the server
      //   this.currentStatus = STATUS_SAVING;
      //   this.uploadedFiles = [].concat(x);
      //   console.log("added! " + this.uploadedFiles);
      //
      //   upload(formData)
      //     .then(x => {
      //
      //       this.currentStatus = STATUS_SUCCESS;
      //     })
      //     .catch(err => {
      //       this.uploadError = err.response;
      //       this.currentStatus = STATUS_FAILED;
      //     });
      // },

      filesChange(fieldName, fileList) {

        // handle file changes
        const formData = new FormData();

        if (!fileList.length) return;

        // append the files to FormData
        Array
          .from(Array(fileList.length).keys())
          .map(x => {
            formData.append(fieldName, fileList[x], fileList[x].name);
            this.fileNames.push(fileList[x].name);
          });

        console.log("form data is " + formData);
        this.formDataExample = formData;

      }
    },
    mounted() {
      this.reset();
    },
  };
</script>
