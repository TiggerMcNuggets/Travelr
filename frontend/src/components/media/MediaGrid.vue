<template>
  <v-container v-bind="{ [`grid-list-xl`]: true }" fluid pt-2 pl-0 pr-0>
    <v-layout row wrap>
      <v-flex v-for="(item, index) in filteredMedia" :key="index" xs12 md3 @click="openElement(item)">
        <AlbumElement v-if="item.content" :album="item" :getImgFromUrl="getImgFromUrl"></AlbumElement>
        <MediaElement v-else :media="item" :getImgFromUrl="getImgFromUrl"></MediaElement>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
	import AlbumElement from "./AlbumElement";
	import MediaElement from "./MediaElement";
  import base_url from "../../repository/BaseUrl";

	export default {
		name: "MediaGrid",

		props: {
			filteredMedia: Array,
			viewingAlbum: Boolean,
			openElement: Function
		},

		components: {
			AlbumElement,
			MediaElement
		},

		data() {
			return {}
		},

		methods: {
      /**
       * Gets the image src url for the server
       * @param filename The photo item to get the image for.
       * @returns {string} A url of where to find the photo to set as src
       */
      getImgFromUrl(filename) {
        return base_url + `/api/users/${this.$route.params.id}/media/` + filename;
      }
    }

  }
</script>