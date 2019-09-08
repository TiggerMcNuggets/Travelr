<template>
  <v-container fluid>
    <v-tabs v-model="active" slider-color="blue">
      <v-tab :key="1" ripple>Overview</v-tab>
      <v-tab :key="2" ripple>Comments</v-tab>
      <v-tab :key="3" ripple>Files</v-tab>

      <v-tab-item :key="1">
        <TripOverview/>
      </v-tab-item>

      <v-tab-item :key="2">
        <TripComments/>
      </v-tab-item>

      <v-tab-item :key="3">
        <TripAlbum/>
      </v-tab-item>
    </v-tabs>
  </v-container>
</template>

<style>
</style>

<script>
import PageHeader from "../common/header/PageHeader";
import TripOverview from "./TripOverview";
import TripComments from "./TripComments";
import TripAlbum from "./TripAlbum";

export default {
  name: "Trip Details",

  data() {
    return {
      active: null
    };
  },

  components: {
    PageHeader,
    TripOverview,
    TripComments,
    TripAlbum
  },

  methods: {}
};
</script>
