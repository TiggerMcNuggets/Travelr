<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-container fluid>
    <PageHeader
      title="Trips"
      :undo="undo"
      :redo="redo"
      :canRedo="rollbackCanRedo"
      :canUndo="rollbackCanUndo"
      :options="options"
    />

    <v-alert :value="undoRedoError" type="error">Cannot undo or redo</v-alert>
    <v-text-field v-if="searchActive" v-model="searchValue" label="Trip name" prepend-icon="search"></v-text-field>

    <v-container v-bind="{ [`grid-list-lg`]: true }" class="no-margin no-padding" fluid>
      <v-layout row wrap>
        <v-flex
          v-for="(item, index) in tripsFiltered"
          :key="index"
          xs12
          md4
          sm6
          xl3
          @click="openTrip(item.id)"
          class="trip-grid-item"
        >
          <v-img
            transition
            src="http://www.businessarchives.org/wp-content/uploads/2017/09/Business-Travel-11.jpg"
            class="trip-grid-img"
            aspect-ratio="1.75"
          ></v-img>

          <v-layout align-center justify-space-between>
            <v-flex pt-3 pb-3>
              <span class="headline">{{ item.name }}</span>
            </v-flex>

            <v-flex>
              <v-layout justify-end>
                <CreateSlackChannelButton v-if="(isMyProfile && hasSlack)" :tripName="item.name"></CreateSlackChannelButton>
                <v-tooltip top>
                  <template v-slot:activator="{ on }">
                    <v-btn icon dark v-on="on">
                      <v-icon
                        v-on:click="downloadTripPdf(item.id, item.name)"
                        color="primary"
                      >picture_as_pdf</v-icon>
                    </v-btn>
                  </template>
                  <span>Download trip pdf</span>
                </v-tooltip>
                <v-btn
                  v-if="(isMyProfile || isAdminUser) && !item.isPublic"
                  icon
                  @click="deleteTrip(item.id)"
                >
                  <v-icon dark color="red lighten-1">delete</v-icon>
                </v-btn>
              </v-layout>
            </v-flex>
          </v-layout>
          <v-divider class="no-margin"></v-divider>
        </v-flex>
      </v-layout>
    </v-container>
  </v-container>
</template>

<style lang="scss">
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
  padding-right: 20px;
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

.trip-grid-item:hover {
  cursor: pointer;

  .trip-grid-img {
    opacity: 0.7;
  }
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";

let tripRepository = RepositoryFactory.get("trip");
import { store } from "../../store/index";
import RollbackMixin from "../mixins/RollbackMixin.vue";
import StoreTripsMixin from "../mixins/StoreTripsMixin.vue";
import PageHeader from "../common/header/PageHeader";

export default {
  store,

  // child components
  components: {
    PageHeader
  },

  mixins: [RollbackMixin, StoreTripsMixin],

  // local variables
  data() {
    return {
      text: "",
      showCreateTrip: false,
      searchValue: "",
      isAdmin: store.getters.getIsUserAdmin,
      isMyProfile: false,
      isAdminUser: false,
      userId: this.$route.params.id,
      searchActive: false,
      undoRedoError: false,
      fillerImageURL:
        "https://cdn0.iconfinder.com/data/icons/vacation-line/100/vacation_luggage-512.png"
    };
  },
  // the place where you want to make the store values readable
  computed: {
    options() {
      return [
        {
          action: this.createTrip,
          icon: "add",
          title: "Create New Trip"
        },
        { action: this.toggleShowSearch, icon: "search", title: "Search Trips" }
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
      return filteredList.sort(function(a, b) {
        return a.id - b.id;
      });
    },

    /**
     * Determines if the user has integrated with slack or not
     */
    hasSlack() {
      return store.getters.getUser.slack;
    }
  },

  methods: {
    createTrip() {
      const tripCreation = { name: "New Trip" };
      this._postTrip(this.$route.params.id, tripCreation).then(response => {
        let tripId = response.data.id;
        let route = `/user/${this.userId}/trips/`;
        this._getTrip(this.userId, tripId).then(() => {
          if (this.isMyProfile || store.getters.getIsUserAdmin) {
            route = `/user/${this.userId}/trips/${tripId}`;
          }
          this.$router.push(route);
        });
      });
    },

    /**
     * Sets all alert error visible fields to invisible
     */
    clearAlerts() {
      this.undoRedoError = false;
    },

    /**
     * Toggles searchActive from true to false or false to true
     */
    toggleShowSearch: function() {
      this.searchActive = !this.searchActive;
    },

    /**
     * If the user is either admin or owns the profile
     * redirects the current page to the trip page
     * @param id the trip id
     */
    openTrip: function(id) {
      let route = `/user/${this.userId}/trips/`;
      this._getTrip(this.userId, id).then(() => {
        if (this.isMyProfile || store.getters.getIsUserAdmin) {
          route = `/user/${this.userId}/trips/${id}`;
        }
        this.$router.push(route);
      });
    },

    /**
     * Toggles showCreateTrip from true to false or false to true
     */
    toggleShowCreateTrip: function() {
      this.showCreateTrip = !this.showCreateTrip;
    },

    /**
     * Deletes the trip from the database.
     * @param tripId The id of the trip to delete.
     */
    deleteTrip: function(tripId) {
      this.clearAlerts();
      this._deleteTrip(this.userId, tripId).then(() => {
        const url = `/users/${this.userId}/trips/${tripId}/toggle_deleted`;
        this.rollbackCheckpoint(
          "DELETE",
          {
            url: url
          },
          {
            url: url
          }
        );
      });
    },

    /**
     * Fetches the PDF file for the selected trip and downloads it on the user machine
     * @param tripId the id of the selected trip
     * @param tripName the name of the selected trip
     */
    downloadTripPdf: function(tripId, tripName) {
      tripRepository.downloadTripPdf(this.userId, tripId).then(res => {
        const url = window.URL.createObjectURL(
          new Blob([res.data], { type: "application/pdf" })
        );
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${tripName}.pdf`);
        document.body.appendChild(link);
        link.click();
      });
    },

    /**
     * Checks if the profile the user is on is theres
     * if it is sets isMyProfile to true
     */
    checkIfProfileOwner: function() {
      let id = this.$route.params.id;
      this.isMyProfile = store.getters.getUser.id == id;
    },

    /**
     * Adds a checkpoint when creating a trip
     */
    checkpointCreateTrip: function(tripId) {
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
    undo: function() {
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
    redo: function() {
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
  created: function() {
    this.checkIfProfileOwner();
    this.isAdminUser = store.getters.getIsUserAdmin;
    this._getTrips(this.userId);
  }
};
</script>