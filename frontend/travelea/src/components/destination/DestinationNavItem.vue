<template>
  <v-flex xs12>
    <v-card flat>
      <v-layout row wrap pt-2 pb-2 pl-3 v-bind:class="{showing: destination.isShowing}">
        <v-flex xs12>
          <strong>{{ destination.data.name }}</strong>
        </v-flex>
        <v-flex xs12>{{ destination.data.district}}, {{ destination.data.country }}</v-flex>
      </v-layout>
    </v-card>
  </v-flex>
</template>

<style>
.showing {
  background-color: rgba(25, 118, 210, 0.8);
  border-bottom: 1px solid white;
  color: white;
}
</style>

<script>
export default {
  data() {
    return {};
  },
  props: {
    destination: Object
  },
  methods: {}
};
</script>

