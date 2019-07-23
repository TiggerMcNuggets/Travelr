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
                       :updateViewTripPage="this.updateViewTripPage" />
      </v-dialog>
      <v-timeline align-top dense>
      <draggable
              class="list-group"
              tag="ul"
              v-model="trip.destinations"
              @start="startDrag"
              @end="endDrag">
          <transition-group type="transition" :name="!drag ? 'flip-list' : null">

              <v-timeline-item
                v-for="(destination, i) in notHiddenTrips"
                :key="i"
                class="trip-timeline-item-width white--text mb-5"
                :color="getDepthData(destination.depth).color"
                :large="getDepthData(destination.depth).large"
              >
                  <template v-slot:icon>
                      <span class="hoverable"
                              v-on:click="toggleExpanded(i)">{{getDepthData(destination.depth).number}}</span>
                  </template>
                <v-form lazy-validation
                        ref="form"
                        v-model="isFormValid">
                    <v-card
                        :color="getDepthData(destination.depth).color"
                    >
                      <v-container class="container-custom-padding">
                          <v-card-title>
                              <v-combobox
                                      :rules="noSameDestinationNameConsecutiveRule"
                                      :items="userDestinations.map(dest => dest.name)"
                                      v-model="destination.name"
                                      label="Select an existing destination"
                              ></v-combobox>
                              <v-btn
                                  v-on:click="toggleHiddenDestinations(destination)"
                                  fab flat small>
                                  <v-icon>view_headline</v-icon>
                              </v-btn>
                              <v-btn
                                      :disabled="!isPromotable(trip.destinations, destination.ordinal)"
                                      v-on:click="promote(destination.ordinal)"
                                      fab flat small>
                                  <v-icon>arrow_upward</v-icon>
                              </v-btn>
                              <v-btn
                                      :disabled="!isDemotable(trip.destinations, destination.ordinal)"
                                      v-on:click="demote(destination.ordinal)"
                                      fab flat small>
                                  <v-icon>arrow_downward</v-icon>
                              </v-btn>
                          </v-card-title>
                          <v-container v-if="destination.expanded">
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
                                                v-on:click="toggleExpanded(i)"
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
                          </v-container>
                      </v-container>
                    </v-card>
                </v-form>
              </v-timeline-item>
          </transition-group>
      </draggable>
    </v-timeline>
      <v-layout flex>
          <v-tooltip top>
              <template v-slot:activator="{ on }">
                  <v-btn
                         v-on:click="validateForm"
                         icon
                         v-on="on">
                      <v-icon color="primary lighten-1">check_circle</v-icon>
                  </v-btn>
              </template>
              <span>Validate</span>
          </v-tooltip>

      </v-layout>
</v-container>
</v-card>

</template>

<style>
    .trip-timeline-item-width {
        min-width: 300px;
        width: 25%;
        padding: 0;
    }

    .date-margin {
        padding: 8px 16px 4px 16px
    }

    .container-custom-padding {
        padding: 0;
    }

</style>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import tripRepo from "../../repository/TripRepository";
import { store } from "../../store/index";
import CreateTrips from "./CreateTrips.vue";
import draggable from 'vuedraggable';
import dateTime from "../common/dateTime/dateTime.js";
import {noSameDestinationNameConsecutiveRule_name, arrivalBeforeDepartureAndDestinationsOneAfterTheOther} from "../form_rules";
import {getChildrenCount, getDepthData, isDemotable, isPromotable} from "./trips_destinations_util"

let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,
  components: {
    draggable,
    CreateTrips
  },
  // local variables
  data() {
    return {
        // drag action
        drag: false,
        draggedSublist: [],

        isFormValid: true,

        isMyProfile: false,
        isAdmin: store.getters.getIsUserAdmin,
        tripId:  this.$route.params.trip_id,
        userId:  this.$route.params.id,
        is_inset: true,
        trip: {destinations:[]},
        userDestinations: [],
        shouldDisplayDialog: false,

        // define functions to make them visible to the script
        getDepthData: getDepthData,
        isPromotable: isPromotable,
        isDemotable: isDemotable
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
        },
        notHiddenTrips() {
            return this.trip.destinations.filter((d) => !d.hidden);
        }
    },
  methods: {

      toggleExpanded(index) {
          this.trip.destinations[index].expanded = !this.trip.destinations[index].expanded;
      },

      startDrag(event) {
        this.drag = true;
        const childrenCount = getChildrenCount(this.trip.destinations, this.trip.destinations[event.oldIndex]);
        if (childrenCount > 0) {
            const subList = this.trip.destinations.splice(event.oldIndex + 1, event.oldIndex + childrenCount - 1);
            this.draggedSublist = subList;
        }
        for (let d of this.trip.destinations) {
            d.hidden = false;
        }

      },

      validateForm() {
          console.log(this.$refs);
        if (!this.$refs.form.validate()) console.log('ciao');
      },

      setOrdinal() {
          this.trip.destinations.forEach((d, i) => {
              this.$set(this.trip.destinations[i], "ordinal", i);
          });
      },

      endDrag(event) {
          this.trip.destinations.splice(event.newIndex + 1, 0, ...this.draggedSublist);
          this.draggedSublist = [];
          this.setOrdinal();
      },

      promote(index) {
          this.$set(this.trip.destinations[index], "depth", this.trip.destinations[index].depth + 1);
      },

      demote(index) {
          this.$set(this.trip.destinations[index], "depth", this.trip.destinations[index].depth - 1);
      },

      toggleHiddenDestinations(parent) {
          let i = parent.ordinal + 1;
          const destsLength = this.trip.destinations.length;
          console.log("-------------------new Parent------------");
          console.log(destsLength);
          console.log(i);
          while (i < destsLength && this.trip.destinations[i].depth > parent.depth) {
              console.log("this.trip.destinations[i]", this.trip.destinations[i]);
              this.$set(this.trip.destinations[i], "hidden", !(this.trip.destinations[i].hidden));
              console.log("this.trip.destinations[i]", this.trip.destinations[i]);
              i = i + 1;
          }
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
            trip.destinations[i].hidden = false;

            // TODO: deleted once done
            trip.destinations[i].depth = 0;

            if (trip.destinations[i].arrivalDate != null) {
              trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(trip.destinations[i].arrivalDate);
            }
            if (trip.destinations[i].arrivalDate != null) {
              trip.destinations[i].departureDate = dateTime.convertTimestampToString(trip.destinations[i].departureDate);
            }
          }
          // TODO: delete once done
          trip.destinations = [...trip.destinations, {name: "ciao", arrivalDate: null, departureDate: null, expanded: false, depth: 1, ordinal: 2}];
          trip.destinations = [...trip.destinations, {name: "ciao", arrivalDate: null, departureDate: null, expanded: false, depth: 2, ordinal: 3}];
          trip.destinations = [...trip.destinations, {name: "ciao", arrivalDate: null, departureDate: null, expanded: false, depth: 0, ordinal: 4}];

          this.trip = trip;
      });
  }
};
</script>
