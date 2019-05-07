/* eslint-disable */

<template>
<v-card >
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
      </div>
    </div>

    <v-divider class="photo-header-divider"></v-divider>

    <v-tabs
            v-model="active"
            slider-color="blue"
    >
      <v-tab
              :key="1"
              ripple
      >
        Browse

      </v-tab>
      <v-tab
              :key="2"
              ripple
      >
        Map
      </v-tab>
      <v-tab-item
              :key="1"
      >
        <ul>
          <li
                  class="destination-list-element"
                  v-for="item in destinations"
                  :value="item.value"
                  :key="item.value"
          >
            <div class="top-destination-content">
              <h2 @click="viewDestination(item.id)">{{ item.name }}</h2>
              <span>
            <!-- item.id -->
            <div v-if="isMyProfile" @click="editDestination(item.id)">
              <a>Edit</a>
            </div>
                <!--Sprint 3 todo<a v-on:click="deleteDestination(item.id)">Delete</a>-->
          </span>
            </div>
            <ul class="horizontal-details">
              <li>
                <p>
                  <strong>COUNTRY:</strong>
                  {{ item.country }}
                </p>
              </li>
              <li>
                <p>
                  <strong>TYPE:</strong>
                  {{ item.type }}
                </p>
              </li>
              <li>
                <p>
                  <strong>DISTRICT:</strong>
                  {{ item.district }}
                </p>
              </li>
              <li>
                <p>
                  <strong>LATITUDE:</strong>
                  {{ item.latitude }}
                </p>
              </li>
              <li>
                <p>
                  <strong>LONGITUDE:</strong>
                  {{ item.longitude }}
                </p>
              </li>
            </ul>
          </li>
        </ul>

      </v-tab-item>
      <v-tab-item
              :key="2"
      >
        <v-card>
          <MapDashboard
            :destinations="this.destinations"/>
        </v-card>
      </v-tab-item>
    </v-tabs>
    

    <v-dialog v-model="dialog" width="800">
       <destination-create :createDestinationCallback="updateDestinationList" />
    </v-dialog>

  </v-container >
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
  background-color: #05386b;
}
.blue-background {
  background-color: #0d47a1;
}
.pink-background {
  
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
import {RepositoryFactory} from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import DestinationCreate from "./DestinationCreate";
import MapDashboard from "../map/MapDashboard";
import DestinationCreate from "./DestinationCreate"


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
      active: null
        showTooltip: false,
        dialog: false,
        showEditDestination: false,
        destinations: [],
        isMyProfile: false,
        user_id: null
    };
  },
    computed: {
    },
   // child components
  components: {
      MapDashboard,
      DestinationCreate: DestinationCreate,
  },
  watch: {
    '$route.params.id': function() {
      this.init();
    }
  },
  methods: {
    init() {
      this.checkIfProfileOwner();
      this.getDestinationList();
    },
    editDestination(id) {
      this.$router.push("/user/"+this.user_id+"/destinations/edit/"+id);
    },
    viewDestination(id) {
      this.$router.push("/user/"+this.user_id+"/destinations/"+id);
    },
    toggleShowCreateDestination: function() {
      this.dialog = !this.dialog;
    },
    deleteDestination: function() {
        // TODO: ion progress
    },
    updateDestinationList: function()  {
      this.getDestinationList();
      this.dialog = false;
    },
    getDestinationList: function () {
      destinationRepository.getDestinations(this.user_id)
      .then(response => {
          this.destinations = response.data
      })
      .catch(err => {
          console.log(err)
    })
    },
    makePublic: function (destId) {
        destinationRepository.makePublic(destId)
            .then((res) => {
                console.log(res);
                this.init();
            })
            .catch((err) => {
                console.log(err);
            });
    },
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.user_id = id;
      this.isMyProfile = (store.getters.getUser.id);
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
