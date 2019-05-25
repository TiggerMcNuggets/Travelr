<template>
  <v-card height="830px" flat>
    <v-layout row wrap pl-2>       
      <v-flex xs12 pb-2 >
          <v-layout>
            <h3>Create a Destination </h3>
          </v-layout>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="destination.name"
              :counter="60"
              label="Destination Name"
              required
              :rules="nameRules"
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
          <v-text-field
              v-model="destination.type"
              :counter="60"
              :rules="nameRules"
              label="Destination Type"
              required
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-select
                    label="Associated Traveller Types"
                    :items="typeList"
                    item-text="name"
                    item-value="id"
                    v-model="travellerTypes"
                    attach multiple>
            </v-select>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="destination.district"
              :counter="60"
              :rules="nameRules"
              label="Destination District"
              required
            ></v-text-field>
      </v-flex>
      <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model="destination.country"
              :counter="60"
              label="Country"
              required
              :rules="nameRules"
        ></v-text-field>
      </v-flex>
       <v-flex xs12 pb-1 px-3>  
          <v-text-field
            v-model.number="destination.latitude"
            type="number"
            :rules="numberRules"
            label="Latitude"
            required
          ></v-text-field>

      </v-flex>
       <v-flex xs12 pb-1 px-3>
        <v-text-field
              v-model.number="destination.longitude"
              type="number"
              :rules="numberRules"
              label="Longitude"
              required
            ></v-text-field>
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
      ...rules,
      typeList: [],
    };
  },
  props: {
    destination: Object,
    newDestination: Boolean,
    passBackDestination: Function
  },

  watch: {      
  },

  computed: {
    createMode() {
      if(destination.data && !destination.data.id) {
        return true;
      } else {
        return false
      }
    }    
  },

  methods: {
   
  }
};
</script>

