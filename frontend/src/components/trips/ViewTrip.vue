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
          v-model="trip.trip.nodes">
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
                  <v-btn icon>
                    <v-icon 
                      color="red lighten-1"
                      @click="deleteNode(i)"
                    >
                      delete
                    </v-icon>
                  </v-btn>
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
                    <v-icon 
                      color="red lighten-1"
                      @click="deleteNode(i)"
                    >
                      delete
                    </v-icon>
                </v-card>
              </v-timeline-item>
            </li>
          </transition-group>
        </draggable>
      </v-timeline>
    </v-form>
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
  import PageHeader from "../common/header/PageHeader";
  import dateTime from "../common/dateTime/dateTime.js";
  import {
    noSameDestinationNameConsecutiveRule,
    arrivalBeforeDepartureAndDestinationsOneAfterTheOther,
    rules
  } from "../form_rules";
  import {deepCopy} from "../../tools/deepCopy";
  import {getDepthData, isDemotable, isPromotable, tripAssembler} from "./trips_destinations_util"
  import {RepositoryFactory} from "../../repository/RepositoryFactory";

  let tripRepository = RepositoryFactory.get("trip")
  let destinationRepository = RepositoryFactory.get("destination");

  export default {
    store,
    components: {
      draggable,
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
        previousTripId: undefined,
        userId: this.$route.params.id,
        isInset: true,
        trip: {
          root: {
            user: {},
            id: this.$route.params.trip_id,
            name: undefined
          },
          trip: {
            id: undefined,
            name: undefined,
            nodes: []
          },
          navigation: []
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
      /**
       * Checks that no same destinations are consecutive
       */
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

      /**
       * Returns a color based on the node type
       * @param the node ("destination or trip")
       */
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
          link.setAttribute('download', `${this.trip.trip.name}.ics`);
          document.body.appendChild(link);
          link.click();
        });
      },


      /**
       * Toggles the type of the node (trip or destination)
       * @param index the index of the node
       */
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
       * Ensures the list of nodes ordinal value is up to date
       */
      setOrdinal(nodes) {
        const copy = deepCopy(nodes);
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
       * Updates and refetches trip
       * @param userId the user's id
       * @param tripId the trip's id
       * @param trip the trip body
       */
      async updateTripAndPopulate(userId, tripId, trip) {
        await tripRepository.updateTrip(userId, tripId, trip);
        await this.updateViewTripPage(userId, tripId);
      },

      /**
       * Updates and refetches trip and pushes to undo redo stack
       */
      async updateTrip() {
        // Get request parameters and body
        const userId = this.userId;
        const tripId = parseInt(this.trip.trip.id);
        let trip = tripAssembler(this.trip);

        // Get undo request parameters and body
        const previousTripId = this.previousTripId;
        const rollbackPreviousBody = this.rollbackPreviousBody;

        // Validate
        if (this.noAdjacentIdenticalDestinations()) {
          this.hasAdjacentIdentical = false;
          if (this.$refs.form.validate()) {
            try {

              await this.updateTripAndPopulate(userId, tripId, trip);

              // Add to undo redo stack
              trip = tripAssembler(this.trip);
              let checkpoint = {
                action: async () => await this.updateTripAndPopulate(userId, tripId, trip),
                reaction: async () => await this.updateTripAndPopulate(userId, previousTripId, rollbackPreviousBody),
              };
              this.pushStack(checkpoint);
              this.rollbackSetPreviousBody(trip);
              this.previousTripId = tripId;
            } catch(e) {
              console.log(e);
            }
          }
        } else {
          this.hasAdjacentIdentical = true;
        }
      },

      async updateViewTripPage(userId, tripId) {
        const result = await tripRepo.getTrip(userId, tripId);
        let trip = result.data;

        // Sorts the destinations ensure they are in the order of their ordinal
        let orderedDests = trip.trip.nodes.sort(function (a, b) {
          return a.ordinal - b.ordinal;
        });

        trip.destinations = orderedDests;
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
      },

      /**
       * Redirects users page to the destination page of the provided destination id
       * @param destId the id of the destination
       */
      viewDestination(destId) {
        this.$router.push("/user/" + this.userId + "/destinations/" + destId);
      },


      /**
       * Deletes the given node from the created/modified trip
       */
      deleteNode: function (index) {
        this.trip.trip.nodes.splice(index, 1);
        this.trip.trip.nodes = this.setOrdinal(this.trip.trip.nodes);
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
          this.trip = deepCopy(this.selectedTrip);
          this.trip.trip = this.tripWithDates(this.trip.trip);
          this.tripId = tripId;
        });

      },

      /**
       * Undoes the last action and calls setDestination() afterwards
       */
      undo: function () {
        const actions = [];
        this.rollbackUndo(actions);
      },

      /**
       * Redoes the last action and calls setDestination() afterwards
       */
      redo: function () {
        const actions = [];
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
        this.trip = deepCopy(this.selectedTrip);
        this.rollbackSetPreviousBody(tripAssembler(this.trip));
        this.previousTripId = this.trip.trip.id;

        this.trip.trip = this.tripWithDates(this.trip.trip);
      });
    }
  }
</script>
