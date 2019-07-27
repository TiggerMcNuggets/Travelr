<template>
  <v-flex xs10>
    <v-container v-bind="{ [`grid-list-xl`]: true }" fluid class="media-container">
      <v-layout row wrap>
        <v-flex v-for="(item, index) in filteredMedia" :key="index" xs12 md3 @click="open(item)">
          <AlbumElement v-if="item.content" :album="item" ></AlbumElement>
          <MediaElement v-else :mediaObject="item"></MediaElement>
        </v-flex>
      </v-layout>
    </v-container>
  </v-flex>
</template>

<style scoped>
  .media-container {
    padding-top: 10px;
  }
</style>

<script>
  import AlbumElement from "./AlbumElement";
  import MediaElement from "./MediaElement";

  export default {
    name: "MediaGrid",

    props: {
      filteredMedia: Array
    },

    components: {
      AlbumElement,
      MediaElement
    },

    data() {
      return {
      }
    },

    methods: {
      open(item) {

        // Trying to open an album
        if (item.content) {
          this.$router.push(`/album/${item.id}`);
        } else {
        // Trying to open an image/video/media

        }

      }
    }
  }
</script>