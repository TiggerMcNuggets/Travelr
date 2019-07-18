
<template>
  <v-layout d-block pl-4 pt-3>
    <v-flex xs3>
      <h1>Media</h1>
    </v-flex>
    <v-layout row wrap>

      <MediaFilter activeFilter="activeFilter" mediaCounts="mediaCounts"></MediaFilter>

      <MediaGrid media="media"></MediaGrid>

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

  export default {
    name: "Media",

    data() {
      return {
        activeFilter: 0,
        media: null
      }
    },

    components: {
      MediaFilter,
      MediaGrid
    },

    methods: {
      changeFilter(num) {
        this.activeFilter = num;
      }
    }
  }
</script>
