<template>
    <v-card flat tile hover class="media-element">

        <!--<v-icon v-if="albumContent.is_public" class="lock-icon" left>lock_open</v-icon>-->
        <v-icon class="lock-icon" left>photo_library</v-icon>

        <!--<div v-if="albumContent.is_public" class="triangle"></div>-->
        <div class="triangle"></div>

        <v-img
                :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"
                height="300px"
        ></v-img>
    </v-card>
</template>>

<style scoped>
    @import "../../assets/css/style.css";
</style>

<script>
  export default {
    name: "Album",

    props: {
      albumContent: Object
    },

    data() {
      return {}
    },

    methods: {}
  }
</script>