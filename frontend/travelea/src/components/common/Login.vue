<template>
    <div class="width-for-container">
        <v-form ref="form">
           <v-container grid-list-xl text-xs-center>
              <v-layout justify-center row wrap align-center justify-space-around="true">
                 <v-flex xs12 md6>
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


                <v-btn large round v-on:click="attemptLogin" color="primary">Login</v-btn></v-flex>
              </v-layout>
           </v-container>
        </v-form>
     </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.width-for-container {
  width: 60%;
}
</style>

<script>

import {attemptLogin} from "../../repository/LoginRepository";
import {RepositoryFactory} from "../../repository/RepositoryFactory"

export default {

    data() {
        return {
          user: {}
        };
      },
    methods: {
        attemptLogin: function() {
            attemptLogin (this.user).then(() => {
                this.$refs.form.reset();
                })
        }
    }
}
</script>
