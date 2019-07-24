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

    <v-btn class="login-button" large round v-on:click="login" color="primary">Login</v-btn>

    <p>
      Don't have an account?
      <router-link to="/signup">Click here</router-link>to sign up
    </p>
    <v-alert :value="loginAlert" color="error">Incorrect email and/or password</v-alert>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.login-button {
  margin: 20px 0px;
}

.form-container {
  width: 50%;
  margin: 50px 0px;
}

h1 {
  margin: 50px 0px;
  font-size: 42px;
}

.width-for-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  vertical-align: middle;
}
</style>

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
        .then(() => {
          this.$router.push("/");
        })
        .catch(() => {
          console.log("invalid login");
          this.loginAlert = true;
        });
    },
    goToPasswordInput() {
      this.$refs.password_input.focus();
    }
  }
};
</script>
