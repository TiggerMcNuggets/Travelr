<template>
  <v-flex xs9>
    <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
      <v-layout row wrap>
        <v-flex v-for="(item, index) in displayedMedia" :key="index" xs12 md4>
          <Album v-if="item.content" :albumContent="item"></Album>
          <MediaElement v-else :mediaObject="item"></MediaElement>
        </v-flex>
      </v-layout>
    </v-container>
  </v-flex>
</template>

<style scoped>
  .media-container {
    padding-top: 0;
  }
</style>

<script>
  import Album from "./Album";
  import MediaElement from "./MediaElement";

  export default {
    name: "MediaGrid",

    props: {
      displayedMedia: Array
    },

    components: {
      Album,
      MediaElement
    },

    data() {
      return {}
    },

    methods: {}
  }
</script>