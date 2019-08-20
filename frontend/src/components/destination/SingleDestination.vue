<template>
  <v-container fluid>
    <PageHeader
      :title="destination.name"
      disableUndoRedo
      enableBackButton
      :options="options"
      :multiOptions="multiOptions"
    />

    <v-layout row wrap>
      <v-flex md2 pr-3>
        <div class>
          <div class="field-section">
            <p class="caption font-weight-light field-title">DESTINATION TYPE</p>
            <p class="body-1 font-weight-normal">{{destination.type}}</p>
          </div>
          <div class="field-section">
            <p class="caption font-weight-light field-title">DISTRICT</p>
            <p class="body-1 font-weight-normal">{{destination.district}}</p>
          </div>
          <div class="field-section">
            <p class="caption font-weight-light field-title">COUNTRY</p>
            <p class="body-1 font-weight-normal">{{destination.country}}</p>
          </div>
          <div class="field-section">
            <p class="caption font-weight-light field-title">LATITUDE</p>
            <p class="body-1 font-weight-normal">{{destination.latitude}}</p>
          </div>
          <div class="field-section">
            <p class="caption font-weight-light field-title">LONGITUDE</p>
            <p class="body-1 font-weight-normal">{{destination.longitude}}</p>
          </div>
          <div class="field-section">
            <p class="caption font-weight-light field-title">TRAVELLER TYPES</p>
            <p class="body-1 font-weight-normal">{{getTravellerTypes(destination.travellerTypes)}}</p>
          </div>
        </div>
      </v-flex>
      <v-flex md10>
        <MediaGrid
          :filteredMedia="files"
          :openElement="() => {}"
          :getAllAlbums="() => {}"
          :openEditAlbumDialog="() => {}"
        ></MediaGrid>
      </v-flex>

      <suggest-traveller-types
        :destinationId="destId"
        :userTravellerTypes="[]"
        :showSuggestTravellerTypes="showSuggestTravellerTypes"
        :close="toggleShowSuggestTravellerTypes"
      />
      <v-dialog v-model="chooseExistingDialog" width="800">
        <PhotoSelect :closeDialog="closeDialog" :setDestinationImages="setDestinationImages"/>
      </v-dialog>

      <v-dialog v-model="showUploadSection" width="800">
        <MediaUpload
          :uploadMedia="uploadMedia"
          :openUploadDialog="toggleShowUploadPhoto"
          :closeUploadDialog="toggleShowUploadPhoto"
          :isDestination="true"
        ></MediaUpload>
      </v-dialog>
    </v-layout>
  </v-container>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import base_url from "../../repository/BaseUrl";
import MediaUpload from "../media/MediaUpload";
import PhotoSelect from "../photos/PhotoSelect";
import SuggestTravellerTypes from "./destination_dialogs/SuggestTravellerTypes";
import { store } from "../../store/index";
import PageHeader from "../common/header/PageHeader";
import MediaGrid from "../media/MediaGrid";

