
<template>
  <v-layout d-block>
    <div>
      <h1>Media</h1>
    </div>
    <v-layout row wrap>
      <v-flex xs2>

        <v-list>
          <v-list-item-group v-model="filter" color="primary">
            <v-list-item v-for="(filter, i) in filterCategories" :key="i">
              <v-list-item-content>
                <v-list-item-title v-html="filter.title"></v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-flex>

      <v-flex xs8>

      </v-flex>
    </v-layout>
  </v-layout>
</template>

<style>
/* @import "../assets/css/main.scss"; */

h1 {
  margin: 10px 0px;
}

.profile-feature {
  display: flex;
  justify-content: center;
  width: 100%;
}

.profile-feature h2 {
  text-align: center;
}
</style>


<script>
export default {
  data: () => ({
    filter: 0,
    filterCategories: [
      {
        title: 'All <span class="grey--text text--lighten-1">(12)</span>',
      },
      {
        title: 'Albums <span class="grey--text text--lighten-1">(2)</span>',
      },
      {
        title: 'Photos <span class="grey--text text--lighten-1">(9)</span>',
      },
      {
        title: 'Videos <span class="grey--text text--lighten-1">(3)</span>',
      }
    ],

  })



};
</script>
