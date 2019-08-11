<template>
  <v-layout>
    <v-flex xs12>
      <UserGroupNavItem
        class="usergroup-nav-item"
        v-bind:key="index"
        v-for="(usergroup, index) in userGroupsFiltered"
        :usergroup="usergroup"
        :isSelected="selectedGroup == usergroup"
        :selectUserGroup="selectUserGroup"
      />
    </v-flex>
  </v-layout>
</template>

<script>
import UserGroupNavItem from "./UserGroupNavItem";

export default {
  components: {
    UserGroupNavItem
  },
  data() {
    return {};
  },
  methods: {},

  props: {
    usergroups: Array,
    selectUserGroup: Function,
    selectedGroup: Object,
    search: String
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

      //Currently sorting trips by id, in future we will sort trips by creation time
      return filteredList.sort(function(a, b) {
        return a.name - b.name;
      });
    }
  }
};
</script>

