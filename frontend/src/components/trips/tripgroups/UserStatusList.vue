<template>
  <v-flex>
    <v-list class="pa-0" v-for="user in selectedTrip.trip.usergroup" :key="user.userId" wrap>
      <v-list-tile avatar>
        <v-list-tile-avatar>
          <v-icon color="success lighten-1" v-if="user.status == 'GOING'">check</v-icon>
          <v-icon color="error lighten-1" v-if="user.status == 'NOT GOING'">close</v-icon>
          <v-icon color="warning" v-if="user.status == 'MAYBE'">?</v-icon>
        </v-list-tile-avatar>

        <v-list-tile-content>
          <v-list-tile-title>{{`${user.firstName} ${user.lastName}`}}</v-list-tile-title>
        </v-list-tile-content>

        <v-list-tile-action v-if="canEdit(user.userId)"> 
          <v-autocomplete
            :items="statuses"
            :v-model="user.status"
            v-on:change="changeStatus($event, user.userId)"
          ></v-autocomplete>
        </v-list-tile-action>
      </v-list-tile>
    </v-list>
  </v-flex>
</template>

<script>
import { RepositoryFactory } from "../../../repository/RepositoryFactory";
let tripRepository = RepositoryFactory.get("trip");
import StoreTripsMixin from "../../mixins/StoreTripsMixin";

export default {
  mixins: [StoreTripsMixin],

  data() {
    return {
      statuses: ["GOING", "NOT GOING", "MAYBE"]
    };
  },

  methods: {
    changeStatus(status, userId) {
      let payload = {"status": status}
      tripRepository.updateGroupTripStatus(userId, this.selectedTrip.trip.id, payload).then(() => this._getTrip(this.$store.getters.getUser.id, this.tripId))
    },

    canEdit(userId) {
      return userId === this.$store.getters.getUser.id || this.$store.getters.getIsUserAdmin
    }
  },

  updated() {
    this._getTrip(this.$store.getters.getUser.id, this.tripId)
  },

  props: {
    tripId: Number
  }
};
</script>


