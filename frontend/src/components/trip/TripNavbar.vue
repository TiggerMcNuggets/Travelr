<template>
    <v-flex xs1 class="trips-nav-bar">
        <v-layout row px-3 py-2 align-center fill-height justify-start>
            <v-layout fill-height align-center>      
                <div class="display-1">
                    {{ title }}
                </div>
                <v-breadcrumbs :items="navigation">
                    <template v-slot:item="props">
                        <v-breadcrumbs-item v-on:click="setTrip(props.item.id)">
                            {{ props.item.name }}
                        </v-breadcrumbs-item>
                    </template>
                </v-breadcrumbs>    
            </v-layout>            
            <v-spacer></v-spacer>
            <v-btn>Manage Groups</v-btn>
        </v-layout>
    </v-flex>
</template>

<style>
    .trips-nav-bar {        
        background-color: white;
        border: 1px solid red;
    }
</style>

<script>
 
  export default {
    components: {    

    },   
    data() {
      return {   
        userId: this.$route.params.id,      
      };
    },  
    computed: {   
        title() {
            return this.$store.getters.getRootTrip.name
        },
        navigation() {
            return this.$store.getters.getNavigation
        }        
    },
    methods: {    
        setTrip(tripId) {
            console.log(tripId)
            this.$router.replace({name:"view-trip", params: {id: this.userId, trip_id: tripId}});
                    }    
        
    },
    created: function () {      
    }
  };
</script>
