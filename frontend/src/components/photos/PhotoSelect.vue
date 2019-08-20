<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Add Exising Media</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
        <v-container v-bind="{ [`grid-list-lg`]: true }" ma-1 pa-0 fluid class="media-container">
          <v-layout row wrap>
            <v-flex v-for="file in files.entries()" :key="file[0]" xs12 md4>
              <v-hover>
                <v-card class="mx-auto" color="grey lighten-4" max-width="600">
                  <v-img
                    class="media-item"
                    :src="getImgUrl(file[1])"
                    height="200px"
                    v-on:click="selectImage(file[1])"
                  >
                    <v-flex v-if="imageIsSelected(file[1])" class="hover-white fill-height">
                      <v-icon size="50" color="white">done</v-icon>
                    </v-flex>
                  </v-img>
                </v-card>
              </v-hover>
            </v-flex>
          </v-layout>
        </v-container>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat v-on:click="closeDialog()">Cancel</v-btn>
      <v-btn ma-3 color="primary" flat v-on:click="processSelected()">Add Existing</v-btn>
    </v-card-actions>
  </v-card>
</template>


<style>
.hover-white {
  background-color: rgba(133, 255, 11, 0.2);
  display: flex;
  align-items: center;
  bottom: 0;
  justify-content: center;
}

.media-item:hover {
  cursor: pointer;
}

.media-container {
  padding-top: 0;
}
</style>


<script>
import { store } from "../../store/index";
import base_url from "../../repository/BaseUrl";
import { RepositoryFactory } from "../../repository/RepositoryFactory";

let mediaRepository = RepositoryFactory.get("media");

export default {
  store,

  // local variables
  data() {
    return {
      files: [],
      clickedImageURL: "",
      id: null,
      selectedImages: [],
      active: "All"
    };
  },

  // props methods being passed in from parent.
  props: {
    closeDialog: Function,
    setDestinationImages: Function
  },

  methods: {
    /**
     * Sets the user's profile photo as the selected
     */
    processSelected() {
      this.setDestinationImages(this.selectedImages);
      this.selectedImages = [];
    },

    /**
     * Gets the image url for the server to get the image.
     * @param item The photo item
     * @returns {string}
     */
    getImgUrl(item) {
      return (
        base_url + `/api/users/${this.$route.params.id}/media/${item.uriString}`
      );
    },

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
     * Gets all album content from the users default album
     */
    getAlbumContent() {
      mediaRepository
        .getAlbumContent(
          this.$store.getters.getUser.id,
          this.$store.getters.getUser.defaultAlbumId
        )
        .then(response => {
          this.files = response.data;
        });
    }
  },

  /**
   * Initialises the component with the image data.
   */
  created: function() {
    this.id = this.$route.params.id;
    this.getAlbumContent();
  }
};
</script>
