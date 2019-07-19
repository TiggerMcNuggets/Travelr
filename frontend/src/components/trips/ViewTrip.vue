<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

  <v-card>
  <v-container class="outer-container" height="100%" style="margin-left: 0px; margin-top: -20px;">
      <div class="section">
      <div>
        <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>
      </div>
          <h2 class="headline">{{ trip.name }}</h2>
        <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="toggleShouldDisplayButton()">
          <v-icon dark>edit</v-icon>
        </v-btn>
      </div>

    <v-divider class="photo-header-divider"/>
      <v-dialog v-model="shouldDisplayDialog" max-width="100%">
          <CreateTrips style="background-color: white;"
                       v-if="true"
                       :regetTrips="() => console.log('no need')"
                       :passedTrip="tripId"
                       :updateViewTripPage="this.updateViewTripPage"
          />
      </v-dialog>

      <v-timeline align-top dense>
      <draggable
              class="list-group"
              tag="ul"
              v-model="trip.destinations"
              @start="drag = true"
              @end="drag = false">
          <transition-group type="transition" :name="!drag ? 'flip-list' : null">

              <v-timeline-item
                v-for="(destination, i) in trip.destinations"
                :key="i"
                class="trip-timeline-item-width"
                color="blue"
                small
              >
                <v-form
                        v-if="destination.expanded">
                    <v-card
                        color="blue"
                        dark
                    >
                      <v-card-title>
                          <v-combobox
                                  :rules="noSameDestinationNameConsecutiveRule"
                                  :items="userDestinations.map(dest => dest.name)"
                                  v-model="destination.name"
                                  label="Select an existing destination"
                          ></v-combobox>
                      </v-card-title>
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
                                  class="date-margin"
                              ></v-text-field>
                          </template>
                          <v-date-picker
                                  v-model="destination.arrivalDate"
                                  @input="destination.arrivalDateMenu = false"
                          ></v-date-picker>
                      </v-menu>
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
                                    class="date-margin"
                                ></v-text-field>
                            </template>
                            <v-date-picker
                                    v-model="destination.departureDate"
                                    @input="destination.departureDateMenu = false"
                            ></v-date-picker>
                        </v-menu>

                        <div>
                            <v-tooltip top>
                                <template v-slot:activator="{ on }">
                                    <v-btn
                                            v-on="on"
                                            v-on:click="deleteDestination(i)"
                                            fab flat small dark>
                                        <v-icon>delete</v-icon>
                                    </v-btn>
                                </template>
                                <span>Delete</span>
                            </v-tooltip>
                            <v-tooltip top>
                                <template v-slot:activator="{ on }">
                                    <v-btn
                                            v-on="on"
                                            v-on:click="toggleDeparted(i)"
                                            fab flat small dark>
                                        <v-icon>visibility_off</v-icon>
                                    </v-btn>
                                </template>
                                <span>Hide</span>
                            </v-tooltip>
                            <v-tooltip top>
                                <template v-slot:activator="{ on }">
                                    <v-btn
                                            v-on="on"
                                            v-on:click="viewDestination(destination.id)"
                                            fab small dark flat>
                                        <v-icon>explore</v-icon>
                                    </v-btn>
                                </template>
                                <span>View Destination</span>
                            </v-tooltip>

                        </div>
                    </v-card>
                </v-form>
                <v-card
                        class="hoverable"
                        v-on:click="toggleDeparted(i)"
                        v-else>
                    <v-card-title>
                        <div>
                            {{destination.name}}
                        </div>
                    </v-card-title>
                </v-card>
              </v-timeline-item>
          </transition-group>
      </draggable>
    </v-timeline>
</v-container>
</v-card>

</template>

<style>
    .trip-timeline-item-width {
        width: 25%;
    }

    .date-margin {
        padding: 8px 16px 4px 16px
    }

</style>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import tripRepo from "../../repository/TripRepository";
let destinationRepository = RepositoryFactory.get("destination");
import { store } from "../../store/index";
import CreateTrips from "./CreateTrips.vue";
import dateTime from "../common/dateTime/dateTime.js";
import {
    noSameDestinationNameConsecutiveRule_name,
    arrivalBeforeDepartureAndDestinationsOneAfterTheOther
} from "../form_rules";

