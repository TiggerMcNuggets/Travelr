<template>
  <v-card height="830px" flat>
    <v-layout row wrap>       
      <v-flex xs12>
        <v-card> 
          <v-layout row wrap> 
              <v-flex xs12>
                <v-tabs v-model="tab">
                  <v-tab :key="1">
                    Private
                  </v-tab>
                  <v-tab :key="2">
                    Public
                  </v-tab>                             
                  <v-tab-item :key="1">
                    <DestinationNavItem class="destination-nav-item"
                                        v-bind:key="index" 
                                        v-for="(destination, index) in privateDestinations" 
                                        :destination="destination"
                                        @click.native="toggleDestination(destination)"
                                        @dblclick="focusDestination(destination)"/>
                  </v-tab-item>
                  <v-tab-item :key="2">
                    <DestinationNavItem 
                                        v-bind:key="index"                                           
                                        v-for="(destination, index) in publicDestinations"
                                        v-bind:class="{showing: destination.isShowing}" 
                                        :destination="destination"
                                        @click.native="toggleDestination(destination)"
                                        @dblclick="focusDestination(destination)"/>
                  </v-tab-item>
                </v-tabs>
              </v-flex>                
            </v-layout>
          </v-card>
        </v-flex>
    </v-layout>
  </v-card>   
</template>

<style>
.destination-side-nav {
  width: 200px;
}

.destination-nav-item {
  cursor: pointer;
}

.showing {
  /* background-color: red; */
}
</style>


<script>

import DestinationNavItem from './DestinationNavItem'

export default {  
  components: {
    DestinationNavItem
  },
  data() {
    return {
      tab: 1
    };
  },
  methods: {
   printHi() {
     console.log("Hi");
   }
  },
  props: {
    destinations: Array,
    focusDestination: Function,
    toggleDestination: Function
  },
  watch: {      
  },

  computed: {
    privateDestinations() {
      return this.destinations.filter(x => !x.data.isPublic);
    },
    publicDestinations() {
      return this.destinations.filter(x => x.data.isPublic);
    },
    privateDestinationCount() {
      this.privateDestinations.length;
    },
    publicDestinationCount() {
      this.publicDestinations.length;
    }
  },

  
};
</script>

