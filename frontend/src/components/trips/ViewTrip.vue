
<template>
  <v-container fluid>
    <PageHeader
      :title="trip.name"
      :options="[{action : toggleShouldDisplayButton, icon: 'edit'}]"
      disableUndoRedo
      enableBackButton
    />

    <v-timeline align-top>
      <v-dialog v-model="shouldDisplayDialog" max-width="100%">
        <create-trip
          style="background-color: white;"
          v-if="true"
          :regetTrips="() => console.log('no need')"
          :passedTrip="tripId"
          :updateViewTripPage="this.updateViewTripPage"
        />
      </v-dialog>
      <v-timeline-item
        v-for="(destination, i) in trip.destinations"
        :key="i"
        color="red lighten-2"
        fill-dot
      >
        <v-card color="red lighten-2" dark>
          <v-card-title class="title">{{ destination.name }}</v-card-title>
          <v-card-text class="white text--primary">
            <p v-if="destination.arrivalDate != null">Arrival Date: {{ destination.arrivalDate }}</p>
            <p
              v-if="destination.departureDate != null"
            >Departure Date: {{ destination.departureDate }}</p>
            <v-btn
              color="red lighten-2"
              class="mx-0"
              outline
              @click="viewDestination(destination.id)"
            >View Destination</v-btn>
          </v-card-text>
        </v-card>
      </v-timeline-item>
    </v-timeline>
  </v-container>
</template>



<script>
import tripRepo from "../../repository/TripRepository";
import { store } from "../../store/index";
import CreateTrips from "./CreateTrips.vue";
import dateTime from "../common/dateTime/dateTime.js";
import PageHeader from "../common/header/PageHeader";

export default {
  store,
  // local variables
  data() {
    return {
      isMyProfile: false,
      isAdmin: store.getters.getIsUserAdmin,
      tripId: this.$route.params.trip_id,
      userId: this.$route.params.id,
      is_inset: true,
      trip: {},
      shouldDisplayDialog: false
    };
  },
  methods: {
    /**
     * Redirects users page to the destination page of the provided destination id
     * @param dest_id the id of the destination
     */
    viewDestination(dest_id) {
      this.$router.push("/user/" + this.userId + "/destinations/" + dest_id);
    },

    /**
     * Decides if the edit trip dialog should be displayed
     */
    toggleShouldDisplayButton: function() {
      this.shouldDisplayDialog = !this.shouldDisplayDialog;
    },

    /**
     * Invoked by child component create-trip once the trip has been modified, is passed as prop
     */
    updateViewTripPage: function() {
      tripRepo.getTrip(this.userId, this.tripId).then(result => {
        let trip = result.data;
        // Sorts the destinations ensure they are in the order of their ordinal
        let ordered_dests = trip.destinations.sort(function(a, b) {
          return a.ordinal - b.ordinal;
        });
        trip.destinations = ordered_dests;
        // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
        for (let i = 0; i < trip.destinations.length; i++) {
          if (trip.destinations[i].arrivalDate != null) {
            trip.destinations[
              i
            ].arrivalDate = dateTime.convertTimestampToString(
              trip.destinations[i].arrivalDate
            );
          }
          if (trip.destinations[i].arrivalDate != null) {
            trip.destinations[
              i
            ].departureDate = dateTime.convertTimestampToString(
              trip.destinations[i].departureDate
            );
          }
        }
        this.trip = trip;
      });
    }
  },
  components: {
    CreateTrip: CreateTrips,
    PageHeader
  },

  created: function() {
    this.isMyProfile = store.getters.getUser.id == this.$route.params.id;
    // If the person viewing the trip is not admin and does not own the trip then takes them back to the page they were on
    if (!this.isMyProfile && !this.isAdmin) {
      this.$router.go(-1);
    }
    tripRepo.getTrip(this.userId, this.tripId).then(result => {
      let trip = result.data;
      let ordered_dests = trip.destinations.sort(function(a, b) {
        return a.ordinal - b.ordinal;
      });
      trip.destinations = ordered_dests;
      // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
      for (let i = 0; i < trip.destinations.length; i++) {
        if (trip.destinations[i].arrivalDate != null) {
          trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(
            trip.destinations[i].arrivalDate
          );
        }
        if (trip.destinations[i].arrivalDate != null) {
          trip.destinations[
            i
          ].departureDate = dateTime.convertTimestampToString(
            trip.destinations[i].departureDate
          );
        }
      }
      this.trip = trip;
    });
  }
};
</script>
