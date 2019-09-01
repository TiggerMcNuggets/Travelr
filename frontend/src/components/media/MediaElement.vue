<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-card flat tile hover class="media-element" @click="openElement">
    <v-icon v-if="!media.is_public" class="lock-icon" left>lock</v-icon>
    <div v-if="!media.is_public" class="triangle"></div>
    <v-img :src="getImgFromUrl(media.filename)" :lazy-src="fillerImageURL" aspect-ratio="1">
      <template v-slot:placeholder>
        <v-layout fill-height align-center justify-center ma-0>
          <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
        </v-layout>
      </template>
    </v-img>
  </v-card>
</template>

<style scoped>
@import "../../assets/css/style.css";
</style>

<script>
export default {
  name: "MediaElement",

  props: {
    media: Object,
    getImgFromUrl: Function,
    fillerImageURL: String,
    openElement: Function
  },

  data() {
    return {};
  },

  methods: {}
};
</script>