<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-card>
    <v-img v-if="clickedImage.filename" :src="mediaURL"></v-img>

    <v-card-title primary-title>
      <div>
        <v-layout d-flex>
          <h5 class="headline mb-0">Description</h5>
          <v-icon
            v-if="editCaption"
            @click="() => {updateMedia(clickedImage); editCaption = false;}"
          >save</v-icon>
          <v-icon v-else @click="editCaption = true">edit</v-icon>
        </v-layout>
        <div v-if="editCaption">
          <v-text-field v-model="clickedImage.caption" :counter="250" label="Caption"></v-text-field>
        </div>
        <div v-else>{{ clickedImage.caption ? clickedImage.caption : "No Description" }}</div>
      </div>
    </v-card-title>

    <v-divider></v-divider>

    <v-card-actions>
      <v-select
        style="width: 20%"
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
      <v-switch
        v-model="clickedImage.is_public"
        @on="updatePhotoVisability()"
        :label="`Public Photo`"
      ></v-switch>
      <v-btn color="error" outline @click="() => {setProfilePhoto()}">Set Profile Photo</v-btn>
      <v-btn
        color="error"
        outline
        @click="() => {deleteMedia(clickedImage); closeMediaDialog();}"
      >Delete</v-btn>
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

let mediaRepository = RepositoryFactory.get("media");
let userRespository = RepositoryFactory.get("user");

export default {
  store,

  props: {
    clickedImage: Object,
    closeMediaDialog: Function,
    updateMedia: Function,
    deleteMedia: Function,
    getAllAlbums: Function
  },

  // local variables
  data() {
    return {
      selectedAlbums: [],
      albums: [],
      editCaption: false,
      value: ""
    };
  },

  computed: {
    mediaURL() {
      return (
        base_url +
        `/api/users/${this.$route.params.id}/media/${this.clickedImage.filename}`
      );
    },

    notSelectedAlbums() {
      return this.albums.filter((album) => {
          for (let a of this.selectedAlbums) {
              if (a.id === album.id) return false;
          }
          return true;
      })
    }
  },

  methods: {
    async updateAlbums() {
        for (let album of this.selectedAlbums) {
            try {
                await mediaRepository
                    .moveMediaToAlbum(
                        this.$route.params.id,
                        album.id,
                        this.clickedImage.id,
                        // requestBody
                    )
            } catch(e) {
                console.log(e);
            }
        }

        for (let album of this.notSelectedAlbums) {
            try {
                await mediaRepository
                    .deleteMedia(
                        this.$route.params.id,
                        album.id,
                        this.clickedImage.id,
                    )
            } catch(e) {
                console.log(e);
            }
            console.log("here");
            this.getAllAlbums();

        }
    },

    /**
     * Sets the user's profile photo as the selected
     */
    setProfilePhoto() {
      userRespository
        .setProfilePic(this.$route.params.id, {
          photo_filename: this.clickedImage.filename
        })
        .then(res => {
          store.dispatch("fetchMe");
        });
    }
  },

  mounted: function() {
      console.log("mounted");
      console.log(this.clickedImage);
    this.selectedAlbums = this.clickedImage.albums
      ? this.clickedImage.albums
      : [];
  },

  watch: {
    clickedImage: function(newImage, oldImage) {
      if (newImage !== oldImage)
          console.log("here");
      console.log(this.clickedImage);
        this.selectedAlbums = this.clickedImage.albums
          ? this.clickedImage.albums
          : [];
    }
  },

  /**
   * Initialises the application on component creation.
   */
  created: function() {
    this.isPublic = this.clickedImage.is_public;
    this.user = store.getters.getUser;
    mediaRepository.getUserAlbums(store.getters.getUser.id).then(res => {
      this.albums = res.data.map(item => {
        return { id: item.id, name: item.name };
      }).filter((a) => a.name.toLowerCase() !== "all");

    });
  }
};
</script>