<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-layout>
    <v-flex mb-3>
      <v-flex ml-0 pl-0 v-if="!disableTitle">
        <span class="title">
          <a @click="openElement">{{ album.name }}</a>
        </span>

        <v-menu bottom left v-if="hasEditPermissions && !album.isPermanant">
          <template v-slot:activator="{ on }">
            <v-btn class="more-icon" icon v-on="on">
              <v-icon>more_vert</v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-tile v-for="(item, i) in options" :key="i" @click="item.handler">
              <v-list-tile-title>{{ item.title }}</v-list-tile-title>
            </v-list-tile>
          </v-list>
        </v-menu>
      </v-flex>
      <v-card flat tile @click="openElement">
        <v-icon v-if="!album.isPublic" class="lock-icon" left>lock</v-icon>
        <div v-if="!album.isPublic" class="triangle"></div>
        <div class="grid-4-container">
          <v-img
            v-for="(thumbnail, id) in albumThumbnails"
            :key="id"
            :src="thumbnail"
            :lazy-src="fillerImageURL"
            :height="145"
          >
            <template v-slot:placeholder>
              <v-layout fill-height align-center justify-center ma-0>
                <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
              </v-layout>
            </template>
          </v-img>
        </div>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<style scoped>
@import "../../assets/css/style.css";

.grid-4-container {
  display: grid;
  grid-template-columns: calc(50% - 5px) calc(50% - 5px);
  grid-template-rows: calc(50% - 5px) calc(50% - 5px);
  grid-gap: 10px;
  transition: opacity 0.4s ease-in-out;
}

.title a {
  color: black;
}
</style>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import { store } from "../../store/index";
let mediaRepository = RepositoryFactory.get("media");

export default {
  name: "Album",

  props: {
    album: Object,
    getImgFromUrl: Function,
    fillerImageURL: String,
    openElement: Function,
    getAllAlbums: Function,
    openEditAlbumDialog: Function,
    disableTitle: Boolean
  },

  data() {
    return {
      options: [
        {
          title: "Edit",
          handler: this.openEditAlbumDialogHandler
        },
        {
          title: "Delete",
          handler: this.deleteAlbum
        }
      ]
    };
  },

  computed: {
    /**
     * Computes the thumbnails to be shown in the 2x2 grid album preview.
     * Fills the empty spaces with a specified filler image.
     */
    albumThumbnails: function() {
      let thumbnails = [];
      let numberOfThumbnails =
        this.album.content.length < 4 ? this.album.content.length : 4;
      let requiredFillerImages = 4 - numberOfThumbnails;

      for (let i = 0; i < numberOfThumbnails; i++) {
        thumbnails.push(this.getImgFromUrl(this.album.content[i].filename));
      }

      for (let i = 0; i < requiredFillerImages; i++) {
        thumbnails.push(this.fillerImageURL);
      }

      return thumbnails;
    },

    /**
     * Returns if the user has edit permissions
     */
    hasEditPermissions: function() {
      let isMyProfile = parseInt(store.getters.getUser.id) === parseInt(this.$route.params.id);
      let isAdminUser = store.getters.getIsUserAdmin;
      return isMyProfile || isAdminUser
    }
  },
  methods: {
    /**
     * Sends a request to delete an album for a user
     */
    async deleteAlbum() {
      await mediaRepository.deleteAlbum(
        this.$store.getters.getUser.id,
        this.album.id
      );
      this.getAllAlbums();
    },

    /**
     * Opens the edit album modal
     */
    openEditAlbumDialogHandler() {
      this.openEditAlbumDialog(this.album);
    }
  }
};
</script>