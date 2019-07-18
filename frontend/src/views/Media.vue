
<template>
  <v-layout d-block pl-4 pt-3>
    <v-flex xs3>
      <h1>Media</h1>
    </v-flex>
    <v-layout row wrap>

      <MediaFilter :activeFilter="activeFilter" :organisedMedia="organisedMedia"></MediaFilter>

      <MediaGrid :organisedMedia="organisedMedia"></MediaGrid>

    </v-layout>
  </v-layout>
</template>

<style scoped>
  h1 {
    margin: 10px 0px;
  }
</style>

<script>
  import MediaFilter from "../components/media/MediaFilter";
  import MediaGrid from "../components/media/MediaGrid";
  import { temp } from "../components/media/temp";

  export default {
    name: "Media",

    data() {
      return {
        activeFilter: 0,
        albums: temp,
        mediaCounts: null
      }
    },

    components: {
      MediaFilter,
      MediaGrid
    },

    computed: {
      organisedMedia: function () {
        let organisedMedia = {
          "images": [],
          "videos": []
        };

        this.albums.forEach(function (album) {
          album.content.forEach(function (media) {

            if (media.type === "image") {
              organisedMedia.images.push(media);
            } else if (media.type === "video") {
              organisedMedia.videos.push(media);
            }
          });
        });

        return organisedMedia;
      },

      albumCount: function () {
        return this.albums.length;
      }
    },

    methods: {
      changeFilter(num) {
        this.activeFilter = num;
      }
    }

  }
</script>
