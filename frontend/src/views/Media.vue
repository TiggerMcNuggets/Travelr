
<template>
  <v-layout d-block pl-4 pt-3>
    <v-flex xs3>
      <h1>Media</h1>
    </v-flex>
    <v-layout row wrap>
      <v-flex xs2>
        <v-list class="list">
          <v-list-tile v-for="(item, i) in filterCategories" :key="i" @click="changeFilter(i)" class="tile">
            <v-list-tile-action v-if="item.icon" class="list-tile-icon">
              <v-icon color="pink">{{item.icon}}</v-icon>
            </v-list-tile-action>

            <v-list-tile-content>
              <v-list-tile-title v-html="item.title"></v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </v-list>
      </v-flex>

      <v-flex xs9>
        <v-container v-bind="{ [`grid-list-lg`]: true }" fluid class="media-container">
          <v-layout row wrap>
            <v-flex v-for="n in 9" :key="n" xs4>
              <v-card flat tile>
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
/* @import "../assets/css/main.scss"; */

  h1 {
    margin: 10px 0px;
  }

  .media-container {
    padding-top: 0;
  }

  .list {
    background: none;
  }

  .list-tile-icon {
    min-width: 30px;
  }

  .tile:hover {
    background: pink;
  }

  .tile:active {
    color: deeppink;
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
      },
      {
        title: 'Upload Media', icon: 'add', dashed: true
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
