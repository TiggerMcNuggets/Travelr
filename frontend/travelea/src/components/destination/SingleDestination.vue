/* eslint-disable */

<template>
  <v-card>
    <div class="outer-container">
      <div class="inner-container">
        <div class="section">
          <div class="dest-name">
            <v-btn
              class="upload-toggle-button"
              fab
              small
              dark
              color="indigo"
              @click="$router.go(-1)"
            >
              <v-icon dark>keyboard_arrow_left</v-icon>
            </v-btn>
        
              <h2 class="headline">{{destination.name}}</h2>
            
          </div>
          <div>
            <v-btn class="upload-toggle-button" fab small dark color="indigo">
              <v-icon dark>lock</v-icon>
            </v-btn>
            <v-btn
              class="upload-toggle-button"
              fab
              small
              dark
              color="indigo"
              v-if="isMyProfile || isAdminUser"
            >
              <v-icon dark>edit</v-icon>
            </v-btn>
            <v-btn
              class="upload-toggle-button"
              fab
              small
              dark
              color="indigo"
              v-if="isMyProfile || isAdminUser"
              @click="toggleShowUploadPhoto"
            >
              <v-icon dark>add</v-icon>
            </v-btn>
          </div>
        </div>
        <v-divider class="photo-header-divider"></v-divider>
         <div class="dest-sub-info">
                <p><strong>Type:</strong> {{destination.type}}</p>
                <span class="dot"></span>
                <p><strong>District:</strong> {{destination.district}}</p>
                <span class="dot"></span>
                <p><strong>Country:</strong> {{destination.country}}</p>
                <span class="dot"></span>
                  <p><strong>Latitude:</strong> {{destination.latitude}}</p>
                <span class="dot"></span>
                <p><strong>Longitude:</strong> {{destination.longitude}}</p>
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
              <v-btn v-on:click="uploadExisting()">Choose Existing</v-btn>
            </div>
          </div>
          <v-alert :value="uploadError" color="error">{{errorText}}</v-alert>
          <v-divider class="photo-header-divider"></v-divider>
        </div>

        <ul>
          <li v-for="row in files" :value="row.value" :key="row.value">
            <div class="personal-photo-row">
              <div
                v-for="item in row"
                :value="item.value"
                :key="item.value"
                class="image-container"
              >
                <v-icon v-if="item.is_public" class="lock-icon" left>lock_open</v-icon>
                <v-icon v-else class="lock-icon" left>lock</v-icon>

                <div v-if="item.is_public" class="triangle pink-color"></div>
                <div v-else class="triangle"></div>

                <v-img
                  @click.stop="dialog = true"
                  v-on:click="setDialogueContent(item)"
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
              <v-spacer></v-spacer>
              <v-switch v-if="clickedImage.is_public" disabled v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>
              <v-switch v-else v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>
              <v-btn color="primary" flat @click="updatePhotoVisability()">Apply changes</v-btn>
            </v-card-actions>
            <v-card-actions>
              <v-btn color="primary" flat @click="dialog = false">Close</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="chooseExistingDialog" width="800">
          <PhotoSelect v-bind="{closeDialogue, setDestinationImages}"/>
        </v-dialog>
      </div>
    </div>
  </v-card>
</template>


<style>
@import "../../assets/css/style.css";
.dot {
  height: 7px;
  width: 7px;
  margin: 0px 10px;
  background-color: #3f51b5;
  border-radius: 50%;
  display: inline-block;
}

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
  margin-left: 70px;
}
.dest-sub-info p {
  margin-bottom: 0px;
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
let destinationRepository = RepositoryFactory.get("destination");
import { store } from "../../store/index";
import {
  storeDestinationImage,
  getImages,
  setProfilePic,
  updateDestinationPhoto,
  addExistingPhoto
} from "../../repository/DestinationPhotoRepository";

export default {
  store,

  components: { PhotoSelect },
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
      destination: {},
      dest_id: null,
      uploadError: false,
      chooseExistingDialog: false,
      errorText:
        "You are trying to upload a duplicate image or an error occured while uploading.",
      
    };
  },

  methods: {
    //sets the user's profile photo as the selected
    setDestinationImages(selectedImages) {
        console.log(this.dest_id)
        for(let i = 0; i < selectedImages.length; i++) {
          console.log(selectedImages[i].photo_filename);
          addExistingPhoto(this.id, this.dest_id, {
            photo_filename: selectedImages[i].photo_filename
          }).then(() => {
          getImages(this.id, this.dest_id).then(result => {
            this.files = this.groupImages(result.data);
          });
        });
        }
      this.closeDialogue();
    },

      uploadExisting() {
      this.chooseExistingDialog = true;
    },

     closeDialogue() {
      this.chooseExistingDialog = false;
    },

    // Sets the file property the the file being uploaded.
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    // Toggles the upload section for the photos
    toggleShowUploadPhoto: function() {
      this.showUploadSection = !this.showUploadSection;
    },

    // Updates whether the photo is public or private depending on the swich state.
    updatePhotoVisability() {
      this.clickedImage.is_public = this.publicPhotoSwitch;
      updateDestinationPhoto(this.clickedImage);
    },

    // Submits the image file and uploads it to the server
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);

      storeDestinationImage(this.id, this.dest_id, formData)
        .then(() => {
          getImages(this.id, this.dest_id).then(result => {
            this.files = this.groupImages(result.data);
          });
        })
        .catch(error => {
          this.uploadError = true;
          this.errorText = error.response.data;
        });
      this.$refs.file.value = "";
    },

    // Gets the image from the server
    getImgUrl(item) {
      return base_url + "/api/destinations/photo/" + item.photo_filename;
    },

    // Gets the local image file path
    setDialogueContent(selectedImage = "") {
      this.dialog = true;
      this.clickedImage = selectedImage;
      this.publicPhotoSwitch = selectedImage.is_public;
      this.clickedImageURL = this.getImgUrl(selectedImage);
      const myImage = new Image();
      myImage.src =
        base_url + "/api/destinations/photo/" + selectedImage.photo_filename;
      this.clickedImageWidth = myImage.width < 400 ? 400 : myImage.width;
    },

    // Groups the images into rows
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

      if (row) newImageList.unshift(row);
      newImageList.reverse();
      return newImageList;
    }
  },

  created: function() {
    this.id = this.$route.params.id;
    this.dest_id = this.$route.params.dest_id;

    if (!this.id) {
      this.id = store.getters.getUser.id;
    }

    this.isMyProfile = store.getters.getUser.id == this.id;
    this.isAdminUser = store.getters.getIsUserAdmin;

    getImages(this.id, this.dest_id).then(result => {
      console.log(result.data);
      this.files = this.groupImages(result.data);
    });

    destinationRepository
      .getDestination(this.id, this.dest_id)
      .then(response => {
        this.destination = response.data;
      })
      .catch(err => {
        console.log(err);
      });
  }
};
</script>
