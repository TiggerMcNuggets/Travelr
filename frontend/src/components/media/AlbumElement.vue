<template>
  <v-layout>
    <v-flex mb-3>
      <v-flex ml-0 pl-0>
        <span class="title">{{ album.name }}</span>
      </v-flex>
      <v-card flat tile hover>

        <v-icon v-if="!album.public" class="lock-icon" left>lock</v-icon>
        <div v-if="!album.public" class="triangle"></div>
        <div class="grid-4-container">
          <v-img
                  v-for="(thumbnail, id) in albumThumbnails"
                  :key="id"
                  :src="thumbnail"
                  :lazy-src="fillerImageURL"
                  :height="145">

            <template v-slot:placeholder>
              <v-layout
                      fill-height
                      align-center
                      justify-center
                      ma-0
              >
                <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
              </v-layout>
            </template>
          </v-img>
        </div>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<style scoped>
  @import "../../assets/css/style.css";

  .grid-4-container {
    display: grid;
    grid-template-columns: calc(50% - 5px) calc(50% - 5px);
    grid-template-rows: calc(50% - 5px) calc(50% - 5px);
    grid-gap: 10px;
    transition: opacity .4s ease-in-out;
  }
</style>

<script>
  export default {
    name: "Album",

    props: {
      album: Object,
      getImgFromUrl: Function,
      fillerImageURL: String
    },

    data() {
      return {}
    },

    computed: {

      /**
       * Computes the thumbnails to be shown in the 2x2 grid album preview.
       * Fills the empty spaces with a specified filler image.
       */
      albumThumbnails: function () {
        let thumbnails = [];
        let numberOfThumbnails = this.album.content.length < 4 ? this.album.content.length : 4;
        let requiredFillerImages = 4 - numberOfThumbnails;

        for (let i = 0; i < numberOfThumbnails; i++) {
          thumbnails.push(this.getImgFromUrl(this.album.content[i].name))
        }

        for (let i = 0; i < requiredFillerImages; i++) {
          thumbnails.push(this.fillerImageURL);
        }

        return thumbnails;
      }
    }
  }
</script>