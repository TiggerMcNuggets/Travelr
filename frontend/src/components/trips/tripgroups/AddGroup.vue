<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Add Group to Trip</h2>
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text>
      <v-layout row wrap>
        <v-flex xs12>
          <v-autocomplete
            v-model="selectedUserGroup"
            :hint="`Select the user group to add`"
            :items="filteredGroups"
            item-text="text"
            item-value="id"
            label="Select"
            persistent-hint
            return-object
            single-line
          ></v-autocomplete>
        </v-flex>
      </v-layout>
      <v-alert :value="isError" :dismissible="true" type="error" class="mb-4">{{errorMessage}}</v-alert>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat @click="closeGroupDialog">Cancel</v-btn>
      <v-btn ma-3 color="primary" flat @click="addUserGroup">Add Group</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import StoreTripsMixin from "../../mixins/StoreTripsMixin";
import { RepositoryFactory } from "../../../repository/RepositoryFactory";

let userGroupRepository = RepositoryFactory.get("userGroup");
let tripRepository = RepositoryFactory.get("trip");

export default {
  props: {
    closeGroupDialog: Function,
    tripId: Number
  },

  mixins: [StoreTripsMixin],

  data() {
    return {
      userGroups: [],
      errorMessage: "",
      isError: false,
      userId: this.$route.params.id,
      selectedUserGroup: {},
    };
  },

  computed: {
    /**
     * Gets the groups which the user is owner for to populate the list.
     */
    filteredGroups() {
      let filteredGroups = this.userGroups.filter(group => {
        return this.isUserOwner(this.$store.getters.getUser.id, group);
      });

      return filteredGroups.map(group => ({
        text: group.name,
        id: group.id
      }));
    }
  },

  methods: {
    /**
     * Checks to see if the given user is an owner of a given group or a site admin
     */
    isUserOwner(userId, selectedGroup) {
      if (selectedGroup.owners.length > 0) {
        return selectedGroup.owners.some(owner => owner === userId);
      }
    },

    /**
     * Calls API to add a user group to the trip.
     */
    addUserGroup() {
      this.isError = false;
      tripRepository
        .toggleGroupTrip(
          this.$store.getters.getUser.id,
          this.tripId,
          this.selectedUserGroup.id
        )
        .then(() => {
          this._getTrip(this.$store.getters.getUser.id, this.tripId).then(() => this.closeGroupDialog());
        })
        .catch(() => {
          this.setErrorMessage("Error adding group to trip.");
        });
    },

    setErrorMessage(error) {
      this.isError = true;
      this.errorMessage = error;
    },

    /**
     * Retrieves user groups from api
     */
    getUserGroups() {
      userGroupRepository.getGroupsForUser(this.userId).then(result => {
        this.userGroups = result.data;
      });
    }
  },

    watch: {
    selectedTrip: function(newTrip, oldTrip) {
      if (newTrip !== oldTrip) {
        this.selectedUserGroup = {
        text: this.selectedTrip.root.groupName,
        id: this.selectedTrip.root.groupId
      };
      }
    }
  },


  mounted() {
    this.getUserGroups();
  }
};
</script>
