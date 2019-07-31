<template>
  <v-container fluid>
    <PageHeader :title="destination.name" disableUndoRedo enableBackButton :options="options"/>

    <div class="dest-sub-info">
      <p>
        <strong>Type:</strong>
        {{destination.type}}
      </p>
      <p>
        <strong>District:</strong>
        {{destination.district}}
      </p>
      <p>
        <strong>Country:</strong>
        {{destination.country}}
      </p>
      <p>
        <strong>Latitude:</strong>
        {{destination.latitude}}
      </p>
      <p>
        <strong>Longitude:</strong>
        {{destination.longitude}}
      </p>
      <p>
        <strong>Traveller Types:</strong>
        {{getTravellerTypes(destination.travellerTypes)}}
      </p>
    </div>
    <v-divider class="photo-header-divider"></v-divider>
    <div v-if="showUploadSection">
      <div class="upload-section section">
        <label>
          <input
            class="choose-file-button"
            type="file"
            id="file"
            ref="file"
            v-on:change="handleFileUpload()"
          >
        </label>
        <div>
          <v-btn v-on:click="submitFile()">Upload Photo</v-btn>
          <!--<v-btn v-on:click="uploadExisting()">Choose Existing</v-btn>-->
        </div>
      </div>
      <v-alert :value="uploadError" color="error">{{errorText}}</v-alert>
      <v-alert :value="uploadSuccessful" color="success">Upload Successful</v-alert>
      <v-divider class="photo-header-divider"></v-divider>
    </div>

    <ul>
      <li v-for="row in files" :value="row.value" :key="row.value">
        <div class="personal-photo-row">
          <div v-for="item in row" :value="item.value" :key="item.value" class="image-container">
            <v-icon v-if="item.is_public" class="lock-icon" left>lock_open</v-icon>
            <v-icon v-else class="lock-icon" left>lock</v-icon>

            <div v-if="item.is_public" class="triangle pink-color"></div>
            <div v-else class="triangle"></div>

            <v-img
              @click.stop="dialog = true"
              v-on:click="setDialogContent(item)"
              class="personal-photo-element"
              :src="getImgUrl(item)"
            ></v-img>
          </div>
        </div>
      </li>
    </ul>

    <suggest-traveller-types
      :destinationId="destId"
      :userTravellerTypes="[]"
      :showSuggestTravellerTypes="showSuggestTravellerTypes"
      :close="toggleShowSuggestTravellerTypes"
    />
    <v-dialog v-model="chooseExistingDialog" width="800">
      <PhotoSelect v-bind="{closeDialog, setDestinationImages}"/>
    </v-dialog>
  </v-container>
</template>

