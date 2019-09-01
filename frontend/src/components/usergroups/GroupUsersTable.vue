<template>
  <v-container class="section-container">
    <SectionHeader :title="name + ' members'" disableUndoRedo :options="userTableOptions"/>
    <div v-if="addUserActive">
      <v-autocomplete
      :items="users"
      :v-model="selectedUserId"
      v-on:change="selectUser"
      ></v-autocomplete>
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
          <td class="text-xs-right">
            {{ isUserOwner(props.item.id) ? "Owner" : "Member" }} 
            <v-btn flat icon @click="togglePromoteUser(group.id, props.item.id)" v-if="isOwnerOrAdmin">
              <v-icon v-if="isOwnerOrAdmin">
                {{ isUserOwner(props.item.id) ? "arrow_downward" : "arrow_upward" }}
              </v-icon>
            </v-btn>
          </td>
          <td class="text-xs-right">
            <v-btn flat icon color="red lighten-2" v-on:click="deleteUser(group.id, props.item.id)" v-if="isDeletable(props.item.id)">
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
    group: Object,
    isOwnerOrAdmin: Boolean,
    checkIfUserIsOwner: Function,
    togglePromoteUser: Function
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
      userGroupRepository.addUserToUserGroup(
        this.$store.getters.getUser.id, this.selectedGroup.id, this.selectedUserId, {isOwner: this.isMaintainer}
        )
        .then(() => {
          this.isMaintainer = false;
          this.getUserGroups();
        });
    },

    /**
     * Checks if user is owner in the group
     * @param: userId The user's id
     */
    isUserOwner(userId) {
      return this.selectedGroup.owners.some((owner) => owner === userId);
    },
    
    /*
     * Checks if a given user id can be deleted by the currently logged in user.
     * Admins and group owners have the same permissions.
     * Group owners cannot delete other group owners.
     * Group owners cannot remove themselves from the group.
     */
    isDeletable(targetUserId) {
      return !!((this.isOwnerOrAdmin && !this.checkIfUserIsOwner(targetUserId, this.group)) || targetUserId === this.$store.getters.getUser.id);
    }
  },

  computed: {

    /**
     * Gets a list of columns for the table in format {text: String, value: String, align: String, sortable: boolean}
     * If the user is an admin add column delete to list.
     * @return *[] list of what columns should be in the table
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
        },
        {
          text: "Status",
          value: "isOwner",
          align: "left",
          sortable: true
        }
      ];

      // Checking if user is admin and adding delete button if they are
      if (this.isOwnerOrAdmin) {
        columns.push({ text: "Delete", align: "left", sortable: false });
      }
      return columns;
    },

    /**
     * returns a list of all button options for GroupUsersTable, each specifying an icon and the function of the button.
     */
    userTableOptions() {
      const options = [];
      if (this.isOwnerOrAdmin) {
        options.push({
          action: this.toggleAddUser,
          icon: "add"
        })
      }
      return options;
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


  }
};
</script>
