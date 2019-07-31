<template>
  <v-container v-bind="{ [`grid-list-xl`]: true }" fluid pt-2 pl-0 pr-0>
    <v-layout row wrap>
      <v-flex v-for="(item, index) in filteredMedia" :key="index" xs12 md3>
        <AlbumElement v-if="item.content" :album="item" :getImgFromUrl="getImgFromUrl" :fillerImageURL="fillerImageURL" :getAllAlbums="getAllAlbums" :openEditAlbumDialog="openEditAlbumDialog" :openElement="() => openElement(item)"></AlbumElement>
        <MediaElement v-else :media="item" :getImgFromUrl="getImgFromUrl" :fillerImageURL="fillerImageURL" :openElement="() => openElement(item)"></MediaElement>
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
            openElement: Function,
            getAllAlbums: Function,
            openEditAlbumDialog: Function
		},

		components: {
			AlbumElement,
			MediaElement
		},

		data() {
			return {
        fillerImageURL: 'https://scontent.fwlg1-1.fna.fbcdn.net/v/t1.15752-9/67378175_348500139423638_1827459196619063296_n.png?_nc_cat=110&_nc_oc=AQk_S4fCW-LL3HlYKvsLYrv3qvB1o1FSUcC7wRMsRw0PNysZF8yNa7EAsOalje3gDeg&_nc_ht=scontent.fwlg1-1.fna&oh=d80948deee840eaf09db4ad4b37ee3c4&oe=5DED9897',
      }
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