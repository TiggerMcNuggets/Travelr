<template>
  <v-card>
    <v-img v-if="clickedImage.filename" :src="mediaURL"></v-img>

    <v-card-title primary-title>
      <div>
        <h5 class="headline mb-0">Image Name</h5>
        <div>Description/Other meta info</div>
      </div>
    </v-card-title>

    <v-divider></v-divider>

    <v-card-actions>
      <v-select
        style="width: 20%"
        v-model="value"
        :items="albums"
        label="Add/Remove Albums"
        multiple
      >
        <template v-slot:selection="{ item, index }">
          <v-chip v-if="index === 0">
            <span>{{ item }}</span>
          </v-chip>
          <span v-if="index === 1" class="grey--text caption">(+{{ value.length - 1 }} others)</span>
        </template>
      </v-select>
      <v-switch :label="`Profile Photo`"></v-switch>
      <v-switch
        v-model="clickedImage.is_public"
        @on="updatePhotoVisability()"
        :label="`Public Photo`"
      ></v-switch>
    </v-card-actions>
    <v-card-actions>
      <v-btn color="error" outline @click="closeMediaDialog">Close</v-btn>
    </v-card-actions>
  </v-card>
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

let mediaRepository = RepositoryFactory.get("media");

export default {
  store,

  props: {
    clickedImage: Object,
    closeMediaDialog: Function
  },

  // local variables
  data() {
    return {
      value: "",
      albums: []
    };
  },

  computed: {
    mediaURL() {
      return (
        base_url +
        `/api/users/${this.$route.params.id}/media/${
          this.clickedImage.filename
        }`
      );
    }
  },

  methods: {},

  /**
   * Initialises the application on component creation.
   */
  created: function() {
    this.isPublic = this.clickedImage.is_public;
    mediaRepository.getUserAlbums(store.getters.getUser.id).then(res => {
      let albums = [];
      for (let i = 0; i < res.data.length; i++) {
        albums.push(res.data[i].name);
      }
      this.albums = albums;
    });
  }
};
</script>