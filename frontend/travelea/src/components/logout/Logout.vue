<template>

</template>

<style>

</style>

<script>
import {store} from "../../store/index";

export default {
  name: "Logout",
  store,
  data() {
    return {};
  },
  mounted() {
    console.log("here");
    logout();
  },
  methods: {
    async logout() {
      await store.dispatch("logout");
      localStorage.setItem("token", "");
      console.log("did i get here?");
      this.$router.push("/login");
    }
  }
};
</script>
