/* eslint-disable */

<template>
    <v-container grid-list-xl>
        <h1>Hello World</h1>
        <v-layout row wrap>
        <v-flex xs12 sm5 md9>
            <profileTable/>
        </v-flex>
      <v-flex xs8 sm5 md3>
            <profileFilter/>
      </v-flex>
    </v-layout>
    </v-container>
</template>

<style>
</style>


<script>

import profileTable from "./profileTable"
import profileFilter from "./searchFilter"

export default {
    data () {
        return {   
    }
    },
    components: {
        profileTable,
        profileFilter
    }
}
</script>
