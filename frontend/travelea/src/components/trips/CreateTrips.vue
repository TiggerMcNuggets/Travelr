/* eslint-disable */

<template>
  <div class="width-for-container">
    <v-form ref="form" lazy-validation>
      <v-container grid-list-xl>
        <h4>Create a new trip</h4>
        <v-layout justify-space-around="true">
          <v-flex xs12 md6 class="row-input-margin">
            <v-text-field v-model="trip.name" :counter="20" label="Trip Name" required></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field :counter="20" label="Destination Type" required></v-text-field>
          </v-flex>
        </v-layout>
        <ul>
          <li v-for="(destination, index) in trip.destinations" :v-bind="index">
            <v-card class="destination-form-padding">
              <v-layout>
                <v-flex xs12 md4>
                  <v-combobox
                    :items="destinationsNamesAndIndex"
                    v-model="destination.title"
                    label="Select an existing destination"
                  ></v-combobox>
                </v-flex>

                <v-flex xs12 md4>
                  <v-card class="times-padding">
                    <!-- Arrival Time -->
                    <v-menu
                      ref="arrivalTimeMenu"
                      v-model="arrivalTimeMenu"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      :return-value.sync="arrivalTime"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      max-width="290px"
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on }">
                        <v-text-field
                          v-model="arrivalTime"
                          label="Arrival time"
                          prepend-icon="access_time"
                          readonly
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="arrivalTimeMenu"
                        v-model="arrivalTime"
                        full-width
                        @click:minute="$refs.arrivalTimeMenu[index].save(arrivalTime)"
                      ></v-time-picker>
                    </v-menu>

                    <!-- Arrival date -->
                    <v-menu
                      v-model="arrivalDateMenu"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on }">
                        <v-text-field
                          v-model="arrivalDate"
                          label="Arrival date"
                          prepend-icon="event"
                          readonly
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker v-model="arrivalDate" @input="arrivalDateMenu = false"></v-date-picker>
                    </v-menu>
                  </v-card>
                </v-flex>
                <v-flex xs12 md4>
                  <v-card class="times-padding">
                    <!-- Departure time -->
                    <v-menu
                      ref="departureTimeMenu"
                      v-model="departureTimeMenu"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      :return-value.sync="departureTime"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      max-width="290px"
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on }">
                        <v-text-field
                          v-model="departureTime"
                          label="Departure time"
                          prepend-icon="access_time"
                          readonly
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="departureTimeMenu"
                        v-model="departureTime"
                        full-width
                        @click:minute="$refs.departureTimeMenu[index].save(departureTime)"
                      ></v-time-picker>
                    </v-menu>
                    <!-- Departure date -->
                    <v-menu
                      v-model="departureDateMenu"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      lazy
                      transition="scale-transition"
                      offset-y
                      full-width
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on }">
                        <v-text-field
                          v-model="departureDate"
                          label="Departure date"
                          prepend-icon="event"
                          readonly
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker v-model="departureDate" @input="departureDateMenu = false"></v-date-picker>
                    </v-menu>
                  </v-card>
                </v-flex>
              </v-layout>
            </v-card>
          </li>
        </ul>

        <v-layout>
          <v-flex xs12 md6>
            <v-text-field label="Latitude" required></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field type="number" label="Longitude" required></v-text-field>
          </v-flex>
        </v-layout>
        <div>
          <v-btn color="red" v-on:click="resetValues">RESET VALUES</v-btn>
          <v-btn>CREATE DESTINATION</v-btn>
        </div>
      </v-container>
    </v-form>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.width-for-container {
  width: 60%;
}
.destination-form-padding {
  padding: 2em;
}
.times-padding {
  padding: 1em;
}
</style>


<script>
// import { store } from "../../store/index";
import moment from "moment";
import { Datetime } from "vue-datetime";
export default {
  // store,
  components: {
    datetime: Datetime
  },
  props: {},
  data() {
    return {
      arrivalTimeMenu: false,
      arrivalDateMenu: false,
      departureTimeMenu: false,
      departureDateMenu: false,
      arrivalDate: null,
      departureDate: null,
      arrivalTime: null,
      departureTime: null,
      trip: {
        destinations: [
          {
            title: "Ciao",
            arrivalTime: null,
            departureTime: moment().add(1, "day")
          }
          // {
          //   title: "Ciao",
          //   arrivalTime: moment(),
          //   departureTime: moment().add(1, "day")
          // }
        ]
      }
    };
  },
  computed: {
    destinations() {
      return [
        { title: "Ciao", something: "Something" },
        { title: "bUONGIORNO", something: "Something" }
      ];
      // return store.state.destinations.destinations;
    },
    destinationsNamesAndIndex() {
      // return store.state.destinations.destinations.map(
      return this.destinations.map(destination => destination.title);
    }
  },
  methods: {
    resetValues: function() {
      this.$refs.form.reset();
    }
  }
};
</script>