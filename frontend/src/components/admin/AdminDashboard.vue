<template>
  <v-container fluid>
    <PageHeader title="Admin Dashboard" :options="[]" :disableUndoRedo="true"/>
    <v-tabs v-model="active" slider-color="blue">
      <v-tab :key="1" ripple>Add New User</v-tab>
      <v-tab :key="2" ripple>Edit Requests</v-tab>

      <v-tab-item :key="1">
        <AdminCreateProfile/>
      </v-tab-item>

      <v-tab-item :key="2">
        <DestinationEditRequests/>
      </v-tab-item>
    </v-tabs>
  </v-container>
</template>

<style>
</style>

<script>
import AdminCreateProfile from "./AdminCreateUser";
import DestinationEditRequests from "./DestinationEditRequests";
import PageHeader from "../common/header/PageHeader";

export default {
  name: "AdminDashboard",

  data() {
    return {
      active: null
    };
  },

  components: {
    PageHeader,
    AdminCreateProfile,
    DestinationEditRequests
  },

  methods: {}
};
</script>
