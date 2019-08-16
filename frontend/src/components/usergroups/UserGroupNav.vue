<template>
  <v-container class="section-container">
    <SectionHeader title="User Groups List" disableUndoRedo :options="usergroupsOptions"/>
    <v-flex v-if="searchActive" xs12 pb-4 pt-4 pr-2 pl-2>
      <v-text-field v-model="search" append-icon="search" label="Search" single-line hide-details></v-text-field>
    </v-flex>
    <CreateGroupForm v-if="createGroupActive" :updateUserGroups="updateUserGroups"/>
    <v-flex class="section-body" pr-0 pl-0 pt-4>
      <UserGroupNavItem
        class="usergroup-nav-item"
        v-bind:key="index"
        v-for="(usergroup, index) in userGroupsFiltered"
        :usergroup="usergroup"
        :isSelected="selectedGroup == usergroup"
        :selectUserGroup="selectUserGroup"
        :updateUserGroups="updateUserGroups"
      />
    </v-flex>
  </v-container>
</template>

<script>
import UserGroupNavItem from "./UserGroupNavItem";
import SectionHeader from "../common/header/SectionHeader";
import CreateGroupForm from "./CreateGroupForm";

export default {
  components: {
    UserGroupNavItem,
    SectionHeader,
    CreateGroupForm
  },

  data() {
    return {
      search: "",
      createGroupActive: false,
      searchActive: false
    };
  },

  props: {
    usergroups: Array,
    selectUserGroup: Function,
    selectedGroup: Object,
    updateUserGroups: Function
  },

  methods: {
    /**
     * Toggles the create user group component
     */
    toggleAddUserGroup() {
      this.searchActive = false;
      this.createGroupActive = !this.createGroupActive;
    },

    /**
     * Toggles showing the search component
     */
    toggleSearch() {
      this.searchActive = !this.searchActive;
      this.createGroupActive = false;
    }
  },

  computed: {
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

    /**
     * Filtered User Groups
     */
    userGroupsFiltered() {
      const filteredList = this.usergroups.filter(
        usergroup =>
          usergroup.name.toLowerCase().search(this.search.toLowerCase()) !== -1
      );

      // sorting alphabetically by name
      return filteredList.sort(function(a, b) {
        return a.name - b.name;
      });
    }
  }
};
</script>

