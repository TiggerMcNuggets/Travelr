<template>
  <v-flex v-if="selectedTrip.trip.usergroup">
    <v-flex
      pa-3
    >{{`${getStatusNumber("GOING")} going, ${getStatusNumber("NOT GOING")} not going and ${getStatusNumber("MAYBE")} maybe`}}</v-flex>
    <v-list class="pa-0" v-for="user in selectedTrip.trip.usergroup" :key="user.userId" wrap>
      <v-list-tile>
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

        <v-list-tile-action>
          <v-icon color="success lighten-1" v-if="user.status == 'GOING'">check</v-icon>
          <v-icon color="error lighten-1" v-if="user.status == 'NOT GOING'">close</v-icon>
          <v-icon color="warning" v-if="user.status == 'MAYBE'">?</v-icon>
        </v-list-tile-action>
      </v-list-tile>
    </v-list>
    <!-- <v-data-table :headers="getColumns" :items="selectedTrip.trip.usergroup">
      <template v-slot:items="props">
        <td class="text-xs-right">{{ props.item.firstName }}</td>
        <td class="text-xs-right">{{ props.item.lastName }}</td>
        <td class="text-xs-right status-col" v-if="canEdit(props.item.userId)">
          <v-autocomplete
            class="status-chooser"
            :items="statuses"
            :v-model="props.item.status"
            v-on:change="changeStatus($event, props.item.userId)"
          ></v-autocomplete>
          <v-icon color="success lighten-1" v-if="props.item.status == 'GOING'">check</v-icon>
          <v-icon color="error lighten-1" v-if="props.item.status == 'NOT GOING'">close</v-icon>
          <v-icon color="warning" v-if="props.item.status == 'MAYBE'">?</v-icon>
        </td>
        <td class="text-xs-right" v-else>
          {{ props.item.status }}
          <v-icon color="success lighten-1" v-if="props.item.status == 'GOING'">check</v-icon>
          <v-icon color="error lighten-1" v-if="props.item.status == 'NOT GOING'">close</v-icon>
          <v-icon color="warning" v-if="props.item.status == 'MAYBE'">?</v-icon>
        </td>
      </template>
    </v-data-table>-->
  </v-flex>
</template>

<style <style lang="scss">
.status-col {
  display: flex;
  justify-content: flex-end;
  .v-input {
    width: 40% !important;
    padding-bottom: 10px;
  }
}
</style>


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

  computed: {
    /**
     * Gets a list of columns for the table in format {text: String, value: String, align: String, sortable: boolean}
     * If the user is an admin add column delete to list.
     * @return *[] list of what columns should be in the table
     */
    getColumns() {
      const columns = [
        {
          text: "First Name",
          value: "firstName",
          align: "left",
          sortable: true
        },
        { text: "Last Name", value: "lastName", align: "left", sortable: true },
        { text: "Status", value: "status", align: "left", sortable: true }
      ];

      // Checking if user is admin and adding delete button if they are
      if (this.isOwnerOrAdmin) {
        columns.push({ text: "Delete", align: "left", sortable: false });
      }
      return columns;
    }
  },

  methods: {
    changeStatus(status, userId) {
      let payload = { status: status };
      tripRepository
        .updateGroupTripStatus(userId, this.selectedTrip.trip.id, payload)
        .then(() => this._getTrip(this.$store.getters.getUser.id, this.tripId));
    },

    getStatusNumber(statusType) {
      return this.selectedTrip.trip.usergroup.filter(
        user => user.status == statusType
      ).length;
    },

    canEdit(userId) {
      return (
        userId === this.$store.getters.getUser.id ||
        this.$store.getters.getIsUserAdmin
      );
    }
  },

  updated() {
    this._getTrip(this.$store.getters.getUser.id, this.tripId);
  },

  props: {
    tripId: Number
  }
};
</script>


