<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

  <v-card>
  <v-container class="outer-container" height="100%" style="margin-left: 0px; margin-top: -20px;">
      <div class="section">
      <div>
        <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>
      </div>
        <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="toggleShouldDisplayButton()">
          <v-icon dark>edit</v-icon>
        </v-btn>
      </div>

    <v-divider class="photo-header-divider"/>
      <undo-redo-buttons
              :canRedo="rollbackCanRedo()"
              :canUndo="rollbackCanUndo()"
              :undo="undo"
              :redo="redo"
      ></undo-redo-buttons>
      <v-form lazy-validation
              ref="form"
              v-model="isFormValid">
      <v-text-field
              v-model="trip.name"
              :rules="nameRules"
              :counter="60"
              label="Trip Name"
              required
      ></v-text-field>
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
                    <v-card
                        :color="getDepthData(destination.depth).color"
                    >
                      <v-container class="container-custom-padding">
                          <v-card-title>
                              <v-combobox
                                      :items="userDestinations"
                                      item-text="name"
                                      v-model="destination.destination"
                                      label="Select an existing destination"
                                      :rules="noSameDestinationNameConsecutiveRule"
                                      return-object
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
              </v-timeline-item>
          </transition-group>
      </draggable>
      </v-timeline>
      </v-form>

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
          <v-tooltip top>
              <template v-slot:activator="{ on }">
                  <v-btn
                          v-on:click="addTripDestination"
                          icon
                          v-on="on">
                      <v-icon color="primary lighten-1">add_circle</v-icon>
                  </v-btn>
              </template>
              <span>Add Destination</span>
          </v-tooltip>
          <v-tooltip top>
              <template v-slot:activator="{ on }">
                  <v-btn
                          icon
                          v-on:click="updateTrip"
                          v-on="on">
                      <v-icon color="primary lighten-1">send</v-icon>
                  </v-btn>
              </template>
              <span>Update trip</span>
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
import RollbackMixin from '../mixins/RollbackMixin';
import UndoRedoButtons from '../common/rollback/UndoRedoButtons';
import tripRepo from "../../repository/TripRepository";
import { store } from "../../store/index";
import draggable from 'vuedraggable';
import dateTime from "../common/dateTime/dateTime.js";
import {noSameDestinationNameConsecutiveRule_name, arrivalBeforeDepartureAndDestinationsOneAfterTheOther, rules} from "../form_rules";
import {getChildrenCount, getDepthData, isDemotable, isPromotable, tripAssembler} from "./trips_destinations_util"
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let tripRepository = RepositoryFactory.get("trip")
let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,
  components: {
    draggable,
    UndoRedoButtons
  },
    mixins: [RollbackMixin],
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
        isDemotable: isDemotable,

        // rules
        ...rules,

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

      /**
       * Expands the destination at given index
       * @param index {number}
       */
      toggleExpanded(index) {
          this.trip.destinations[index].expanded = !this.trip.destinations[index].expanded;
      },

      /**
       * Action performed as soon a destination starts being dragged, slices the destination
       * children and temporarily adds them to a variable in the data.
       */
      startDrag(event) {
        this.drag = true;
        const childrenCount = getChildrenCount(this.trip.destinations, this.trip.destinations[event.oldIndex]);
        const copy = JSON.parse(JSON.stringify(this.trip.destinations));
        if (childrenCount > 0) {
            this.draggedSublist = copy.splice(event.oldIndex + 1, event.oldIndex + childrenCount - 1);
        }
        for (let d of copy) {
            d.hidden = false;
        }
        this.trip.destinations = this.setOrdinal(copy);
      },

      /**
       * Action performed as soon a destination ends being dragged,
       * reattaches the sub list stored on start drag at the right index.
       */
      endDrag(event) {
          const copy = JSON.parse(JSON.stringify(this.trip.destinations));
          copy.splice(event.newIndex + 1, 0, ...this.draggedSublist);
          this.draggedSublist = [];
          this.trip.destinations = this.setOrdinal(copy);
      },

      /**
       * Validates the trip form
       */
      validateForm() {
        !this.$refs.form.validate()
      },

      /**
       * Adds a destination to the trip destinations
       */
      addTripDestination() {
        const destinationsSize = this.trip.destinations.length;
        this.trip.destinations.push({
            arrivalDate: null,
            departureDate: null,
            depth: 0,
            destination: {name: null},
            expanded: false,
            hidden: false,
            ordinal: destinationsSize,
        });
      },

      /**
       * Ensures the list of destinations ordinal value is up to date
       */
      setOrdinal(destinations) {
          const copy = JSON.parse(JSON.stringify(destinations));
          copy.forEach((d, i) => {
              d.ordinal = i;
          });
          return copy
      },

      /**
       * Increases the depth of destination at given index
       */
      promote(index) {
          this.$set(this.trip.destinations[index], "depth", this.trip.destinations[index].depth + 1);
      },

      /**
       * Decreases the depth of destination at given index
       */
      demote(index) {
          this.$set(this.trip.destinations[index], "depth", this.trip.destinations[index].depth - 1);
      },

      /**
       * Hides or shows all the children of a destination
       */
      toggleHiddenDestinations(parent) {
          let i = parent.ordinal + 1;
          const destsLength = this.trip.destinations.length;
          while (i < destsLength && this.trip.destinations[i].depth > parent.depth) {
              this.$set(this.trip.destinations[i], "hidden", !(this.trip.destinations[i].hidden));
              i = i + 1;
          }
      },

      /**
     * Checks if the update trip form passes validation
     * If it does then updates trip and updates the view trip page
     */
    updateTrip() {
        if (this.$refs.form.validate()) {

            const trip = tripAssembler(this.trip);
            const userId = this.userId;
            const tripId = parseInt(this.trip.id);
            tripRepository
                .updateTrip(userId, tripId, trip)
                .then(() => {
                    const url = `/users/${userId}/trips/${tripId}`;
                    console.log("trip", trip);
                    this.rollbackCheckpoint(
                        'PUT',
                        {
                            url: url,
                            body: trip
                        },
                        {
                            url: url,
                            body: this.rollbackPreviousBody
                        }
                    );

                    // Update previous body to be used for the next checkpoints reaction
                    this.rollbackSetPreviousBody(trip);
                    this.updateViewTripPage();
                })
                .catch(e => {
                    console.log(e);
                });
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
          let newDestinations = JSON.parse(JSON.stringify(this.trip.destinations));
          newDestinations.splice(index, 1);
          this.trip.destinations = this.setOrdinal(newDestinations);
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

      getTrip: function() {
          return tripRepo.getTrip(this.userId, this.tripId).then((result) => {
              let trip = result.data;
              trip.destinations = trip.destinations.sort(function(a, b){
                  return a.ordinal - b.ordinal;
              });
              // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
              for (let i = 0; i < trip.destinations.length; i++) {
                  trip.destinations[i].expanded = false;
                  trip.destinations[i].hidden = false;


                  if (trip.destinations[i].arrivalDate != null) {
                      trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(trip.destinations[i].arrivalDate);
                  }
                  if (trip.destinations[i].arrivalDate != null) {
                      trip.destinations[i].departureDate = dateTime.convertTimestampToString(trip.destinations[i].departureDate);
                  }
              }
              this.trip = trip;
              return this.trip;
          });
      },

      /**
       * Undoes the last action and calls setDestination() afterwards
       */
      undo: function() {
          const actions = [this.getTrip];
          this.rollbackUndo(actions);
      },

      /**
       * Redoes the last action and calls setDestination() afterwards
       */
      redo: function() {
          const actions = [this.getTrip];
          this.rollbackRedo(actions);
      },
  },


  created: function() {
      this.getDestinations();
      this.isMyProfile = (store.getters.getUser.id == this.$route.params.id);
      // If the person viewing the trip is not admin and does not own the trip then takes them back to the page they were on
      if (!this.isMyProfile && !this.isAdmin) {
        this.$router.go(-1);
      }
      this.getTrip().then((trip) => this.rollbackSetPreviousBody(tripAssembler(trip)));
  }
};
</script>
