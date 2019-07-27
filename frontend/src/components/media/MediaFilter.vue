<template>

  <v-flex xs2>
    <v-list class="list">
      <v-list-tile v-for="(item, i) in filterCategories" :key="i" @click="changeFilter(i)" class="tile">
        <v-list-tile-content>
          <v-list-tile-title v-html="item.title +  ` (` + item.count +`)`"></v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>

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
</style>

<script>
  export default {
    name: "MediaFilter",

    props: {
      changeFilter: Function,
      mediaCounts: Object
    },

    data() {
      return {

      }
    },

    computed: {
      filterCategories: function () {
        return [
          {
            title: 'All', count: this.mediaCounts.all
          },
          {
            title: 'Albums', count: this.mediaCounts.albums
          },
          {
            title: 'Photos', count: this.mediaCounts.images
          },
          {
            title: 'Videos', count: this.mediaCounts.videos
          }
        ];
      }
    },

    methods: {

    }
  }
</script>