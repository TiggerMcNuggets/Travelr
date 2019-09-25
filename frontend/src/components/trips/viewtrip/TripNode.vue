<template>
  <v-timeline-item color="error">
    <v-card v-if="canEdit">
      <node-user-attendance class="user-attendance" v-if="displayUserAttendance" :node="node"/>
      <v-text-field
        class="v-timeline-trip-item-style"
        v-model="node.name"
        :rules="nameRules"
        :counter="60"
        label="Trip Name"
      ></v-text-field>
      <div v-if="!node.notCreated">
        <v-btn icon>
          <v-icon color="red lighten-1" @click="deleteNode(i)">delete</v-icon>
        </v-btn>

        <v-btn
          flat
          text
          small
          color="error"
          class="align-with-arrow"
          @click="getSelectedTrip(node.id)"
        >
          View Trip
          <v-icon left color="error">arrow_right_alt</v-icon>
        </v-btn>
      </div>
      <div v-else>
        <v-btn outline color="error" @click="updateTrip()">Create</v-btn>
        <v-btn icon @click="removeNode(i)" flat color="error">
          <v-icon>cancel</v-icon>
        </v-btn>
      </div>
    </v-card>
    <v-card v-else>
      <v-flex ml-3 pt-1>
        <node-user-attendance v-if="displayUserAttendance" :node="node"/>
        <h2>{{ node.name }}</h2>
        <v-btn flat text small color="error" @click="getSelectedTrip(node.id)">
          View Trip
          <v-icon left color="error">arrow_right_alt</v-icon>
        </v-btn>
      </v-flex>
    </v-card>
  </v-timeline-item>
</template>

<style>
.v-timeline-trip-item-style {
  padding: 8px 16px 4px 16px;
}
.user-attendance {
  padding: 8px 16px 4px 16px;
}
.align-with-arrow {
  float: right;
}
</style>

<script>
import { rules } from "../../form_rules";
import NodeUserAttendance from "./NodeUserAttendance";

export default {
  name: "TripNode",
  components: {
    NodeUserAttendance: NodeUserAttendance
  },
  props: {
    node: Object,
    trip: Object,
    i: Number,
    getSelectedTrip: Function,
    updateTrip: Function,
    removeNode: Function,
    deleteNode: Function,
    canEdit: Boolean
  },

  data() {
    return {
      ...rules
    };
  },

  computed: {
    /**
     * The helper method to ensure the user attendance component needs to be displayed
     * @return {boolean} check if the node is not in creation mdoe and if the trip has a group associated to it
     */
    displayUserAttendance() {
      if (this.trip) {
        return !this.node.notCreated && this.trip.trip.usergroup.length;
      } else {
        return false;
      }
    }
  },

  methods: {}
};
</script>
