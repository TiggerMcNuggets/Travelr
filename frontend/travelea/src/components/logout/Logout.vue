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
    this.logout();
  },
  methods: {
    async logout() {
      await store.dispatch("logout");
      localStorage.setItem("token", "");

      this.$router.push("/login");
    }
  }
};
</script>
