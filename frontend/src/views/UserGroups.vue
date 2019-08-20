

<template>
  <v-container fluid>
    <PageHeader
      title="User Groups"
      :canRedo="rollbackCanRedo"
      :canUndo="rollbackCanUndo"
      :undo="undo"
      :redo="redo"
    />
    <v-layout row wrap>
      <v-flex xs12 sm4 md3 pr-4>
        <UserGroupList
          :rollbackCheckpoint="rollbackCheckpoint"
          :selectUserGroup="selectUserGroup"
          :selectedGroup="selectedGroup"
          :usergroups="usergroups"
          :updateUserGroups="getUserGroups"
          :checkpoint="rollbackCheckpoint"
        />
      </v-flex>
      <v-flex xs12 sm8 md9>
        <GroupUsersTable
          :groupUsers="groupUsers"
          :selectedGroup="selectedGroup"
          :name="selectedGroup.name"
          :deleteUser="deleteUser"
          :isError="isError"
          :group="selectedGroup"
          :getUserGroups="getUserGroups"
          :isOwner="isOwner"
          :checkIfUserIsOwner="checkIfUserIsOwner"
        />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import GroupUsersTable from "../components/usergroups/GroupUsersTable";
import UserGroupList from "../components/usergroups/UserGroupNav";

import PageHeader from "../components/common/header/PageHeader";
import RollbackMixin from "../components/mixins/RollbackMixin.vue";
import { RepositoryFactory } from "../repository/RepositoryFactory";
let userGroupRepository = RepositoryFactory.get("userGroup");

export default {
  mixins: [RollbackMixin],
  data() {
    return {
      search: "",
      isError: false,
      selectedGroup: {id: null, name: null, description: null, owners: [], members: []},
      usergroups: [],
      isOwner: true
    };
  },
  components: {
    GroupUsersTable,
    UserGroupList,
    PageHeader
  },

  computed: {
    /**
     * returns a list of members/users in the selected group
     */
    groupUsers() {
      return this.selectedGroup.members;
    },
  },

  methods: {

    /**
     * Retrieves user groups from api
     */
    getUserGroups() {
      userGroupRepository.getGroupsForUser(this.$store.getters.getUser.id)
      .then(result => {
        this.usergroups = result.data;
        this.selectedGroup = result.data.find((res) => res.id === this.selectedGroup.id);
        if (!this.selectedGroup && result.data.length > 0) {
          this.selectedGroup = result.data[0];
        } else if (result.data.length == 0) {
          this.selectedGroup = {id: null, name: null, description: null, owners: [], members: []};
        }
        this.isOwner = this.checkIfUserIsOwner(this.$store.getters.getUser.id);
      })
    },

    /**
     * Checks to see if the given user is an owner of the currently selected group
     */
    checkIfUserIsOwner(userId) {
        if (this.selectedGroup.owners.length != 0) {
          return this.selectedGroup.owners.some((owner) => owner === userId);
        }
    },

    /**
     * Sets the selected user group
     */
    selectUserGroup(group) {
      this.selectedGroup = group;
      this.isOwner = this.checkIfUserIsOwner(this.$store.getters.getUser.id);
      // This is set to later be pushed as a reaction to the rollback stack
      this.rollbackSetPreviousBody(group);
    },

    /**
     * Removes user from a user group
     * @param userId the id of the user group to remove
     */
    async deleteUser(groupId, memberId) {
      this.isError = false;
      const url = `/users/${this.$store.getters.getUser.id}/group/${groupId}/member/${memberId}/toggle_deleted`;

      userGroupRepository.removeUserInUserGroup(
        this.$store.getters.getUser.id, 
        groupId, 
        memberId
      ).then(() => {
        this.getUserGroups();
        this.rollbackCheckpoint(
          "DELETE",
          {
            url: url
          },
          {
            url: url
          }
        );
      });
    },

    /**
     * Undoes the last action and gets users afterwards
     */
    undo: function() {
      this.isError = false;
      const actions = [this.getUserGroups];
      this.rollbackUndo(actions);
    },

    /**
     * Redoes the last action and gets users afterwards
     */
    redo: function() {
      this.isError = false;
      const actions = [this.getUserGroups];
      this.rollbackRedo(actions);
    },

    /**
     * Callback for rollback mixin rollbackCheckpoint function
     * @param {string} type The original action http method
     * @param {url: string, body: Object} actionBody The url and json body for the action request
     * @param {url: string, body: Object} reactionBody The url and json body for the reaction request
     */
    checkpoint: function(type, action, reaction) {
        this.rollbackCheckpoint(type, action, reaction);
    }
  },

  /**
   * Sets user administration status.
   */
  created: async function() {
    this.getUserGroups();
    await this.$store.dispatch("getUsers", false);
    if (this.$store.getters.getIsUserAdmin) {
      this.isAdmin = true;
    }
  }
};
</script>
