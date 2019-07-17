<template>
  <v-container fluid fill-height ma-0 pa-0>
    <v-layout row>
      <v-flex col sm6>
        <img class="home__logo" src="../assets/logo_dark.png" />
        <Login class="home__login_area" />
      </v-flex>
      <v-flex col sm6>
        <img class="home__feature_img" src="../assets/homepage_graphic.png" />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
.home__logo {
  width: 30%;
  padding: 40px 20px;
}

.home__feature_img {
  width: 100%;
}

.home__login_area {
  width: 80%;
  padding: 20px;
}

.overlay {
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0px;
  left: 0px;
  background-image: url("https://farm9.staticflickr.com/8629/16141602477_82d6d91a2d_b.jpg");
  filter: grayscale(50%);
  background-size: 100% 100%;
  background-repeat: no-repeat;
}
</style>

<script>
import Login from "../components/login/Login";

export default {
  name: "Home",

  components: {
    Login
  },

  data() {
    return {};
  }
};
</script>