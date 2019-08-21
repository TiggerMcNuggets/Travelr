<template>
  <v-app id="inspire">
    <v-container fluid fill-height ma-0 pa-0>
      <v-layout row>
        <v-flex col sm6 pa-5 ma-3 mt-5 ml-5>
          <div class="heading-container">
            <img class="home__logo" src="../assets/logo2_dark.png" />
            <h3 mt-2 class="home__tagline">Business trips made easy</h3>
          </div>
          <router-view class="home__login_area"></router-view>
        </v-flex>
        <v-flex col sm6>
          <img class="home__feature_img" src="../assets/homepage_graphic.png" />
        </v-flex>
      </v-layout>
    </v-container>
  </v-app>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap");

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
  font-family: "Roboto", sans-serif;
  color: lightslategray;
  font-weight: 300;
  font-size: 20px;
  margin-top: 15px;
}

.heading-container {
  margin-bottom: 60px;
}
</style>

<script>
export default {
  name: "Home",

  data() {
    return {};
  }
};
</script>