<style>
@import "../../assets/css/style.css";
.dest-name {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.dest-name div {
  text-align: start;
}

.dest-sub-info {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.dest-sub-info p {
  margin-right: 20px;
  color: grey;
}

.dest-name button {
  margin-right: 20px;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import base_url from "../../repository/BaseUrl";
import PhotoSelect from "../photos/PhotoSelect";
import SuggestTravellerTypes from "./destination_dialogs/SuggestTravellerTypes";
import { store } from "../../store/index";
import {
  storeDestinationImage,
  getImages,
  updateDestinationPhoto,
  addExistingPhoto
} from "../../repository/DestinationPhotoRepository";
import PageHeader from "../common/header/PageHeader";

let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,

  components: {
    PhotoSelect,
    SuggestTravellerTypes,
    PageHeader,
  },

  // local variables
  data() {
    return {
      files: [],
      TravellerTypes: [],
      typeList: [],
      clickedImageURL: "",
      clickedImage: {},
      clickedImageWidth: 0,
      dialog: false,
      publicPhotoSwitch: false,
      showUploadSection: false,
      userId: null,
      isMyProfile: false,
      isAdminUser: false,
      destination: {},
      destId: null,
      uploadError: false,
      chooseExistingDialog: false,
      uploadSuccessful: false,
      errorText:
        "You are trying to upload a duplicate image or an error occured while uploading.",
      showSuggestTravellerTypes: false
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

      if (this.isMyProfile || this.isAdminUser) {
        options.push({
          action: this.toggleShowUploadPhoto,
          icon: "insert_photo"
        });
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
      for (let i = 0; i < selectedImages.length; i++) {
        addExistingPhoto(this.userId, this.destId, {
          photo_filename: selectedImages[i].photo_filename
        }).then(() => {
          getImages(this.userId, this.destId).then(result => {
            this.files = this.groupImages(result.data);
          });
        });
      }
      this.closeDialog();
    },

    /**
     * Opens the choose existing photo selector dialog.
     */
    uploadExisting() {
      this.chooseExistingDialog = true;
    },

    /**
     * Closes the choose existing photo dialog.
     */
    closeDialog() {
      this.chooseExistingDialog = false;
    },

    /**
     * Closes the choose existing photo dialog.
     */
    closeMediaDialog() {
      this.dialog = false;
    },

    /**
     * Sets the file property the the file being uploaded.
     */
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    /**files
     * Toggles the upload section for the photos
     */
    toggleShowUploadPhoto: function() {
      this.showUploadSection = !this.showUploadSection;
    },

    /**
     * Updates whether the photo is public or private depending on the switch state.
     */
    updatePhotoVisability() {
      this.clickedImage.is_public = this.publicPhotoSwitch;
      updateDestinationPhoto(this.clickedImage);
    },

    /**
     * Submits the image file and uploads it to the server
     */
    submitFile() {
      this.uploadError = false;
      this.uploadExisting = false;
      let formData = new FormData();
      formData.append("picture", this.file);

      storeDestinationImage(this.userId, this.destId, formData)
        .then(() => {
          getImages(this.userId, this.destination.defaultAlbumId).then(result => {
            this.uploadExisting = true;
            console.log(result);
            this.files = this.groupImages(result.data);
          });
        })
        .catch(error => {
          this.uploadError = true;
        });
      this.$refs.file.value = "";
    },

    /**
     * Gets the image src url for the server
     * @param item The photo item to get the image for.
     * @returns {string} A url of where to find the photo to set as src
     */
    getImgUrl(item) {
      return base_url + "/api/users/" + this.userId + "/media/" + item.uriString;
    },

    /**
     * Sets the width of the dialog based on the image width.
     * @param selectedImage The image which has been clicked on.
     */
    setDialogContent(selectedImage = "") {
      this.dialog = true;
      this.clickedImage = selectedImage;
      this.publicPhotoSwitch = selectedImage.is_public;
      this.clickedImageURL = this.getImgUrl(selectedImage);
      const myImage = new Image();
      myImage.src =
        base_url + "/api/destinations/photo/" + selectedImage.photo_filename;
      this.clickedImageWidth = myImage.width < 400 ? 400 : myImage.width;
    },

    /**
     * Groups the images into rows with four columns.
     * @param imageList The list of image data from the server.
     * @returns {Array} A list of rows each with four images.
     */
    groupImages(imageList) {
      let newImageList = [];
      let row = [];
      const num_cols = 4;
      for (let i = 0; i < imageList.length; i++) {
        if (i % num_cols === 0 && row.length !== 0) {
          newImageList.unshift(row);
          row = [];
        }
        row.push(imageList[i]);
      }

      newImageList.unshift(row);
      newImageList.reverse();
      return newImageList;
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

    // Gets the information relating to selected destination.
    destinationRepository
      .getDestination(this.userId, this.destId)
      .then(response => {
        this.destination = response.data;
          console.log(this.destination);
          // Gets all the images to display on the page.
          getImages(this.userId, this.destination.defaultAlbumId).then(result => {
              console.log("here")

              this.files = this.groupImages(result.data);
          });
      })
      .catch(err => {
        console.log(err);
      });
  }
};
</script>
