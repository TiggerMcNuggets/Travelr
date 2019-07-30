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
      <v-btn ma-3 color="error" outline v-on:click="cancel()">Cancel</v-btn>
      <v-btn ma-3 color="error" flat v-on:click="confirm()">Confirm</v-btn>
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

  data() {},

  methods: {}
};
</script>
