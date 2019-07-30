<template>
  <v-card height="830px" flat>
    <v-layout row wrap pl-2 v-if="createMode || editMode">
      <v-form ref="form" lazy-validation>
        <v-flex xs12 mt-3 pb-2 px-3>
          <p v-if="createMode" class="font-weight-bold dest-heading">Create Destination</p>
          <p v-if="editMode && !createMode" class="font-weight-bold dest-heading">Edit Destination</p>
        </v-flex>
        <v-flex xs12 pb-1 px-3>
          <v-text-field
            v-model="destination.name"
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
            v-model="destination.type"
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
            v-model="destination.travellerTypes"
            return-object
            name="travellerTypeField"
            @change="pushDestination"
            attach
            multiple
          ></v-select>
        </v-flex>
        <v-flex xs12 pb-1 px-3>
          <v-text-field
            v-model="destination.district"
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
            v-model="destination.country"
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
            v-model.number="destination.latitude"
            type="number"
            :rules="numberRules"
            label="Latitude"
            @change="pushDestination"
            required
          ></v-text-field>
        </v-flex>
        <v-flex xs12 pb-1 px-3>
          <v-text-field
            v-model.number="destination.longitude"
            @change="pushDestination"
            type="number"
            :rules="numberRules"
            label="Longitude"
            required
          ></v-text-field>
        </v-flex>
        <v-flex xs12>
          <v-layout justify-start>
            <v-btn @click="goToViewDestination" v-if="editMode">Cancel</v-btn>
            <v-btn color="primary" v-if="editMode" @click="updateDestination">Update</v-btn>
            <v-btn color="primary" v-else @click="updateDestination">Create</v-btn>
          </v-layout>
        </v-flex>
      </v-form>
    </v-layout>
    <v-layout row wrap px-2 v-if="!createMode && !editMode">
      <v-flex pb-2 px-2>
        <v-layout row justify-space-between align-start>
          <!-- <h3>{{ focussedDestination.name }}</h3> -->
          <div>
            <h4 class="headline font-weight-normal">{{ focussedDestination.name }}</h4>
            <p
              class="body-2 font-weight-light no-margin"
            >{{ focussedDestination.district }}, {{ focussedDestination.country }}</p>
            <p
              class="body-3 font-weight-light no-margin"
            >{{ focussedDestination.latitude }}, {{ focussedDestination.longitude }}</p>
          </div>
          <v-btn fab flat small color="primary" @click="editMode = true" v-if="allowedToEdit">
            <v-icon>edit</v-icon>
          </v-btn>
        </v-layout>
      </v-flex>
      <v-flex xs12>
        <!-- <v-img src="https://picsum.photos/510/300?random" aspect-ratio="1.7"></v-img> -->
        <v-img
          src="https://perkinsdesigns.files.wordpress.com/2015/11/jan15_mountains_galore3.png"
          aspect-ratio="1.7"
        ></v-img>
      </v-flex>
      <!-- <img  src="https://perkinsdesigns.files.wordpress.com/2015/11/jan15_mountains_galore3.png" /> -->

      <v-flex xs12 mt-3 px-2>
        <p class="caption font-weight-light no-margin">TYPE</p>
        <v-divider class="no-margin"></v-divider>
        <p class="body-1 font-weight-normal no-margin">{{ focussedDestination.type }}</p>
      </v-flex>
      <v-flex xs12 mt-3 mb-2 px-2>
        <p class="caption font-weight-light no-margin">ASSOCIATED TRAVELLER TYPES</p>
        <v-divider class="no-margin"></v-divider>
      </v-flex>

      <v-flex xs12 pb-1 px-2>
        <v-layout row wrap>
          <v-chip
            :key="index"
            v-for="(travellerType, index) in focussedDestination.travellerTypes"
          >{{ travellerType.name }}</v-chip>
        </v-layout>
      </v-flex>
      <v-flex>
        <v-layout justify-start mt-2 px-2>
          <v-btn color="primary" outline @click="gotoDest">View Destination</v-btn>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<style>
.dest-heading {
  font-size: 20px;
}

.no-margin {
  margin: 0px;
}
</style>


<script>
import { rules } from "../form_rules";
import SelectDataRepository from "../../repository/SelectDataRepository";

export default {
  data() {
    return {
      editMode: false,
      destination: {
        data: {}
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
        if (this.focussedDestination && !this.focussedDestination.id) {
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
          if(oldValue.id !== newValue.id) {
            this.goToViewDestination();
          }
          if (newValue !== this.destination) {
            this.destination = this.focussedDestination;
          }
        },
        deep: true
      }
    },

  methods: {
    /**
     * Turns off edit mode.
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

    /**
     * Updates the destination and returns to viewing mode.
     */
    updateDestination() {
      if (this.$refs.form.validate()) {
        this.passBackDestination(this.destination);
        this.goToViewDestination();
      }
    },

    /**
     * Redirects user to the destination details page.
     */
    gotoDest() {
      let userId = this.$store.getters.getUser.id;
      this.$router.push(
        `/user/${userId}/destinations/${this.focussedDestination.id}`
      );
    },

      /**
       * populated list of traveller types for user to select from
       **/
      async populateSelects() {
        const travellerTypes = await SelectDataRepository.travellerTypes();
        this.typeList = travellerTypes.data;

      },
  },

  /**
   * Populates the select elements in the form and sets the selected destination element once component has mounted.
   */
  mounted() {
    this.populateSelects();
    this.destination = this.focussedDestination;
  }
};
</script>

