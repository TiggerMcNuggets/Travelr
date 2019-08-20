<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

  <v-container fluid>

    <PageHeader
      :title="trip.trip.name"
      :undo="undo"
      :redo="redo"
      :canRedo="rollbackCanRedo"
      :canUndo="rollbackCanUndo"
      enableBackButton/>

    <v-form lazy-validation
            ref="form"
            v-model="isFormValid">
      <v-breadcrumbs :items="trip.navigation">
        <template v-slot:item="props">
          <v-breadcrumbs-item v-on:click="getSelectedTrip(props.item.id)">

            {{ props.item.name }}

          </v-breadcrumbs-item>
        </template>
      </v-breadcrumbs>
      <v-text-field
        v-model="trip.trip.name"
        :rules="nameRules"
        :counter="60"
        label="Trip Name"

        required
      ></v-text-field>
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
              v-on:click="addTripNode(true)"
              icon
              v-on="on">
              <v-icon color="primary lighten-1">add_location</v-icon>
            </v-btn>
          </template>
          <span>Add Destination</span>
        </v-tooltip>
        <v-tooltip top>
          <template v-slot:activator="{ on }">
            <v-btn
              v-on:click="addTripNode(false)"
              icon
              v-on="on">
              <v-icon color="primary lighten-1">local_airport</v-icon>
            </v-btn>
          </template>
          <span>Add Trip</span>
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
        <v-tooltip v-if="hasMissingDates && canDownloadTrip" bottom>
          <template v-slot:activator="{on}">
            <v-btn
              icon
              @click="downloadTrip()"
              flat small color="primary lighten-1"
              v-on="on"
            >
              <v-icon>calendar_today</v-icon>
            </v-btn>
          </template>
          <span>This calendar has missing destinations</span>
        </v-tooltip>
        <v-btn
          v-else-if="!canDownloadTrip"
          icon
          disabled
          flat
          @click="downloadTrip()">
          <v-icon>calendar_today</v-icon>
        </v-btn>
        <v-btn v-else
               @click="downloadTrip()" flat fab small dark color="primary lighten-1">
          <v-icon>calendar_today</v-icon>
        </v-btn>


      </v-layout>
      <v-container>
        <v-alert class="same-dist-alert" :value="hasAdjacentIdentical" color="error">Cannot have same destination
          consecutive.
        </v-alert>
      </v-container>
      <v-timeline align-top dense>
        <draggable
          class="list-group"
          tag="ul"
          v-model="trip.trip.nodes"
          @start="startDrag"
          @end="endDrag">
          <transition-group type="transition" :name="!drag ? 'flip-list' : null">
            <li v-for="(node, i) in trip.trip.nodes"
                :key="i"
                class="trip-timeline-item-width white--text mb-5">
              <v-timeline-item
                v-if="node.type.toLowerCase() === 'destination'">
                <v-card
                  class="v-timeline-destination-item-style">
                  <v-combobox
                    :items="userDestinations"
                    item-text="name"
                    v-model="node.destination"
                    label="Select an existing destination"
                    return-object
                  >
                  </v-combobox>
                  <v-menu
                    v-model="node.arrivalDateMenu"
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
                        v-model="node.arrivalDate"
                        label="Arrival date"
                        prepend-icon="event"
                        readonly
                        v-on="on"
                        class="date-margin"
                        :rules="arrivalBeforeDepartureAndDestinationsOneAfterTheOther"
                      ></v-text-field>
                    </template>
                    <v-date-picker
                      v-model="node.arrivalDate"
                      @input="node.arrivalDateMenu = false"
                    ></v-date-picker>
                  </v-menu>
                  <v-menu
                    v-model="node.departureDateMenu"
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
                        :rules="arrivalBeforeDepartureAndDestinationsOneAfterTheOther"
                        v-model="node.departureDate"
                        label="Departure date"
                        prepend-icon="event"
                        readonly
                        v-on="on"
                        class="date-margin"
                      ></v-text-field>
                    </template>
                    <v-date-picker
                      v-model="node.departureDate"
                      @input="node.departureDateMenu = false"
                    ></v-date-picker>
                  </v-menu>

                </v-card>
              </v-timeline-item>
              <v-timeline-item
                v-else
                color="red"
              >
                <v-card>
                  <v-text-field
                    class="v-timeline-trip-item-style"
                    v-model="node.name"
                    :rules="nameRules"
                    :counter="60"
                    label="Trip Name"></v-text-field>
                  <div class="view-trip-button">
                    <div v-on:click="getSelectedTrip(node.id)">
                      <v-btn flat text small color="error" class="align-with-arrow">View Trip</v-btn>
                      <v-icon color="error">arrow_right_alt</v-icon>
                    </div>
                  </div>
                </v-card>
              </v-timeline-item>
            </li>
          </transition-group>
        </draggable>
      </v-timeline>
    </v-form>
    <!--<TripMap-->
    <!--:nodes="trip.trip.nodes"-->
    <!--class="trip-map"/>-->
  </v-container>

</template>

