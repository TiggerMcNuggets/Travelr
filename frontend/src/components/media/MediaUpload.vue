<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Upload Media</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <div id="dropzone">Drag photos or click to open file explorer.</div>
      <v-flex mt-3>
        <MediaGridWithDelete :media="files" :deletePhoto="deleteImage"/>
      </v-flex>
    </v-card-text>

    <div class="album-select" v-if="!activeAlbumMetadata && !isDestination">
      <v-divider></v-divider>

      <v-card-title primary-title>
        <h3 class="media">Select Albums</h3>
      </v-card-title>

      <div>
        <v-layout wrap>
          <v-flex class="album-element" xs12 md6>
            <v-select
              :items="albumNames"
              label="Select"
              v-model="selectedAlbumNames"
              attach
              multiple
              chips
            ></v-select>
          </v-flex>

          <v-flex xs12 md4 offset-md2>
            <div v-if="isDestination">
            </div>
            <div v-else>
              <v-btn v-model="isDestination" @click="openCreateAlbumDialog">Create new album</v-btn>
            </div>
          </v-flex>
        </v-layout>
      </div>
    </div>

    <div class="selected-albums">
      <v-layout row wrap>
        <v-flex v-for="item in selectedAlbums" v-bind:key="item.id" xs12 sm6 class="album-element">
          <AlbumElement
            v-if="item.content"
            :album="item"
            disableTitle
            :getImgFromUrl="getImgFromUrl"
            :openElement="() => {}"
          ></AlbumElement>
        </v-flex>
      </v-layout>
    </div>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat v-on:click="closeUploadDialog()">Cancel</v-btn>
      <v-btn
        ma-3
        color="primary"
        flat
        v-on:click="uploadMediaAndClearMediaBox(rawFiles, selectedAlbums)"
      >Upload Media</v-btn>
    </v-card-actions>

    <v-dialog v-model="createAlbumDialogActive" width="800">
      <AlbumCreate
        :createNewAlbum="createNewAlbum"
        :closeCreateAlbumDialog="closeCreateAlbumDialog"
      />
    </v-dialog>
  </v-card>
</template>


<style>
.album-select {
  width: 100%;
  padding: 0 0 10px 20px;
}

.selected-albums {
  width: 100%;
  padding: 0 20px 10px 20px;
}

.album-element {
  padding: 0 10px;
}

#dropzone {
  background: white;
  border-radius: 5px;
  border: 2px dashed rgb(0, 135, 247);
  border-image: none;
  padding: 50px;
}

#dropzone.dragover {
  background: rgba(0, 135, 247, 0.4);
}
</style>


<script>
import MediaGridWithDelete from "../media/MediaGridWIthDelete";
import AlbumElement from "./AlbumElement";
import AlbumCreate from "./AlbumCreate";
import base_url from "../../repository/BaseUrl";

export default {
  components: {
    MediaGridWithDelete,
    AlbumElement,
    AlbumCreate
  },

  props: {
    uploadMedia: Function,
    openUploadDialog: Function,
    closeUploadDialog: Function,
    openCreateAlbumDialog: Function,
    closeCreateAlbumDialog: Function,
    allAlbums: Array,
    createNewAlbum: Function,
    toggleCreateAlbumDialog: Function,
    createAlbumDialogActive: Boolean,
    activeAlbumMetadata: Object,
    isDestination: {
      default: false,
      type: Boolean,
    },
  },

  // local variables
  data() {
    return {
      files: [],
      rawFiles: [],
      selectedAlbumNames: []
    };
  },

  methods: {
    /**
     * Gets the image src url for the server
     * @param filename The photo item to get the image for.
     * @returns {string} A url of where to find the photo to set as src
     */
    getImgFromUrl(filename) {
      return base_url + `/api/users/${this.$route.params.id}/media/` + filename;
    },

    /**
     * Deselects the image and removes from the list of selected images.
     * @param selectedImage The image to remove from the selected images.
     */
    deleteImage(selectedImage) {
      for (let i = 0; i <= this.files.length; i++) {
        if (selectedImage === this.files[i]) {
          this.files.splice(i, 1);
          this.rawFiles.splice(i, 1);
        }
      }
    },

    triggerCallback(e, callback) {
      let files;
      if (e.dataTransfer) {
        files = e.dataTransfer.files;
      } else if (e.target) {
        files = e.target.files;
      }
      callback.call(null, files);
    },

    makeDroppable(ele, callback) {
      let input = document.createElement("input");
      input.setAttribute("type", "file");
      input.setAttribute("multiple", true);
      input.style.display = "none";
      input.addEventListener("change", e => {
        this.triggerCallback(e, callback);
      });
      ele.appendChild(input);

      ele.addEventListener("dragover", function(e) {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.add("dragover");
      });

      ele.addEventListener("dragleave", function(e) {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.remove("dragover");
      });

      ele.addEventListener("drop", e => {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.remove("dragover");
        this.triggerCallback(e, callback);
      });

      ele.addEventListener("click", function() {
        input.value = null;
        input.click();
      });
    },

    /**
     * Uploads media to the selected albums if viewing the media page.
     * Uploads media to the currently active album if viewing an individual album.
     */
    uploadMediaAndClearMediaBox(rawFiles, selectedAlbums) {
      if (this.activeAlbumMetadata) {
        this.uploadMedia(rawFiles, [this.activeAlbumMetadata]);
      } else {
        this.uploadMedia(rawFiles, selectedAlbums);
      }
      this.rawFiles = [];
      this.files = [];
      this.selectedAlbumNames = [];
    }
  },

  mounted: function() {
    let dropzone = document.getElementById("dropzone");

    this.makeDroppable(dropzone, files => {
      this.rawFiles = Array.from(files);
      for (let i = 0; i < files.length; i++) {
        let file = files[i];
        let reader = new FileReader();

        reader.onload = e => {
          this.files.push(e.target.result);
        };
        reader.readAsDataURL(file);
      }
    });
  },

  /**
   * Initialises the component with the`image data.
   */
  created: function() {
    this.files = []; // This should actually be done when upload is clicked!
  },

  computed: {
    /**
     * Retrieves the names of all albums
     */
    albumNames() {
      let albumNames = [];
      if (!this.isDestination) {
      for (let i = 0; i < this.allAlbums.length; i++) {
        if (!this.allAlbums[i].isPermanent && (this.allAlbums[i].name.toLowerCase() !== ('all'))
        ) {
          albumNames.push(this.allAlbums[i].name);
          }
        }
      }
      return albumNames;
    },

    /**
     * computes list of all albums that have been selected
     */
    selectedAlbums() {
      let selectedAlbums = [];
      if (!this.isDestination) {
        for (let i = 0; i < this.allAlbums.length; i++) {
          if (
            this.selectedAlbumNames.includes(this.allAlbums[i].name) &&
            !this.allAlbums[i].isPermanant
          ) {
            selectedAlbums.push(this.allAlbums[i]);
          }
        }
      }
      return selectedAlbums;
    }
  }
};
</script>
