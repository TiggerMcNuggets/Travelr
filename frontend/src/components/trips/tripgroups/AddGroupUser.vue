<template>
  <v-flex :v-if="selectedTrip && selectedTrip.trip.usergroup && isOwnerOrAdmin">
    <v-flex>
        <h2>Add User</h2>
    </v-flex>
    <v-autocomplete
    :items="users"
    :v-model="selectedUserId"
    v-on:change="selectUser"
    ></v-autocomplete>
    <v-btn outline color="error" v-on:click="addUserToGroup" class="save-btn">Add User to Group</v-btn>
  </v-flex>
</template>

<script>
import { RepositoryFactory } from "../../../repository/RepositoryFactory";
let userGroupRepository = RepositoryFactory.get("userGroup");
import StoreTripsMixin from "../../mixins/StoreTripsMixin";

export default {
  mixins: [StoreTripsMixin],

  data() {
    return {
      selectedUserId: 0,
      userId: this.$store.getters.getUser.id
    };
  },

  computed: {
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
  },

  methods: {
    /**
     * Updates the selectedUserId variable to the given user id
     */
    selectUser(userId) {
      this.selectedUserId = userId;
    },

    /**
    * Sends a request to add the selected user to the group
    */
    addUserToGroup() {
      userGroupRepository.addUserToUserGroup(
        this.$store.getters.getUser.id, this.selectedTrip.root.groupId, this.selectedUserId, {isOwner: false}
        )
        .then(() => {
          this._getTrip(this.$store.getters.getUser.id, this.tripId).then(() => this.closeGroupDialog());
          this.getUserGroups();
        });
    },
  },

  mounted: async function() {
    await this.$store.dispatch("getUsers", false);
  }
};
</script>


