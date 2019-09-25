<template>
  <v-timeline align-top dense>
    <div v-if="canEdit">
      <draggable class="list-group" tag="ul" v-model="trip.trip.nodes" @change="updateTrip()">
        <transition-group type="transition" :name="!drag ? 'flip-list' : null">
          <li
            v-for="(node, i) in trip.trip.nodes"
            :key="i"
          >
            <destination-node 
              v-if="node.type.toLowerCase() === 'destination'" 
              :trip="trip"
              :node="node"
              :i="i"
              :updateTrip="updateTrip"
              :removeNode="removeNode"
              :deleteNode="deleteNode"
              :canEdit="canEdit"
            />
            <trip-node
              v-else
              :node="node"
              :trip="trip"
              :i="i"
              :getSelectedTrip="getSelectedTrip"
              :updateTrip="updateTrip"
              :removeNode="removeNode"
              :deleteNode="deleteNode"
              :canEdit="canEdit"
            />
          </li>
        </transition-group>
      </draggable>
    </div>
    <div v-else>
      <li
          v-for="(node, i) in trip.trip.nodes"
          :key="i"
        >
          <destination-node 
            v-if="node.type.toLowerCase() === 'destination'" 
            :trip="trip"
            :node="node"
            :i="i"
            :updateTrip="updateTrip"
            :removeNode="removeNode"
            :deleteNode="deleteNode"
          />
          <trip-node
            v-else
            :node="node"
            :trip="trip"
            :i="i"
            :getSelectedTrip="getSelectedTrip"
            :updateTrip="updateTrip"
            :removeNode="removeNode"
            :deleteNode="deleteNode"
          />
        </li>
    </div>
  </v-timeline>
</template>

<style>
</style>

<script>
  import draggable from "vuedraggable";
  import TripNode from "./TripNode";
  import DestinationNode from "./DestinationNode";
  import { deepCopy } from "../../../tools/deepCopy";

  export default {
    name: "TripTimeline",

    props: {
      trip: Object,
      updateTrip: Function,
      getSelectedTrip: Function,
      setSelectedTrip: Function,
      canEdit: Boolean
    },

    components: {
      draggable,
      TripNode,
      DestinationNode
    },

    data() {
      return {
        drag: false
      };
    },

    methods: {
      /**
       * Deletes the given node from the created/modified trip
       */
      deleteNode: function(index) {
        this.removeNode(index);
        this.updateTrip();
      },

      /**
       * Only removes the node from frontend but doesn't update database
       * @param index the index of the node
       */
      removeNode: function(index) {
        let trip = this.trip;
        trip.trip.nodes.splice(index, 1);
        trip.trip.nodes = this.setOrdinal(this.trip.trip.nodes);
        this.setSelectedTrip(trip);
      },

      /**
       * Ensures the list of nodes ordinal value is up to date
       */
      setOrdinal(nodes) {
        const copy = deepCopy(nodes);
        copy.forEach((d, i) => {
          d.ordinal = i;
        });
        return copy;
      }
    }
  };
</script>
