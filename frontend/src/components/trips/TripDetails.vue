<template>
  <v-container>
    <v-breadcrumbs :items="trip.navigation" class="trip-breadcrumbs">
      <template v-slot:item="props">
        <v-breadcrumbs-item v-on:click="getSelectedTrip(props.item.id)">{{ props.item.name }}</v-breadcrumbs-item>
      </template>
    </v-breadcrumbs>
    <v-tabs v-model="active" slider-color="blue">
      <v-tab :key="1" ripple>Overview</v-tab>
      <v-tab :key="2" ripple>Comments</v-tab>
      <v-tab :key="3" ripple>Media</v-tab>
      <v-tab :key="4" ripple>Files</v-tab>
      <v-tab :key="5" ripple v-if="isExtraSmall || isSmall || isMedium">Map</v-tab>

      <v-tab-item class="scrollable-y" :key="1">
        <TripOverview :trip="trip" :updateTrip="updateTrip" />
      </v-tab-item>

      <v-tab-item :key="2">
        <TripComments :trip="trip" />
      </v-tab-item>

      <v-tab-item class="scrollable-y" :key="3">
        <TripAlbum :trip="trip" />
      </v-tab-item>

      <v-tab-item class="scrollable-y" :key="4">
        <TripFiles :trip="trip" :hasWritePermissions="hasWritePermissions" :pushStack="pushStack" />
      </v-tab-item>

      <v-tab-item class="scrollable-y" :key="5">
        <TripMap :nodes="trip.trip.nodes" />
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
import TripFiles from "./TripFiles";
import TripMap from "./TripMap";
import DeviceSizeMixin from "../mixins/DeviceSizeMixin";
import StoreTripsMixin from "../mixins/StoreTripsMixin";
import { deepCopy } from "../../tools/deepCopy";

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
      userId: this.$route.params.id,
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

  mixins: [DeviceSizeMixin, StoreTripsMixin],

  methods: {
    /**
     * Gets the selected trip by its id
     * @param tripId The trip id
     */
    getSelectedTrip(tripId) {
      this._getTrip(this.userId, tripId).then(() => {
        let trip = deepCopy(this.selectedTrip);
        this._setSelectedTrip(trip);
      });
    }
  }
};
</script>
