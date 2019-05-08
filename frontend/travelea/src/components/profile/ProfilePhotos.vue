<template>
  <v-card>
    <personalPhotos></personalPhotos>
  </v-card>
</template>


<style>
</style>


<script>
import PersonalPhotos from "./PersonalPhotos";
import { store } from "../../store/index";

export default {
  name: "Profile",
  store,

  data() {
    return {};
  },

  components: {
    PersonalPhotos
  },

  methods: {}
};
</script>
