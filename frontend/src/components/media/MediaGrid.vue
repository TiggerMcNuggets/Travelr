<template>
  <v-flex xs9>
    <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
      <v-layout row wrap>
        <v-flex v-for="(item, index) in displayedMedia" :key="index" xs12 md4>
          <v-card flat tile class="media-element" v-if="item.content">
            <v-img
                    :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"
                    height="300px"
            ></v-img>
          </v-card>
          <v-card flat tile class="media-element" v-else>
            <v-img
                    :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"
                    height="300px"
            ></v-img>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>
  </v-flex>
</template>

<style scoped>
  .media-container {
    padding-top: 0;
  }

  .media-element:hover {
    border-color: pink;
  }
</style>

<script>
  export default {
    name: "MediaGrid",

    props: {
      displayedMedia: Object
    },

    data() {
      return {}
    },

    methods: {}
  }
</script>