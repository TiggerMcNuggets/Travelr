

<template>
  <div class="outer-container">
    <div class="inner-container">
      <div class="section">
        <div class="dest-name">
          <h2 class="headline">Personal Photos</h2>
        </div>
        <div>
          <v-btn
            class="upload-toggle-button"
            fab
            small
            dark
            color="indigo"
            @click="toggleShowUploadPhoto"
            v-if="isMyProfile || isAdminUser"
          >
            <v-icon dark>add</v-icon>
          </v-btn>
        </div>
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
            />
          </label>
          <v-btn v-on:click="submitFile()">Upload Photo</v-btn>
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

              <div v-if="item.is_public" class="triangle"></div>
              <div v-else class="triangle pink-color"></div>

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

      <v-dialog v-model="dialog" :width="clickedImageWidth">
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
            <div v-if="isAdminUser || isMyProfile" class="photo-popup-options">
              <v-switch v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>

              <v-btn @click="updatePhotoVisability">Apply changes</v-btn>
              <v-btn @click="setProfilePhoto">Set Profile Photo</v-btn>
            </div>
          </v-card-actions>
          <v-card-actions>
            <v-btn color="red" @click="dialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>


<style>
@import "../../assets/css/style.css";
</style>


<script>
import { store } from "../../store/index";
import base_url from "../../repository/BaseUrl";
import {
  storeImage,
  getImages,
  setProfilePic,
  updatePersonalPhoto
} from "../../repository/PersonalPhotosRepository";

export default {
  store,
  // local variables
  data() {
    return {
      files: [],
      clickedImageURL: "",
      clickedImage: {},
      clickedImageWidth: 0,
      dialog: false,
      publicPhotoSwitch: false,
      showUploadSection: false,
      id: null,
      isMyProfile: false,
      isAdminUser: false,
      uploadError: false,
      uploadSuccessful: false,
      errorText:
        "You are trying to upload a duplicate image or an error occured while uploading."
    };
  },

  methods: {
    /**
     * Sets the file property the the file being uploaded.
     */
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    /**
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
      updatePersonalPhoto(this.clickedImage);
    },

    /**
     * Sets the user's profile photo as the selected
     */
    setProfilePhoto() {
      setProfilePic(this.id, {
        photo_filename: this.clickedImage.photo_filename
      }).then(() => {
        window.location = "/user/" + this.id + "/photos";
      });
    },

    /**
     * Submits the image file and uploads it to the server
     */
    submitFile() {
      this.uploadError = false;
      this.uploadSuccessful = false;
      let formData = new FormData();
      formData.append("picture", this.file);

      storeImage(this.id, formData)
        .then(() => {
          getImages(this.id).then(result => {
            this.files = this.groupImages(result.data);
            this.uploadSuccessful = true;
          });
        })
        .catch(error => {
          this.uploadError = true;
          this.errorText = error.response.data;
        });

      this.$refs.file.value = "";
    },

    //
    /**
     * Gets the image for the server
     * @param item The photo item
     * @returns {string} The url to get that image.
     */
    getImgUrl(item) {
      return base_url + "/api/travellers/photo/" + item.photo_filename;
    },

    /**
     * Sets the width of the dialog based on image width.
     * @param selectedImage
     */
    setDialogContent(selectedImage = "") {
      this.dialog = true;
      this.clickedImage = selectedImage;
      this.publicPhotoSwitch = selectedImage.is_public;
      this.clickedImageURL = this.getImgUrl(selectedImage);
      const myImage = new Image();
      myImage.src =
        base_url + "/api/travellers/photo/" + selectedImage.photo_filename;
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
    }
  },

  /**
   * Initialises user id and image data on component creation.
   */
  created: function() {
    this.id = this.$route.params.id;

    if (!this.id) {
      this.id = store.getters.getUser.id;
    }

    this.isMyProfile = store.getters.getUser.id == this.id;
    this.isAdminUser = store.getters.getIsUserAdmin;

    getImages(this.id).then(result => {
      this.files = this.groupImages(result.data);
    });
  }
};
</script>
