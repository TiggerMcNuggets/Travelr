

<template>
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <UserStatusList :usergroup="trip.trip.usergroup"/>
    </v-flex>
  </v-layout>
</template>

<script>
import UserStatusList from './tripgroups/UserStatusList'

export default {
  name: "TripOverview",

  props: {
    trip: Object
  },

  components: {
    UserStatusList
  },

  
  updated() {
    console.log("Trip!!!!!!!!!!!!!!!!!!!")
    console.log(this.trip.trip.usergroup)
  },

  data() {
    return {};
  },

  computed: {
    // usergroup() {
    //   console.log(this.$store.getters.getSelectedTrip.trip)
    //   return this.$store.getters.getSelectedTrip.trip.usergroup
    // }
  },

  methods: {}
};
</script>
