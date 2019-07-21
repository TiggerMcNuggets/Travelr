

<template>
  <v-card>
    <v-container class="outer-container" height="100%" style="margin-left: 0; margin-top: -20px;">
      <PageHeader title="Destinations" :undo="undo" :redo="redo" :options="options"/>

      <v-text-field
        v-if="searchActive"
        v-model="searchValue"
        label="Trip name"
        prepend-icon="search"
      ></v-text-field>

      <v-tabs v-model="active" slider-color="blue">
        <v-tab :key="1" ripple>Browse</v-tab>

        <v-tab-item :key="1">
          <li
            class="destination-list-element"
            v-for="item in destinationsFiltered"
            :value="item.value"
            :key="item.value"
          >
            <v-card class="top-destination-content destination-container">
              <div class="row-container">
                <div
                  class="private-public-side-bar side-border"
                  v-bind:class="{ 'blue-background': item.isPublic, 'pink-background': !item.isPublic }"
                ></div>
                <div class="hoverable destination-container" v-on:click="viewDestination(item.id)">
                  <h2>{{item.name}} | {{item.country}} | {{item.district}}</h2>
                  <div class="row-container">
                    <h3>Lat: {{item.latitude}} | Lng: {{item.longitude}}</h3>
                  </div>
                  <div class="row-container">
                    <h3>Type: {{item.type}}</h3>
                  </div>
                  <div class="row-container">
                    <h3>Traveller Types:</h3>
                    <v-list v-for="type in item.travellerTypes" :value="type.id" :key="type.id">
                      <v-chip small>{{type.name}}</v-chip>
                    </v-list>
                  </div>
                </div>
              </div>
              <div>
                <v-btn
                  v-if="(isMyProfile || isAdminUser) && item.ownerId === parseInt(userId)"
                  icon
                  @click="deleteDestination(item.id)"
                >
                  <v-icon color="red lighten-1">delete</v-icon>
                </v-btn>
                <v-btn
                  v-if="(isMyProfile || isAdminUser) && item.ownerId === parseInt(userId)"
                  icon
                  @click="editDestination(item.id)"
                >
                  <v-icon color="orange lighten-1">edit</v-icon>
                </v-btn>
                <v-btn v-if="!item.isPublic" icon @click="makePublic(item.id)">
                  <v-icon color="blue lighten-1">lock</v-icon>
                </v-btn>
                <v-btn
                  v-if="item.isPublic && item.ownerId === parseInt(userId)"
                  color="#FF69B4"
                  flat
                  icon
                  @click="makePrivate(item.id)"
                >
                  <v-icon color="hotpink lighten-1">lock_open</v-icon>
                </v-btn>
              </div>
            </v-card>
          </li>
        </v-tab-item>
      </v-tabs>

      <v-dialog v-model="dialog" width="800">
        <destination-create :createDestinationCallback="updateDestinationList"/>
      </v-dialog>
    </v-container>
    <v-alert :value="undoRedoError" type="error">Cannot undo or redo</v-alert>
    <v-alert :value="setPrivateError" type="error">Cannot set destination private</v-alert>
  </v-card>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");
.outer-container {
  max-width: 100%;
  width: 100% !important;
}

ul {
  padding-left: 0px;
}

h2 {
  align-self: flex-end;
}

hr {
  margin-bottom: 25px;
}

