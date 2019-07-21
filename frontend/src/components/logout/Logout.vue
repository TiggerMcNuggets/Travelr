<template>
  <button></button>
</template>

<style>
</style>

<script>
import { store } from "../../store/index";

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

    /**
     * Logs the user out and sets their auth token to null in the store. Redirects the user back to login.
     */
    logout() {
      store
        .dispatch("logout")
        .then(() => {
          localStorage.setItem("token", "");
          this.$router.push("/login");
        })
        .catch(() => {
          localStorage.setItem("token", "");
          this.$router.push("/login");
        });
    }
  }
};
</script>
