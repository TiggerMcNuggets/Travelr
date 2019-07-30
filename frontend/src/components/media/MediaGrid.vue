<template>
  <v-container v-bind="{ [`grid-list-xl`]: true }" fluid pt-2 pl-0 pr-0>
    <v-layout row wrap>
      <v-flex v-for="(item, index) in filteredMedia" :key="index" xs12 md3 @click="openElement(item)">
        <AlbumElement v-if="item.content" :album="item" :getImgFromUrl="getImgFromUrl" :fillerImageURL="fillerImageURL"></AlbumElement>
        <MediaElement v-else :media="item" :getImgFromUrl="getImgFromUrl" :fillerImageURL="fillerImageURL"></MediaElement>
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
			return {
        fillerImageURL: 'https://scontent.fwlg1-1.fna.fbcdn.net/v/t1.15752-9/67622453_448347632682894_4713729652491812864_n.png?_nc_cat=100&_nc_oc=AQmwt1aVyhkq6u08MMigxwz83vbJyhYCz4IRAn-rq6R-6BUFm0EC-74ZEgPGe_lO5_c&_nc_ht=scontent.fwlg1-1.fna&oh=485a6544f41ab5fdeac3ff4bbd486a8d&oe=5DAACB1C',
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