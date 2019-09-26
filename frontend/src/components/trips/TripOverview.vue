<template>
  <v-layout>
    <v-flex xs12 ma-2 mt-4>
      <div v-if="canEdit">
        <h2>Trip Name</h2>
        <v-form ref="form" lazy-validation>
          <v-text-field v-model="trip.trip.name" :rules="nameRules" :counter="60" required></v-text-field>
          <v-btn outline color="error" @click="update()" class="save-btn">Save Name</v-btn>
        </v-form>
        <v-layout md12 row wrap mb-3>
          <v-flex lg4 xl3>
            <v-btn outline color="error" @click="update()" class="save-btn">Save Name</v-btn>
          </v-flex>
          <v-flex lg8 xl9 v-if="hasSlack && groupAddedToTrip && !groupSlackWorkspace">
            <CreateSlackChannelButton :trip="trip"/>
          </v-flex>
        </v-layout>
      </div>
      <UserStatusList/>
    </v-flex>
  </v-layout>
</template>

<style>
.save-btn {
  margin: 0px 0px 30px 0px;
}
</style>

<script>
  import { store } from "../../store/index";
  import { rules } from "../form_rules";
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  let userGroupRepository = RepositoryFactory.get("userGroup");

  import UserStatusList from "./tripgroups/UserStatusList";
  import CreateSlackChannelButton from "../common/slack/CreateSlackChannelButton";
  import StoreTripsMixin from "../mixins/StoreTripsMixin";

export default {
  name: "TripOverview",
  mixins: [StoreTripsMixin],
  props: {
    trip: Object,
    updateTrip: Function,
    canEdit: Boolean
  },

  components: {
    UserStatusList,
    CreateSlackChannelButton
  },

  data() {
    return {
      ...rules,
      slackWorkspaceDomain: null
    };
  },

  methods: {
    /**
     * Validates the form and updates the trip
     */
    update() {
      if (this.$refs.form.validate()) {
        this.updateTrip();
      }
    },

    /**
     * Determines if the current trip's group has a Slack workspace.
     */
    getSlackWorkspaceDomain(tripId) {
      userGroupRepository
        .getGroupingForTrip(this.$store.getters.getUser.id, tripId)
        .then(result => {
          console.log(result);
          return result;
        });
    }
  },

  computed: {
    /**
     * Determines if the user has integrated with slack or not
     */
    hasSlack() {
      return store.getters.getUser.slack;
    },

    /**
     * Determines if the current trip has a group attached to it.
     */
    groupAddedToTrip() {
      return this.selectedTrip.trip.usergroup.length > 0
    }
  },

  watch: {
    trip: function () {
      if (this.groupAddedToTrip) {
        this.slackWorkspaceDomain = this.getSlackWorkspaceDomain(this.trip.root.id);
      }
    }
  }
};
</script>