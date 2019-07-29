<template>
  <v-layout>
    <v-flex mb-3>
      <v-flex ml-0 pl-0>
        <span class="title">{{ album.name }}</span>
      </v-flex>
      <v-card flat tile hover>

        <v-icon v-if="!album.public" class="lock-icon" left>lock</v-icon>
        <div v-if="!album.public" class="triangle"></div>

        <!--:src="getImgFromUrl(album.content[n].filename)"-->

        <div class="grid-4-container">
          <v-img
                  v-for="n in 4"
                  :key="n"
                  src=""
                  lazy-src="https://picsum.photos/id/11/100/60"
                  :height="145">

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
        </div>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<style scoped>
  @import "../../assets/css/style.css";

  .grid-4-container {
    display: grid;
    grid-template-columns: calc(50% - 5px) calc(50% - 5px);
    grid-template-rows: calc(50% - 5px) calc(50% - 5px);
    grid-gap: 10px;
    transition: opacity .4s ease-in-out;
  }
</style>

<script>
	export default {
		name: "Album",

		props: {
			album: Object,
      getImgFromUrl: Function
		},

		data() {
			return {}
		},

		methods: {

    }
	}
</script>