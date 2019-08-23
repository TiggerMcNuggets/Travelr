<template>
    <v-layout column fill-height>
      <TripNavbar />
      <SelectedTrip />
    </v-layout>     
</template>

<style>

</style>

<script>
  
  import TripNavbar from "../components/trip/TripNavbar";
  import SelectedTrip from "../components/trip/SelectedTrip";

  export default {
    components: {     
      TripNavbar,
      SelectedTrip
    },
    // mixins: [RollbackMixin, StoreTripsMixin],
    data() {
      return {    
        userId: this.$route.params.id,      
      };
    },  
    methods: {   
      setTrip(tripId) {
            this.$store.dispatch("getTrip", {
            userId: this.$route.params.id,
            tripId: tripId
          })    
        }     
    },
    beforeRouteUpdate(to, from, next) {
      this.setTrip(to.params.trip_id);
      next();
    }
  };
</script>
