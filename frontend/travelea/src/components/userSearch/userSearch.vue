/* eslint-disable */

<template>
    <v-container grid-list-xl>
        <h1>Hello World</h1>
        <v-layout row wrap>
        <v-flex xs12 sm5 md9>
            <userTable/>
        </v-flex>
      <v-flex xs8 sm5 md3>
            <searchFilter/>
      </v-flex>
    </v-layout>
    <li
    v-for="item in users"
    :value="item.value"
    :key="item.key"
    >
    <h1>{{ item.fname }}</h1>
    </li>
    </v-container>
</template>

<style>
</style>


<script>
import { store } from "../../store/index";
import userTable from "./userTable"
import searchFilter from "./searchFilter"

export default {
    data () {
        return {   
    }
    },
    components: {
        userTable,
        searchFilter
    },
    // the place where you want to make the store values readable
    computed: {
        users() {
        return store.state.users.users;
        }
    },
    created: async function() {
        store.commit("setUsers");
    }
}
</script>
