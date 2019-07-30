<template>
    <v-dialog v-model="imageDialog" width="70%">
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
            <v-select
                style="width: 20%"
                v-model="value"
                :items="albums"
                label="Albums"
                multiple
            >
                <template v-slot:selection="{ item, index }">
                    <v-chip v-if="index === 0">
                    <span>{{ item }}</span>
                    </v-chip>
                    <span
                    v-if="index === 1"
                    class="grey--text caption"
                    >(+{{ value.length - 1 }} others)</span>
                </template>
            </v-select>
            
          <v-spacer></v-spacer>
          <v-switch
            v-model="makeProfileMedia"
            :label="`Made Profile Media`"
          ></v-switch>
          <v-switch
            v-if="isPublic"
            disabled
            v-model="publicPhotoSwitch"
            :label="`Public Photo`"
          ></v-switch>
          <v-switch v-else v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>
          <v-btn color="primary" flat @click="updatePhotoVisability()">Apply changes</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
        <v-card-actions>
          <v-btn color="primary" flat @click="closeMediaDialogue">Close</v-btn>
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
let mediaRepository = RepositoryFactory.get("media");

export default {
  store,

  props: [
    "clickedImageWidth",
    "clickedImageURL",
    "clickedImage",
    "publicPhotoSwitch",
    "imageDialog",
    "closeMediaDialogue"
  ],

  // local variables
  data() {
    return {
        isPublic: false,
        dialog: false,
        makeProfileMedia: false,
        publicPhoto: publicPhotoSwitch,
        value: '',
        albums: []
    };
  },

  methods: {

    updatePublicStatus() {
        if (this.publicPhotoSwitch === false) {
            this.publicPhotoSwitch = true;
        }
        this.makeProfileMedia = true;
    },
      
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
      this.isPublic = this.clickedImage.is_public;
      this.imageDialog = this.dialog;
      mediaRepository.getUserAlbums(store.getters.getUser.id).then(res => {
          let albums = []
          for (let i = 0; i < res.data.length; i++) {
              albums.push(res.data[i].name);
          }
          this.albums = albums;
      })
  }
}
</script>