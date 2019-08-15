<template>
  <v-container class="section-container">
    <SectionHeader :title="name + ' members'" disableUndoRedo />

    <v-flex class="section-body">
      <v-data-table :headers="getColumns" :items="users">
        <template v-slot:items="props">
          <td @click="goToUser(props.item.id)" class="text-xs-right">{{ props.item.firstName }}</td>
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
    users: Array,
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
      isAdmin: false
    };
  },

  watch: {
    users: () => {
    }
  },

  methods: {
    /**
     * Takes in a users id and redirects current page to that users account.
     * @param id
     */
    goToUser(id) {
      var endpoint = "/user/" + id;
      this.$router.push(endpoint);
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
