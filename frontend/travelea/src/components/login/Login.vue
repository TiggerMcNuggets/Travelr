<template>
    <div class="width-for-container">
        <div class="form-container">
        
            <h1>Login</h1>
    
            <v-text-field
                    v-model="user.email"
                    label="Email"
            ></v-text-field>

            <v-text-field
                    v-model="user.password"
                    label="Password"
                    type="password"
            ></v-text-field>

            <v-btn class="login-button" large round v-on:click="login" color="primary">Login</v-btn>

            <v-alert :value="loginAlert" color="error">Incorrect email and/or password</v-alert>
        </div>
     </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.form-element {
    background-color: darkred;
}

.login-button {
    margin: 20px 0px;
}

.form-container {
    width: 50%;
    margin: 50px 0px;
    padding: 20px;
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
          loginAlert: false,
        };
      },
    methods: {
        login() {
            this.loginAlert = false;
            store.dispatch("login", this.user)
            .then(() => {
                console.log("valid login")
                this.$router.push("/profile");
            })
            .catch(() => {
                console.log("invalid login")
                this.loginAlert = true;
            })
        }
    }
}
</script>
