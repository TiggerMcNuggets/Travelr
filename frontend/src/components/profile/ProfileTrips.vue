<template>
  <v-card>
    <trips></trips>
  </v-card>
</template>

<style>

aside {
  width: 25%;
  margin-right: 20px;
}

main {
  margin-left: 20px;
}

</style>


<script>
import Trips from "../trips/Trips";

export default {
  name: "Profile",

  data() {
    return {};
  },
  components: {
    Trips
  },
  methods: {}
};
</script>
