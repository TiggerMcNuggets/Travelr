<template>
    <v-layout row wrap>
        <v-flex xs12> Title </v-flex>
        <v-flex xs12> 
            <v-layout row wrap> 
                Hi
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
    destination: Object,
    newDestination: Boolean,
    passBackDestination: Function
  },

  watch: {      
  },

  computed: {
    
  },

  methods: {
   
  }
};
</script>

