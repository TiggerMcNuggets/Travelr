<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <h2>General</h2>
    <!-- Download iCal button -->
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" icon @click="downloadICal()" flat fab color="error">
          <v-icon>calendar_today</v-icon>
        </v-btn>
      </template>
      <span>Export Calendar</span>
    </v-tooltip>

    <!-- Download trip pdf -->
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" icon @click="downloadTripPdf()" flat fab color="error">
          <v-icon>picture_as_pdf</v-icon>
        </v-btn>
      </template>
      <span>Download Trip PDF</span>
    </v-tooltip>


    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" v-if="hasWritePermissions" @click="emailTrip()" flat fab color="error" >
          <v-icon>contact_mail</v-icon>
        </v-btn>
      </template>
      <span>Email Trip & Calendar To Group</span>
    </v-tooltip>

    <!-- Email iCal and PDF button -->
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" v-if="hasWritePermissions" @click="emailTrip(true)" flat fab color="error" >
          <v-icon>email</v-icon>
        </v-btn>
      </template>
      <span>Email Trip & Calendar To You</span>
    </v-tooltip>

    <UserFiles 
      v-if="trip.trip.usergroup.length" 
      :hasWritePermissions="hasWritePermissions"
      :pushStack="pushStack"
    />
  </div>
</template>

<style>
  .download-calendar {
    display: inline-block;
  }
</style>

<script>
  import { store } from "../../store/index";
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  import { download } from "./trips_destinations_util";
  import UserFiles from "./viewtrip/UserFiles";

  let tripRepository = RepositoryFactory.get("trip");

  export default {
    name: "TripFiles",

    props: {
      trip: Object,
      hasWritePermissions: Boolean,
      pushStack: Function
    },

    components: {
      UserFiles
    },

    data() {
      return {
        userId: this.$route.params.id,
        tripId: this.$route.params.trip_id,
        isAdmin: store.getters.getIsUserAdmin
      };
    },

    methods: {
      /**
       * Emails the iCal and pdf to all of the users in the group
       */
      emailTrip(onlyMe) {
        tripRepository.emailPdfAndICal(this.userId, this.tripId, onlyMe);
      },

      /**
       * Downloads the trip from the database as an ics
       */
      downloadICal() {
        tripRepository.downloadICal(this.userId, this.tripId).then(res => {
          download(res, `${this.trip.trip.name}.ics`);
        });
      },

      /**
       * Downloads the trip from the database as a pdf
       */
      downloadTripPdf() {
        tripRepository.downloadTripPdf(this.userId, this.tripId).then(res => {
          download(res, `${this.trip.trip.name}.pdf`);
        })
      },
    },
  };
</script>
