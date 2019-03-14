/* eslint-disable */

<template>
  <v-container grid-list-xl v-if="showAddDestination">
    <v-layout justify-space-around="true">
      <v-flex xs12 md6 class="row-input-margin">
        <v-text-field
          v-model="destination.name"
          :rules="nameRules"
          :counter="10"
          label="Destination Name"
          required
        ></v-text-field>
      </v-flex>

      <v-flex xs12 md6>
        <v-text-field
          v-model="destination.destination_type"
          :rules="nameRules"
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
          :rules="nameRules"
          :counter="10"
          label="Destination District"
          required
        ></v-text-field>
      </v-flex>

      <v-flex xs12 md6>
        <v-text-field
          v-model="destination.country"
          :rules="nameRules"
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
          :rules="nameRules"
          :counter="10"
          label="Latitude"
          required
        ></v-text-field>
      </v-flex>

      <v-flex xs12 md6>
        <v-text-field v-model="destination.crd_longitude" :rules="nameResult < 20">
          :counter="10"
          label="Longitude"
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
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepostory = RepositoryFactory.get("destination");

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
      destinationRepostory.createDestination(this.destination).then(result => {
        console.log(result);
        this.toggleShowCreateDestination();
      });
    }
  }
};
</script>
