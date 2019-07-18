<template>
  <v-flex xs9>
    <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
      <v-layout row wrap>
        <v-flex v-for="n in 38" :key="n" xs12 md4>
          <v-card flat tile class="media-element">
            <!--
            <v-icon v-if="item.is_public" class="lock-icon" left>lock_open</v-icon>
            <v-icon v-else class="lock-icon" left>lock</v-icon>

            <div v-if="item.is_public" class="triangle"></div>
            <div v-else class="triangle pink-color"></div>
-->
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
      organisedMedia: Object
    },

    data() {
      return {

      }
    },

    methods: {

    }
  }
</script>
