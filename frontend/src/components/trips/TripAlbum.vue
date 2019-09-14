<template>
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex >
        <MediaGrid
          :filteredMedia="files"
          :openElement="() => {}"
          :getAllAlbums="() => {}"
          :openEditAlbumDialog="() => {}"
          smallGrid
        ></MediaGrid>
      </v-flex>
    </v-flex>
  </v-layout>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import MediaGrid from "../media/MediaGrid";
let mediaRepository = RepositoryFactory.get("media");

export default {
  name: "TripAlbums",

  components: {
    MediaGrid,
  },

  props: {
    trip: Object
  },

  data() {
    return {
      files: [],
      };
  },

  watch: {
    trip: function() {
      this.getTripImages()
    }
  },

  methods: {
    /**
     * Gets all the trip photos within the trip album
     */
    getTripImages() {
      mediaRepository
        .getAlbumContent(
          this.trip.root.user.id,
          this.trip.root.albumId
        )
        .then(response => {
          response.data.forEach(item => {
            item.filename = item.uriString;
          });
          this.files = response.data;
        });
    }
  },

  created: function() {
    if (this.trip.root.albumId) {
      this.getTripImages();
    }
  }
  
};
</script>
