<template>
  <div >
    <a v-if="!connectedWithSlack"
      :href="`https://slack.com/oauth/authorize?scope=${this.permissionScope}&client_id=${this.clientId}&redirect_uri=${redirectUrl}`">
      <img alt="Add to Slack" height="40" src="../../../assets/connect_slack.png"/>
    </a>
    <img v-else alt="Connected to Slack" height="40" src="../../../assets/connected_to_slack.png"/>

  </div>
</template>

<script>
  import { store } from "../../../store/index";
  import { RepositoryFactory } from "../../../repository/RepositoryFactory";
  let userRepository = RepositoryFactory.get("user");

  export default {
    store,

    data() {
      return {
        permissionScope: "incoming-webhook,commands,admin,channels:write,team:read",
        clientId: "737773912711.735910477760",
        connectedWithSlack: store.getters.getUser.slack
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
        return this.$route.query.code;
      }
    },

    methods: {
      attemptAuthorisationGrant() {
        userRepository.slackOAuthStep3(this.$route.params.id, {code: this.authorisationCode})
          .then(response => {
            console.log(response);
            store.dispatch("fetchMe");
          });
      }
    },

    mounted() {
      if (this.$route.query.code) {
        this.attemptAuthorisationGrant();
      }
    }
  };
</script>