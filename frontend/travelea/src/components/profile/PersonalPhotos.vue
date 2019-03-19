/* eslint-disable */

<template>
  <div class="outer-container">
    <div class="banner">
      <h1>MY PHOTOS</h1>
      <hr>
    </div>
    <div class="container">
      <h2>UPLOAD PHOTO</h2>
      <hr>
      <div class="flex-space-between">
        <label>
          <input type="file" id="file" ref="file" v-on:change="handleFileUpload()">
        </label>
        <v-btn v-on:click="submitFile()">Submit</v-btn>
      </div>
    </div>
  </div>
</template>


<style>
h2 {
  padding-bottom: 10px;
}

hr {
  margin-bottom: 25px;
}

input {
}

.flex-space-between {
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
import { storeImage } from "../../repository/PersonalPhotosRepository";

export default {
  store,
  // local variables
  data() {
    return {
      file: ""
    };
  },
  methods: {

    // Sets the file property the the file being uploaded.
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    // Submits the image file and uploads it to the server
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);
      storeImage(this.$route.params.id, formData);
    }
  }
};
</script>
