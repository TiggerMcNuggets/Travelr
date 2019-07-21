

<template>
  <v-card>
    <div class="outer-container">
      <div class="inner-container">
        <v-card-title primary-title class="headline indigo white--text">
          <h5 class="font-weight-regular">Add Existing Photo</h5>
        </v-card-title>

        <div>
          <v-tabs
                  v-model="active"
                  light
                  slider-color="indigo"
          >
            <v-tab
                    class="button"
                    :key="All"
                    ripple
            >
              All

            </v-tab>
            <v-tab-item
                    :key="All"
            >
              <v-card flat>
                <v-card-text>All Media</v-card-text>
              </v-card>
            </v-tab-item>
            <v-tab
                    class="button"
                    :key="Photos"
                    ripple
            >
              Photos

            </v-tab>
            <v-tab-item
                    :key="Photos"
            >
              <v-card flat>
                <v-card-text>All Photos</v-card-text>
              </v-card>
            </v-tab-item>
            <v-tab
                    class="button"
                    :key="Videos"
                    ripple
            >
              Videos

            </v-tab>
            <v-tab-item
                    :key="Videos"
            >
              <v-card flat>
                <v-card-text>All Videos</v-card-text>
              </v-card>
            </v-tab-item>
            <v-tab
                    class="button"
                    :key="Albums"
                    ripple
            >
              Albums

            </v-tab>
            <v-tab-item
                    :key="Albums"
            >
              <v-card flat>
                <v-card-text>All Albums</v-card-text>
              </v-card>
            </v-tab-item>
          </v-tabs>

        </div>

        <ul>
          <li v-for="row in files" :value="row.value" :key="row.value">
            <div class="personal-photo-row">
              <div
                v-for="item in row"
                :value="item.value"
                :key="item.value"
                class="select-image-container"
              >
                <v-icon v-if="imageIsSelected(item)" class="lock-icon" left>done</v-icon>
                <div v-if="imageIsSelected(item)" class="triangle image-selected"></div>

                <v-img
                  v-on:click="selectImage(item)"
                  class="personal-photo-element"
                  :src="getImgUrl(item)"
                ></v-img>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <v-divider></v-divider>
    <v-btn class="button" color="error" v-on:click="processSelected()">Add</v-btn>
    <v-btn class="button" outline color="error" @click="closeDialogue">Cancel</v-btn>
  </v-card>
</template>


<style>

.button {
  text-transform: initial;
}

.image-selected {
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 70px 70px 0 0;
  border-color: greenyellow transparent transparent transparent !important;
  opacity: 0.3;
  position: absolute;
  z-index: 10;
}

.lock-icon {
  color: white !important;
  opacity: 1;
  position: absolute;
  z-index: 12;
  font-size: 2.3em;
  margin-top: 0.2em;
  margin-left: 0.2em;
  align-self: flex-start !important;
}

.select-image-container {
  width: 24%;
  height: 170px;
  border: 1px solid lightgrey;
  background-position: center;
  padding: 7px;
  overflow: hidden;
  display: flex;
  justify-content: flex-start;
}

.image-container:hover .personal-photo-element {
  cursor: pointer;
  opacity: 0.8;
}

.personal-photo-element {
  height: 100%;
  overflow: hidden;
}

.personal-photo-row {
  display: flex;
  justify-content: space-evenly;
  margin-bottom: 30px;
}

ul {
  padding-left: 0px;
}

h2 {
  align-self: flex-end;
}

hr {
  margin-bottom: 25px;
}

</style>


<script>
import { store } from "../../store/index";
import base_url from "../../repository/BaseUrl";
import { getImages } from "../../repository/PersonalPhotosRepository";

export default {
  store,

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
    }
  },

  /**
   * Initialises the component with the image data.
   */
  created: function() {
    this.id = this.$route.params.id;

    getImages(this.id).then(result => {
      this.files = this.groupImages(result.data);
    });
  }
};
</script>
