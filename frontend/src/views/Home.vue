<template>
  <v-app id="inspire">
    <div v-if="!isSmall && !isExtraSmall" class="home-container">
      <img class="home__feature_img" src="../assets/homepage_graphic.png" />
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
              class="home__mobile_feature_img"
              src="../assets/homepage_graphic_mobile.png"
            />

            <div>
              <img class="home__logo" src="../assets/logo2_dark.png" />
              <h3 mt-2 class="home__tagline">Business trips made easy</h3>
            </div>
          </v-flex>
          <router-view class="home__login_area"></router-view>
        </v-flex>
      </v-layout>
    </v-container>
  </v-app>
</template>

<style lang="scss">
@import url("https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap");

@media screen and (max-width: 960px) {
  .home__logo {
    width: 90%;
    display: block;
    margin-left: auto;
    margin-right: auto;
  }

  .home__mobile_feature_img {
    width: 90%;
    display: block;
    margin: 50px auto;
  }

  .home__login_area {
    width: 90%;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 20px;

    .login-button {
      width: 100%;
    }
  }

  .home__tagline {
    font-size: 4vw;
    text-align: center;
  }
}

@media screen and (min-width: 960px) {
  .home__logo {
    width: 60%;
  }

  .home__feature_img {
    height: 100%;
  }

  .home__login_area {
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

  .home__tagline {
    font-size: 1.5vw;
  }
}

.heading-container {
  margin-top: 0px;
  margin-bottom: 60px;
}

.home__tagline {
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