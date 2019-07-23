

<template>
  <v-container fluid>
    <PageHeader title="User Search" :options="[]" :disableUndoRedo="true" />
    <v-layout row wrap>
      <v-flex xs12 sm8 md9>
        <userTable />
      </v-flex>
      <v-flex xs12 sm4 md3>
        <searchFilter />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
</style>


<script>
import userTable from "./userTable";
import searchFilter from "./searchFilter";
import PageHeader from "../common/header/PageHeader";

export default {
  data() {
    return {};
  },
  components: {
    userTable,
    searchFilter,
    PageHeader
  }
};
</script>
