<template>
    <v-flex grow class="trips-selected">
        <v-layout row wrap px-3 py-2>
            <v-flex xs12>
                <h2>My Subtrip</h2>
            </v-flex>
            <v-layout>
                <v-flex xs4>Test</v-flex>
                <v-flex xs4>Test</v-flex>
                <v-flex xs4>Test</v-flex>
            </v-layout>
        </v-layout>        
    </v-flex>
</template>

<style>
    .trips-selected {
        background-color: white;
        border: 1px solid blue;
    }
</style>

<script>
 
  export default {
    components: {    

    },   
    data() {
      return {    
        tripId: this.$route.params.trip_id,
        userId: this.$route.params.id,   
      };
    },  
    computed: {
        nodes() {
            return this.$store.getters.getSelectedTrip.nodes
        }       
    },
    methods: {
        fetchTrip() {
            this.$store.dispatch("getTrip", {
            userId: this.userId,
            tripId: this.tripId
            })   
        }      
    },
    created: function () {
         this.fetchTrip();
    }
  };
</script>