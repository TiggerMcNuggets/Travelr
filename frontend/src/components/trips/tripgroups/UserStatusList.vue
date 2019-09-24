<template>
  <v-flex v-if="selectedTrip && selectedTrip.trip.usergroup">
    <v-flex>
      <h2>{{selectedTrip.root.groupName}}</h2>
      <p
        class="sub-text"
      >{{`${getStatusNumber("GOING")} going, ${getStatusNumber("NOT_GOING")} not going and ${getStatusNumber("MAYBE")} maybe`}}</p>
    </v-flex>

    <p v-if="selectedTrip.trip.usergroup.length === 0">No group added.</p>

    <v-list class="pa-0" v-for="user in selectedTrip.trip.usergroup" :key="user.userId" wrap>
      <v-list-tile class="status-list">
        <v-list-tile-content>
          <v-list-tile-title>{{`${user.firstName} ${user.lastName}`}}</v-list-tile-title>
        </v-list-tile-content>

        <v-list-tile-action v-if="canEdit(user.userId)">
          <v-select
            class="status-select"
            :items="statuses"
            :v-model="defaultStatus"
            :value="user.status"
            single-line
            item-text="title"
            item-value="value"
            v-on:change="changeStatus($event, user.userId)"
          ></v-select>
        </v-list-tile-action>

        <v-list-tile-action>
          <v-icon color="success lighten-1" v-if="user.status == 'GOING'">check_circle</v-icon>
          <v-icon color="error lighten-1" v-if="user.status == 'NOT_GOING'">cancel</v-icon>
          <v-icon color="warning" v-if="user.status == 'MAYBE'">help</v-icon>
          <v-icon color="secondary" v-if="user.status == 'NOT ANSWERED'">remove_circle</v-icon>
        </v-list-tile-action>
      </v-list-tile>
    </v-list>
  </v-flex>
</template>

<style lang="scss">
.status-select {
  width: 120px;
}

.status-list .v-list__tile {
  padding: 0px;
}

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
      defaultStatus: null,
      statuses: [
        {
          title: "Going",
          value: "GOING"
        },
        {
          title: "Not Going",
          value: "NOT_GOING"
        },
        {
          title: "Interested",
          value: "MAYBE"
        }
      ],
      userId: this.$route.params.id
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
    /**
     * Sends a request for changing status.
     */
    changeStatus(status, userId) {
      let payload = { status: status };
      tripRepository
        .updateGroupTripStatus(userId, this.selectedTrip.trip.id, payload)
        .then(() => {
          this._getTrip(this.$store.getters.getUser.id, this.tripId);
        });
    },

    /**
     * Gets the number of responses for a given status
     */
    getStatusNumber(statusType) {
      return this.selectedTrip.trip.usergroup.filter(
        user => user.status == statusType
      ).length;
    },

    /**
     * Checks if the user status can be edited.
     */
    canEdit(userId) {
      return (
        userId === this.$store.getters.getUser.id ||
        this.$store.getters.getIsUserAdmin
      );
    }
  },
};
</script>


