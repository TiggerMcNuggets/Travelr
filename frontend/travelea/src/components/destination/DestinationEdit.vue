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
              :rules="nameRules"
              :counter="60"
              label="Country"
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
              v-model="destination.travellerTypes"
              attach multiple>
            </v-select>
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
let destinationRepository = RepositoryFactory.get("destination");
import { rules } from "../form_rules";
import RollbackMixin from "../mixins/RollbackMixin.vue";
import UndoRedoButtons from "../common/rollback/UndoRedoButtons.vue";
import SelectDataRepository from "../../repository/SelectDataRepository";

export default {
  mixins: [RollbackMixin],
  components: {
    UndoRedoButtons: UndoRedoButtons
  },

  props: {
    editDestinationCallback: Function,
    prefillData: Object
  },

  data() {
    return {
      destination: {},
      isError: false,
      typeList: [],
      ...rules
    };
  },

  watch: {
    /*
     * Watches the prefillData object which contains data about the destination being edited.
     */
    prefillData: function(newPrefillData) {
      this.destination = newPrefillData;
    }
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
     * Requests a destination and sets destination property to it
     */
    setDestination: function() {
      destinationRepository
        .getDestination(this.$route.params.id, this.$route.params.dest_id)
        .then(result => {
          this.destination = result.data;

          // This is set to later be pushed as a reaction to the rollback stack
          this.rollbackSetPreviousBody(result.data);
        });
    },

     /**
     * Updates a destination by through a request to the API based on the updated data in the form.
     * This function will first check if the data is valid and only submit successfully if it is.
     */
    updateDestination: function() {
      if (this.$refs.form.validate()) {
        const userId = this.$route.params.id;
        const destId = this.$route.params.dest_id ? this.$route.params.dest_id : this.destination.id;

        var list = [];

        this.destination.travellerTypes.forEach(dist => {
          list.push(dist.id);
        });

        this.destination.travellerTypes = list;

        // Call the update request
        destinationRepository
          .updateDestination(userId, destId, this.destination)
          .then(() => {
            this.$refs.form.reset();
            this.isError = false;
            this.$emit('close-map-info-window');
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
    this.populateSelects();
    // If the destination edit window has been opened on the Map
    if (this.prefillData) {
      this.destination = this.prefillData;
    }
  }
};
</script>
