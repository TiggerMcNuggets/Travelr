<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-dialog v-model="showSuggestTravellerTypes" max-width="900px">
    <v-card min-height="400px" style="padding: 20px">
      <v-layout row wrap style="padding-left: 40px">
        <v-flex xs12 md12>
          <h2>Traveller Types</h2>
        </v-flex>
        <v-flex xs12 md11>
          <v-select
            label="Associated Traveller Types"
            :items="typeList"
            item-text="name"
            v-model="selectedTravellerTypes"
            attach
            multiple
            return-object
          ></v-select>
        </v-flex>
        <v-flex xs12 md11 style="text-align: center">
          <v-btn v-on:click="submitTravellerTypes()">Submit</v-btn>
        </v-flex>
      </v-layout>
    </v-card>
  </v-dialog>
</template>


<script>
import { RepositoryFactory } from "../../../repository/RepositoryFactory";
let selectDataRepository = RepositoryFactory.get("selectData");
let destinationRepository = RepositoryFactory.get("destination");
import { deepCopy } from "../../../tools/deepCopy";

export default {
  data() {
    return {
      typeList: [],
      selectedTravellerTypes: []
    };
  },
  props: {
    destinationId: {
      type: String,
      required: true
    },
    userTravellerTypes: {
      type: Array,
      required: true
    },
    showSuggestTravellerTypes: {
      type: Boolean,
      required: true
    },
    close: {
      type: Function,
      required: true
    }
  },
  methods: {
    /**
     * populated list of traveller types for user to select from
     **/
    async populateSelects() {
      const travellerTypes = await selectDataRepository.travellerTypes();
      this.typeList = travellerTypes.data;
    },
    /**
     * adds a request to the destination to add/remove traveller types
     **/
    submitTravellerTypes() {
      let destRequest = {
        destinationId: this.destinationId,
        travellerTypeIds: this.selectedTravellerTypes
      };
      destinationRepository
        .addDestinationEditRequest(destRequest)
        .then(() => {
          this.close();
        })
        .catch(err => {
          console.log(err);
        });
    }
  },

  /**
   * Populates the selection form items once component has mounted.
   */
  mounted() {
    this.populateSelects();
    this.selectedTravellerTypes = deepCopy(this.userTravellerTypes);
  }
};
</script>