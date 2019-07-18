<template>
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
</template>

<style scoped>
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
  name: "MediaFilter",

  props: {
    updateMediaFilter: Function,
    mediaCounts: Array
  },

  data() {
    return {
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
      ]
    }
  },

  methods: {

  }
}
</script>
