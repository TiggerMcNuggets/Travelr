<template>

  <v-list class="list">
    <v-list-tile :disabled="item.count == 0" v-for="(item, i) in filterCategories" :key="i" @click="changeFilter(item.title)" class="tile">
      <v-list-tile-content >
        <v-list-tile-title  v-html="item.title +  ` (` + item.count +`)`"></v-list-tile-title>
      </v-list-tile-content>
    </v-list-tile>
  </v-list>

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
			return {}
		},

		computed: {
			filterCategories: function () {
				return [
					{
						title: 'All', count: this.mediaCounts.all
					},
					{
						title: 'Photos', count: this.mediaCounts.photos
					},
					{
						title: 'Videos', count: this.mediaCounts.videos
					}
				];
			}
		},

		methods: {}
	}
</script>