<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-container>
    <v-form lazy-validation ref="form" v-model="isFormValid">
      <v-layout row wrap>
        <v-btn @click="addTripNode(false)" color="error">
          <v-icon small>airplanemode_active</v-icon>&nbsp Add Trip
        </v-btn>
        <v-btn @click="addTripNode(true)" color="error">
          <v-icon small>room</v-icon>&nbsp Add Destination
        </v-btn>
      </v-layout>

      <v-container>
        <v-alert :value="hasAdjacentIdentical" color="error">
          Cannot have same destination
          consecutive.
        </v-alert>
      </v-container>

      <TripTimeline
        class="scrollable-y"
        :trip="selectedTrip"
        :getSelectedTrip="getSelectedTrip"
        :setSelectedTrip="_setSelectedTrip"
        :updateTrip="updateTrip"
      />
    </v-form>
  </v-container>
</template>

<style lang="scss">
</style>

<script>
import TripTimeline from "./TripTimeline";
import StoreTripsMixin from "../../mixins/StoreTripsMixin";
import { deepCopy } from "../../../tools/deepCopy";

export default {
  name: "TripEditor",

  props: {
    updateTrip: Function,
    hasAdjacentIdentical: Boolean
  },

  components: {
    TripTimeline
  },

  mixins: [StoreTripsMixin],

  data() {
    return {
      userId: this.$route.params.id,
      isFormValid: true
    };
  },

  methods: {
    /**
     * Adds a node to the trip destinations
     * @param {boolean} isDestination
     */
    addTripNode(isDestination) {
      let trip = deepCopy(this.selectedTrip);
      trip.trip.nodes.push({
        name: "",
        type: isDestination ? "destination" : "trip",
        arrivalDate: null,
        departureDate: null,
        arrivalDateMenu: false,
        departureDateMenu: false,
        destination: {
          name: null
        },
        notCreated: true
      });
      this._setSelectedTrip(trip);
    },

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
