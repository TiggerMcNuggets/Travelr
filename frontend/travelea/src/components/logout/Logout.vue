<template>
<button></button>
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
    logout() {
      store.dispatch("logout").then(() => {
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
