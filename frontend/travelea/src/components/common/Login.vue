<template>
    <div class="width-for-container">
        <v-form ref="form">
          <v-container grid-list-xl v-if="showAddDestination">
            <v-layout align-center justify-center row fill-height/>
                <v-flex xs12 sm6 md3>
                    <v-text-field
                    v-model="user.email"
                    label="Email"
                    ></v-text-field>
                </v-flex>

                <v-flex xs12 sm6 md3>
                    <v-text-field
                    v-model="user.password"
                    label="Password"
                    ></v-text-field>
                </v-flex>

                <v-btn round v-on:click.login color="primary">Login</v-btn>
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
import {store} from "../../store/index";

export default {
    store,
    data() {
        return {
          destination: {}
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