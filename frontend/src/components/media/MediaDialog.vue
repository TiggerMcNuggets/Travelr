<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-card>
    <v-layout justify-space-around>
      <v-icon @click="clickedImage--">mdi-minus</v-icon>
      <v-icon @click="clickedImage++">mdi-plus</v-icon>
    </v-layout>

    <v-img v-if="clickedImage.filename" :src="mediaURL"></v-img>

    <v-card-title primary-title>
      <div>
        <v-layout d-flex>
          <h5 class="headline mb-0 mr-2">Description</h5>
          <v-icon v-if="editCaption" @click="updateMediaAndSave(clickedImage)">save</v-icon>
          <v-icon v-else-if="canCRUDMedia" @click="editCaption = true">edit</v-icon>
        </v-layout>
        <div v-if="editCaption">
          <v-text-field v-model="clickedImage.caption" :counter="250" label="Caption"></v-text-field>
        </div>
        <div v-else>{{ clickedImage.caption ? clickedImage.caption : "No Description" }}</div>
      </div>
    </v-card-title>

    <v-divider v-if="canCRUDMedia"></v-divider>

    <v-card-actions v-if="canCRUDMedia">
      <v-select
        v-model="selectedAlbums"
        :items="albums"
        label="Add/Remove Albums"
        item-text="name"
        item-value="id"
        return-object
        multiple
        v-on:change="updateAlbums"
      >
        <template v-slot:selection="{ item, index }">
          <v-chip v-if="index === 0">
            <span>{{ item.name }}</span>
          </v-chip>
          <span v-if="index === 1" class="grey--text caption">(+{{ value.length - 1 }} others)</span>
        </template>
      </v-select>
      <v-spacer></v-spacer>

      <v-layout>
        <v-flex>
          <p class="title mb-0 mr-2" v-if="photoIsPublic">Public</p>
          <p class="title mb-0 mr-2" v-else >Private</p>
          <v-icon
            v-if="editVisibility"

            @click="updateVisibilityAndSave(clickedImage)"
          >save
          </v-icon>
          <v-icon @click="editVisibility = true">edit</v-icon>

          <div v-if="editVisibility">
            <v-switch v-model="clickedImage.is_public" label="Public Photo"></v-switch>
          </div>
        </v-flex>
      </v-layout>
    </v-card-actions>
    <v-card-actions v-if="canCRUDMedia">
      <v-btn color="error" @click="setProfilePhoto">Set Profile Photo</v-btn>
      <v-btn color="error" @click="openConfirmDelete">Delete</v-btn>
      <v-spacer></v-spacer>
      <v-btn color="error" outline @click="closeMediaDialog">Close</v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import base_url from "../../repository/BaseUrl";
import { store } from "../../store/index";

let mediaRepository = RepositoryFactory.get("media");
let userRespository = RepositoryFactory.get("user");

export default {
  store,

  props: {
    clickedImage: Object,
    closeMediaDialog: Function,
    updateMedia: Function,
    openConfirmDelete: Function,
    deleteMedia: Function,
    getAllAlbums: Function,
    isMyProfile: Boolean,
    isAdminUser: Boolean
  },

  // local variables
  data() {
    return {
      albums: [],
      editCaption: false,
      editVisibility: false,
      value: "",
      selectedAlbums: []
    };
  },

  computed: {
    canCRUDMedia() {
      return this.isAdminUser || this.isMyProfile;
    },

    mediaURL() {
      return (
        base_url +
        `/api/users/${this.$route.params.id}/media/${
          this.clickedImage.filename
        }`
      );
    },

    notSelectedAlbums() {
      return this.albums.filter(album => {
        for (let a of this.selectedAlbums) {
          if (a.id === album.id) return false;
        }
        return true;
      });
    },
    photoIsPublic() {
      if (this.clickedImage.is_public) {
        return true;
      } else {
        return false;
      }
    }
  },

  methods: {
    async updateAlbums() {
      for (let album of this.selectedAlbums) {
        try {
          await mediaRepository.moveMediaToAlbum(
            this.$route.params.id,
            album.id,
            this.clickedImage.id
            // requestBody
          );
        } catch (e) {
          console.log(e);
        }
      }

      for (let album of this.notSelectedAlbums) {
        try {
          await mediaRepository.deleteMedia(
            this.$route.params.id,
            album.id,
            this.clickedImage.id
          );
        } catch (e) {
          console.log(e);
        }
        this.getAllAlbums();
      }
      this.getAllAlbums();
    },

    /**
     * Sets the user's profile photo as the selected
     */
    setProfilePhoto() {
      userRespository
        .setProfilePic(this.$route.params.id, {
          photo_filename: this.clickedImage.filename
        })
        .then(() => {
          store.dispatch("fetchMe");
        });
    },

    /**
     * updates the media description and saves it
     */
    updateMediaAndSave(clickedImage) {
      this.updateMedia(clickedImage);
      this.editCaption = false;
    },

    /**
     * updates the media visibility and saves it
     */
    updateVisibilityAndSave(clickedImage) {
      this.updateMedia(clickedImage);
      this.editVisibility = false;
    }
  },

  mounted: function() {
    this.selectedAlbums = this.clickedImage.albums
      ? this.clickedImage.albums
      : [];

    mediaRepository.getUserAlbums(store.getters.getUser.id).then(res => {
      this.albums = res.data
        .map(item => {
          return { id: item.id, name: item.name };
        })
        .filter(a => a.name.toLowerCase() !== "all");
    });
  },

  watch: {
    clickedImage: function(newImage, oldImage) {
      if (newImage !== oldImage) {
        this.selectedAlbums = this.clickedImage.albums
          ? this.clickedImage.albums
          : [];
      }

      mediaRepository.getUserAlbums(store.getters.getUser.id).then(res => {
        this.albums = res.data
          .map(item => {
            return { id: item.id, name: item.name };
          })
          .filter(a => a.name.toLowerCase() !== "all");
      });
    }
  },

  /**
   * Initialises the application on component creation.
   */
  created: function() {
    this.isPublic = this.clickedImage.is_public;
    this.user = store.getters.getUser;
  }
};
</script>