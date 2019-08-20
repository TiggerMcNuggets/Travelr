<template>
  <v-container v-bind="{ [`grid-list-lg`]: true }" ma-0 pa-0 fluid class="media-container">
    <v-layout row wrap>
      <v-flex v-for="file in media.entries()" :key="file[0]" xs12 md4>
        <v-hover>
          <v-card slot-scope="{ hover }" class="mx-auto" color="grey lighten-4" max-width="600">
            <v-img
              class="media-item"
              :src="file[1]"
              height="200px"
              v-on:click="deletePhoto ? deletePhoto(file[1]) : () => {}"
            >
              <v-flex v-if="hover" class="hover-white fill-height">
                <v-icon size="50" color="white">delete</v-icon>
              </v-flex>
            </v-img>
          </v-card>
        </v-hover>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style scoped>
.hover-white {
  background-color: rgba(250, 170, 255, 0.3);
  display: flex;
  align-items: center;
  bottom: 0;
  justify-content: center;
}

.media-item:hover {
  cursor: pointer;
}

.media-container {
  padding-top: 0;
}

.media-element:hover {
  border-color: pink;
}
</style>

<script>
export default {
  name: "MediaGridWithDelete",

  props: {
    media: Array,
    deletePhoto: Function
  }
};
</script>
