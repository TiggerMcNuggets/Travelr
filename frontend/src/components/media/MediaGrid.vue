<template>
  <v-flex xs9>
    <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
      <v-layout row wrap>
        <v-flex v-for="(item, index) in displayedMedia" :key="index" xs12 md4>
          {{ item }}
          <v-card flat tile class="media-element">
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
  h1 {
    margin: 10px 0px;
  }

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