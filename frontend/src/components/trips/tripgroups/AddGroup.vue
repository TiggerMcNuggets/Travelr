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
            :items="filteredGroups"
            :v-model="selectedUserGroup"
            v-on:change="selectGroup"
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
import { RepositoryFactory } from "../../../repository/RepositoryFactory";

let userGroupRepository = RepositoryFactory.get("userGroup");
let tripRepository = RepositoryFactory.get("trip");

export default {
  props: {
    closeGroupDialog: Function,
    tripId: Number
  },

  data() {
    return {
      selectedUserGroup: {},
      userGroups: [],
      errorMessage: "",
      isError: false
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
        value: group.id,
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
          this.selectedUserGroup
        )
        .then(this.closeGroupDialog)
        .catch(this.setErrorMessage("Error adding group to trip."));
    },

    setErrorMessage(error) {
      this.isError = true;
      this.errorMessage = error;
    },

    /**
     * Updates the selectedUserGroup variable to the given group id
     */
    selectGroup(groupId) {
      this.selectedUserGroup = groupId;
    },

    /**
     * function to get a list of users mapping first and last name to text and id to value for the v-select
     */
    users() {
      return this.$store.state.users.users.map(user => ({
        text: user.firstName + " " + user.lastName,
        value: user.id,
        id: user.id
      }));
    },

    /**
     * Retrieves user groups from api
     */
    getUserGroups() {
      userGroupRepository
        .getGroupsForUser(this.$store.getters.getUser.id)
        .then(result => {
          this.userGroups = result.data;
        });
    }
  },

  mounted() {
    this.getUserGroups();
  }
};
</script>
