<template>
  <v-layout white justify-space-around>
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>Create new destination</h4>
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
        <v-layout justify-space-around>
            <v-btn color="red" v-on:click="resetValues">RESET VALUES</v-btn>
            <v-btn v-on:click="createDestination">CREATE DESTINATION</v-btn>
        </v-layout>
      <v-alert
        :value="isError"
        type="error"
        >
        This destination is already available to you
      </v-alert>
      </v-container>
    </v-form>
  </v-layout>
</template>

<script>
  import { rules } from "../form_rules";
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  let destinationRepository = RepositoryFactory.get("destination");
  import SelectDataRepository from "../../repository/SelectDataRepository";

  export default {
    props: {
      createDestinationCallback: Function,
      prefillData: Object
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

    mounted() {
      this.populateSelects();
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
      createDestination: function () {
        if (this.$refs.form.validate()) {
          destinationRepository.createDestination(this.userId, this.destination)
                  .then(() => {
                    this.$refs.form.reset();
                    this.isError = false;
                    this.createDestinationCallback();
                  })
                  .catch((err) => {
                    console.log(err);
                    this.isError = true;
                    console.log("error creating destination");
                  })
        }
      },

      /**
       * Resets the form values to blank.
       */
      resetValues: function () {
        this.$refs.form.reset();
      }
    },

    created() {
      // If the destination create window has been opened on the Map and already has latitude and longitude.
      if (this.prefillData) {
        this.destination = this.prefillData;
      }
    }
  };
</script>
