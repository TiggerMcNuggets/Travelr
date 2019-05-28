<template>
  <v-menu
    v-model="dob_menu"
    :close-on-content-click="false"
    :nudge-right="40"
    lazy
    transition="scale-transition"
    offset-y
    full-width
    min-width="290px"
  >
    <template v-slot:activator="{ on }">
      <v-text-field
        label="Date of Birth"
        :value="dob"
        :rules="rules.required"
        @input="$emit('update:dob', $event)"
        readonly
        v-on="on"
      ></v-text-field>
    </template>
    <v-date-picker
      :value="dob"
      :rules="rules.required"
      :max="this.current_date"
      @input="$emit('updateDob', $event); dob_menu=false"
    ></v-date-picker>
  </v-menu>
</template>

<style>
</style>

<script>
import rules from "../formRules.js";
export default {
  name: "Datepicker",
  props: ["dob"],
  data() {
    return {
      current_date: new Date().toISOString().substr(0, 10),
      rules,
      dob_menu: null
    };
  }
};
</script>
