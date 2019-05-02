/* eslint-disable */

<template>

<v-container>

   <v-btn round color="primary" v-on:click="goBack" dark>Go Back</v-btn>
    <v-btn round color="#FF5722" v-on:click="toggleShouldDisplayButton" dark>Edit trip</v-btn>
    <v-alert class="success" :value="modifiedTrip" color="success">Trip edited successfully!</v-alert>
    <v-dialog v-model="shouldDisplayDialog" max-width="100%">
        <create-trip style="background-color: white;"
                     v-if="true"
                     :regetTrips="() => console.log('no need')"
                     :passedTrip="tripId"
                     :updateViewTripPage="this.updateViewTripPage"
        />
    </v-dialog>
    <h1>{{ trip.name }}</h1>
    <v-card>
    <v-list two-line>
    <template v-for="destination in trip.destinations">
            <v-list-tile :key="destination.ordinal">
              <v-list-tile-avatar>
                <img src="https://cdn.vuetifyjs.com/images/lists/1.jpg"><!-- where we will add the link to our primary image -->
              </v-list-tile-avatar>
              <v-list-tile-content>
                <v-list-tile-title v-html="destination.name"></v-list-tile-title>
                <v-list-tile-sub-title v-html="'<span>Arriving On: '+destination.arrivalDate+' - Departing On: '+destination.departureDate+'</span>'"></v-list-tile-sub-title>
              </v-list-tile-content>
            </v-list-tile>
            <v-divider :key="destination.name+destination.ordinal" :inset="is_inset"></v-divider>
          </template>
    </v-list>
    </v-card>
</v-container>

</template>



<script>
import tripRepo from "../../repository/TripRepository";
import { store } from "../../store/index";
import CreateTrips from "./CreateTrips.vue";


export default {
  store,
  // local variables
  data() {
    return {
        tripId:  this.$route.params.trip_id,
        userId:  this.$route.params.user_id,
        modifiedTrip: false,
        is_inset: true,
        trip: {},
        shouldDisplayDialog: false
    };
  },
  methods: {
      goBack: function() {
        window.history.back();
      },

      // decides if the edit trip dialog should be displayed
      toggleShouldDisplayButton: function() {
        this.shouldDisplayDialog = !this.shouldDisplayDialog;
      },

      // toggles the boolean that makes the alert dialog pop up
      toggleModifiedTrip() {
          this.modifiedTrip = !this.modifiedTrip;
      },

      // invoked by child component creat-trip once the trip has been modified, passed as prop
      updateViewTripPage: function() {
          tripRepo.getTrip(this.userId, this.tripId).then((result) => {
              let trip = result.data;
              let ordered_dests = trip.destinations.sort(function(a, b){
                  return a.ordinal - b.ordinal;
              });
              trip.destinations = ordered_dests;
              this.trip = trip;
              this.toggleShouldDisplayButton();
              this.toggleModifiedTrip();
              setTimeout(() => {
                  this.toggleModifiedTrip();
              }, 2000)
          });
      }
  },
    components: {
        CreateTrip: CreateTrips
    },

  created: function() {
      tripRepo.getTrip(this.userId, this.tripId).then((result) => {
          let trip = result.data;
          let ordered_dests = trip.destinations.sort(function(a, b){
              return a.ordinal - b.ordinal;
          });
          trip.destinations = ordered_dests;
          this.trip = trip;
      });
      /*tripRepo.getTrip(this.$route.params.id).then((result) => {
          let trip = result.data;
          let ordered_dests = trip.destinations.sort(function(a, b){
              return a.ordinal - b.ordinal;
          });
          trip.destinations = ordered_dests;
          this.trip = trip;
      });*/
  }
};
</script>
