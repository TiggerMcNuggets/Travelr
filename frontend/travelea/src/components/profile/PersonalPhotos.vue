/* eslint-disable */

<template>
  <div class="outer-container">
  <!-- <v-container > -->
     <div fluid class="banner">
    <!-- <div class="banner"> -->
      <h1>MY PHOTOS</h1>
      <hr>
     </div>
    
    <!-- </div> -->
    <!-- <div class="container"> -->
     <v-container>
      <!-- <h2 class="headline mb-0">UPLOAD PHOTO</h2>
      <v-divider></v-divider>-->

      <div class="section">
        <h2 class='headline'>MY PHOTOS</h2>
        <v-btn class="upload-toggle-button" fab small dark color="indigo"  @click="toggleShowUploadPhoto">
          <v-icon dark>add</v-icon>
        </v-btn>
      </div>
      <v-divider class='photo-header-divider'></v-divider>
      <div  v-if="showUploadSection" >
      <div class="upload-section  section">
        <label>
          <input class='choose-file-button' type="file" id="file" ref="file" v-on:change="handleFileUpload()">
        </label>
        <v-btn v-on:click="submitFile()">Upload Photo</v-btn>
       </div>
        <v-divider class='photo-header-divider'></v-divider>
      </div>
      
      <!-- <hr> -->

      <ul>
        <li v-for="row in files" :value="row.value" :key="row.value">
          <div class="personal-photo-row">
            <div v-for="item in row" :value="item.value" :key="item.value" class="image-container">
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
      <!-- </v-container> -->
    <!-- </div> -->
    

    <v-dialog v-model="dialog" width="500">
      <v-card>
        <v-img :src="clickedImageURL" class="dialogue-image"></v-img>

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
          <v-btn color="primary" flat @click="dialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
  </div>
</template>


<style>
/*.v-dialog__content {
  width: auto !important;
}

.dialogue-image {
  height: auto;
}*/
.choose-file-button {
  background-color: #f5f5f5;
  color: rgba(0,0,0,0.87);
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
  align-self: flex-end;
}

.photo-header-divider {
  margin-top:16px;
}

.image-container {
  width: 24%;
  height: 270px;
  border: 1px solid lightgrey;

  background-position: center;
  padding: 7px;
  overflow: hidden;
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
}

.buttons-div {
  margin-top: 2em;
}

.update-button {
  margin-top: 1em;
  width: 49%;
}

.container {
  margin: 10px 100px;
  align-self: center;
  display: inline-block;
  text-align: left;
}

.banner {
  height: 300px;
  width: 100%;

  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url("https://gallery.yopriceville.com/var/albums/Backgrounds/Autumn_Landscape_Background.jpg?m=1442666745");
  background-position: center;
}

.banner h1 {
  font-family: "Karla", sans-serif;
  text-align: center;
  color: white;
  padding-top: 60px;
  font-size: 65px;
  font-weight: bold;
}

.banner hr {
  margin: 10px 200px;
  margin-top: 30px;
  color: white;
  opacity: 0.5;
}
</style>


<script>
import { store } from "../../store/index";
import {
  storeImage,
  getImages,
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
      dialog: false,
      publicPhotoSwitch: false,
      showUploadSection: false
    };
  },

  // computed: {
  //     personalPhotos() {
  //     return store.state.personalPhotos.personalPhotos;
  //   }
  // },

  methods: {
    // Sets the file property the the file being uploaded.
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    toggleShowUploadPhoto: function() {
      this.showUploadSection = !this.showUploadSection;
    },

    updatePhotoVisability() {
      this.clickedImage.is_public = this.publicPhotoSwitch;
      updatePersonalPhoto(this.clickedImage);
    },

    // Submits the image file and uploads it to the server
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);

      storeImage(this.$route.params.id, formData).then(() => {
        getImages(this.$route.params.id).then(result => {
          this.files = this.groupImages(result);
        });
      });
    },

    // Gets the local image file path
    getImgUrl(item, place = "somewhere else") {
      return require("../../../../../backend/resources/images/" +
        item.photo_filename);
    },

    // Gets the local image file path
    setDialogueContent(selectedImage = "") {
      this.dialog = true;
      this.clickedImage = selectedImage;
      this.publicPhotoSwitch = selectedImage.is_public;
      this.clickedImageURL = this.getImgUrl(selectedImage);
    },

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
    getImages(this.$route.params.id).then(result => {
      this.files = this.groupImages(result);
    });
  }
};
</script>
