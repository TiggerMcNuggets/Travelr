<template>
  <v-dialog :value="dialogActive" @input="closeGroupDialog" width="500">
    
    <v-card>
      <v-card-title primary-title>
        <h2 class="headline">Add Group to Trip</h2>
      </v-card-title>
      <v-divider />
      <v-card-text>
        <v-layout row wrap>
          <v-flex xs12>
            <v-autocomplete
              v-model="selectedUserGroup"
              :hint="`Select the user group to add`"
              :items="filteredGroups"
              item-text="text"
              item-value="id"
              label="Select Group"
              persistent-hint
              return-object
              single-line
            ></v-autocomplete>
          </v-flex>
        </v-layout>
        <v-alert :value="isError" :dismissible="true" type="error" class="mb-4">{{errorMessage}}</v-alert>
      </v-card-text>

      <v-divider />

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn ma-3 flat v-on:click="closeGroupDialog">Cancel</v-btn>
        <v-btn ma-3 color="primary" flat v-on:click="setUserGroup">Set Group</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import StoreTripsMixin from "../../mixins/StoreTripsMixin";
import { RepositoryFactory } from "../../../repository/RepositoryFactory";

let userGroupRepository = RepositoryFactory.get("userGroup");
let tripRepository = RepositoryFactory.get("trip");

export default {
  props: {
    closeGroupDialog: Function,
    dialogActive: Boolean
  },

  mixins: [StoreTripsMixin],

  data() {
    return {
      userGroups: [],
      errorMessage: "",
      isError: false,
      userId: this.$route.params.id,
      selectedUserGroup: {text: "None"},
      initialUserGroup: {id: null, name: undefined}
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

      filteredGroups = filteredGroups.map(group => ({
        text: group.name,
        id: group.id
      }));

      filteredGroups.unshift({text: "None"});

      return filteredGroups;
    },
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
    setUserGroup() {
      this.isError = false;
      if (this.selectedUserGroup.text == "None") {
        // Check if the initial user group is a group or not before trying to remove
        if (this.initialUserGroup.id !== null) {
          tripRepository.removeGroupTrip(this.userId, this.tripId, this.initialUserGroup.id)
          .then(() => {
            this._getTrip(this.$store.getters.getUser.id, this.tripId).then(() => this.closeGroupDialog());
          });
        }
        this.initialUserGroup = {text: "None"};
        this.closeGroupDialog()
      } else {
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
        this.initialUserGroup = this.selectedUserGroup;
      }
    },

    /**
     * Sets an error message if error
     * @param error The error message
     */
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
        if (this.selectedTrip.root.groupId) {
          this.initialUserGroup = {
            text: this.selectedTrip.root.groupName,
            id: this.selectedTrip.root.groupId
          };
          this.selectedUserGroup = {
            text: this.selectedTrip.root.groupName,
            id: this.selectedTrip.root.groupId
          };
        }
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
      this.initialUserGroup = this.selectedUserGroup;
      }
    },
    selectedUserGroup: function(newUserGroup) {
      if ((newUserGroup.id != this.initialUserGroup.id) && (this.initialUserGroup.text != "None") && (this.initialUserGroup.id != null)) {
        this.errorMessage = "Changing the group will reset the attendance status of all members."
        this.isError = true;
      } else {
        this.isError = false;
      }
    }
  },


  mounted() {
    this.getUserGroups();
  }
};
</script>
