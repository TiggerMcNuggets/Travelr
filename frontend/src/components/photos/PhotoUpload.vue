

<template>
  <v-card>
    <div class="outer-container">
      <div class="inner-container">
        <v-card-title primary-title>
          <h2 class="headline">Upload Photo</h2>
        </v-card-title>
        <v-divider></v-divider>
        <div id="dropzone">Drag photos or click to open file explorer.</div>
        <v-flex  mt-3 >
        <MediaGrid :media="files" />
        </v-flex>
      </div>
    </div>
    <v-divider></v-divider>
    <v-btn color="primary" flat @click="closeDialogue">Close</v-btn>
    <v-btn color="primary" flat v-on:click="processSelected()">Add Photos to Destination</v-btn>
  </v-card>
</template>


<style>
#dropzone {
  background: white;
  border-radius: 5px;
  border: 2px dashed rgb(0, 135, 247);
  border-image: none;
  padding: 50px;
}
#dropzone.dragover {
  background: rgba(0, 135, 247, 1);
}


</style>


<script>
import { store } from "../../store/index";
import base_url from "../../repository/BaseUrl";
import { getImages } from "../../repository/PersonalPhotosRepository";
import MediaGrid from "../media/MediaGrid";

export default {
  store,

  components: {
    MediaGrid
  },

  // local variables
  data() {
    return {
      files: [],
      clickedImageURL: "",
      id: null,
      selectedImages: []
    };
  },

  // props methods being passed in from parent.
  props: ["closeDialogue", "setDestinationImages"],

  methods: {
    /**
     * Sets the user's profile photo as the selected
     */
    processSelected() {
      this.setDestinationImages(this.selectedImages);
    },

    /**
     * Gets the image url for the server to get the image.
     * @param item The photo item
     * @returns {string}
     */
    getImgUrl(item) {
      return base_url + "/api/travellers/photo/" + item.photo_filename;
    },

    //
    /**
     * Checks if the photo is selected that is it is present in the selected images list.
     * @param selectedImage The image that is been clicked to check
     * @returns {boolean} Whether the image is selected or not.
     */
    imageIsSelected(selectedImage) {
      for (let i = 0; i <= this.selectedImages.length; i++) {
        if (selectedImage === this.selectedImages[i]) {
          return true;
        }
      }
      return false;
    },

    /**
     * Deselects the image and removes from the list of selected images.
     * @param selectedImage The image to remove from the selected images.
     */
    unselectImage(selectedImage) {
      for (let i = 0; i <= this.selectedImages.length; i++) {
        if (selectedImage === this.selectedImages[i]) {
          this.selectedImages.splice(i, 1);
        }
      }
    },

    /**
     * Toggles if the image is selected and adding or removing from selected list.
     * @param selectedImage The image which was clicked.
     */
    selectImage(selectedImage) {
      this.imageIsSelected(selectedImage)
        ? this.unselectImage(selectedImage)
        : this.selectedImages.push(selectedImage);
    },

    /**
     * Groups the images into rows with four columns.
     * @param imageList The list of image data from the server.
     * @returns {Array} A list of rows each with four images.
     */
    groupImages(imageList) {
      let newImageList = [];
      let row = [];
      const num_cols = 4;
      for (let i = 0; i < imageList.length; i++) {
        if (i % num_cols === 0 && row.length !== 0) {
          newImageList.unshift(row);
          row = [];
        }
        row.push(imageList[i]);
      }

      newImageList.unshift(row);
      newImageList.reverse();
      return newImageList;
    },

    triggerCallback(e, callback) {
      var files;
      if (e.dataTransfer) {
        files = e.dataTransfer.files;
      } else if (e.target) {
        files = e.target.files;
      }
      callback.call(null, files);
    },

    makeDroppable(ele, callback) {
      var input = document.createElement("input");
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
    }
  },

  mounted: function() {
    var dropzone = document.getElementById("dropzone");

    this.makeDroppable(dropzone, files => {
      for (let i = 0; i < files.length; i++) {
        let file = files[i];

        var image = new Image();
        var reader = new FileReader();
        var vm = this;

        reader.onload = e => {
          this.files.push(e.target.result);
        };
        reader.readAsDataURL(file);
      }
    });
  },

  /**
   * Initialises the component with the image data.
   */
  created: function() {
    this.id = this.$route.params.id;

    this.files = [];
  }
};
</script>
