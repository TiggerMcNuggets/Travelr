<template>
<v-layout
    <v-flex xs10>
    <v-layout column align-center justify-start="">
        <v-flex blue>
            test
        </v-flex>
        <v-flex blue>
            test
        </v-flex>

    </v-layout>
</template>

<style>

</style>


<script>
import { store } from "../../store/index";
// import {RepositoryFactory} from "../../repository/RepositoryFactory";
// let destinationRepository = RepositoryFactory.get("destination");
// import DestinationCreate from "./DestinationCreate"

export default {
  store,
  // local variables
  data() {
    return {      
    };
  }, 
   // child components
//   components: {
//     DestinationCreate: DestinationCreate
//   },
  methods: {
    
  },
  created: function() {    
  }  
};
</script>
