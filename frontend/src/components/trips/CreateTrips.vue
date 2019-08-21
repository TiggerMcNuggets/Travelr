

<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="create-trip-container">
    <v-form ref="form" lazy-validation>
      <div class="add-trip-form">
        <h3>{{this.dialogName}}</h3>
        <UndoRedoButtons
              v-if="passedTrip"
              :canRedo="rollbackCanRedo()"
              :canUndo="rollbackCanUndo()"
              :undo="undo"
              :redo="redo"></UndoRedoButtons>
        <v-layout class="trip-name">
          <v-flex xs12 md12 class="row-input-margin">
            <v-text-field v-model="trip.name" :rules="nameRules" :counter="60" label="Trip Name"></v-text-field>
          </v-flex>
        </v-layout>
        <ul>
          <draggable
            :list="trip.nodes"
            :disabled="!draggableEnabled"
            class="list-group"
            ghost-class="ghost"
            @start="dragging = true"
            @end="dragging = false"
          >
            <li
              v-for="(node, index) in trip.nodes"
              :v-bind="index"
              :key="index"
              class="list-group-item"
            >
              <v-card class="destination-form-padding">
                <v-layout>
                  <v-flex xs12 md4 class="create-trip-item">
                    <v-combobox
                        v-if="!node.isTrip"
                        :items="userDestinations"
                        item-text="name"
                        v-model="node.destination"
                        label="Select an existing destination"
                        :rules="noSameDestinationNameConsecutiveRule"
                        return-object
                ></v-combobox>
                  <v-text-field
                          v-else
                          v-model="node.name"
                          label="The trip name"
                          :rules="nameRules"
                          v-on:change="removeDestinationFromNode(index)"
                  ></v-text-field>
                      <v-switch
                              v-model="node.isTrip"
                              :label="'Trip'"></v-switch>
                      <v-btn
                              flat
                              small
                              color="error"
                              v-if="index > 1"
                              v-on:click="deleteDestination(index)">
                        Remove
                      </v-btn>
                  </v-flex>

                  <v-flex xs12 md4 class="create-trip-item">
                    <v-card v-if="!node.isTrip"
                            class="times-padding">
                      <!-- Arrival date -->
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
                            :rules="arrivalBeforeDepartureAndDestinationsOneAfterTheOther"
                            label="Arrival date"
                            prepend-icon="event"
                            readonly
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker
                          v-model="node.arrivalDate"
                          @input="node.arrivalDateMenu = false"
                        ></v-date-picker>
                      </v-menu>
                    </v-card>
                  </v-flex>
                  <v-flex xs12 md4 class="create-trip-item">
                    <v-card
                            v-if="!node.isTrip"
                            class="times-padding">
                      <!-- Departure date -->
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
                            v-model="node.departureDate"
                            :rules="arrivalBeforeDepartureAndDestinationsOneAfterTheOther"
                            label="Departure date"
                            prepend-icon="event"
                            readonly
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker
                          v-model="node.departureDate"
                          @input="node.departureDateMenu = false"
                        ></v-date-picker>
                      </v-menu>
                    </v-card>
                  </v-flex>
                  <v-tooltip right>
                    <template v-slot:activator="{ on }">
                      <v-btn
                        flat
                        icon
                        color="red lighten-2"
                        v-on:click="resetDestinationDate(index)"
                      >
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
        <div class="create-trip-options">
          <v-btn color="red" v-on:click="resetValues">RESET</v-btn>
          <v-btn v-on:click="onConfirm">CONFIRM</v-btn>
          <v-btn v-on:click="addDestinationToTrip">ADD DESTINATION</v-btn>
        </div>
      </div>
    </v-form>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.create-trip-container {
  text-align: left;
}

.add-trip-form {
  padding: 30px;
  padding-top: 10px;
}

.add-trip-form .trip-name {
  padding: 20px;
}

.add-trip-form .create-trip-item {
  padding: 15px;
}

.create-trip-options {
  display: flex;
  justify-content: flex-start;
}

.node-form-padding {
  padding: 2em;
}
.times-padding {
  padding: 1em;
}
.list-group-item {
  cursor: move;
  margin-bottom: 20px;
}
.list-group-item:hover i {
  cursor: pointer !important;
}
</style>


<script>
import { store } from "../../store/index";
import moment from "moment";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import draggable from "vuedraggable";
import {
  rules,
  noSameDestinationNameConsecutiveRule,
  arrivalBeforeDepartureAndDestinationsOneAfterTheOther
} from "../form_rules";
import {deepCopy} from "../../tools/deepCopy"
import {isDemotable, isPromotable, getDepthData} from "./trips_destinations_util";


import StoreTripsMixin from '../mixins/StoreTripsMixin'
import RollbackMixin from '../mixins/RollbackMixin';
import UndoRedoButtons from '../common/rollback/UndoRedoButtons';
// let tripRepository = RepositoryFactory.get("trip");
let destinationRepository = RepositoryFactory.get("destination");

