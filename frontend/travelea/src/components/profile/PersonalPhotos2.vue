/* eslint-disable */

<template>
  <div class="outer-container">
    <div class="inner-container">
      <div class="section">
        <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>
        <h2 class="headline">PHOTOS</h2>

        <v-btn
          class="upload-toggle-button"
          fab
          small
          dark
          color="indigo"
          @click="toggleShowUploadPhoto"
          v-if="isMyProfile"
        >
          <v-icon dark>add</v-icon>
        </v-btn>
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
          <v-btn v-on:click="submitFile()">Upload Photo</v-btn>
        </div>
        <v-divider class="photo-header-divider"></v-divider>
      </div>

      <ul>
        <li v-for="row in files" :value="row.value" :key="row.value">
          <div class="personal-photo-row">
            <div v-for="item in row" :value="item.value" :key="item.value" class="image-container">
              <v-icon v-if="item.is_public" class="lock-icon" left>lock_open</v-icon>
              <v-icon v-else class="lock-icon" left>lock</v-icon>
                
                <div v-if="item.is_public" class="triangle pink-color" > </div>
                <div v-else class="triangle" > </div>
                
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
            <v-switch v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>
            <v-btn color="primary" flat @click="updatePhotoVisability()">Apply changes</v-btn>
            <v-btn color="primary" flat @clocklick="setProfilePhoto()">Set Profile Photo</v-btn>
          </v-card-actions>
          <v-card-actions>
          <v-btn color="primary" flat @click="dialog = false">Close</v-btn>
          </v-card-actions>  
        </v-card>
      </v-dialog>
    
    </div>
  </div>
</template>relative


<style>

.pink-color {
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 100px 100px 0 0;
  border-color: hotpink transparent transparent transparent !important;
  opacity: 0.3;
  position: absolute;
  z-index: 10;

}


.lock-icon {
  color: white !important;
  opacity: 1;
  position: absolute;   
  z-index: 12;
  font-size: 2.3em;
  margin-top: 0.3em;
  margin-left: 0.3em;
  align-self: flex-start !important;
}

.triangle {
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 100px 100px 0 0;
  border-color: #007bff transparent transparent transparent;
  opacity: 0.3;
  position: absolute;
  z-index: 10;

}

.choose-file-button {
  background-color: #f5f5f5;
  color: rgba(0, 0, 0, 0.87);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;

  align-items: center;
  border-radius: 2px;
  display: inline-flex;
  height: 36px;
  flex: 0 0 auto;
  font-size: 14px;
  font-weight: 500;
  justify-content: center;
  margin: 6px 8px;
  min-width: 88px;
  outline: 0;
  text-transform: uppercase;
  text-decoration: none;
  position: relative;
  vertical-align: middle;
}

.upload-section {
  padding: 20px 0px;
}

.upload-toggle-button {
  position: relative;
  top: 10px;
  align-self: flex-end;
}

.photo-header-divider {
  margin-top: 16px;
}

.image-container {
  width: 24%;
  height: 270px;
  border: 1px solid lightgrey;
  background-position: center;
  padding: 7px;
  overflow: hidden;
  display: flex;
  justify-content: flex-start;
}

.image-container:hover .personal-photo-element {
  cursor: pointer;
  opacity: 0.8;
}

.personal-photo-element {
  height: 100%;
  overflow: hidden;
}

.personal-photo-row {
  display: flex;
  justify-content: space-evenly;
  margin-bottom: 30px;
}

ul {
  padding-left: 0px;
}

h2 {
  align-self: flex-end;
}

hr {
  margin-bottom: 25px;
}

.section {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.outer-container {
  text-align: center;
  padding-bottom: 15px;
}

.inner-container {
  margin: 0px 20px;
}

.buttons-div {
  margin-top: 2em;
}

.update-button {
  margin-top: 1em;
  width: 49%;
}
</style>


<script>
import { store } from "../../store/index";
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
      isMyProfile: false
    };
  },

  methods: {
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
      updatePersonalPhoto(this.clickedImage);
    },

    //sets the user's profile photo as the selected
    setProfilePhoto() {
      setProfilePic(this.id, {"photo_filename": this.clickedImage.photo_filename}).then(() => {
        window.location = "/profile/photos";
      });
    },

    // Submits the image file and uploads it to the server
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);

      storeImage(this.id, formData).then(() => {
        getImages(this.id).then(result => {
          this.files = this.groupImages(result.data);
         
        });
      });
    },

    // Gets the image from the server
    getImgUrl(item) {
      return "http://localhost:9000/assets/images/" + item.photo_filename;
    },

    // Gets the local image file path
    setDialogueContent(selectedImage = "") {

      this.dialog = true;
      this.clickedImage = selectedImage;
      this.publicPhotoSwitch = selectedImage.is_public;
      this.clickedImageURL = this.getImgUrl(selectedImage);
      const myImage = new Image();
      myImage.src = "http://localhost:9000/assets/images/" + selectedImage.photo_filename;
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
    // committing to the store like this allows you to trigger the setDestinations mutation you can find in the destinations module for the store
    // store.commit("setPersonalImages", this.$route.params.id);
    this.id = this.$route.params.id;
    
    if(!this.id) { 
      this.id = store.getters.getUser.id
    }

    this.isMyProfile = (store.getters.getUser.id == this.id)
    getImages(this.id).then(result => {
      this.files = this.groupImages(result.data);
    });
  }
};
</script>
