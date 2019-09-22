<template>
  <v-tooltip top>
    <template v-slot:activator="{ on }">
      <v-btn class="slack-button"
        v-on:click="createSlackChannel"
        icon
        v-on="on">
        <v-avatar size="30">
          <img src="../../../assets/slack_logo_circle.png">
        </v-avatar>
      </v-btn>
    </template>
    <span>Create Slack Channel</span>
  </v-tooltip>
</template>

<script>
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  let userRepository = RepositoryFactory.get("user");

  export default {

    props: {
      tripName: String
    },

    methods: {
      createSlackChannel() {
        userRepository.createSlackChannel(this.$route.params.id, {channelName: this.tripName})
          .then(response => {
            console.log(response)
          });
      }
    }
  };
</script>