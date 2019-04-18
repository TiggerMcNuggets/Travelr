/* eslint-disable */

<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="width-for-container">
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>{{this.dialogName}}</h4>
        <v-layout>
          <v-flex xs12 md12 class="row-input-margin">
            <v-text-field v-model="trip.title" :rules="nameRules" :counter="20" label="Trip Name"></v-text-field>
          </v-flex>
        </v-layout>
        <ul>
          <draggable
                  :list="trip.destinations"
                  :disabled="!draggableEnabled"
                  class="list-group"
                  ghost-class="ghost"
                  @start="dragging = true"
                  @end="dragging = false"
          >
          <li v-for="(destination, index) in trip.destinations" :v-bind="index" :key="index" class="list-group-item">
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
          </draggable>
        </ul>
        <div>
          <v-btn color="red" v-on:click="resetValues">RESET</v-btn>
          <v-btn v-on:click="onConfirm">CONFIRM</v-btn>
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

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}
.list-group-item {
  cursor: move;
}
.list-group-item:hover i {
  cursor: pointer !important;
}
</style>


<script>
import { store } from "../../store/index";
import moment from "moment";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import draggable from 'vuedraggable';
import {
  rules,
  noSameDestinationNameConsecutiveRule,
  arrivalBeforeDepartureAndDestinationsOneAfterTheOther
} from "../form_rules";

let tripRepository = RepositoryFactory.get("trip");
let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,
  components: {
      draggable: draggable
  },
  props: {
    toggleShowCreateTrip: Function,
    regetTrips: Function,
    passedTrip: String,
    updateViewTripPage: Function
  },
  data() {
    return {
      tripToDisplay: null,
      draggableEnabled: true,
      dialogName: "Create a new trip",
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
    },
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

    onConfirm: function() {
      if(this.passedTrip === null){
        this.createTrip();
      } else {
          this.updateTrip();
      }
    },

    createTrip: function() {
      if (this.$refs.form.validate()) {
        const trip = this.tripAssembler();
        tripRepository
          .createTrip(trip)
          .then( () => {
            this.regetTrips();
          })
          .catch(e => {
            console.log(e);
          });
      }
    },

    updateTrip: function() {
        if (this.$refs.form.validate()) {
            const trip = this.tripAssembler();
            tripRepository
                .updateTrip(parseInt(this.passedTrip),trip)
                .then( () => {
                    this.updateViewTripPage()
                })
                .catch(e => {
                    console.log(e);
                });
        }
    },

    /**
     * Creates a trip object from the data passed that conforms with the API specs
     * @return trip
     **/
    tripAssembler: function() {
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
    return trip;
    }
  },
    /**
     * When the component finished mounting, the destinations for the combobox are retrieved and then
     * in case the parent component passed a valid trip id, the trip is retrieved and the
     * existing trip params populate the form.
     * Makes component usable for both create and edit component
     */
    mounted() {
      this.getDestinations();
      if (this.passedTrip !== null) {
        console.log('past if');
        this.dialogName = "Edit current trip";
        let tripToEdit = {title: '', destinations: []};
        tripRepository.getTrip(this.passedTrip)
            .then((result) => {
                const tripById = result.data;
                tripToEdit.title = tripById.name;
                console.log(tripById);
                for (let i = 0; i < tripById.destinations.length; i++){
                  const destToAdd = {};
                  const currentDest = tripById.destinations[i];
                  destToAdd.title = currentDest.name;
                  destToAdd.arrivalDate = currentDest.arrivalDate === null ? null : moment.unix(currentDest.arrivalDate).format('YYYY-MM-DD');
                  destToAdd.departureDate = currentDest.departureDate === null ? null : moment.unix(currentDest.departureDate).format('YYYY-MM-DD');
                  destToAdd.arrivalDateMenu = false;
                  destToAdd.departureDateMenu = false;
                  tripToEdit.destinations.push(destToAdd);
                  this.trip = tripToEdit;
                }
            });
    }
  }
};
</script>