/* eslint-disable */

<template>
  <v-container grid-list-xl v-if="showAddDestination">
    <v-layout justify-space-around="true">
      <v-flex xs12 md6 class="row-input-margin">
        <v-text-field
          v-model="destination.name"
          :counter="10"
          label="Destination Name"
          required
        ></v-text-field>
      </v-flex>

      <v-flex xs12 md6>
        <v-text-field
          v-model="destination.destination_type"
          :counter="10"
          label="Destination Type"
          required
        ></v-text-field>
      </v-flex>
    </v-layout>

    <v-layout>
      <v-flex xs12 md6>
        <v-text-field
          v-model="destination.district"
          :counter="10"
          label="Destination District"
          required
        ></v-text-field>
      </v-flex>

      <v-flex xs12 md6>
        <v-text-field
          v-model="destination.country"
          :counter="10"
          label="Country"
          required
        ></v-text-field>
      </v-flex>
    </v-layout>

    <v-layout>
      <v-flex xs12 md6>
        <v-text-field
          v-model="destination.crd_latitude"
          :counter="10"
          label="Latitude"
          required
        ></v-text-field>
      </v-flex>

      <v-flex xs12 md6>
        <v-text-field v-model="destination.crd_longitude">
          :counter="10"
          label="Longitude"
          type="number"
          required
          >
        </v-text-field>
      </v-flex>
    </v-layout>
    <v-btn class="update-button" v-on:click="createDestination">CREATE DESTINATION</v-btn>
  </v-container>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

/* .container {
  background-color: white;

  align-self: center;
  display: inline-block;
  text-align: left;
} */
</style>


<script>
import {createDestination} from "../../repository/DestinationEditRepository";
import destinations from '../../store/destinations/destinations';

export default {
  props: {
    showAddDestination: Boolean,
    toggleShowCreateDestination: Function
  },
  data() {
    return {
      destination: {}
    };
  },
  methods: {
    createDestination: function() {
      console.log(this.destination.crd_latitude);
      console.log("Destination to create", this.destination);
      createDestination(this.destination).then(result => {
        console.log(result);
        this.toggleShowCreateDestination();
      });
    }
  }
};
</script>