.section {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.dest-name {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.dest-name div {
  text-align: start;
}
.dest-sub-info p {
  margin-bottom: 0px;
  color: grey;
}

.dest-name button {
  margin-right: 20px;
}

.outer-container {
  text-align: center;
  padding-bottom: 15px;
}

.horizontal-details {
  padding-top: 15px;
}

.pink-background {
  background-color: hotpink;
}

.side-border {
  width: 15px;
}

.destination-container {
  padding: 20px;
}

.hoverable:hover {
  cursor: pointer;
}

.row-container {
  display: flex;
  flex-direction: row;
  margin-top: 10px;
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

.destination-list-element {
  padding-top: 20px;
  list-style-type: none;
}
</style>


<script>
import { store } from "../../store/index";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import RollbackMixin from "../mixins/RollbackMixin.vue";
import DestinationCreate from "./DestinationCreate";
import PageHeader from "../common/header/PageHeader";

export default {
  store,
  mixins: [RollbackMixin],
  components: {
    PageHeader,
    DestinationCreate
  },

  data() {
    return {
      dialog: false,
      showEditDestination: false,
      destinations: [],
      isMyProfile: false,
      ownerId: null,
      active: null,
      showTooltip: false,
      filteredList: [],
      searchValue: "",
      searchActive: false,
      undoRedoError: false,
      setPrivateError: false
    };
  },

  watch: {
    "$route.params.id": function() {
      this.init();
    }
  },

  computed: {
    options() {
      return [
        { action: this.toggleShowCreateDestination, icon: "add" },
        { action: this.toggleShowSearch, icon: "search" }
      ];
    },
    destinationsFiltered() {
      const filteredList = this.destinations.filter(
        destination =>
          destination.name
            .toLowerCase()
            .search(this.searchValue.toLowerCase()) !== -1
      );
      //Currently sorting trips by id, in future we will sort trips by creation time
      return filteredList.sort(function(a, b) {
        return a.id - b.id;
      });
    }
  },

  methods: {
    /**
     * Initialises the application and checks if the user is displaying thier destinations or viewing someone elses.
     * Gets the destination list information to display.
     */

    init() {
      this.checkIfProfileOwner();
      this.getDestinationList();
    },

    /**
     * Sets all visible fields to invisible
     */
    clearAlerts() {
      this.undoRedoError = false;
      this.setPrivateError = false;
    },

    /**
     * Redirects the user to the destinations edit page.
     * @param id The id of the destination to redirect to.
     */
    editDestination(id) {
      this.$router.push("/user/" + this.userId + "/destinations/edit/" + id);
    },

    /**
     * Redirects the user to the single destination page based on a given id
     * @param id The id of the single destination which was clicked.
     */
    viewDestination(id) {
      this.$router.push("/user/" + this.userId + "/destinations/" + id);
    },

    /**
     * Toggles the dialog to create a new destination.
     */
    toggleShowCreateDestination: function() {
      this.dialog = !this.dialog;
    },

    /**
     * Toggles the search bar to search the destinations by name.
     */
    toggleShowSearch: function() {
      this.searchActive = !this.searchActive;
    },

    /**
     * Deletes the destination from the database.
     * @param destId The id of the destination to delete.
     */
    deleteDestination: function(destId) {
      this.clearAlerts();
      destinationRepository.deleteDestination(this.userId, destId).then(() => {
        const url = `/users/${
          this.userId
        }/destinations/${destId}/toggle_deleted`;
        this.rollbackCheckpoint(
          "DELETE",
          {
            url: url
          },
          {
            url: url
          }
        );
        this.getDestinationList();
      });
    },

    /**
     * Refreshes the destination list
     */
    updateDestinationList: function(destId) {
      this.clearAlerts();
      const url = `/users/${this.userId}/destinations/${destId}/toggle_deleted`;
      this.rollbackCheckpoint(
        "POST",
        {
          url: url
        },
        {
          url: url
        }
      );
      this.getDestinationList();
      this.dialog = false;
    },

    /**
     * Sets the list of destinations by request to the API
     */
    getDestinationList: function() {
      destinationRepository
        .getDestinations(this.userId)
        .then(response => {
          this.destinations = response.data;
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Makes a destination public and checks for error.
     * @param destId The destination id to make public
     */
    makePublic: function(destId) {
      this.clearAlerts();
      destinationRepository
        .makePublic(destId)
        .then(() => {
          this.rollbackCheckpoint(
            "POST",
            {
              url: `/destinations/${destId}/make_public`
            },
            {
              url: `/destinations/${destId}/make_private`
            }
          );
          this.init();
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Makes a destination private and checks for error
     * @param destId The destination id to make private
     */
    makePrivate: function(destId) {
      this.clearAlerts();
      destinationRepository
        .makePrivate(destId)
        .then(() => {
          this.rollbackCheckpoint(
            "POST",
            {
              url: `destinations/${destId}/make_private`
            },
            {
              url: `destinations/${destId}/make_public`
            }
          );
          this.init();
        })
        .catch(err => {
          console.log(err);
          this.setPrivateError = true;
        });
    },

    /**
     * Checks if the id of the page being viewed belongs to the signed in user.
     */
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.userId = id;
      this.isMyProfile = store.getters.getUser.id;
    },

    /**
     * Logs the event to the window.
     * @param evt The event to log
     */
    log: function(evt) {
      window.console.log(evt);
    },

    /**
     * Undoes the last action and gets destinations afterwards
     */
    undo: function() {
      const actions = [this.getDestinationList, this.clearAlerts];
      try {
        this.rollbackUndo(actions);
      } catch (err) {
        this.undoRedoError = true;
      }
    },

    /**
     * Redoes the last action and gets destinations afterwards
     */
    redo: function() {
      const actions = [this.getDestinationList, this.clearAlerts];
      try {
        this.rollbackRedo(actions);
      } catch (err) {
        this.undoRedoError = true;
      }
    }
  },

  /**
   * Initialises the function on component creation.
   */
  created: function() {
    this.init();
  }
};
</script>