<style>
  .same-dist-alert {
    width: 32%;
    margin-left: 0;
  }

  .align-with-arrow {
    padding: 0px 0px 30px 30px;
    margin-right: 2px;
  }

  .view-trip-button {
    display: flex;
    justify-content: flex-end;
  }

  .trip-timeline-item-width {
    min-width: 300px;
    width: 25%;
    padding: 0;
  }

  .trip-map {
    width: 55%;
    height: 100px;
    padding: 0;
    position: fixed;
    top: 25%;
    right: 5%;
  }

  .date-margin {
    padding: 8px 16px 4px 16px
  }

  .v-timeline-destination-item-style {
    padding: 1.5em 1em 1.5em 1em;
  }

  .v-timeline-trip-item-style {
    padding: 1.5em 1em 0.7em 1em;
  }

</style>

<script>
  import RollbackMixin from '../mixins/RollbackMixin';
  import StoreTripsMixin from '../mixins/StoreTripsMixin';
  import tripRepo from "../../repository/TripRepository";
  import {store} from "../../store/index";
  import draggable from 'vuedraggable';
  import TripMap from "./TripMap.vue";
  import UndoRedoButtons from '../common/rollback/UndoRedoButtons';
  import PageHeader from "../common/header/PageHeader";
  import dateTime from "../common/dateTime/dateTime.js";
  import {
    noSameDestinationNameConsecutiveRule,
    arrivalBeforeDepartureAndDestinationsOneAfterTheOther,
    rules
  } from "../form_rules";
  import {deepCopy} from "../../tools/deepCopy";
  import {getChildrenCount, getDepthData, isDemotable, isPromotable, tripAssembler} from "./trips_destinations_util"
  import {RepositoryFactory} from "../../repository/RepositoryFactory";

  let tripRepository = RepositoryFactory.get("trip")
  let destinationRepository = RepositoryFactory.get("destination");

  export default {
    store,
    components: {
      draggable,
      TripMap,
      UndoRedoButtons,
      PageHeader
    },
    mixins: [RollbackMixin, StoreTripsMixin],
    // local variables
    data() {
      return {
        // drag action
        drag: false,
        draggedSublist: [],

        isFormValid: true,

        isMyProfile: false,
        hasMissingDates: true,
        isAdmin: store.getters.getIsUserAdmin,
        tripId: this.$route.params.trip_id,
        userId: this.$route.params.id,
        is_inset: true,
        trip: {
          "root": {
            user: {},
            "id": 1,
            "name": "trip1"
          },
          "trip": {
            id: undefined,
            name: undefined,
            nodes: []
          },
          "navigation": []
        },
        userDestinations: [],
        shouldDisplayDialog: false,
        canDownloadTrip: true,

        // define functions to make them visible to the script
        getDepthData: getDepthData,
        isPromotable: isPromotable,
        isDemotable: isDemotable,
        arrivalDateMenu: false,
        hasAdjacentIdentical: false,

        // rules
        ...rules,

      };
    },

    computed: {
      noSameDestinationNameConsecutiveRule() {
        return noSameDestinationNameConsecutiveRule(this.trip.trip.nodes);
      },
      /**
       * A rule to enforce arrival and departure times are in a valid order.
       */
      arrivalBeforeDepartureAndDestinationsOneAfterTheOther() {
        return arrivalBeforeDepartureAndDestinationsOneAfterTheOther(
          this.trip.trip.nodes
        );
      },
    },
    methods: {


      getColor: function (node) {
        if (node.type === "destination") {
          return 'error';
        } else {
          return 'blue';
        }


      },

      /**
       * Downloads the trip from the database as an ics.
       */
      downloadTrip: function () {
        tripRepository.downloadTrip(this.userId, this.tripId).then(res => {
          //The below code is needed to download something using JavaScript
          const url = window.URL.createObjectURL(new Blob([res.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', `${this.trip.name}.ics`);
          document.body.appendChild(link);
          link.click();
        });
      },


      changeNodeType(index) {
        const destinationType = this.trip.trip.nodes[index].type.toLowerCase() === 'destination';
        this.trip.trip.nodes[index].type = destinationType ? 'trip' : 'destination';
      },

      /**
       * Validates the trip form
       */
      validateForm() {
        !this.$refs.form.validate()
      },

      /**
       * Adds a node to the trip destinations
       * @param {boolean} isDestination
       */
      addTripNode(isDestination) {
        this.trip.trip.nodes.push({
            name: '',
            type: isDestination ? 'destination' : 'trip',
            arrivalDate: null,
            departureDate: null,
            arrivalDateMenu: false,
            departureDateMenu: false,
            destination: {
              name: null
            }
          }
        );
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
       * Checks if we have any identical destinations that are consecutive before updating trip
       */
      noAdjacentIdenticalDestinations() {
        for (let i = 0; i < (this.trip.trip.nodes.length - 1); i++) {
          if ((this.trip.trip.nodes[i].type === 'destination') && (this.trip.trip.nodes[i + 1].type === 'destination')) {
            if (this.trip.trip.nodes[i].destination.id === this.trip.trip.nodes[i + 1].destination.id) {
              //same consecutive destination
              return false;
            }
          }
        }
        return true;
      },

      /**
       * Checks if the update trip form passes validation
       * If it does then updates trip and updates the view trip page
       */
      updateTrip() {
        if (this.noAdjacentIdenticalDestinations()) {
          this.hasAdjacentIdentical = false;
          if (this.$refs.form.validate()) {
            const trip = tripAssembler(this.trip);
            const userId = this.userId;
            const tripId = parseInt(this.trip.trip.id);
            tripRepository
              .updateTrip(userId, tripId, trip)
              .then(() => {
                const url = `/users/${userId}/trips/${tripId}`;
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
        } else {
          this.hasAdjacentIdentical = true;
        }

      },

      /**
       * Redirects users page to the destination page of the provided destination id
       * @param dest_id the id of the destination
       */
      viewDestination(dest_id) {
        this.$router.push("/user/" + this.userId + "/destinations/" + dest_id);
      },

      /**
       * Invoked by child component create-trip once the trip has been modified, is passed as prop
       */
      updateViewTripPage: function () {
        tripRepo.getTrip(this.userId, this.tripId).then((result) => {
          let trip = result.data;

          // trip.trip.nodes.nodes.forEach((node, index) => {
          //   console.log("dest name is" + node.destination.name);
          // });

          // Sorts the destinations ensure they are in the order of their ordinal
          let ordered_dests = trip.trip.nodes.sort(function (a, b) {
            return a.ordinal - b.ordinal;
          });
          trip.destinations = ordered_dests;
          // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
          for (let i = 0; i < trip.destinations.length; i++) {
            trip.destinations[i].expanded = false;
            if (trip.destinations[i].arrivalDate) {
              trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(trip.destinations[i].arrivalDate);
            } else {
              trip.destinations[i].arrivalDate = null;
            }
            if (trip.destinations[i].departureDate) {
              trip.destinations[i].departureDate = dateTime.convertTimestampToString(trip.destinations[i].departureDate);
            } else {
              trip.destinations[i].departureDate = null;
            }
          }
          this.trip = trip;
        });
      },

      /**
       * Deletes the given destination from the created/modified trip
       */
      deleteDestination: function (index) {
        let newDestinations = JSON.parse(JSON.stringify(this.trip.destinations));
        newDestinations.splice(index, 1);
        this.trip.destinations = this.setOrdinal(newDestinations);
      },

      /**
       * Gets the list of valid destinations available to a user
       */
      getDestinations: function () {
        return destinationRepository
          .getDestinations(this.userId)
          .then(res => {
            this.userDestinations = res.data;
          })
          .catch(e => {
            console.log(e);
          });
      },

      getDateInFormat: function (dateInt) {
        return dateTime.convertTimestampToString(dateInt)
      },

      tripWithDates: function (trip) {
        this.hasMissingDates = false;
        let numOfMissingDates = 0;
        // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
        for (let i = 0; i < trip.nodes.length; i++) {
          if (trip.nodes[i].type === "destination") {
            trip.nodes[i].arrivalDateMenu = false;
            trip.nodes[i].departureDateMenu = false;
            if (trip.nodes[i].arrivalDate === 0) {
              this.hasMissingDates = true;
              numOfMissingDates++;
            }
            if (trip.nodes[i].arrivalDate !== 0) {
              trip.nodes[i].arrivalDate = dateTime.convertTimestampToString(trip.nodes[i].arrivalDate);
            } else {
              trip.nodes[i].arrivalDate = null;
            }
            if (trip.nodes[i].departureDate !== 0) {
              trip.nodes[i].departureDate = dateTime.convertTimestampToString(trip.nodes[i].departureDate);
            } else {
              trip.nodes[i].departureDate = null;
            }
          }

        }
        if (numOfMissingDates === trip.nodes.length) {
          this.canDownloadTrip = false;
        }

        return trip;
      },
      

      getSelectedTrip(tripId) {
        this._getTrip(this.userId, tripId).then(() => {
          this.trip = deepCopy(this.selected_trip);
          this.trip.trip = this.tripWithDates(this.trip.trip);
          this.tripId = tripId;
        });

      },

      /**
       * Undoes the last action and calls setDestination() afterwards
       */
      undo: function () {
        const actions = [this.getTrip];
        this.rollbackUndo(actions);
      },

      /**
       * Redoes the last action and calls setDestination() afterwards
       */
      redo: function () {
        const actions = [this.getTrip];
        this.rollbackRedo(actions);
      },
    },


    created: function () {
      this.getDestinations();
      this.isMyProfile = (store.getters.getUser.id == this.$route.params.id);
      // If the person viewing the trip is not admin and does not own the trip then takes them back to the page they were on
      if (!this.isMyProfile && !this.isAdmin) {
        this.$router.go(-1);
      }
      this._getTrip(this.userId, this.tripId).then(() => {
        this.trip = deepCopy(this.selected_trip);

        // this.trip.trip.nodes.forEach((node, index) => {
        //   console.log("dest name on creation is" + node.destination.name);
        // });
        this.trip.trip = this.tripWithDates(this.trip.trip);


      });
      // this._getTrip(this.userId, this.tripId).then((trip) => this.rollbackSetPreviousBody(tripAssembler(trip)));


    }
  };
</script>
