/* eslint-disable */

<template>
  <div class="width-for-container">
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl v-if="showAddDestination">
        <h4>Add new destination</h4>
        <v-layout justify-space-around="true">
          <v-flex xs12 md6 class="row-input-margin">
            <v-text-field
              v-model="destination.name"
              :counter="20"
              label="Destination Name"
              required
              :rules="nameRules"
            ></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field
              v-model="destination.destination_type"
              :counter="20"
              :rules="nameRules"
              label="Destination Type"
              required
            ></v-text-field>
          </v-flex>
        </v-layout>

        <v-layout>
          <v-flex xs12 md6>
            <v-text-field
              v-model="destination.district"
              :counter="20"
              :rules="nameRules"
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
              :rules="nameRules"
            ></v-text-field>
          </v-flex>
        </v-layout>

        <v-layout>
          <v-flex xs12 md6>
            <v-text-field
              v-model.number="destination.crd_latitude"
              type="number"
              :rules="numberRules"
              label="Latitude"
              required
            ></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field
              v-model.number="destination.crd_longitude"
              type="number"
              :rules="numberRules"
              label="Longitude"
              required
            ></v-text-field>
          </v-flex>
        </v-layout>
        <div>
          <v-btn color="red" v-on:click="resetValues">RESET VALUES</v-btn>
          <v-btn v-on:click="createDestination">CREATE DESTINATION</v-btn>
        </div>
      </v-container>
    </v-form>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.width-for-container {
  width: 60%;
}
</style>


<script>
import { createDestination } from "../../repository/DestinationEditRepository";
import { rules } from "../form_rules";
import { store } from "../../store/index";

export default {
  store,
  props: {
    showAddDestination: Boolean,
    toggleShowCreateDestination: Function
  },
  data() {
    return {
      destination: {},
      ...rules
    };
  },
  methods: {
    createDestination: function() {
      console.log("Destination to create", this.destination);
      if (this.$refs.form.validate()) {
        createDestination(this.destination).then(() => {
          this.$refs.form.reset();
          this.toggleShowCreateDestination();
          store.commit("setDestinations");
        });
      }
    },
    resetValues: function() {
      this.$refs.form.reset();
    }
  }
};
</script>
