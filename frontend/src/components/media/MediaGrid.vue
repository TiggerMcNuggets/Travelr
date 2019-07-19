<template>
  <v-container v-bind="{ [`grid-list-lg`]: true }" ma-0 pa-0 fluid class="media-container">
    <v-layout row wrap>
      <v-flex v-for="file in media" xs12 md4>
        <v-card flat tile class="media-element">
              <v-hover>
                  <v-card
      slot-scope="{ hover }"
      class="mx-auto"
      color="grey lighten-4"
      max-width="600"
    >
          <v-img :src="file" height="200px"><v-expand-transition>
          <div
            v-if="hover"
            class="d-flex transition-fast-in-fast-out red darken-2 v-card--reveal display-3 white--text"
            style="height: 100%;"
          >
            <v-icon>delete</v-icon>
          </div>
        </v-expand-transition></v-img>    </v-card></v-hover>
          <v-btn></v-btn>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style scoped>

.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: .5;
  position: absolute;
  width: 100%;
}


h1 {
  margin: 10px 0px;
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
  name: "MediaGrid",

  props: {
    media: Array
  },

  updated() {
    console.log("media");
    console.log(this.media);
  },

  data() {
    return {};
  },

  methods: {}
};
</script>
