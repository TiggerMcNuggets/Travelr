<template>
  <v-flex>
    <v-card-title></v-card-title>
    <v-data-table :headers="getColumns" :items="users" :search="search">
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
          <v-btn flat icon color="red lighten-2" v-on:click="deleteUser(props.item.id)">
            <v-icon>delete</v-icon>
          </v-btn>
        </td>
      </template>
    </v-data-table>
    <v-alert :value="isError" type="error">Cannot delete yourself or the global admin</v-alert>
  </v-flex>
</template>


<script>
export default {
  props: {
    search: String,
    deleteUser: Function,
    isError: Boolean
  },

  data() {
    return {
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
    }
  },

  computed: {
    users() {
      return this.$store.state.users.users;
    },

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
      //Checking if user is admin and addind delete button if they are
      if (this.isAdmin) {
        columns.push({ text: "Delete", align: "left", sortable: false });
      }
      return columns;
    }
  },

  /**
   * Gets users on component creation and sets user administration status.
   */
  created: async function() {
    await this.$store.dispatch("getUsers", false);
    if (this.$store.getters.getIsUserAdmin) {
      this.isAdmin = true;
    }
  }
};
</script>
