/* eslint-disable */

<template>
<v-container>
   <v-btn round color="primary" v-on:click="goBack" dark>Go Back</v-btn>
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

export default {
  store,
  // local variables
  data() {
    return {
      is_inset: true,
      trip: {}
    };
  },
  methods: {
    goBack: function() {
      window.history.back();
    }
  },
  created: function() {
      tripRepo.getTrip(this.$route.params.id).then((result) => {
          let trip = result.data;
          console.log(trip);
          let ordered_dests = trip.destinations.sort(function(a, b){
              return a.ordinal - b.ordinal;
          });
          trip.destinations = ordered_dests;
          this.trip = trip;
      });
  }
};
</script>
