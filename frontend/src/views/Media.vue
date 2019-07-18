
<template>
  <v-layout d-block pl-4 pt-3>
    <v-flex xs3>
      <h1>Media</h1>
    </v-flex>
    <v-layout row wrap>
      <v-flex xs2>
        <v-list class="list">
          <v-list-tile v-for="(item, i) in filterCategories" :key="i" @click="changeFilter(i)" class="tile">

            <v-list-tile-content>
              <v-list-tile-title v-html="item.title" ></v-list-tile-title>
            </v-list-tile-content>

          </v-list-tile>
        </v-list>

        <v-btn class="upload-button" @click="" flat>
          <v-icon left dark>add</v-icon>
          Upload
        </v-btn>

      </v-flex>

      <v-flex xs9>
        <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
          <v-layout row wrap>
            <v-flex v-for="n in 9" :key="n" xs12 md4>
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
    </v-layout>
  </v-layout>
</template>

<style scoped>
  @import "../assets/css/style.css";

  h1 {
    margin: 10px 0px;
  }

  .media-container {
    padding-top: 0;
  }

  .media-element:hover {
    border-color: pink;
  }

  .list {
    background: none;
  }

  .tile:hover {
    background: none;
  }

  .tile:active {
    color: deeppink;
  }

  .upload-button {
    margin-top: 30px;
    margin-left: 0px;
    height: 48px;
  }

</style>


<script>
  export default {
    data: () => ({
      activeFilter: 0,
      filterCategories: [
        {
          title: 'All <span class="grey--text text--lighten-1">(12)</span>',
        },
        {
          title: 'Albums <span class="grey--text text--lighten-1">(2)</span>',
        },
        {
          title: 'Photos <span class="grey--text text--lighten-1">(9)</span>',
        },
        {
          title: 'Videos <span class="grey--text text--lighten-1">(3)</span>',
        }
      ],

    }),

    methods: {
      changeFilter(num) {
        this.activeFilter = num;
      }
    }
  };
</script>
