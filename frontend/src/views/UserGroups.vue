

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
        <UserGroupNav
          :rollbackCheckpoint="rollbackCheckpoint"
          :selectUserGroup="selectUserGroup"
          :selectedGroup="selectedGroup"
          :userGroups="userGroups"
          :updateUserGroups="getUserGroups"
          :checkpoint="rollbackCheckpoint"
          :isOwnerOrAdmin="isOwnerOrAdmin"
          :checkIfUserIsOwner="checkIfUserIsOwner"
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
          :isOwnerOrAdmin="isOwnerOrAdmin"
          :checkIfUserIsOwner="checkIfUserIsOwner"
          :togglePromoteUser="togglePromoteUser"
        />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import GroupUsersTable from "../components/usergroups/GroupUsersTable";
import UserGroupNav from "../components/usergroups/UserGroupNav";
import dateTime from "../components/common/dateTime/dateTime.js";
import PageHeader from "../components/common/header/PageHeader";
import RollbackMixin from "../components/mixins/RollbackMixin.vue";
import { RepositoryFactory } from "../repository/RepositoryFactory";
import {deepCopy} from "../tools/deepCopy";
let userGroupRepository = RepositoryFactory.get("userGroup");

export default {
  mixins: [RollbackMixin],
  data() {
    return {
      search: "",
      isError: false,
      selectedGroup: {id: null, name: null, description: null, owners: [], members: []},
      userGroups: [],
      isOwner: true
    };
  },
  components: {
    GroupUsersTable,
    UserGroupNav,
    PageHeader
  },

  computed: {
    /**
     * returns a list of members/users in the selected group
     */
    groupUsers() {
      return this.selectedGroup.members.map(item => {item.dateOfBirth = dateTime.convertTimestampToString(item.dateOfBirth); return item });
    },

    /**
     * Returns {boolean} stating if user is admin or group owner
     */
    isOwnerOrAdmin() {
      return (this.$store.getters.getIsUserAdmin || this.isOwner);
    },
  },

  methods: {

    /**
     * Retrieves user groups from api
     */
    getUserGroups() {
      userGroupRepository.getGroupsForUser(this.$store.getters.getUser.id)
      .then(result => {
        this.userGroups = result.data;
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
     * Checks to see if the given user is an owner of a given group or a site admin
     */
    checkIfUserIsOwner(userId, selectedGroup) {
      let group = selectedGroup ? deepCopy(selectedGroup) : deepCopy(this.selectedGroup);
      if (group.owners.length > 0) {
        return group.owners.some((owner) => owner === userId);
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
     * Sends a request to toggle the member's ownership status
     * @param: memberId The member's id
     */
    togglePromoteUser(groupId, memberId) {
      this.isError = false;
      const url = `/users/${this.$store.getters.getUser.id}/group/${groupId}/member/${memberId}/promote`

      userGroupRepository.togglePromoteUser(
        this.$store.getters.getUser.id, 
        groupId, 
        memberId
      ).then(() => {
        this.getUserGroups();
        this.rollbackCheckpoint(
          "PUT",
          {
            url: url
          },
          {
            url: url
          }
        );
      }).catch(() => {
        this.isError = true;
      })
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
  }
};
</script>
