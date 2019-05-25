<template>
    <v-layout row wrap>
        <v-flex xs12> Title </v-flex>
        <v-flex xs12> 
            <v-layout row wrap> 
                <v-flex xs12>
                  <v-card v-bind:key="index" v-for="(destination, index) in destinations">
                    {{ destination.data.name }}
                  </v-card>
                </v-flex>
                
            </v-layout>
        </v-flex>
    </v-layout>
</template>

<style>
.destination-side-nav {
  width: 200px;
}
</style>


<script>


export default {  
  data() {
    return {
      
    };
  },
  props: {
    destinations: Array,
    focusDestination: Function,
    toggleDestination: Function
  },
  


  watch: {      
  },

  computed: {
    
  },

  methods: {
   
  }
};
</script>

