<template>
  <v-flex lg4 md6 sm6 xs12 pa-2>
    <v-container class="no-padding">
      <v-tabs v-model="active" slider-color="blue">
        <v-tab :key="1" ripple>Overview</v-tab>
        <v-tab :key="2" ripple>Comments</v-tab>
        <v-tab :key="3" ripple>Media</v-tab>
        <v-tab :key="4" ripple>Files</v-tab>
        <v-tab :key="5" ripple v-if="isExtraSmall || isSmall || isMedium">Map</v-tab>

        <v-tab-item :key="1">
          <TripOverview 
            :trip="trip" 
            :updateTrip="updateTrip"
          />
        </v-tab-item>

        <v-tab-item :key="2">
          <TripComments :trip="trip" />
        </v-tab-item>

        <v-tab-item :key="3">
          <TripAlbum :trip="trip" />
        </v-tab-item>

        <v-tab-item :key="4">
          <TripFiles 
            :trip="trip" 
            :hasWritePermissions="hasWritePermissions"
            :pushStack="pushStack"
          />
        </v-tab-item>

        <v-tab-item :key="5">
          <TripMap 
            :nodes="trip.trip.nodes"
          />
        </v-tab-item>

      </v-tabs>
    </v-container>
  </v-flex>
</template>

<style>
</style>

<script>
import TripOverview from "./TripOverview";
import TripComments from "./TripComments";
import TripAlbum from "./TripAlbum";
import TripFiles from "./TripFiles";
import TripMap from "./TripMap";
import DeviceSizeMixin from "../mixins/DeviceSizeMixin";

export default {
  name: "TripDetails",

  props: {
    trip: Object,
    hasWritePermissions: Boolean,
    updateTrip: Function,
    pushStack: Function
  },

  data() {
    return {
      active: null
    };
  },

  components: {
    TripOverview,
    TripComments,
    TripAlbum,
    TripFiles,
    TripMap
  },

  mixins: [
    DeviceSizeMixin
  ],

  methods: {}
};
</script>
