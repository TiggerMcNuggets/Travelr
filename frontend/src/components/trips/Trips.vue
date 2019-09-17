<template>
  <v-container fluid>
    <PageHeader title="Trips" :undo="undo" :redo="redo" :canRedo="rollbackCanRedo" :canUndo="rollbackCanUndo"
                :options="options"/>

    <v-alert :value="undoRedoError" type="error">Cannot undo or redo</v-alert>
    <v-text-field v-if="searchActive" v-model="searchValue" label="Trip name" prepend-icon="search"></v-text-field>

    <div v-if="showCreateTrip">
      <create-trip
        v-if="showCreateTrip"
        :toggleShowCreateTrip="toggleShowCreateTrip"
        :passedTrip="null"
        :updateViewTripPage="() => getUserTrips"
        :checkpointCreateTrip="checkpointCreateTrip"
      />
    </div>

    <v-layout row wrap>
      <v-flex
        class="trips-list-element"
        v-for="(item, index) in tripsFiltered" :key="index" xs12 sm6 md6 lg4 xl3>

        <v-hover v-slot:default="{ hover }">
          <v-card
            v-on:click="openTrip(item.id)"
            :elevation="hover ? 12 : 2"
          >
            <v-img
              class="white--text"
              height="200px"
              :src="fillerImageURL"
            >
              <v-card-title
                class="align-end fill-height title error--text"
                color="red"
              >{{ item.name }}
              </v-card-title>
            </v-img>
            <!--<v-card-text>I'm card text</v-card-text>-->
            <v-card-actions
              class="align-end justify-end">
              <v-btn
                v-if="(isMyProfile || isAdminUser) && !item.isPublic"
                class="align-end justify-end"
                icon
                @click="deleteTrip(item.id)"
              >
                <v-icon color="red lighten-1">delete</v-icon>
              </v-btn>
    <emoji-picker v-model="text"/>
    {{text}}

            </v-card-actions>
          </v-card>
        </v-hover>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
  @import url("https://fonts.googleapis.com/css?family=Karla:400,700");

  .crud-options {
    display: flex;
    justify-content: flex-end;
  }

  .horizontal-details li {
    display: inline-block;
    padding-right: 20px;
    color: white;
  }

  .top-destination-content {
    display: flex;
    justify-content: space-between;
  }

  .top-destination-content span {
    padding: 10px 20px;
  }

  .top-destination-content a {
    font-family: "Karla", sans-serif;
    font-size: 15px;
    margin-left: 10px;
  }

  ul {
    list-style: none;
  }

  .trips-list-element {
    padding: 10px;
  }

</style>


<script>
  import {store} from "../../store/index";
  import CreateTrips from "./CreateTrips.vue";
  import RollbackMixin from "../mixins/RollbackMixin.vue";
  import StoreTripsMixin from "../mixins/StoreTripsMixin.vue";
  import PageHeader from "../common/header/PageHeader";
import EmojiPicker from "../comment/emoji/EmojiPicker";


  export default {
    store,

    mixins: [
      RollbackMixin, StoreTripsMixin
    ],


    // local variables
    data() {
      return {
        showCreateTrip: false,
        searchValue: "",
        isAdmin: store.getters.getIsUserAdmin,
        isMyProfile: false,
        isAdminUser: false,
        userId: this.$route.params.id,
        searchActive: false,
        undoRedoError: false,
        fillerImageURL: "https://cdn0.iconfinder.com/data/icons/vacation-line/100/vacation_luggage-512.png"

      };
    },
    // the place where you want to make the store values readable
    computed: {
      options() {
        return [
          {action: this.toggleShowCreateTrip, icon: "add", title: "Create New Trip"},
          {action: this.toggleShowSearch, icon: "search", title: "Search Trips"}
        ];
      },

      /**
       * Filters the list of trips according to the search value
       */
      tripsFiltered() {
        const filteredList = this.trips.filter(
          trip =>
            trip.name.toLowerCase().search(this.searchValue.toLowerCase()) !== -1
        );
        //Currently sorting trips by id, in future we will sort trips by creation time
        return filteredList.sort(function (a, b) {
          return a.id - b.id;
        });
      }
    },

    // child components
    components: {
      CreateTrip: CreateTrips,
      PageHeader
    },

    methods: {
      /**
       * Sets all alert error visible fields to invisible
       */
      clearAlerts() {
        this.undoRedoError = false;
      },

      /**
       * Toggles searchActive from true to false or false to true
       */
      toggleShowSearch: function () {
        this.searchActive = !this.searchActive;
      },

      /**
       * If the user is either admin or owns the profile
       * redirects the current page to the trip page
       * @param id the trip id
       */
      openTrip: function (id) {
        let route = `/user/${this.userId}/trips/`;
        if (this.isMyProfile || store.getters.getIsUserAdmin) {
          route = `/user/${this.userId}/trips/${id}`;
        }
        this.$router.push(route);
      },

      /**
       * Toggles showCreateTrip from true to false or false to true
       */
      toggleShowCreateTrip: function () {
        this.showCreateTrip = !this.showCreateTrip;
      },

      /**
       * Deletes the trip from the database.
       * @param tripId The id of the trip to delete.
       */
      deleteTrip: function (tripId) {
        this.clearAlerts();
        this._deleteTrip(this.userId, tripId).then(() => {
          const url = `/users/${this.userId}/trips/${tripId}/toggle_deleted`;
          this.rollbackCheckpoint(
            'DELETE',
            {
              url: url,
            },
            {
              url: url,
            }
          );
        });
      },

      /**
       * Checks if the profile the user is on is theres
       * if it is sets isMyProfile to true
       */
      checkIfProfileOwner: function () {
        let id = this.$route.params.id;
        this.isMyProfile = store.getters.getUser.id == id;
      },

      /**
       * Adds a checkpoint when creating a trip
       */
      checkpointCreateTrip: function (tripId) {
        const url = `/users/${this.userId}/trips/${tripId}/toggle_deleted`;
        this.rollbackCheckpoint(
          "POST",
          {
            url: url
          },
          {
            url: url
          }
        );
      },

      /**
       * Undoes the last action and gets destinations afterwards
       */
      undo: function () {
        const actions = [() => this._getTrips(this.userId), this.clearAlerts];
        try {
          this.rollbackUndo(actions);
        } catch (err) {
          this.undoRedoError = true;
        }
      },

      /**
       * Redoes the last action and gets destinations afterwards
       */
      redo: function () {
        const actions = [() => this._getTrips(this.userId), this.clearAlerts];
        try {
          this.rollbackRedo(actions);
        } catch (err) {
          this.undoRedoError = true;
        }
      }
    },

    /**
     * Sets user privalages and gets trips accordingly.
     */
    created: function () {
      this.checkIfProfileOwner();
      this.isAdminUser = store.getters.getIsUserAdmin;
      this._getTrips(this.userId);
    }
  };
</script>