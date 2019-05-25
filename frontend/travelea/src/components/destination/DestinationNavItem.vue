<template>
  <v-flex xs12 >
  <v-card flat>
    <v-layout row wrap pt-1 pb-1 pl-3 v-bind:class="{showing: destination.isShowing}">
      <v-flex xs12>
        {{ destination.data.name }}
      </v-flex>
      <v-flex xs12>
        {{ destination.data.district}}, {{ destination.data.country }}
      </v-flex>
    </v-layout>
    
  </v-card>
  </v-flex>
</template>

<style>
.showing { 
  background-color: #d7dae0
}
</style>

<script>


export default {  
  data() {
    return {      
    };
  },
  props: {
    destination: Object
  },
  methods: {
   
  }
};
</script>

