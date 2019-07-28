<template>
  <div>
    <v-text-field
      ref="email_input"
      v-model="user.email"
      v-on:keyup.enter="goToPasswordInput"
      label="Email"
    ></v-text-field>

    <v-text-field
      ref="password_input"
      v-model="user.password"
      v-on:keyup.enter="login"
      label="Password"
      type="password"
    ></v-text-field>

    <v-alert :value="loginAlert" color="error">Incorrect email and/or password</v-alert>

    <v-btn class="login-button" large v-on:click="login" color="error">Login</v-btn>
    <router-link class="no-underline" to="/signup">
      <v-btn class="login-button" large outline color="error">Sign up</v-btn>
    </router-link>
  </div>
</template>

<script>
import { store } from "../../store/index";

export default {
  store,

  data() {
    return {
      user: {},
      loggedIn: false,
      loginAlert: false
    };
  },

  methods: {
    /** Allows the user to login. This calls a function which validates the user data and if valid sets it as the
     * signed in user within the store as well as the returned auth token. This is used for user validation within the
     * app. Redirects the user to their profile page on success.
     * If the user has incorrect credentials an error alert is displayed.
     */
    login() {
      this.loginAlert = false;
      store
        .dispatch("login", this.user)
        .then(res => {
          this.$router.push(`/user/${res.data.id}`);
        })
        .catch(() => {
          this.loginAlert = true;
        });
    },
    goToPasswordInput() {
      this.$refs.password_input.focus();
    }
  }
};
</script>
