<template>
  <v-layout>
    <v-flex xs12>
      <v-layout wrap class="outer-container" justify-center fill-height>
        <v-flex xs12 class="secondary-nav-container">
          <v-layout justify-center>
            <v-flex md8 sm12 class="secondary-nav-bar" >
              Secondary Navbar
            </v-flex>   
          </v-layout>       
        </v-flex>
        <v-flex md8 sm12 class="inner-container" pt-3>
          <v-layout row wrap justify-center align-center>
            <v-flex v-for="index in 10" :key="index" pb-2 px-1>
              <v-card width=200px>
                <v-img                               
                  src="https://www.canterbury.ac.nz/homepage-events-carousel/matari-building-home-events.jpg">
                </v-img>
                <v-card-text pa-0>
                UC | Christchurch | New Zealand
                </v-card-text>
              </v-card>
            </v-flex>
          </v-layout>         
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<style>


.outer-container {
  /* border: 1px solid pink; */
}

.inner-container {
  /* border: 1px solid blue; */
}

.secondary-nav-bar {
    /* border: 2px solid orange; */
    background-color: white;
    height: 40px;
}

.secondary-nav-container {
  background-color: white;
}

.main-content {
    /* border: 2px solid green; */
}



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
      destinations: [
      ]     
    };
  }, 
   // child components
//   components: {
//     DestinationCreate: DestinationCreate
//   },
  methods: {
    
  },
  computed: {
    containerWidth () {
       switch (this.$vuetify.breakpoint.name) {
        case 'xs': return '220px'
        case 'sm': return '400px'
        case 'md': return '960px'
        case 'lg': return '1200px'
        case 'xl': return '1200px'
      }
    }
  },
  created: function() {    
  }  
};
</script>
