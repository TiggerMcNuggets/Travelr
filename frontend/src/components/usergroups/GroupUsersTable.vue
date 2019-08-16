<template>
  <v-container class="section-container">
    <SectionHeader :title="name + ' members'" disableUndoRedo :options="userTableOptions"/>
    <div v-if="addUserActive">
      <v-select
      :items="users"
      :v-model="selectedUserId"
      v-on:change="selectUser"
      ></v-select>
      <v-checkbox
        v-model="isMaintainer"
        label="Group Admin"
      ></v-checkbox>
      <v-btn
        color="error"
        v-on:click="addUserToGroup">
        Add user to group
      </v-btn>
    </div>


    <v-flex class="section-body">
      <v-data-table :headers="getColumns" :items="groupUsers">
        <template v-slot:items="props">
          <td v-on:click="goToUser(props.item.id)" class="text-xs-right">{{ props.item.firstName }}</td>
          <td class="text-xs-right">{{ props.item.lastName }}</td>
          <td class="text-xs-right">{{ props.item.dateOfBirth }}</td>
          <td class="text-xs-right">{{ props.item.gender }}</td>
          <td>
            <ul style="list-style-type:none">
              <li v-for="(nationality, index) in props.item.nationalities" :key="index">
                {{ nationality.name }}
                <br />
              </li>
            </ul>
          </td>
          <td>
            <ul style="list-style-type:none">
              <li v-for="(travelType, index) in props.item.travellerTypes" :key="index">
                {{ travelType.name }}
                <br />
              </li>
            </ul>
          </td>
          <td v-if="isAdmin" class="text-xs-right">
            <v-btn flat icon color="red lighten-2" v-on:click="deleteUser(group.id, props.item.id)">
              <v-icon>delete</v-icon>
            </v-btn>
          </td>
        </template>
      </v-data-table>
    </v-flex>
    <v-alert :value="isError" type="error">Cannot delete yourself or the global admin</v-alert>
  </v-container>
</template>


<script>
import SectionHeader from "../common/header/SectionHeader";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let userGroupRepository = RepositoryFactory.get("userGroup");

export default {
  props: {
    groupUsers: Array,
    getUserGroups: Function,
    selectedGroup: Object,
    name: String,
    deleteUser: Function,
    isError: Boolean,
    group: Object
  },

  components: {
    SectionHeader
  },

  data() {
    return {
      selectedUserId: 0,
      isMaintainer: false,
      addUserActive: false,
      isAdmin: false
    };
  },

  methods: {
    /**
     * Takes in a users id and redirects current page to that users account.
     * @param id
     */
    goToUser(id) {
      var endpoint = "/user/" + id;
      this.$router.push(endpoint);
    },
    
    /**
     * Toggles add user to group content
     */
    toggleAddUser() {
      this.addUserActive = !this.addUserActive;
    },

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
      userGroupRepository.addUserToUserGroup(this.$store.getters.getUser.id, this.selectedGroup.id, this.selectedUserId, {
        isOwner: this.isMaintainer
      }).then(() => {
        this.getUserGroups();
      });
    }
  },

  computed: {
    /**
     * Gets a list of columns for the table in format {text: String, value: String, align: String, sortable: boolean}
     * If the user is an admin add column delete to list.
     * @return a list of what columns should be in the table
     */
    getColumns() {
      const columns = [
        {
          text: "First Name",
          value: "firstName",
          align: "left",
          sortable: true
        },
        { text: "Last Name", value: "lastName", align: "left", sortable: true },
        { text: "DOB", value: "dateOfBirth", align: "left", sortable: true },
        { text: "Gender", value: "gender", align: "left", sortable: true },
        {
          text: "Nationalities",
          value: "nationalities",
          align: "left",
          sortable: true
        },
        {
          text: "Traveller Types",
          value: "types",
          align: "left",
          sortable: true
        }
      ];

      // Checking if user is admin and adding delete button if they are
      if (this.isAdmin) {
        columns.push({ text: "Delete", align: "left", sortable: false });
      }
      return columns;
    },

    /**
     * returns a list of all button options for GroupUsersTable, each specifying an icon and the function of the button.
     */
    userTableOptions() {
      return [
        {
          action: this.toggleAddUser,
          icon: "add"
        },
      ]
    },

    /**
     * function to get a list of users mapping first and last name to text and id to value for the v-select
     */
    users() {
      return this.$store.state.users.users.map(user => ({text: user.firstName + " " + user.lastName, value: user.id, id: user.id}));
    },
  },

  /**
   * Sets user administration status.
   */
  created: async function() {
    if (this.$store.getters.getIsUserAdmin) {
      this.isAdmin = true;
    }
  }
};
</script>
