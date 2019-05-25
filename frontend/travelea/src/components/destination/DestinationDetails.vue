<template>
  <v-card height="830px" flat>
    <v-layout row wrap pl-2 v-if="createMode || editMode">       
      <v-flex xs12 pb-2 >
          <v-layout>
            <h3 v-if="createMode">Create a Destination </h3>
            <h3 v-if="editMode">Edit Destination </h3>
          </v-layout>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="focussedDestination.data.name"
              :counter="60"
              label="Destination Name"
              required
              :rules="nameRules"
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
          <v-text-field
              v-model="focussedDestination.data.type"
              :counter="60"
              :rules="nameRules"
              label="Destination Type"
              required
            ></v-text-field>
      </v-flex>
      <!-- <v-flex xs12 pb-1 px-3>
        <v-select
                    label="Associated Traveller Types"
                    :items="typeList"
                    item-text="name"
                    item-value="id"
                    v-model="travellerTypes"
                    attach multiple>
            </v-select>
      </v-flex> -->
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="focussedDestination.data.district"
              :counter="60"
              :rules="nameRules"
              label="Destination District"
              required
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="focussedDestination.data.country"
              :counter="60"
              label="Country"
              required
              :rules="nameRules"
        ></v-text-field>
      </v-flex>
       <v-flex xs12 pb-1 px-3>  
          <v-text-field
            v-model.number="focussedDestination.data.latitude"
            type="number"
            :rules="numberRules"
            label="Latitude"
            @change="pushLongAndLat"
            required
          ></v-text-field>

      </v-flex>
       <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model.number="focussedDestination.data.longitude"
              @change="pushLongAndLat"
              type="number"
              :rules="numberRules"
              label="Longitude"
              required
            ></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout px-2 v-if="!createMode && !editMode">        
        <v-flex xs12 pb-2 >
          <v-layout row justify-space-between>
            <h3> {{ focussedDestination.data.name }} </h3>
            <v-btn @click="editMode = true">Edit</v-btn>
          </v-layout>
      </v-flex>
    </v-layout>
  </v-card>   
</template>

<style>
</style>


<script>

import { rules } from "../form_rules";

export default {  
  data() {
    return {
      editMode: false,
      ...rules,
      typeList: []      
    };
  },
  props: {
    focussedDestination: Object,
    passBackDestination: Function,
    focusDestination: Function
  },
  computed: {
    /**
     * Is it in create mode or viewing/edit mode
     */
    createMode() {
      if(this.focussedDestination.data && !this.focussedDestination.data.id) {
        return true;
      } else {
        return false
      }
    },  
  },
  watch: {
    /**
     * Reset back to viewing mode on destination change
     */
    focussedDestination: {
      handler: function(newValue, oldValue) {
        if(oldValue.data.id !== newValue.data.id) {
          this.editMode = false;
        }
      }, 
      deep: true
    }
  },
  methods: {
    /**
     * Updated long and lat on the map marker when fields are changed
     */
   pushLongAndLat() {
     this.focusDestination(this.focussedDestination);
   }

  }
};
</script>

