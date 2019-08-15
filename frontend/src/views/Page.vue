<template>
  <v-app id="inspire">
    <v-toolbar fixed app clipped-right class="main-header">
      <router-link to="/" class="primary-logo">
        <v-toolbar-title class="fill-height toolbar toolbar-title">
          <img class="fill-height" src="../assets/logo2_white.png">
        </v-toolbar-title>
      </router-link>
    </v-toolbar>
    <v-navigation-drawer fixed app :mini-variant="mini" hide-overlay>
      <v-toolbar flat class="transparent">
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
        <v-list-tile @click.stop="mini = !mini">
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

.primary-logo {
  height: 50%;
}

.main-header {
  display: flex;
  justify-content: flex-end;
}

.fill-height {
  height: 100%;
}
</style>


<script>
import { store } from "../store/index";
import DefaultPic from "../assets/defaultPic.png";
import base_url from "../repository/BaseUrl";

export default {
  name: "App",
  store,
  data() {
    return {
      mini: true,
      right: null,
      url: ""
      // traveller: {}
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
          },
          { name: "Destination Map",
            icon: "map",
            link: "/destinations"
          },
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
        ];
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
      return store.getters.getUser;
    },

    /**
     * Checks if the user is logged in.
     */
    loggedIn() {
      return store.getters.isLoggedIn;
    }
  },

  watch: {
    traveller: function(newImage, oldImage) {
      if (newImage !== oldImage)
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
    }
  },

  created() {
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
