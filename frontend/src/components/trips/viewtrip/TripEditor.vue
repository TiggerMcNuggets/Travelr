<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-flex lg3 md6 sm6 xs12 pa-2 v-if="selectedTrip">
    <v-breadcrumbs :items="selectedTrip.navigation" class="trip-breadcrumbs">
      <template v-slot:item="props">
        <v-breadcrumbs-item v-on:click="getSelectedTrip(props.item.id)">{{ props.item.name }}</v-breadcrumbs-item>
      </template>
    </v-breadcrumbs>

    <v-form lazy-validation ref="form" v-model="isFormValid">
      <v-layout v-if="canEdit" flex>
        <v-btn @click="addTripNode(false)" outline color="error" class="no-margin">Add Trip</v-btn>
        <v-btn @click="addTripNode(true)" outline color="error" class="add-destination-button">Add Destination</v-btn>
      </v-layout>

      <v-container>
        <v-alert :value="hasAdjacentIdentical" color="error">
          Cannot have same destination
          consecutive.
        </v-alert>
      </v-container>

      <TripTimeline
        :trip="selectedTrip"
        :getSelectedTrip="getSelectedTrip"
        :setSelectedTrip="_setSelectedTrip"
        :updateTrip="updateTrip"
        :canEdit="canEdit"
      />
    </v-form>
  </v-flex>
</template>

<style>
  .add-destination-button {
    margin: 0px 0px 0px 10px;
  }
</style>

<script>
  import TripTimeline from "./TripTimeline";
  import StoreTripsMixin from "../../mixins/StoreTripsMixin";
  import { deepCopy } from "../../../tools/deepCopy";

  export default {
    name: "TripEditor",

    props: {
      updateTrip: Function,
      hasAdjacentIdentical: Boolean,
      canEdit: Boolean
    },

    components: {
      TripTimeline
    },

    mixins: [
      StoreTripsMixin
    ],

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
    },
    }
  };
</script>
