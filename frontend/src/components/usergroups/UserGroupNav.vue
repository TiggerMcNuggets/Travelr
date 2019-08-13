<template>
  <v-container class="section-container">
    <SectionHeader title="User Groups List" disableUndoRedo :options="usergroupsOptions"/>
    <v-flex class="section-body">
      <UserGroupNavItem
        class="usergroup-nav-item"
        v-bind:key="index"
        v-for="(usergroup, index) in userGroupsFiltered"
        :usergroup="usergroup"
        :isSelected="selectedGroup == usergroup"
        :selectUserGroup="selectUserGroup"
      />
    </v-flex>
  </v-container>
</template>

<script>
import UserGroupNavItem from "./UserGroupNavItem";
import SectionHeader from "../common/header/SectionHeader";

export default {
  components: {
    UserGroupNavItem,
    SectionHeader
  },
  data() {
    return {};
  },
  methods: {},

  props: {
    usergroups: Array,
    selectUserGroup: Function,
    selectedGroup: Object,
    search: String,
    usergroupsOptions: Object
  },

  computed: {
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

