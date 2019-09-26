<template>
  <v-btn class="slack-button"
         v-on:click="joinSlackChannel"
         color="error">
    <v-avatar size="26">
      <img src="../../../assets/slack_logo_circle.png">
    </v-avatar>
     JOIN SLACK CHANNEL
  </v-btn>
</template>

<style>
  .slack-button {
    margin: 0;
  }
</style>

<script>
  export default {

    props: {
      slackWorkspaceDomain: String
    },

    methods: {
      joinSlackChannel() {
        window.open("https://" + this.slackWorkspaceDomain + ".slack.com", '_blank');
      }
    }
  };
</script>