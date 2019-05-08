/* eslint-disable */

<template>
    <v-container grid-list-xl>
        <v-layout row wrap>
            <v-flex xs12 sm5 md9>
                <userTable/>
            </v-flex>
            <v-flex xs12 sm5 md3>
                <searchFilter/>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<style>
</style>


<script>
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
    }
}
</script>
