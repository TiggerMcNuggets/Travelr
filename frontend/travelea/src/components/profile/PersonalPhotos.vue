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
      <div class="section">
        <label>
          <input type="file" id="file" ref="file" v-on:change="handleFileUpload()">
        </label>
        <v-btn v-on:click="submitFile()">Submit</v-btn>
      </div>


      <h2>MY PHOTOS</h2>
      <hr>

      <ul>
      <li
        
        v-for="row in groupImages(files)"
        :value="row.value"
        :key="row.value"
      >
      <div
        class="personal-photo-row"
        
      >
        <!-- <p>{{item.photo_filepath}}</p> -->
        <div v-for="item in row"
        :value="item.value"
        :key="item.value" class='image-container'>
        <img class="personal-photo-element" :src="getImgUrl(item)">
        </div>
        <!-- <img src='../../../../../backend/resources/images/avatar.jpg'> -->
      </div>

      </li>
    </ul>




    </div>
  </div>
</template>


<style>
.image-container {
   width: 250px;;
   height: 250px;
   border: 1px solid grey;

     overflow: hidden;
      background-position: center;
}

.image-container:hover .personal-photo-element {
  opacity: 0.8;
}



.personal-photo-element {
   height: 100%;
  
  overflow: hidden;
 
}

.personal-photo-row {
   display: flex;
   justify-content: space-between;
   margin-bottom: 30px;
}

ul {
  padding-left: 0px;
}

h2 {
  padding-bottom: 10px;
}

hr {
  margin-bottom: 25px;
}

input {
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
import { storeImage, getImages } from "../../repository/PersonalPhotosRepository";

export default {
  store,
  // local variables
  data() {
    return {
      files: []
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

    // Submits the image file and uploads it to the server
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);
      storeImage(this.$route.params.id, formData).then (() => {
         getImages(this.$route.params.id).then((result) => {
          this.files = result;
         }
      );
      });
     
    },

    // Gets the local image file path
     getImgUrl(item) {
      return require('../../../../../backend/resources/images/' + item.photo_filename)
    },

    groupImages(imageList) {
      let newImageList = [];
      let row = [];
      const num_cols = 4
      for(let i = 0; i < this.files.length; i++) {
        row.push(this.files[i]);
        if (i % num_cols === 0 && row.length !== 0) {
          newImageList.unshift(row);
          row = []
        }
      }
      console.log('new image list')
      console.log(newImageList);
      return newImageList;
    }
  },

  created: function() {
    // committing to the store like this allows you to trigger the setDestinations mutation you can find in the destinations module for the store
    // store.commit("setPersonalImages", this.$route.params.id);
    getImages(this.$route.params.id).then((result) => {
      console.log(result)
      this.files = result;
    } );


  
  }
};
</script>
