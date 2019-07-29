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
              v-model="destination.travellerTypes"
              attach
              multiple
              return-object
            ></v-select>
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
        <v-alert :value="isError" type="error">This destination is already available to you</v-alert>
      </v-container>
    </v-form>
  </v-layout>
</template>

<script>
  import { rules } from "../form_rules";
  import SelectDataRepository from "../../repository/SelectDataRepository";
  import StoreDestinationsMixin from "../mixins/StoreDestinationsMixin";

  export default {
    mixins: [StoreDestinationsMixin],
    props: {
      createDestinationCallback: Function,
      prefillData: Object
    },

    data() {
      return {
        destination: {},
        userId: this.$route.params.id,
        isError: false,
        typeList: [],
        ...rules
      };
    },

  /**
   * Populates the select form elements when the component mounts.
   */
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
          this._postDestination(this.userId, this.destination)
                  .then(res => {
                    this.$refs.form.reset();
                    this.isError = false;
                    this.$emit('close-map-info-window');
                    this.createDestinationCallback(res.data.id);
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
    resetValues: function() {
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
