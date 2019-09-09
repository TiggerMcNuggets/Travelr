<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Add Group to Trip</h2>
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text>
      <v-layout row wrap>
        <v-flex xs12 ml-3 mr-3>
          <v-autocomplete
            :items="getFilteredUserGroups()"
            :v-model="selectedUserGroup"
            v-on:change="selectGroup"
          ></v-autocomplete>
        </v-flex>
      </v-layout>
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
import { deepCopy } from "../../../tools/deepCopy";

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
      userGroups: []
    };
  },

  methods: {
    /**
     * Calls API to add a user group to the trip.
     */
    addUserGroup() {
      tripRepository.toggleGroupTrip(
        this.$store.getters.getUser.id,
        this.tripId,
        this.selectedUserGroup
      );
    },

    /**
     * Updates the selectedUserGroup variable to the given group id
     */
    selectGroup(groupId) {
      this.selectedUserGroup = groupId;
    },

    /**
     * Gets the groups which the user is owner for to populate the list.
     */
    getFilteredUserGroups() {
      let filteredGroups = this.userGroups.filter(group => {
        return this.checkIfUserIsOwner(this.$store.getters.getUser.id, group);
      });

      return filteredGroups.map(group => ({
        text: group.name,
        value: group.id,
        id: group.id
      }));
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
    },

    /**
     * Checks to see if the given user is an owner of a given group or a site admin
     */
    checkIfUserIsOwner(userId, selectedGroup) {
      let group = deepCopy(selectedGroup);
      if (group.owners.length > 0) {
        return group.owners.some(owner => owner === userId);
      }
    }
  },

  mounted() {
    this.getUserGroups();
  }
};
</script>
