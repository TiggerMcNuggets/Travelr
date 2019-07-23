<template>
  <v-flex pa-3>
    <p v-if="editRequests.length === 0">No Requests available</p>
    <li v-for="request in editRequests" :key="request.id" style="padding: 20px">
      <v-card>
        <v-layout row wrap s>
          <v-flex xs12 sm12 md12 style="text-align: center;">
            <h3>{{ request.destination.name }}</h3>
            <v-divider />
          </v-flex>
          <v-flex xs12 sm12 md2 style="text-align: center; padding-left: 20px">
            <v-btn v-on:click="$router.push('/user/'+request.user.id)">View User</v-btn>
          </v-flex>
          <v-flex xs12 sm12 md2 style="text-align: center; padding-left: 20px">
            <v-btn
              v-on:click="$router.push('/user/' + userId + '/destinations/' + request.destination.id)"
            >View Destination</v-btn>
          </v-flex>
          <v-flex xs12 sm12 md3 offset-lg1>
            <v-select
              :items="getTravellerTypes(request.destination.travellerTypes)"
              label="Current Traveller Types"
              attach
              solo
            ></v-select>
          </v-flex>
          <v-flex xs12 sm12 md3>
            <v-select
              :items="getTravellerTypes(request.travellerTypes)"
              label="Sugguested Traveller Types"
              attach
              solo
            ></v-select>
          </v-flex>
          <v-flex xs12 sm12 md3 offset-lg3 style="text-align: center;">
            <v-btn color="warning" v-on:click="denyRequest(request.id)">Deny</v-btn>
          </v-flex>
          <v-flex xs12 sm12 md3 style="text-align: center;">
            <v-btn color="info" v-on:click="acceptRequest(request.id)">Accept</v-btn>
          </v-flex>
        </v-layout>
      </v-card>
    </li>
  </v-flex>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
import { store } from "../../store/index";
let destinationRepository = RepositoryFactory.get("destination");
export default {
  store,

  data() {
    return {
      userId: null,
      editRequests: []
    };
  },

  /**
   * Initialises the component on creation and sets the user id of the authenticated user.
   */
  created: function() {
    this.userId = store.getters.getUser.id;
    // Gets the edit requests
    this.init();
  },

  methods: {
    /**
     * Initialises the component by getting all edit requests and saving this to a component variable.
     */
    init() {
      // Gets the edit requests
      destinationRepository
        .getEditRequests()
        .then(response => {
          this.editRequests = response.data.requests;
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Sends a request to the server to deny an edit request.
     */
    denyRequest(requestId) {
      destinationRepository
        .denyEditRequest(requestId)
        .then(() => {
          this.init();
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Sends a request to the server to accept an edit request.
     */
    acceptRequest(requestId) {
      destinationRepository
        .acceptEditRequest(requestId)
        .then(() => {
          this.init();
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Creates a list of all the names of the traveller types.
     */
    getTravellerTypes(travellerTypes) {
      if (travellerTypes !== null) {
        let travellerTypeList = [];
        for (let i = 0; i < travellerTypes.length; i++) {
          travellerTypeList.push(travellerTypes[i].name);
        }
        return travellerTypeList;
      } else {
        return [];
      }
    }
  }
};
</script>
