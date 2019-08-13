

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
        <!-- <v-btn
          icon
          @click="() => {searchActive = false; createGroupActive=!createGroupActive}"
          flat
          small
          color="primary lighten-1"
          v-on="on"
        >
          <v-icon>add</v-icon>
        </v-btn>
        <v-btn
          icon
          @click="() => {searchActive = !searchActive; createGroupActive=false}"
          flat
          small
          color="primary lighten-1"
          v-on="on"
        >
          <v-icon>search</v-icon>
        </v-btn>-->
        <!-- <v-layout class="section-heading" d-flex justify-space-between>
          <h3>User Groups List</h3>
          <v-flex class="section-options-container">
            <v-btn
              icon
              @click="() => {searchActive = false; createGroupActive=!createGroupActive}"
              flat
              small
              color="white"
              v-on="on"
            >
              <v-icon>add</v-icon>
            </v-btn>
            <v-btn
              icon
              @click="() => {searchActive = !searchActive; createGroupActive=false}"
              flat
              small
              color="white"
              v-on="on"
            >
              <v-icon>search</v-icon>
            </v-btn>
          </v-flex>
        </v-layout>-->
        <v-flex v-if="searchActive" xs12 pb-4 pt-4 pr-2 pl-2>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-flex>
        <!-- <CreateGroupForm/> -->
        <CreateGroupForm v-if="createGroupActive"/>
        <UserGroupList
          :search="search"
          :selectUserGroup="selectUserGroup"
          :selectedGroup="selectedGroup"
          :usergroups="usergroups"
          :usergroupsOptions="usergroupsOptions()"
        />
      </v-flex>
      <v-flex xs12 sm8 md9>
        <GroupUsersTable
          :users="users"
          :name="selectedGroup.name"
          :deleteUser="deleteUser"
          :isError="isError"
        />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import GroupUsersTable from "../components/usergroups/GroupUsersTable";
import UserGroupList from "../components/usergroups/UserGroupNav";
import CreateGroupForm from "../components/usergroups/CreateGroupForm";

import PageHeader from "../components/common/header/PageHeader";
import RollbackMixin from "../components/mixins/RollbackMixin.vue";
import sampleUserGroups from "./usergroups.json";

export default {
  mixins: [RollbackMixin],
  data() {
    return {
      search: "",
      isError: false,
      selectedGroup: sampleUserGroups[0],
      usergroups: sampleUserGroups,
      searchActive: false,
      createGroupActive: false
    };
  },
  components: {
    GroupUsersTable,
    UserGroupList,
    CreateGroupForm,
    PageHeader
  },

  computed: {
    users() {
      return this.selectedGroup.members;
    }
  },

  methods: {
    /**
     * Options used in the header component.
     */
    usergroupsOptions() {
      return [
        {
          action: this.toggleAddUserGroup,
          icon: "add"
        },
        {
          action: this.toggleSearch,
          icon: "search"
        }
      ];
    },

    toggleSearch() {
      this.searchActive = false;
      this.createGroupActive = !this.createGroupActive;
    },

    toggleAddUserGroup() {
      this.searchActive = !this.searchActive;
      this.createGroupActive = false;
    },

    /**
     * Retrieves user groups from api
     */
    getUserGroups() {
      // TODO: Connect to uesr groups endpoint
    },

    selectUserGroup(group) {
      this.selectedGroup = group;
    },

    /**
     * Removes user from a user group
     * @param userId the id of the user group to remove
     */
    async deleteUser() {
      this.isError = false;
      // TODO: Connect to delete user from group

      this.getUserGroups();
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
    }
  },

  /**
   * Sets user administration status.
   */
  created: async function() {
    this.getUserGroups();
    if (this.$store.getters.getIsUserAdmin) {
      this.isAdmin = true;
    }
  }
};
</script>
