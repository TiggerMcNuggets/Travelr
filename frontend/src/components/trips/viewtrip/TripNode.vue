<template>
  <v-timeline-item color="error">
    <v-card>
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

        <v-btn flat text small color="error" class="align-with-arrow" @click="getSelectedTrip(node.id)">
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
  </v-timeline-item>
</template>

<style>
  .v-timeline-trip-item-style {
    padding: 1.5em 1em 0.7em 1em;
  }
  .align-with-arrow {
    float: right;
  }
</style>

<script>
  import { rules } from "../../form_rules"

  export default {
    name: "TripNode",

    props: {
      node: Object,
      i: Number,
      getSelectedTrip: Function,
      updateTrip: Function,
      removeNode: Function,
      deleteNode: Function
    },

    data() {
      return {
        ...rules
      };
    },

    methods: {}
  };
</script>
