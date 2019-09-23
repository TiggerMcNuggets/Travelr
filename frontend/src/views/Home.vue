<template>
  <v-app id="inspire">
    <div v-if="!isSmall && !isExtraSmall" class="home-container">
      <img class="home-feature-img" src="../assets/homepage_graphic.png" />
    </div>
    <v-container fluid fill-height ma-0 pa-0>
      <v-layout row wrap d-flex direction="column" align-center>
        <v-flex
          col
          sm12
          md7
          :class="!isSmall && !isExtraSmall ? 'pl-5 ml-5': 'ma-4' "
        >
          <v-flex :class="!isSmall && !isExtraSmall ? 'heading-container pt-3 pb-3': 'heading-container mt-5 mb-5'">
            <img
              v-if="isSmall || isExtraSmall"
              class="home-mobile-feature-img"
              src="../assets/homepage_graphic_mobile.png"
            />

            <div>
              <img class="home-logo" src="../assets/logo2_dark.png" />
              <h3 mt-2 class="home-tagline">Business trips made easy</h3>
            </div>
          </v-flex>
          <router-view class="home-login-area"></router-view>
        </v-flex>
      </v-layout>
    </v-container>
  </v-app>
</template>

<style lang="scss">
@import url("https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap");

@media screen and (max-width: 960px) {
  .home-logo {
    width: 90%;
    display: block;
    margin-left: auto;
    margin-right: auto;
  }

  .home-mobile-feature-img {
    width: 90%;
    display: block;
    margin: 50px auto;
  }

  .home-login-area {
    width: 90%;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 20px;

    .login-button {
      width: 100%;
    }
  }

  .home-tagline {
    font-size: 4vw;
    text-align: center;
  }
}

@media screen and (min-width: 960px) {
  .home-logo {
    width: 60%;
  }

  .home-feature-img {
    height: 100%;
  }

  .home-login-area {
    width: 60%;

    .login-button {
      margin-bottom: 30px
    }
  }

  .home-container {
    position: fixed;
    right: 0;
    height: 100%;
  }

  .home-tagline {
    font-size: 1.5vw;
  }
}

.heading-container {
  margin-top: 0px;
  margin-bottom: 60px;
}

.home-tagline {
  font-family: "Roboto", sans-serif;
  color: lightslategray;
  font-weight: 300;
  margin-top: 30px;
}
</style>

<script>
import DeviceSizeMixin from "../components/mixins/DeviceSizeMixin";

export default {
  mixins: [DeviceSizeMixin],

  name: "Home",

  data() {
    return {};
  }
};
</script>