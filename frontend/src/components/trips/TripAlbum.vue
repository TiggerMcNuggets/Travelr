<template>
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex >
        <MediaGrid
          :filteredMedia="files"
          :openElement="openImage"
          :getAllAlbums="() => {}"
          :openEditAlbumDialog="() => {}"
          smallGrid
        ></MediaGrid>
      </v-flex>
    </v-flex>
    <v-dialog v-model="viewMediaDialogActive" :width="clickedImageWidth">
      <MediaDialog
        :clickedImage="clickedImage ? clickedImage : {}"
        :closeMediaDialog="() => viewMediaDialogActive = false"
        :deleteMedia="deleteMedia"
        :updateMedia="updateMedia"
        :openConfirmDelete="() => {confirmDeletionDialogActive = true}"
        :isMyProfile="isMyProfile"
        :isAdminUser="isAdminUser"
        :getAllAlbums="getTripImages"
      />
    </v-dialog>
    <v-dialog v-model="confirmDeletionDialogActive" width="500">
      <ConfirmDelete
        title="Confirm Delete"
        text="Would you like to delete this media from all albums?"
        :confirm="() => {deleteAndCloseDialog(true)}"
        :cancel="() => {deleteAndCloseDialog(false)}"
      />
    </v-dialog>
  </v-layout>
</template>

<script>
import ConfirmDelete from "../common/ConfirmDialog";
import { store } from "../../store/index";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import MediaGrid from "../media/MediaGrid";
import MediaDialog from "../media/MediaDialog";
import base_url from "../../repository/BaseUrl";
let mediaRepository = RepositoryFactory.get("media");

export default {
  store,
  name: "TripAlbums",

  components: {
    MediaGrid,
    MediaDialog,
    ConfirmDelete
  },

  props: {
    trip: Object
  },

  data() {
    return {
      clickedImageWidth: 0,
      viewMediaDialogActive: false,
      clickedImage: null,
      isMyProfile: false,
      confirmDeletionDialogActive: false,
      isAdminUser: false,
      files: [],
      };
  },

  watch: {
    trip: function() {
      this.getTripImages()
    }
  },

  methods: {
    deleteAndCloseDialog(deleteAll) {
      this.confirmDeletionDialogActive = false;
      this.deleteMedia(this.clickedImage, deleteAll);
      this.viewMediaDialogActive = false;
    },
    updateMedia(clickedImage) {
      let payload = {
        "is_public": clickedImage.is_public,
        "caption": clickedImage.caption
      }
      mediaRepository
        .updateMedia(this.trip.root.user.id, clickedImage.id, clickedImage)
        .then(() => {
          this.getTripImages();
        });
    },

    deleteMedia(clickedImage, deleteAll) {
      mediaRepository
        .deleteMedia(
          this.$route.params.id,
          this.trip.root.albumId,
          clickedImage.id,
          deleteAll
        )
        .then(() => {
          this.getTripImages();
        });
    },
    deleteAndCloseDialog(deleteAll) {
      this.confirmDeletionDialogActive = false;
      this.deleteMedia(this.clickedImage, deleteAll);
      this.viewMediaDialogActive = false;
    },
    openImage(image) {
        // Trying to open an photo/video/media
        const myImage = new Image();
        myImage.src =
          base_url +
          `/api/users/${this.$route.params.id}/media/${image.filename}`;
        this.clickedImageWidth = myImage.width < 400 ? 400 : myImage.width;
        this.clickedImage = image;
        this.viewMediaDialogActive = true;
    },
    /**
     * Gets all the trip photos within the trip album
     */
    getTripImages() {
      mediaRepository
        .getAlbumContent(
          this.trip.root.user.id,
          this.trip.root.albumId
        )
        .then(response => {
          this.files = response.data.mediaItems;
        });
    }
  },

  created: function() {
    if (this.trip.root.albumId) {
      this.getTripImages();
    }
    this.isAdminUser = store.getters.getIsUserAdmin;
  }
  
};
</script>