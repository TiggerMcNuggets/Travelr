<template>
  <v-flex md5 pa-2>
    <v-container class="no-padding">
      <v-tabs v-model="active" slider-color="blue">
        <v-tab :key="1" ripple>Overview</v-tab>
        <v-tab :key="2" ripple>Comments</v-tab>
        <v-tab :key="3" ripple>Media</v-tab>
        <v-tab :key="4" ripple>Files</v-tab>
        <v-tab :key="5" ripple v-if="!isLarge && !isMedium">Map</v-tab>

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
            :isGroupOwner="isGroupOwner" 
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
import TripMap from "./TripMap.vue";
import DeviceSizeMixin from "../mixins/DeviceSizeMixin";

export default {
  name: "TripDetails",

  props: {
    trip: Object,
    isGroupOwner: Boolean,
    updateTrip: Function
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
