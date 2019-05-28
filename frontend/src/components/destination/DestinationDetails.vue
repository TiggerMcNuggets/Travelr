<template>
  <v-card height="830px" flat>
    <v-layout row wrap pl-2 v-if="createMode || editMode">
      <v-form ref="form" lazy-validation>
        <v-flex xs12 pb-2>
          <v-layout>
            <h3 v-if="createMode">Create a Destination</h3>
            <h3 v-if="editMode && !createMode">Edit Destination</h3>
          </v-layout>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="destination.data.name"
              :counter="60"
              label="Destination Name"
              required
              :rules="nameRules"
              name="nameField"
              @change="pushDestination"
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
          <v-text-field
              v-model="destination.data.type"
              :counter="60"
              :rules="nameRules"
              label="Destination Type"
              required
              name="typeField"
              @change="pushDestination"
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-select
              label="Associated Traveller Types"
              :items="typeList"
              item-text="name"
              v-model="destination.data.travellerTypes"
              return-object
              name="travellerTypeField"
              @change="pushDestination"
              attach multiple>
            </v-select>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="destination.data.district"
              :counter="60"
              :rules="nameRules"
              label="District"
              required
              name="districtField"
              @change="pushDestination"
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="destination.data.country"
              :counter="60"
              label="Country"
              required
              :rules="nameRules"
              name="countryField"
              @change="pushDestination"
        ></v-text-field>
      </v-flex>
       <v-flex xs12 pb-1 px-3>  
          <v-text-field
            v-model.number="destination.data.latitude"
            type="number"
            :rules="numberRules"
            label="Latitude"
            @change="pushDestination"
            required
          ></v-text-field>

      </v-flex>
       <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model.number="destination.data.longitude"
              @change="pushDestination"
              type="number"
              :rules="numberRules"
              label="Longitude"
              required
            ></v-text-field>
      </v-flex>
      <v-flex xs12>
          <v-layout justify-center>
            <v-btn @click="goToViewDestination">Cancel</v-btn>
            <v-btn color="primary" v-if="editMode" @click="updateDestination">Update</v-btn>
            <v-btn color="primary" v-else @click="updateDestination">Create</v-btn>
          </v-layout>
        </v-flex>
      </v-form>
    </v-layout>
    <v-layout row wrap px-2 v-if="!createMode && !editMode">
      <v-flex xs12 pb-2>
        <v-layout row justify-space-between align-center>
          <h3>{{ focussedDestination.data.name }}</h3>
          <v-btn @click="editMode = true" v-if="allowedToEdit">
            <v-icon>edit</v-icon>
          </v-btn>
        </v-layout>
      </v-flex>
      <v-flex xs12 pb-1 px-3>{{ focussedDestination.data.type }}</v-flex>
      <v-flex xs12 pb-1 px-3>{{ focussedDestination.data.district }}</v-flex>
      <v-flex xs12 pb-1 px-3>{{ focussedDestination.data.country }}</v-flex>
      <v-flex xs12 pb-1 px-3>Long: {{ focussedDestination.data.longitude }}</v-flex>
      <v-flex xs12 pb-1 px-3>Lat: {{ focussedDestination.data.latitude }}</v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-layout row wrap>
          <v-chip
            :key="index"
            v-for="(travellerType, index) in focussedDestination.data.travellerTypes"
          >{{ travellerType.name }}</v-chip>
        </v-layout>
      </v-flex>
      <v-flex xs12>
        <v-layout justify-center>
          <v-btn color="primary" @click="gotoDest">View Destination</v-btn>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<style>
</style>


<script>
import { rules } from "../form_rules";
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  let destinationRepository = RepositoryFactory.get("destination");
  import SelectDataRepository from "../../repository/SelectDataRepository";
  import { stringify } from "../../tools/deepCopy" 

export default {
  data() {
    return {
      editMode: false,
      destination: {
        data: {
        }
      },
      ...rules,
      typeList: [],
      travellerTypes: []
    };
  },
  /**
   * focussedDestination: Clicked on / selected point
   * passBackDestination: What todo when save/create is clicked
   * focusDestination: Send a update focused destination to the DestinationPage
   */
  props: {
    focussedDestination: Object,
    passBackDestination: Function,
    focusDestination: Function,
    allowedToEdit: Boolean
  },
  computed: {

    /**
     * Is it in create mode or viewing/edit mode
     */
    createMode() {
      if (this.focussedDestination.data && !this.focussedDestination.data.id) {
        return true;
      } else {
        return false;
      }
    }
  },
  watch: {
    /**
     * Reset back to viewing mode on destination change
     */
    focussedDestination: {
      handler: function(newValue, oldValue) {
        if(oldValue.data.id !== newValue.data.id) {
          this.goToViewDestination();
        } 
        if (newValue.data !== this.destination.data) {
          this.destination = this.focussedDestination;
        }
      },
      deep: true
    }
  },
  methods: {
    /**
     * 
     */
    goToViewDestination() {
      this.editMode = false;
    },
    /**
     * Updated long and lat on the map marker when fields are changed
     */
    pushDestination() {
      this.focusDestination(this.destination);
    },
    updateDestination(){
      if(this.$refs.form.validate()) {
        this.passBackDestination(this.destination);
        this.goToViewDestination();
      }
    },
    /**
     * populated list of traveller types for user to select from
     **/
    async populateSelects() {
      const travellerTypes = await SelectDataRepository.travellerTypes();
      this.typeList = travellerTypes.data;
    },
    gotoDest() {
      var userId = this.$store.getters.getUser.id;
      this.$router.push(
        `/user/${userId}/destinations/${this.focussedDestination.data.id}`
      );
    }
  },
  mounted() {
    this.populateSelects();
    this.destination = this.focussedDestination;
  },
};
</script>

