<template>
    <v-dialog v-model="imageDialog" :width="clickedImageWidth">
      <v-card>
        <v-img :src="clickedImageURL"></v-img>

        <v-card-title primary-title>
          <div>
            <h5 class="headline mb-0">Image Name</h5>
            <div>Description/Other meta info</div>
          </div>
        </v-card-title>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-switch
            v-if="isPublic"
            disabled
            v-model="publicPhotoSwitch"
            :label="`Public Photo`"
          ></v-switch>
          <v-switch v-else v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>
          <v-btn color="primary" flat @click="updatePhotoVisability()">Apply changes</v-btn>
        </v-card-actions>
        <v-card-actions>
          <v-btn color="primary" flat @click="imageDialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import base_url from "../../repository/BaseUrl";
import { store } from "../../store/index";
import {
  storeDestinationImage,
  getImages,
  updateDestinationPhoto,
  addExistingPhoto
} from "../../repository/DestinationPhotoRepository";

let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,

  props: [
    "clickedImageWidth",
    "clickedImageURL",
    "clickedImage",
    "publicPhotoSwitch",
    "imageDialog"
  ],

  // local variables
  data() {
    return {
        isPublic: false,
        dialog: false,
    };
  },

  methods: {
    /**
     * Updates whether the photo is public or private depending on the switch state.
     */
    updatePhotoVisability() {
      this.clickedImage.is_public = this.publicPhotoSwitch;
      updateDestinationPhoto(this.clickedImage);
    },
  },
  
  /**
   * Initialises the application on component creation.
   */
  created: function() {
      console.log(this.isPublic);
      this.isPublic = this.clickedImage.is_public;
      this.imageDialog = this.dialog;
      console.log(this.isPublic);
      console.log(this.clickedImage);
  }
}
</script>