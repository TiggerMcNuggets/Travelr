<template>
  <v-container fluid fill-height ma-0 pa-0>
    <v-layout row>
      <v-flex col sm6 pa-5 ma-3 mt-5 ml-5 >
        <div class="heading-container">
        <img class="home__logo" src="../assets/logo2_dark.png" />
        <h3 mt-2 class='home__tagline' >Taking the hassle out of your next business trip</h3>
        </div>
        <Login class="home__login_area" />
      </v-flex>
      <v-flex col sm6>
        <img class="home__feature_img" src="../assets/homepage_graphic.png" />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
@import url('https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap');

.home__logo {
  width: 60%;
}

.home__feature_img {
  width: 100%;
}

.home__login_area {
  width: 80%;
}

.home__tagline {
    font-family: 'Roboto', sans-serif;
    color: lightslategray;
    font-weight: 300;
    font-size: 20px;
    margin-top: 15px;
}

.heading-container {
    margin-bottom: 60px;
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