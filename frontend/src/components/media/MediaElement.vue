<template>
    <v-card flat tile hover class="media-element">
        <!--<v-icon v-if="mediaObject.is_public" class="lock-icon" left>lock_open</v-icon>-->
        <v-icon class="lock-icon" left>lock</v-icon>

        <!--<div v-if="mediaObject.is_public" class="triangle"></div>-->
        <div class="triangle pink-color"></div>
        <v-img
                :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"
                height="300px"
        ></v-img>
    </v-card>
</template>

<style scoped>
    @import "../../assets/css/style.css";
</style>

<script>
  export default {
    name: "MediaElement",

    props: {
      mediaObject: Object
    },

    data() {
      return {}
    },

    methods: {}
  }
</script>