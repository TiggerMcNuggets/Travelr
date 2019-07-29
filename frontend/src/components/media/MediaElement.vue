<template>
  <v-card flat tile hover class="media-element">
    <v-icon v-if="!media.is_public" class="lock-icon" left>lock</v-icon>
    <div v-if="!media.is_public" class="triangle"></div>
    <v-img

            :src="getImgFromUrl(media.filename)"
            lazy-src="https://picsum.photos/id/11/100/60"
            height="300px"
    >
      <template v-slot:placeholder>
        <v-layout
                fill-height
                align-center
                justify-center
                ma-0
        >
          <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
        </v-layout>
      </template>
    </v-img>
  </v-card>
</template>

<style scoped>
  @import "../../assets/css/style.css";
</style>

<script>
  export default {
		name: "MediaElement",

		props: {
			media: Object,
      getImgFromUrl: Function
		},

		data() {
			return {}
		},

		methods: {

    }
	}
</script>