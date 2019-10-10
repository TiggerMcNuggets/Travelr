<template>
  <v-btn class="slack-button"
         v-on:click="createSlackChannel"
         color="error">
    <v-avatar size="26">
      <img src="../../../assets/slack_logo_circle.png">
    </v-avatar>
    CREATE SLACK CHANNEL
  </v-btn>
</template>

<style>
  .slack-button {
    margin: 0 0 10px 0;
  }
</style>

<script>
  import {RepositoryFactory} from "../../../repository/RepositoryFactory";

  let userRepository = RepositoryFactory.get("user");

  export default {

    props: {
      trip: Object
    },

    methods: {
      createSlackChannel() {
        userRepository.createSlackChannel(this.$route.params.id, {
          channelName: this.trip.trip.name,
          tripId: this.trip.trip.id
        })
        .then(() => {
          this.showSuccessSnackbar(this._snackbarMessages.slackChannelCreateSuccess, 5000);
          this.$store.dispatch("getTrip", { userId: this.$store.getters.getUser.id, tripId: this.trip.trip.id });
        })
        .catch(() => {
          this.showErrorSnackbar(this._snackbarMessages.slackChannelAlreadyExists, 5000);
        });
      }
    }
  };
</script>