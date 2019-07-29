<template>
    <v-form>
      <v-layout wrap class="side-filter">
        <v-flex xs12 sm6 md6>
          <v-text-field v-model="search_params.firstName" label="First Name"></v-text-field>
        </v-flex>
        <v-flex xs12 sm6 md6>
          <v-text-field v-model="search_params.lastName" label="Last Name"></v-text-field>
        </v-flex>
        <v-flex xs12 sm6 md6>
          <v-text-field v-model.number="search_params.minAge" label="Min Age" type="number"></v-text-field>
        </v-flex>
        <v-flex xs12 sm6 md6>
          <v-text-field v-model.number="search_params.maxAge" label="Max Age" type="number"></v-text-field>
        </v-flex>
        <v-flex xs12 sm12 md12>
          <v-select
            v-model="search_params.gender"
            :items="['Male', 'Female']"
            chips
            attach
            label="Gender"
          ></v-select>
        </v-flex>
        <v-flex xs12 sm12 md12>
          <v-select
            v-model="search_params.nationality"
            :items="nationalities"
            attach
            chips
            multiple
            label="Nationality"
          ></v-select>
        </v-flex>
        <v-flex xs12 sm12 md12>
          <v-select
            v-model="search_params.travellerType"
            :items="travellerTypes"
            attach
            chips
            multiple
            label="Traveller Type"
          ></v-select>
        </v-flex>
        <v-flex xs12 sm12 md12>
          <v-select
            v-model="search_params.orderBy"
            :items="['Nationality', 'Traveller Type']"
            attach
            chips
            label="Order By"
          ></v-select>
        </v-flex>
        <v-flex xs12 sm12 md12>
          <v-btn color="error" v-on:click="searchUsers">Search</v-btn>
          <v-btn color="error" outline v-on:click="resetSearch">Reset</v-btn>
        </v-flex>
      </v-layout>
    </v-form>
</template>

<style>
.side-filter {
  padding: 20px;
}
</style>

<script>
import { store } from "../../store/index";

export default {
  store,
  data() {
    return {
      ageRules: [
        v => v >= 0 || v <= 150 || "Age must be positive and less than 150"
      ],
      search_params: {
        firstName: "",
        lastName: "",
        gender: "",
        minAge: "",
        maxAge: "",
        nationality: [],
        travellerType: [],
        orderBy: ""
      }
    };
  },
  methods: {
    /**
     * Gets all users that match the search params located in data.
     */
    async searchUsers() {
      await store.dispatch("getUsers", this.search_params);
    },
    /**
     * Resets all search_params to empty strings.
     * Calls searchUsers function to reget all users with no search parameters.
     */
    resetSearch: function() {
      this.search_params.firstName = "";
      this.search_params.lastName = "";
      this.search_params.gender = "";
      this.search_params.minAge = "";
      this.search_params.maxAge = "";
      this.search_params.nationality = [];
      this.search_params.travellerType = [];
      this.search_params.orderBy = "";
      this.searchUsers();
    }
  },
  computed: {
    /**
     * Gets a list of nationalities from the store
     * @return list of nationalities
     */
    nationalities() {
      return store.state.users.nationalities;
    },
    /**
     * Gets a list of traveller types from the store
     * @return list of traveller types
     */
    travellerTypes() {
      return store.state.users.travellerTypes;
    }
  },

  /**
   * Gets all avaliable traveller types and nationalities on compoment creation.
   */
  created: async function() {
    await store.dispatch("getAllTravellerTypes");
    await store.dispatch("getAllNationalities");
  }
};
</script>