<template>
  <v-container class="no-padding">
    <v-tabs v-model="active" slider-color="blue">
      <v-tab :key="1" ripple>Overview</v-tab>
      <v-tab :key="2" ripple>Comments</v-tab>
      <v-tab :key="3" ripple>Albums</v-tab>

      <v-tab-item :key="1">
        <TripOverview :trip="trip"/>
      </v-tab-item>

      <v-tab-item :key="2">
        <TripComments :trip="trip"/>
      </v-tab-item>

      <v-tab-item :key="3">
        <TripAlbum :trip="trip"/>
      </v-tab-item>
    </v-tabs>
  </v-container>
</template>

<style>
</style>

<script>
import TripOverview from "./TripOverview";
import TripComments from "./TripComments";
import TripAlbum from "./TripAlbum";

export default {
  name: "TripDetails",

  props: {
    trip: Object
  },

  data() {
    return {
      active: null
    };
  },

  components: {
    TripOverview,
    TripComments,
    TripAlbum
  },

  methods: {}
};
</script>
