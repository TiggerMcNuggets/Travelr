<template>
  <v-card>
    <trips></trips>
  </v-card>
</template>

<style>
.trips-tile {
  margin-left: 20px;
}

.destinations-tile {
  margin-top: 20px;
  height: 290px;
}

.photos-tile {
  height: 300px;
}

aside {
  width: 25%;
  margin-right: 20px;
}

main {
  margin-left: 20px;
}

.profile-inner-container {
  width: 100%;
  margin: 20px;
}

.profile-outer-container {
  text-align: center;
  display: flex;
  justify-content: center;
}

.profile-main {
  width: 100%;
  margin: 0px;
  text-align: left;
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
