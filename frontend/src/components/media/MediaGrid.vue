<template>
  <v-flex xs9>
    <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
      <v-layout row wrap>
        <v-flex v-for="n in 15" :key="n" xs12 md4>
          <v-card flat tile class="media-element">
            <!--
            <v-icon v-if="item.is_public" class="lock-icon" left>lock_open</v-icon>
            <v-icon v-else class="lock-icon" left>lock</v-icon>

            <div v-if="item.is_public" class="triangle"></div>
            <div v-else class="triangle pink-color"></div>
-->
            <v-img
                    src="https://www.canterbury.ac.nz/engineering/contact-us/people/1485116691697_Moffat-Mathews-low.jpg"
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
      media: Array
    },

    data() {
      return {

      }
    },

    methods: {

    }
  }
</script>
