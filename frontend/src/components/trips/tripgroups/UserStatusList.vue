<template>
  <v-flex>
    <v-list class="pa-0" v-for="user in usergroup" :key="user.id" wrap>
      <v-list-tile avatar>
        <v-list-tile-avatar>
          <v-icon color="success lighten-1" v-if="user.status == 'GOING'">check</v-icon>
          <v-icon color="error lighten-1" v-if="user.status == 'NOT GOING'">close</v-icon>
          <v-icon color="warning" v-if="user.status == 'MAYBE'">?</v-icon>
        </v-list-tile-avatar>

        <v-list-tile-content>
          <v-list-tile-title>{{`${user.firstName} ${user.lastName}`}}</v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>
  </v-flex>
</template>

<script>
export default {
  data() {
    return {};
  },

  props: {
    usergroup: Array
  }
};
</script>


