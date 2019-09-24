<template>
  <v-app id="inspire">
    <v-toolbar fixed app class='main-header' >
          <v-btn text icon v-on:click="expand = !expand" v-if="small">
            <v-icon color="white" v-model="small">menu</v-icon>
          </v-btn>
            <router-link to="/" class="primary-logo">
              <v-toolbar-title class="fill-height toolbar toolbar-title">
                <img class="fill-height" src="../assets/logo2_white.png">
              </v-toolbar-title>
            </router-link>
    </v-toolbar>
    <v-navigation-drawer disable-resize-watcher fixed app :mini-variant="mini" v-model="displayNav" hide-overlay>
      <v-toolbar flat class='transparent profile-icon' >
      <v-list class="pa-0">
          <v-list-tile avatar>
            <v-list-tile-avatar>
              <img :src="url">
            </v-list-tile-avatar>

            <v-list-tile-content>
              <v-list-tile-title>{{traveller.firstName}} {{traveller.lastName}}</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </v-list>
            </v-toolbar>
      <v-list class="pt-0">
        <v-divider></v-divider>
        <v-list-tile v-for="item in menuOptions" :key="item.name" :to="item.link">
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>{{ item.name }}</v-list-tile-content>
        </v-list-tile>
        <v-list-tile v-if="loggedIn" v-on:click="logout">
          <v-list-tile-action>
            <v-icon>lock_open</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>Logout</v-list-tile-content>
        </v-list-tile>
      </v-list>

      <v-list class="pt-0 pb-0 collapse">
        <v-list-tile @click.stop="mini = !mini" v-if="!this.small">
          <v-list-tile-action>
            <v-icon v-if="mini">chevron_right</v-icon>
            <v-icon v-else>chevron_left</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Collapse Sidebar</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-content>
      <v-container fluid fill-height ma-0 pa-0>
        <v-layout>
          <v-flex>
            <router-view></router-view>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<style lang="scss">
@import "../assets/css/main.scss";

.v-navigation-drawer .collapse {
  border-top: 1px solid rgba(0, 0, 0, 0.12);
  position: absolute;
  bottom: 0;
  width: 100%;
}

.profile-icon .v-toolbar__content {
  justify-content: normal !important 
}

.primary-logo {
  height: 50%;
}

@media screen and (max-width: 1264px) {
  .v-toolbar__content {
    display: flex;
    justify-content: space-between;
  }
}

@media screen and (min-width: 1264px) {
  .v-toolbar__content {
    justify-content: flex-end;
  }
}

.fill-height {
  height: 100%;
}
</style>


<script>
import { store } from "../store/index";
import DefaultPic from "../assets/defaultPic.png";
import base_url from "../repository/BaseUrl";
import DeviceSizeMixin from "../components/mixins/DeviceSizeMixin.vue";

export default {
  name: "App",
  store,
  mixins: [DeviceSizeMixin],
  data() {
    return {
      displayNav: true,
      expand: false,
      small: true,
      mini: false,
      right: null,
      url: ""
    };
  },
  computed: {

    /**
     * Defines the menu options to appear in the side navigation.
     */
    menuOptions() {
      let menuOptions = [];

      if (store.getters.isLoggedIn) {
        menuOptions = [
          {
            name: "Dashboard",
            icon: "dashboard",
            link: "/user/" + store.getters.getUser.id + "/dashboard"
          },
          {
            name: "Media",
            icon: "insert_photo",
            link: "/user/" + store.getters.getUser.id + "/media"
          },
          {
            name: "Profile",
            icon: "account_circle",
            link: "/user/" + store.getters.getUser.id + "/profile"
          },
          {
            name: "Users",
            icon: "supervised_user_circle",
            link: "/users"
          },
          { name: "User Groups",
            icon: "group",
            link: "/usergroups"
          }
        ];
        
        //Checking the media size to remove from Mobile
        if (!this.isExtraSmall && !this.isSmall) {
          menuOptions.push(
            {
              name: "Destination Map",
              icon: "map",
              link: "/destinations"
            }
          );
        }
        menuOptions.push(
          {
            name: "Destination List",
            icon: "list",
            link: "/user/" + store.getters.getUser.id + "/destinations"
          },
          {
            name: "My Trips",
            icon: "flight",
            link: "/user/" + store.getters.getUser.id + "/trips"
          }
        );
      }

      if (store.getters.getIsUserAdmin && store.getters.isLoggedIn) {
        menuOptions.push({
          name: "Admin Panel",
          icon: "playlist_add_check",
          link: "/admin_dash"
        });
      }

      return menuOptions;
    },

    traveller() {
      if (store.getters.getUser) {
        return store.getters.getUser;
      } else {
        return {"firstName":"", "lastName": ""};
      }
    },

    /**
     * Checks if the user is logged in.
     */
    loggedIn() {
      return store.getters.isLoggedIn;
    },

    /**
     * Checks for the size of the page and calls a function to set variables depending on the size.
     */
    isSmall() {
      if (this.windowSizes.width >= 1264) {
        this.largeWindow();
      }
      if (this.windowSizes.width < 1264) {
        this.smallWindow();
      }
      return true;
    },
  },

  watch: {
    traveller: function(newImage, oldImage) {
      if (this.loggedIn && newImage !== oldImage)
        if (
          !this.traveller.userProfilePhoto ||
          this.traveller.userProfilePhoto == "defaultPic.png"
        ) {
          this.url = DefaultPic;
        } else {
          this.url =
            base_url +
            "/api/travellers/profile-photo/" +
            this.traveller.id +
            "?" +
            new Date().getTime();
        }
    },

    /**
     * Watches expand variable binded to burger menu button and checks to see if the sidebar should be expanded when window is small.
     */
    expand: function(newExpand) {
      if (newExpand && this.small) {
        this.mini = false;
        this.displayNav = true;
      } else if (!newExpand && this.small) {
        this.mini = false;
        this.displayNav = false;
      }
    },

    /**
     * watches the displayNav attribute and resets expand and mini when window is enlarged
     */
    displayNav: function(newDisplayNav) {
      if (!newDisplayNav) {
        this.expand = false;
        this.mini = true;
      }
    }

  },

  methods: {
    /**
     * Executes the logout action and redirects the user to the login page.
     */
    logout() {
      store
        .dispatch("logout")
        .then(() => {
          this.$router.push("/login");
        })
        .catch(() => {
          this.$router.push("/login");
        });
    },

    /**
     * sets mini(sidebar) and small(window) to false and true respectively when window is resized to a smaller size.
     */
    smallWindow() {
      this.mini = false;
      this.small = true;
    },

    /**
     * sets small(window) and displayNav(sidebar) to false and true respectively when window is resized to a larger size.
     */
    largeWindow() {
      this.small = false;
      this.displayNav = true;
    }
  },

  created() {
    this.initialize;
    if (
      !this.traveller.userProfilePhoto ||
      this.traveller.userProfilePhoto == "defaultPic.png"
    ) {
      this.url = DefaultPic;
    } else {
      this.url =
        base_url +
        "/api/travellers/profile-photo/" +
        this.traveller.id +
        "?" +
        new Date().getTime();
    }
  }
};
</script>
