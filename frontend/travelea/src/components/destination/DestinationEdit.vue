<template>
  <v-layout white justify-space-around>
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>Edit destination</h4>
        <v-layout justify-space-around>
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
        <v-layout justify-space-around>
          <v-btn v-on:click="updateDestination">UPDATE</v-btn>
        </v-layout>
        <v-alert :value="isError" type="error">
          This destination is already available to you
        </v-alert>
      </v-container>
    </v-form>
  </v-layout>
</template>

<style>
  @import url("https://fonts.googleapis.com/css?family=Karla:400,700");

  .outer-container {
    text-align: center;
  }

  .buttons-div {
    margin-top: 2em;
  }

  .container {
    align-self: center;
    display: inline-block;
    text-align: left;
  }
</style>

<script>
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  import { rules } from "../form_rules";
  let destinationRepository = RepositoryFactory.get("destination");

  export default {
    props: {
      editDestinationCallback: Function,
      prefillData: Object
    },

    data() {
      return {
        destination: {},
        isError: false,
        ...rules
      };
    },

    watch: {
      /*
       * Watches the prefillData object from destination edit which contains data about the destination being edited.
       */
      prefillData: function(newPrefillData) {
        this.destination = newPrefillData;
      }
    },

    methods: {
       /**
       * Updates a destination by through a request to the API based on the updated data in the form.
       * This function will first check if the data is valid and only submit successfully if it is.
       */
      updateDestination: function() {
        if (this.$refs.form.validate()) {
          const userId = this.$route.params.id;
          const destId = this.destination.id;

          // Call the update request
          destinationRepository
            .updateDestination(userId, destId, this.destination)
            .then(() => {
              this.$refs.form.reset();
              this.isError = false;
              this.editDestinationCallback();
            })
            .catch((err) => {
              console.log(err);
              this.isError = true;
            });
        }
      }
    },
    created() {
      // If the destination edit window has been opened on the Map
      if (this.prefillData) {
        this.destination = this.prefillData;
      }
    }
  };
</script>
