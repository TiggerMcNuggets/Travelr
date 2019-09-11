

<template>
  <v-layout >
    <v-flex xs12 ma-2>
      <UserStatusList :tripId="trip.trip.id"/>
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

  data() {
    return {};
  },


  methods: {}
};
</script>
