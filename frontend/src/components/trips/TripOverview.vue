<template>
  <v-layout>
    <v-flex xs12 ma-2 mt-4>
      <div v-if="canEdit">
        <h2>Trip Name</h2>
        <v-form ref="form" lazy-validation>
          <v-text-field v-model="trip.trip.name" :rules="nameRules" :counter="60" required></v-text-field>
          <v-btn outline color="error" @click="update()" class="save-btn">Save Name</v-btn>
        </v-form>
      </div>
      <UserStatusList/>
    </v-flex>
  </v-layout>
</template>

<style>
.save-btn {
  margin: 0px 0px 30px 0px;
}
</style>

<script>
import UserStatusList from "./tripgroups/UserStatusList";
import { rules } from "../form_rules";

export default {
  name: "TripOverview",

  props: {
    trip: Object,
    updateTrip: Function,
    canEdit: Boolean
  },

  components: {
    UserStatusList
  },

  data() {
    return {
      ...rules
    };
  },

  methods: {
    /**
     * Validates the form and updates the tirp
     */
    update() {
      if (this.$refs.form.validate()) {
        this.updateTrip();
      }
    },

    computed: {
      /**
       * Determines if the user has integrated with slack or not
       */
      hasSlack() {
        return store.getters.getUser.slack;
      },

      /**
       * Determines if the current trip has a group attached to it.
       */
      groupAddedToTrip() {
        return this.selectedTrip.trip.usergroup.length > 0
      }
    },

    watch: {
      trip: function () {
        if (this.groupAddedToTrip) {
          this.slackWorkspaceDomain = this.getSlackWorkspaceDomain(this.trip.root.id);
        }
      }
    }
  }
};
</script>
