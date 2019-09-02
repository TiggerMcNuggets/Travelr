<template>
  <div>
    <a
      :href="`https://slack.com/oauth/authorize?scope=${this.permissionScope}&client_id=${this.clientId}&redirect_uri=${redirectUrl}`">
      <img alt="Add to Slack" height="40" src="../../assets/connect_slack.png"/>
    </a>
    {{ authorisationCode }}
  </div>
</template>

<script>
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  let userRepository = RepositoryFactory.get("user");

  export default {

    data() {
      return {
        permissionScope: "incoming-webhook,commands,admin,channels:write,team:read",
        clientId: "737773912711.735910477760"
      };
    },

    computed: {
      /**
       * Gets the current frontend url and appends the desired redirect route when finished with slack OAuth
       * @returns The redirect url for slack
       */
      redirectUrl() {
        return window.location.origin + this.$route.path;
      },

      /**
       * Captures the authorisation code from Slack
       * @returns The authorisation code from Slack
       */
      authorisationCode() {
        if (this.$route.query.code) {
          return this.$route.query.code;
        }
        return null;
      }
    },

    watch: {
      authorisationCode: function() {
        console.log("watcher here");
        this.attemptAuthorisationGrant();
      }
    },

    methods: {
      attemptAuthorisationGrant() {
        userRepository.slackOAuthStep3(this.$route.params.id, this.authorisationCode)
          .then(response => {
            console.log(response)
          });
      }
    }
  };
</script>