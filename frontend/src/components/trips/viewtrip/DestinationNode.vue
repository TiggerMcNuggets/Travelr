<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-timeline-item outlined>
    <v-expansion-panel :value="node.notCreated ? 0 : undefined">
      <v-layout v-if="canEdit">
      <v-expansion-panel-content>
        <template v-slot:header>
          <div class="mr-5" v-on:click.stop>
            <div>
              <node-user-attendance
                      v-if="displayUserAttendance"
                      :node="node"/>
              <v-combobox
                :items="userDestinations"
                item-text="name"
                v-model="node.destination"
                label="Select an existing destination"
                return-object
              ></v-combobox>
            </div>
          </div>

        </template>
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
            <v-btn v-if="!node.notCreated" icon>
              <v-icon color="red lighten-1" @click="deleteNode(i)">delete</v-icon>
            </v-btn>
            <div v-else>
              <v-btn outline color="error" @click="updateTrip()">Create</v-btn>
              <v-btn icon @click="removeNode(i)" flat color="error">
                <v-icon>cancel</v-icon>
              </v-btn>
            </div>
        </v-expansion-panel-content>
      </v-layout>
      <v-layout v-else column align-start>
        <v-flex ml-2 pt-1>
          <h2>{{ node.destination.name }}</h2>
          <p v-if="node.arrivalDate" class="sub-text">Arrival Date: {{ node.arrivalDate }}</p>
          <p v-else class="sub-text">Arrival Date: N/A</p>
          <p v-if="node.departureDate" class="sub-text">Departure Date: {{ node.departureDate }}</p>
          <p v-else class="sub-text">Departure Date: N/A</p>
        </v-flex>
      </v-layout>
    </v-expansion-panel>
  </v-timeline-item>
</template>

<style>
  .date-margin {
    padding: 8px 16px 4px 16px;
  }
</style>

<script>
  import {
    noSameDestinationNameConsecutiveRule,
    arrivalBeforeDepartureAndDestinationsOneAfterTheOther,
    rules
  } from "../../form_rules";
  import { RepositoryFactory}  from "../../../repository/RepositoryFactory";
  let destinationRepository = RepositoryFactory.get("destination");
  import NodeUserAttendance from './NodeUserAttendance';
  import StoreTripsMixin from "../../mixins/StoreTripsMixin";


  export default {
    name: "DestinationNode",

    mixins: [StoreTripsMixin],
    components: {
        NodeUserAttendance: NodeUserAttendance
    },
    props: {
      trip: Object,
      node: Object,
      i: Number,
      updateTrip: Function,
      removeNode: Function,
      deleteNode: Function,
      canEdit: Boolean
    },

    data() {
      return {
        ...rules,
        userId: this.$route.params.id,
        userDestinations: [],
      };
    },

    computed: {

      /**
       * The helper method to ensure the user attendance component needs to be displayed
       * @return {boolean} check if the node is not in creation mdoe and if the trip has a group associated to it
       */
      displayUserAttendance() {
          return !this.node.notCreated && this.trip.trip.usergroup.length;
      },

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
       * Gets the list of valid destinations available to a user
       */
      getDestinations() {
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

    mounted() {
      this.getDestinations();
    }
  };
</script>