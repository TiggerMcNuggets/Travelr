/* eslint-disable */

<template>
  <!-- <div class="width-for-container"> -->
  <v-layout white>
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>Add new destination</h4>
        <v-layout justify-space-around="true">
          <v-flex xs12 md6 class="row-input-margin">
            <v-text-field
              v-model="destination.name"
              :counter="60"
              label="Destination Name"
              required
              :rules="nameRules"
            ></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field
              v-model="destination.type"
              :counter="60"
              :rules="nameRules"
              label="Destination Type"
              required
            ></v-text-field>
          </v-flex>
        </v-layout>

        <v-layout>
          <v-flex xs12 md12>
            <v-select
                    label="Associated Traveller Types"
                    :items="typeList"
                    item-text="name"
                    item-value="id"
                    v-model="travellerTypes"
                    attach multiple>
            </v-select>
          </v-flex>
        </v-layout>

        <v-layout>
          <v-flex xs12 md6>
            <v-text-field
              v-model="destination.district"
              :counter="60"
              :rules="nameRules"
              label="Destination District"
              required
            ></v-text-field>
          </v-flex>
          <v-flex xs12 md6>
            <v-text-field
              v-model="destination.country"
              :counter="60"
              label="Country"
              required
              :rules="nameRules"
            ></v-text-field>
          </v-flex>
        </v-layout>

        <v-layout>
          <v-flex xs12 md6>
            <v-text-field
              v-model.number="destination.latitude"
              type="number"
              :rules="numberRules"
              label="Latitude"
              required
            ></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field
              v-model.number="destination.longitude"
              type="number"
              :rules="numberRules"
              label="Longitude"
              required
            ></v-text-field>
          </v-flex>
        </v-layout>
        <div>
          <v-btn color="red" v-on:click="resetValues">RESET VALUES</v-btn>
          <v-btn v-on:click="createDestination">CREATE DESTINATION</v-btn>
        </div>
      <v-alert
        :value="isError"
        type="error"
        >
        This destination is already available to you
      </v-alert>
      </v-container>
    </v-form>
    <!-- </div> -->
  </v-layout>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.width-for-container {
  width: 60%;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");

import { rules } from "../form_rules";
import { store } from "../../store/index";
import SelectDataRepository from "../../repository/SelectDataRepository";

export default {
  store,

  props: {
    createDestinationCallback: Function
  },

  mounted() {
    this.populateSelects();
  },


  data() {
    return {
      travellerTypes: [],
      destination: {},
      userId: this.$route.params.id,
      isError: false,
      typeList: [],
      ...rules
    };
  },

  methods: {
    /**
     * populated list of traveller types for user to select from
     **/
    async populateSelects() {
      const travellerTypes = await SelectDataRepository.travellerTypes();
      this.typeList = travellerTypes.data;
    },
    /**
     * Sends a request to the API to create a destination based on the data entered into the form.
     * Checks for an error and logs result if unsuccessful.
     */
    createDestination: function() {
      if (this.$refs.form.validate()) {
        this.destination.travellerTypes = this.travellerTypes;
        destinationRepository.createDestination(this.userId, this.destination)
        .then((response) => {
          this.$refs.form.reset();
          this.isError = false;



          this.createDestinationCallback(response.data.id);
        })
        .catch(() => {
          this.isError = true;
          console.log("error creating destination");
        })
      }
    },

    /**
     * Resets the form values to blank.
     */
    resetValues: function() {
      this.$refs.form.reset();
    }
  }
};
</script>
