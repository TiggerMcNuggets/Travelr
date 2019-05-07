/* eslint-disable */

<template>
  <v-card>
    <v-container class="outer-container" height="100%" style="margin-left: 0px; margin-top: -20px;">
      <div class="section">
        <div class="dest-name">
          <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
            <v-icon dark>keyboard_arrow_left</v-icon>
          </v-btn>
          <h2 class="headline">Destinations</h2>
        </div>
        <div>
          <v-btn
            class="upload-toggle-button"
            fab
            small
            dark
            color="indigo"
            v-if="isMyProfile || isAdminUser"
            @click="toggleShowCreateDestination"
          >
            <v-icon dark>add</v-icon>
          </v-btn>

          <v-btn
            class="upload-toggle-button"
            fab
            small
            dark
            color="indigo"
            @click="toggleShowSearch"
          >
            <v-icon dark>search</v-icon>
          </v-btn>
        </div>
      </div>

      <v-divider class="photo-header-divider"></v-divider>
      <v-text-field
        v-if="searchActive"
        v-model="searchValue"
        label="Trip name"
        prepend-icon="search"
      ></v-text-field>

      <v-tabs v-model="active" slider-color="blue">
        <v-tab :key="1" ripple>Browse</v-tab>
        <v-tab :key="2" ripple>Map</v-tab>

        <v-tab-item :key="1">
          <ul>
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
                  <div
                    class="hoverable destination-container"
                    v-on:click="viewDestination(item.id)"
                  >
                    <h2>{{item.name}} | {{item.country}} | {{item.district}}</h2>
                    <div class="row-container">
                      <h3>Lat: {{item.latitude}} | Lng: {{item.longitude}}</h3>
                    </div>
                    <div class="row-container">
                      <h3>Type: {{item.type}}</h3>
                    </div>
                  </div>
                </div>
                <div>
                  <v-btn
                          v-if="(isMyProfile || isAdminUser) && !item.isPublic"
                          icon
                          @click="deleteDestination(item.id)">
                    <v-icon color="red lighten-1">delete</v-icon>
                  </v-btn>
                  <v-btn
                          v-if="(isMyProfile || isAdminUser) && !item.isPublic"
                          icon
                          @click="editDestination(item.id)">
                    <v-icon color="orange lighten-1">edit</v-icon>
                  </v-btn>
                  <v-btn
                          v-if="!item.isPublic"
                          icon
                          @click="makePublic(item.id)">
                    <v-icon color="blue lighten-1">lock</v-icon>
                  </v-btn>
                  <v-btn
                    v-if="item.isPublic"
                    color="#FF69B4"
                    flat
                    icon
                    @click="() => console.log('clicked on open lock')"
                  >
                    <v-icon color="hotpink lighten-1">lock_open</v-icon>
                  </v-btn>
                </div>
              </v-card>
            </li>
          </ul>
        </v-tab-item>
        <v-tab-item :key="2">
          <v-card>
            <MapDashboard :destinations="this.destinations"/>
          </v-card>
        </v-tab-item>
      </v-tabs>

      <v-dialog v-model="dialog" width="800">
        <destination-create :createDestinationCallback="updateDestinationList"/>
      </v-dialog>
    </v-container>
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
}
</style>


<script>
import { store } from "../../store/index";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import MapDashboard from "../map/MapDashboard";
import DestinationCreate from "./DestinationCreate";

export default {
  store,
  // local variables
  data() {
    return {
      dialog: false,
      showEditDestination: false,
      destinations: [],
      isMyProfile: false,
      user_id: null,
      active: null,
      showTooltip: false,
      filteredList: [],
      searchValue: "",
      searchActive: false
    };
  },
  // child components
  components: {
    MapDashboard,
    DestinationCreate: DestinationCreate
  },
  watch: {
    "$route.params.id": function() {
      this.init();
    }
  },

  computed: {
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
    init() {
      this.checkIfProfileOwner();
      this.getDestinationList();
    },
    editDestination(id) {
      this.$router.push("/user/" + this.user_id + "/destinations/edit/" + id);
    },
    viewDestination(id) {
      this.$router.push("/user/" + this.user_id + "/destinations/" + id);
    },
    toggleShowCreateDestination: function() {
      this.dialog = !this.dialog;
    },
    toggleShowSearch: function() {
      this.searchActive = !this.searchActive;
    },
    deleteDestination: function(destId) {
      destinationRepository.deleteDestination(this.user_id, destId).then(() => {
          this.init();
      });
    },
    updateDestinationList: function() {
      this.getDestinationList();
      this.dialog = false;
    },
    getDestinationList: function() {
      destinationRepository
        .getDestinations(this.user_id)
        .then(response => {
          this.destinations = response.data;
        })
        .catch(err => {
          console.log(err);
        });
    },
    makePublic: function(destId) {
      destinationRepository
        .makePublic(destId)
        .then(res => {
          console.log(res);
          this.init();
        })
        .catch(err => {
          console.log(err);
        });
    },
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.user_id = id;
      this.isMyProfile = store.getters.getUser.id;
    },
    log: function(evt) {
      window.console.log(evt);
    }
  },
  created: function() {
    this.init();
  }
};
</script>

