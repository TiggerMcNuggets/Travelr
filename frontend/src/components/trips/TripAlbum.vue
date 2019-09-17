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
        :openConfirmDelete="() => {confirmDeletionDialogActive = true}"
        :isMyProfile="isMyProfile"
        :isAdminUser="isAdminUser"
      />
    </v-dialog>
  </v-layout>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import MediaGrid from "../media/MediaGrid";
import MediaDialog from "../media/MediaDialog";
import base_url from "../../repository/BaseUrl";
let mediaRepository = RepositoryFactory.get("media");

export default {
  name: "TripAlbums",

  components: {
    MediaGrid,
    MediaDialog
  },

  props: {
    trip: Object
  },

  data() {
    return {
      clickedImageWidth: 0,
      viewMediaDialogActive: false,
      clickedImage: null,
      isMyProfile: true,
      isAdminUser: true,
      files: [],
      };
  },

  watch: {
    trip: function() {
      this.getTripImages()
    }
  },

  methods: {
    deleteMedia() {
      console.log("Not sure here");
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
          response.data.forEach(item => {
            item.filename = item.uriString;
          });
          this.files = response.data;
        });
    }
  },

  created: function() {
    if (this.trip.root.albumId) {
      this.getTripImages();
    }
  }
  
};
</script>