let mediaRepository = RepositoryFactory.get("media");
let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,
  components: {
    SuggestTravellerTypes,
    PageHeader,
    MediaGrid,
    MediaUpload,
    PhotoSelect
  },

  // local variables
  data() {
    return {
      files: [],
      TravellerTypes: [],
      dialog: false,
      userId: null,
      isMyProfile: false,
      isAdminUser: false,
      destination: {},
      destId: null,
      uploadError: false,
      chooseExistingDialog: false,
      uploadSuccessful: false,
      showSuggestTravellerTypes: false,
      showUploadSection: false
    };
  },

  computed: {
    /**
     * Options used in the header component.
     */
    options() {
      let options = [];

      if (
        (this.isMyProfile || this.isAdminUser) &&
        this.destination.ownerId === parseInt(this.userId)
      ) {
        options.push({ action: this.editDestination, icon: "edit" });
      }

      if (
        this.destination.isPublic &&
        this.destination.ownerId !== parseInt(this.userId)
      ) {
        options.push({
          action: this.toggleShowSuggestTravellerTypes,
          icon: "card_travel"
        });
      }

      return options;
    },

    /**
     * Multi options used in the header component.
     */
    multiOptions() {
      let options = [];

      if (this.isMyProfile || this.isAdminUser) {
        options.push({
          icon: "add_photo_alternate",
          actions: [
            { text: "Upload Existing", callback: this.uploadExisting },
            { text: "Upload New", callback: this.toggleShowUploadPhoto }
          ]
        });
      }
      return options;
    }
  },

  methods: {
    /**
     * Toggles the upload section for the photos
     */
    toggleShowSuggestTravellerTypes: function() {
      this.showSuggestTravellerTypes = !this.showSuggestTravellerTypes;
    },

    /**
     * Redirects the user to the edit destination page.
     */
    editDestination() {
      this.$router.push(
        "/user/" + this.userId + "/destinations/edit/" + this.destId
      );
    },

    /**
     * Sets the selected existing photos to be added to destination photos which have been selected from the dialog to
     * choose existing photos. Takes each photo filename which has been selected and calls the API to add it to the users destination photos.
     * Closes the dialog once done.
     * @param selectedImages The photo details for all the photos selected by the user.
     */
    setDestinationImages(selectedImages) {
      selectedImages.forEach(image => {
        mediaRepository
          .moveMediaToAlbum(
            this.$store.getters.getUser.id,
            this.destination.defaultAlbumId,
            image.id
          )
          .catch(error => console.log(error))
          .then(() => {
            this.getDestinationImages();
          });
      });
      this.closeDialog();
    },

    /**
     * Sends a request to the backend containing formdata with the image to be added to a specified album
     * given an user id and an album id.
     */
    uploadToAlbum(albumId, file) {
      let formData = new FormData();
      formData.append("picture", file);

      mediaRepository
        .uploadMediaToAlbum(this.userId, albumId, formData)
        .then(() => {
          return this.getDestinationImages();
        });
    },

    /**
     * Uploads the given media files to the backend.
     */
    uploadMedia(files) {
      let albumId = this.destination.defaultAlbumId;
      for (let i = 0; i < files.length; i++) {
        let file = files[i];
        this.uploadToAlbum(albumId, file);
      }

      this.toggleShowUploadPhoto();
    },

    /**
     * Opens the choose existing photo selector dialog.
     */
    uploadExisting() {
      this.chooseExistingDialog = true;
    },

    toggleShowUploadPhoto() {
      this.showUploadSection = !this.showUploadSection;
    },

    /**
     * Closes the choose existing photo dialog.
     */
    closeDialog() {
      this.chooseExistingDialog = false;
    },

    /**
     * Gets the image src url for the server
     * @param item The photo item to get the image for.
     * @returns {string} A url of where to find the photo to set as src
     */
    getImgUrl(item) {
      return `${base_url}/api/users/${this.userId}/media/${item.uriString}`;
    },

    /**
     * Gets a list of traveller type names.
     */
    getTravellerTypes(travellerTypes) {
      if (travellerTypes) {
        let travellerTypesString = "";
        for (let i = 0; i < travellerTypes.length; i++) {
          travellerTypesString += travellerTypes[i].name;
          if (i !== travellerTypes.length - 1) {
            travellerTypesString += ", ";
          }
        }
        return travellerTypesString;
      } else {
        return "-";
      }
    },

    /**
     * Gets all the destination photos within the destionation album
     */
    getDestinationImages() {
      mediaRepository
        .getAlbumContent(
          this.$store.getters.getUser.id,
          this.destination.defaultAlbumId
        )
        .then(response => {
          response.data.forEach(item => {
            item.filename = item.uriString;
          });
          this.files = response.data;
        });
    }
  },

  /**
   * Initialises the application on component creation.
   */
  created: function() {
    this.userId = this.$route.params.id;
    this.destId = this.$route.params.dest_id;

    if (!this.userId) {
      this.userId = store.getters.getUser.id;
    }

    this.isMyProfile = store.getters.getUser.id == this.userId;
    this.isAdminUser = store.getters.getIsUserAdmin;

    destinationRepository
      .getDestination(this.userId, this.destId)
      .then(response => {
        this.destination = response.data;
        // Gets all the images to display on the page.
        this.getDestinationImages();
      })
      .catch(err => {
        console.log(err);
      });
  }
};
</script>
