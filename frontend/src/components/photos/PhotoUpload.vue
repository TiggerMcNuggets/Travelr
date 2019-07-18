

<template>
  <v-card>
    <div class="outer-container">
      <div class="inner-container">
        <v-card-title primary-title>
          <h2 class="headline">Upload Photo</h2>
        </v-card-title>

        <v-divider></v-divider>

        <div id='dropzone'>Drag photos or click to open file explorer.</div>

        <ul>
          <li v-for="file in files" >
            <!-- <div class="personal-photo-row"> -->
              <!-- <div
                v-for="item in row"
                :value="item.value"
                :key="item.value"
                class="select-image-container"
              >
                <v-icon v-if="imageIsSelected(item)" class="lock-icon" left>done</v-icon>
                <div v-if="imageIsSelected(item)" class="triangle image-selected"></div> -->
                <v-img
                  v-on:click="selectImage(item)"
                  class="personal-photo-element"
                  :src="file"
                ></v-img>
                  <!-- <img
                   class="personal-photo-element"
                  :src="file" /> -->
                <!-- <v-img
                  v-on:click="selectImage(item)"
                  class="personal-photo-element"
                  :src="(file)"
                ></v-img> -->
              <!-- </div> -->
            <!-- </div> -->
          </li>
        </ul>
      </div>
    </div>
    <v-divider></v-divider>
    <v-btn color="primary" flat @click="closeDialogue">Close</v-btn>
    <v-btn color="primary" flat v-on:click="processSelected()">Add Photos to Destination</v-btn>
  </v-card>
</template>


<style>

.dropzone {
    background: white;
    border-radius: 5px;
    border: 2px dashed rgb(0, 135, 247);
    border-image: none;
    max-width: 500px;
    margin-left: auto;
    margin-right: auto;
}
.image-selected {
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 70px 70px 0 0;
  border-color: greenyellow transparent transparent transparent !important;
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
  margin-top: 0.2em;
  margin-left: 0.2em;
  align-self: flex-start !important;
}

.select-image-container {
  width: 24%;
  height: 170px;
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

#dropzone {
    /* background: #08c;
    color: #fff;
    padding: 100px 0;
    text-align: center; */
      background: white;
    border-radius: 5px;
    border: 2px dashed rgb(0, 135, 247);
    border-image: none;
    padding: 50px;
    /* max-width: 500px;
    margin-left: auto;
    margin-right: auto; */
  }
  #dropzone.dragover {
    background: rgba(0, 135, 247, 1);;
  }

</style>


<script>
import { store } from "../../store/index";
import base_url from "../../repository/BaseUrl";
import { getImages } from "../../repository/PersonalPhotosRepository";

export default {
  store,

  // local variables
  data() {
    return {
      files: [],
      clickedImageURL: "",
      id: null,
      selectedImages: []
    };
  },

  // props methods being passed in from parent.
  props: ["closeDialogue", "setDestinationImages"],

  methods: {

    
    /**
     * Sets the user's profile photo as the selected
     */
    processSelected() {
      this.setDestinationImages(this.selectedImages);
    },

    /**
     * Gets the image url for the server to get the image.
     * @param item The photo item
     * @returns {string}
     */
    getImgUrl(item) {
      return base_url + "/api/travellers/photo/" + item.photo_filename;
    },

    //
    /**
     * Checks if the photo is selected that is it is present in the selected images list.
     * @param selectedImage The image that is been clicked to check
     * @returns {boolean} Whether the image is selected or not.
     */
    imageIsSelected(selectedImage) {
      for (let i = 0; i <= this.selectedImages.length; i++) {
        if (selectedImage === this.selectedImages[i]) {
          return true;
        }
      }
      return false;
    },

    /**
     * Deselects the image and removes from the list of selected images.
     * @param selectedImage The image to remove from the selected images.
     */
    unselectImage(selectedImage) {
      for (let i = 0; i <= this.selectedImages.length; i++) {
        if (selectedImage === this.selectedImages[i]) {
          this.selectedImages.splice(i, 1);
        }
      }
    },


    /**
     * Toggles if the image is selected and adding or removing from selected list.
     * @param selectedImage The image which was clicked.
     */
    selectImage(selectedImage) {
      this.imageIsSelected(selectedImage)
        ? this.unselectImage(selectedImage)
        : this.selectedImages.push(selectedImage);
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

  triggerCallback(e, callback) {
    var files;
    if(e.dataTransfer) {
      files = e.dataTransfer.files;
    } else if(e.target) {
      files = e.target.files;
    }
    callback.call(null, files);
  },

  makeDroppable(ele, callback) {
    // console.log("here")
    // console.log(ele)
    // callback("stuffs")
      var input = document.createElement('input');
      input.setAttribute('type', 'file');
      input.setAttribute('multiple', true);
      input.style.display = 'none';
      input.addEventListener('change', (e) => {
        this.triggerCallback(e, callback);
      });
      ele.appendChild(input);
      
      ele.addEventListener('dragover', function(e) {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.add('dragover');
      });

      ele.addEventListener('dragleave', function(e) {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.remove('dragover');
      });

      ele.addEventListener('drop', (e) => {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.remove('dragover');
        this.triggerCallback(e, callback);
      });
      
      ele.addEventListener('click', function() {
        input.value = null;
        input.click();
      });
    }


  },

  mounted: function() {
     var dropzone = document.getElementById("dropzone");

    this.makeDroppable(dropzone, files => {
      console.log("made it to the call back yay!")
      console.log(files);
      // this.files = [...files].map((image) => {
        for (let i = 0; i < files.length; i++) {
          let file = files[i];
          console.log(file);
      var image = new Image();
      var reader = new FileReader();
      var vm = this;

      reader.onload = (e) => {
        this.files.push(e.target.result);
      };
      reader.readAsDataURL(file);
      // console.log(processedThing)
      // this.files.push(processedThing);
      };

          console.log(this.files);
      // var output = document.querySelector('.output');
      // output.innerHTML = '';
      // for(var i=0; i<files.length; i++) {
      //   if(files[i].type.indexOf('image/') === 0) {
      //     output.innerHTML += '<img width="200" src="' + URL.createObjectURL(files[i]) + '" />';
      //   }
      //   output.innerHTML += '<p>'+files[i].name+'</p>';
      // }
    });
  },
  

  /**
   * Initialises the component with the image data.
   */
  created: function() {

   

    this.id = this.$route.params.id;

    getImages(this.id).then(result => {
      this.files = this.groupImages(result.data);
    });
  }
};
</script>
