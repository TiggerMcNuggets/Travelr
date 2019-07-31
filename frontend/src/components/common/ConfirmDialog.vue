<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">{{ title }}</h2>
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text>{{ text }}</v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 color="error" v-on:click="cancel()">No</v-btn>
      <v-btn ma-3 color="error" v-on:click="confirm()">Yes</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: {
    title: String,
    text: String,
    confirm: Function,
    cancel: Function
  },

  data() {
    return {};
  },

  methods: {}
};
</script>
