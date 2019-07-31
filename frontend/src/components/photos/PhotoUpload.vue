

<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Upload Media</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <div id="dropzone">Drag photos or click to open file explorer.</div>
      <v-flex mt-3>
        <MediaGridWithDelete :media="files" :deletePhoto="deleteImage"/>
      </v-flex>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn ma-3 flat v-on:click="closeMediaUploadDialog()">Cancel</v-btn>
        <v-btn ma-3 color="primary" flat v-on:click="uploadMedia(rawFiles)">Upload Media</v-btn>
    </v-card-actions>
  </v-card>
</template>


<style>

#dropzone {
  background: white;
  border-radius: 5px;
  border: 2px dashed rgb(0, 135, 247);
  border-image: none;
  padding: 50px;
}
#dropzone.dragover {
  background: rgba(0, 135, 247, 0.4);
}

</style>


<script>
import MediaGridWithDelete from "../media/MediaGridWIthDelete";

export default {

  components: {
    MediaGridWithDelete
  },

  props: {
    uploadMedia: Function,
    closeMediaUploadDialog: Function
  },

  // local variables
  data() {
    return {
      files: [],
      rawFiles: []
    };
  },

  methods: {
    /**
     * Deselects the image and removes from the list of selected images.
     * @param selectedImage The image to remove from the selected images.
     */
    deleteImage(selectedImage) {
      for (let i = 0; i <= this.files.length; i++) {
        if (selectedImage === this.files[i]) {
          this.files.splice(i, 1);
          this.rawFiles.splice(i, 1);
        }
      }
    },

    triggerCallback(e, callback) {
      let files;
      if (e.dataTransfer) {
        files = e.dataTransfer.files;
      } else if (e.target) {
        files = e.target.files;
      }
      callback.call(null, files);
    },

    makeDroppable(ele, callback) {
      let input = document.createElement("input");
      input.setAttribute("type", "file");
      input.setAttribute("multiple", true);
      input.style.display = "none";
      input.addEventListener("change", e => {
        this.triggerCallback(e, callback);
      });
      ele.appendChild(input);

      ele.addEventListener("dragover", function(e) {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.add("dragover");
      });

      ele.addEventListener("dragleave", function(e) {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.remove("dragover");
      });

      ele.addEventListener("drop", e => {
        e.preventDefault();
        e.stopPropagation();
        ele.classList.remove("dragover");
        this.triggerCallback(e, callback);
      });

      ele.addEventListener("click", function() {
        input.value = null;
        input.click();
      });
    }
  },

  mounted: function() {
    let dropzone = document.getElementById("dropzone");

    this.makeDroppable(dropzone, files => {
      this.rawFiles = files;
      for (let i = 0; i < files.length; i++) {
        let file = files[i];
        let reader = new FileReader();

        reader.onload = e => {
          this.files.push(e.target.result);
        };
        reader.readAsDataURL(file);
      }
    });
  },

  /**
   * Initialises the component with the`image data.
   */
  created: function() {
    this.files = []; // This should actually be done when upload is clicked!
  }
};
</script>