export default {
  store,
  mixins: [
          RollbackMixin,
          StoreTripsMixin
  ],
  components: {
    UndoRedoButtons,
    draggable: draggable
  },
  props: {
    toggleShowCreateTrip: Function,
    passedTrip: String,
    updateViewTripPage: Function,
    checkpointCreateTrip: Function
  },
  data() {
    return {
      userId: this.$route.params.user_id
        ? this.$route.params.user_id
        : this.$route.params.id,
      tripToDisplay: null,
      draggableEnabled: true,
      dialogName: "Create a new trip",
      emptyDest: {
            name: '',
            isTrip: false,
            arrivalDate: null,
            departureDate: null,
            arrivalDateMenu: false,
            departureDateMenu: false,
            destination: {name: null}
          },
      trip: {
        name: "",
        nodes: [{
            name: '',
            isTrip: false,
            arrivalDate: null,
            departureDate: null,
            arrivalDateMenu: false,
            departureDateMenu: false,
            destination: {name: null}
          }, {
            name: '',
            isTrip: false,
            arrivalDate: null,
            departureDate: null,
            arrivalDateMenu: false,
            departureDateMenu: false,
            destination: {name: null}
          },]
      },
      userDestinations: [],
      ...rules,
      id: this.$route.params.id,
      tripID: this.$route.params.trip_id,
      isAdminUser: false,
      isMyProfile: false,

        // utils
        getDepthData: getDepthData,
        isPromotable: isPromotable,
        isDemotable: isDemotable,
    };
  },
  computed: {
    /**
     * A rule for the form to ensure there is no consecutive destination names in a trip.
     */
    noSameDestinationNameConsecutiveRule() {
      return noSameDestinationNameConsecutiveRule(this.trip.nodes);
    },

    /**
     * A rule to enforce arrival and departure times are in a valid order.
     */
    arrivalBeforeDepartureAndDestinationsOneAfterTheOther() {
      return arrivalBeforeDepartureAndDestinationsOneAfterTheOther(
        this.trip.nodes
      );
    }
  },

  methods: {

      /**
       * @param index: the node index
       * ensures the node will not contain a destination if toggled to be of trip type
       */
      removeDestinationFromNode(index) {
          if (this.trip.nodes[index].isTrip) {
              this.trip.nodes[index].destination = {name: null}
          }
      },


    /**
     * Gets the list of valid destinations available to a user
     */
    getDestinations: function() {
      return destinationRepository
        .getDestinations(this.id)
        .then(res => {
          this.userDestinations = res.data;
        })
        .catch(e => {
          console.log(e);
        });
    },

    /**
     * Checks if the url id is the same as the current users id
     * If it is sets isMyProfile to true
     * else sets isMyProfile to false
     */
    checkIfProfileOwner() {
      this.id = this.$route.params.id;
      this.isMyProfile = store.getters.getUser.id == this.id;
    },

    /**
     * Resets the form
     */
    resetValues: function() {
      this.$refs.form.reset();
    },

    /**
     * Resets arrival and departure date for given destination while editing/creating a trip
     * @param destIndex the index of the destination
     */
    resetDestinationDate: function(destIndex) {
      let newDestinations = this.trip.nodes;
      newDestinations[destIndex].arrivalDate = null;
      newDestinations[destIndex].departureDate = null;
      this.trip.nodes = newDestinations;
    },

    /**
     * Deletes the given destination from the created/modified trip
     */
    deleteDestination: function(index) {
      let newDestinations = deepCopy(this.trip.nodes);
      newDestinations.splice(index, 1);
      this.trip.nodes = newDestinations;
    },

    /**
     * Adds a template empty destination object to the form
     */
    addDestinationToTrip: function() {
      let newDestinations = this.trip.nodes;
      newDestinations.push(deepCopy(this.emptyDest));
      this.trip.nodes = newDestinations;
    },

    /**destination
     * Checks if passedTrip is null
     * If it is then calls fucntion createTrip
     * else calls function updateTrip
     */
    onConfirm: function() {
        this.createTrip();
    },

    /**
     * Checks if the create trip form passes validation
     * If the user is an admin calls the function createTripForUser
     * else calls the function createTrip
     * then calls the function regetTrips
     */
    createTrip: function() {
      if (this.$refs.form.validate()) {
        const trip = this.tripAssembler();
        const tripCreation = {name: trip.name};
        this._postTrip(this.id, tripCreation)
          .then(res => {
            this.checkpointCreateTrip(res.data.id)
            return this._putTrip(this.id, res.data.id, trip);
          })
          .then(() => {
              this.toggleShowCreateTrip();
          })
          .catch(e => {
            console.log(e);
          });
      }
    },

    /**
     * Creates a trip object from the data passed that conforms with the API specs
     * @return {name: string, destinations: Array}
     **/
    tripAssembler: function() {
      let trip = { name: this.trip.name, nodes: [] };
      this.trip.nodes.forEach((destination, index) => {
        trip.nodes.push({
          name: destination.name ? destination.name : undefined, 
          ordinal: index,
          type: destination.isTrip ? 'trip': 'destination',
          destination: {...destination.destination}, 
          arrivalDate: moment(destination.arrivalDate).unix(),
          departureDate: moment(destination.departureDate).unix()
        });
      });
      return trip;
    },
    /**
     * Undoes the last action and calls setDestination() afterwards
     */
    undo: function() {
        const actions = [];
        this.rollbackUndo(actions);
    },

    /**
     * Redoes the last action and calls setDestination() afterwards
     */
    redo: function() {
        const actions = [];
        this.rollbackRedo(actions);
    },
  },


  /**
   * When the component finished mounting, the destinations for the combobox are retrieved and then
   * in case the parent component passed a valid trip id, the trip is retrieved and the
   * existing trip params populate the form.
   * Makes component usable for both create and edit component
   */
  mounted() {
    this.getDestinations(this.userId)
        .then(() => {
            // needed for rollback functionality
            this.rollbackSetPreviousBody(this.tripAssembler());
        });
  },

  /**
   * Sets if the user is the owner or if admin
   */
  created: function() {
    this.checkIfProfileOwner();
    this.isAdminUser = store.getters.getIsUserAdmin;
  }
};
</script>