import draggable from 'vuedraggable';

export default {
  store,
  components: {
    draggable,
    CreateTrips
  },
  // local variables
  data() {
    return {
        drag: false,
        isMyProfile: false,
        isAdmin: store.getters.getIsUserAdmin,
        tripId:  this.$route.params.trip_id,
        userId:  this.$route.params.id,
        is_inset: true,
        trip: {},
        userDestinations: [],
        shouldDisplayDialog: false
    };
  },

    computed: {
        noSameDestinationNameConsecutiveRule() {
            return noSameDestinationNameConsecutiveRule_name(this.trip.destinations);
        },
        arrivalBeforeDepartureAndDestinationsOneAfterTheOther() {
            return arrivalBeforeDepartureAndDestinationsOneAfterTheOther(
                this.trip.destinations
            );
        }
    },
  methods: {
      /**
       *
       */
      toggleDeparted(index) {
          console.log("here");
          this.trip.destinations[index].expanded = !this.trip.destinations[index].expanded;
          console.log(this.trip.destinations);
      },
      /**
       * Redirects users page to the destination page of the provided destination id
       * @param dest_id the id of the destination
       */
      viewDestination(dest_id) {
        this.$router.push("/user/"+this.userId+"/destinations/"+dest_id);
      },

      /**
       * Decides if the edit trip dialog should be displayed
       */
      toggleShouldDisplayButton: function() {
        this.shouldDisplayDialog = !this.shouldDisplayDialog;
      },

      /**
       * Invoked by child component create-trip once the trip has been modified, is passed as prop
       */
      updateViewTripPage: function() {
          tripRepo.getTrip(this.userId, this.tripId).then((result) => {
              let trip = result.data;
              // Sorts the destinations ensure they are in the order of their ordinal
              let ordered_dests = trip.destinations.sort(function(a, b){
                  return a.ordinal - b.ordinal;
              });
              trip.destinations = ordered_dests;
              // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
              for (let i = 0; i < trip.destinations.length; i++) {
                if (trip.destinations[i].arrivalDate != null) {
                  trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(trip.destinations[i].arrivalDate);
                }
                if (trip.destinations[i].arrivalDate != null) {
                  trip.destinations[i].departureDate = dateTime.convertTimestampToString(trip.destinations[i].departureDate);
                }
              }
              this.trip = trip;
          });
      },

      /**
       * Deletes the given destination from the created/modified trip
       */
      deleteDestination: function(index) {
          let newDestinations = this.trip.destinations;
          newDestinations.splice(index, 1);
          this.trip.destinations = newDestinations;
      },

      /**
       * Gets the list of valid destinations available to a user
       */
      getDestinations: function() {
          return destinationRepository
              .getDestinations(this.userId)
              .then(res => {
                  this.userDestinations = res.data;
              })
              .catch(e => {
                  console.log(e);
              });
      },
  },

  created: function() {

      this.getDestinations();
      this.isMyProfile = (store.getters.getUser.id == this.$route.params.id);
      // If the person viewing the trip is not admin and does not own the trip then takes them back to the page they were on
      if (!this.isMyProfile && !this.isAdmin) {
        this.$router.go(-1);
      }
      tripRepo.getTrip(this.userId, this.tripId).then((result) => {
          let trip = result.data;
          let ordered_dests = trip.destinations.sort(function(a, b){
              return a.ordinal - b.ordinal;
          });
          trip.destinations = ordered_dests;
          // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
          for (let i = 0; i < trip.destinations.length; i++) {
            trip.destinations[i].expanded = false;
            if (trip.destinations[i].arrivalDate != null) {
              trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(trip.destinations[i].arrivalDate);
            }
            if (trip.destinations[i].arrivalDate != null) {
              trip.destinations[i].departureDate = dateTime.convertTimestampToString(trip.destinations[i].departureDate);
            }
          }
          trip.destinations = [...trip.destinations, {name: "ciao", arrivalDate: null, departureDate: null, expanded: false}];
          this.trip = trip;
      });
  }
};
</script>
