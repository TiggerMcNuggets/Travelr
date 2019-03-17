/* eslint-disable */

<template>
  <div class="width-for-container">
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>Create a new trip</h4>
        <v-layout justify-space-around="true">
          <v-flex xs12 md6 class="row-input-margin">
            <v-text-field v-model="trip.name" :counter="20" label="Trip Name" required></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field :counter="20" label="Destination Type" required></v-text-field>
          </v-flex>
        </v-layout>
        <ul>
          <li v-for="(destination, index) in trip.destinations" :v-bind="index">
            <v-card class="destination-form-padding">
              <v-layout>
                <v-flex xs12 md4>
                  <v-combobox
                    :items="destinationsNamesAndIndex"
                    v-model="destination.title"
                    label="Select an existing destination"
                  ></v-combobox>
                </v-flex>

                <v-flex xs12 md4>
                  <v-text-field :counter="10" label="Country" required></v-text-field>
                </v-flex>
                <v-flex xs12 md4>
                  <v-text-field :counter="10" label="Country" required></v-text-field>
                </v-flex>
              </v-layout>
            </v-card>
          </li>
        </ul>

        <v-layout>
          <v-flex xs12 md6>
            <v-text-field label="Latitude" required></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field type="number" label="Longitude" required></v-text-field>
          </v-flex>
        </v-layout>
        <div>
          <v-btn color="red" v-on:click="resetValues">RESET VALUES</v-btn>
          <v-btn>CREATE DESTINATION</v-btn>
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
.destination-form-padding {
  padding: 2em;
}
</style>


<script>
// import { store } from "../../store/index";
import moment from "moment";

export default {
  // store,
  props: {},
  data() {
    return {
      trip: {
        destinations: [
          {
            title: "Ciao",
            arrivalTime: moment(),
            departureTime: moment().add(1, "day")
          },
          {
            title: "Ciao",
            arrivalTime: moment(),
            departureTime: moment().add(1, "day")
          }
        ]
      }
    };
  },
  computed: {
    destinations() {
      return [
        { title: "Ciao", something: "Something" },
        { title: "bUONGIORNO", something: "Something" }
      ];
      // return store.state.destinations.destinations;
    },
    destinationsNamesAndIndex() {
      // return store.state.destinations.destinations.map(
      return this.destinations.map(destination => destination.title);
    }
  },
  methods: {
    resetValues: function() {
      this.$refs.form.reset();
    }
  }
};
</script>