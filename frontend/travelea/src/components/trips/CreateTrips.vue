/* eslint-disable */

<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="width-for-container">
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>Create a new trip</h4>
        <v-layout justify-space-around="true">
          <v-flex xs12 md12 class="row-input-margin">
            <v-text-field v-model="trip.title" :rules="nameRules" :counter="20" label="Trip Name"></v-text-field>
          </v-flex>
        </v-layout>
        <ul>
          <li v-for="(destination, index) in trip.destinations" :v-bind="index" :key="index">
            <v-card class="destination-form-padding">
              <v-layout>
                <v-flex xs12 md4>
                  <v-combobox
                    :rules="noSameDestinationNameConsecutiveRule"
                    :items="userDestinations.map(dest => dest.name)"
                    v-model="destination.title"
                    label="Select an existing destination"
                  ></v-combobox>
                  <v-btn
                    flat
                    small
                    color="error"
                    v-if="index > 1"
                    v-on:click="deleteDestination(index)"
                  >Remove</v-btn>
                </v-flex>

                <v-flex xs12 md4>
                  <v-card class="times-padding">
                    <!-- Arrival date -->
                    <v-menu
                      v-model="destination.arrivalDateMenu"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on }">
                        <v-text-field
                          v-model="destination.arrivalDate"
                          :rules="arrivalBeforeDepartureAndDestinationsOneAfterTheOther"
                          label="Arrival date"
                          prepend-icon="event"
                          readonly
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="destination.arrivalDate"
                        @input="destination.arrivalDateMenu = false"
                      ></v-date-picker>
                    </v-menu>
                  </v-card>
                </v-flex>
                <v-flex xs12 md4>
                  <v-card class="times-padding">
                    <!-- Departure date -->
                    <v-menu
                      v-model="destination.departureDateMenu"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on }">
                        <v-text-field
                          v-model="destination.departureDate"
                          :rules="arrivalBeforeDepartureAndDestinationsOneAfterTheOther"
                          label="Departure date"
                          prepend-icon="event"
                          readonly
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="destination.departureDate"
                        @input="destination.departureDateMenu = false"
                      ></v-date-picker>
                    </v-menu>
                  </v-card>
                </v-flex>
                <v-tooltip right>
                  <template v-slot:activator="{ on }">
                    <v-btn flat icon color="red lighten-2" v-on:click="resetDestinationDate(index)">
                      <v-icon color="red" dark v-on="on">remove_circle</v-icon>
                    </v-btn>
                  </template>
                  <span>Reset dates</span>
                </v-tooltip>
              </v-layout>
            </v-card>
          </li>
        </ul>
        <div>
          <v-btn color="red" v-on:click="resetValues">RESET</v-btn>
          <v-btn v-on:click="createTrip">CREATE TRIP</v-btn>
          <v-btn v-on:click="addDestinationToTrip">ADD DESTINATION</v-btn>
        </div>
      </v-container>
    </v-form>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.width-for-container {
  width: 60%;
}
.destination-form-padding {
  padding: 2em;
}
.times-padding {
  padding: 1em;
}
</style>


<script>
import { store } from "../../store/index";
import moment from "moment";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let tripRepository = RepositoryFactory.get("trip");
let destinationRepository = RepositoryFactory.get("destination");
import {
  rules,
  noSameDestinationNameConsecutiveRule,
  arrivalBeforeDepartureAndDestinationsOneAfterTheOther
} from "../form_rules";

export default {
  store,
  components: {},
  props: {
    toggleShowCreateTrip: Function,
    regetTrips: Function
  },
  data() {
    return {
      trip: {
        title: "",
        destinations: [
          {
            title: null,
            arrivalDate: null,
            departureDate: null,
            arrivalDateMenu: false,
            departureDateMenu: false
          },
          {
            title: null,
            arrivalDate: null,
            departureDate: null,
            arrivalDateMenu: false,
            departureDateMenu: false
          }
        ]
      },
      userDestinations: [],
      ...rules
    };
  },
  computed: {
    noSameDestinationNameConsecutiveRule() {
      return noSameDestinationNameConsecutiveRule(this.trip.destinations);
    },
    arrivalBeforeDepartureAndDestinationsOneAfterTheOther() {
      return arrivalBeforeDepartureAndDestinationsOneAfterTheOther(
        this.trip.destinations
      );
    }
  },
  methods: {
    getDestinations: function() {
      destinationRepository
        .getDestinations()
        .then(res => {
          this.userDestinations = res.data;
        })
        .catch(e => {
          console.log(e);
        });
    },
    resetValues: function() {
      this.$refs.form.reset();
    },
    resetDestinationDate: function(destIndex) {
      let newDestinations = this.trip.destinations;
      newDestinations[destIndex].arrivalDate = null;
      newDestinations[destIndex].departureDate = null;
      this.trip.destinations = newDestinations;
    },
    deleteDestination: function(index) {
      let newDestinations = this.trip.destinations;
      newDestinations.splice(index, 1);
      this.trip.destinations = newDestinations;
    },
    addDestinationToTrip: function() {
      const template = {
        title: null,
        arrivalDate: null,
        departureDate: null,
        arrivalDateMenu: false,
        departureDateMenu: false
      };
      let newDestinations = this.trip.destinations;
      newDestinations.push(template);
      this.trip.destinations = newDestinations;
    },
    createTrip: function() {
      if (this.$refs.form.validate()) {
        let trip = { name: this.trip.title, destinations: [] };
        this.trip.destinations.forEach((destination, index) => {
          const destById = this.userDestinations.find(
            dest => destination.title === dest.name
          );
          trip.destinations.push({
            id: destById.id,
            ordinal: index,
            arrivalDate: moment(destination.arrivalDate).unix(),
            departureDate: moment(destination.departureDate).unix()
          });
        });
        console.log("Trip", trip.destinations[0].departureDate);
        tripRepository
          .createTrip(trip)
          .then(res => {
            this.regetTrips();
          })
          .catch(e => {
            console.log(e);
          });
      }
    }
  },
  mounted() {
    this.getDestinations();
  }
};
</script>