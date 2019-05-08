<template >
  <div
    class="side-menu-height map-side-menu-container"
    :class="{ 'side-menu-width': isShowingMenu, 'no-width': !isShowingMenu}"
  >
    <div v-ripple v-on:click="onMenuClick" class="blue-background side-menu-button-size">
      <v-icon color="white">drag_handle</v-icon>
    </div>
    <div v-if="isShowingMenu" style="max-height: 100%" class="side-menu-height scroll-y">
      <div style="margin-top: 15px">
        <h3>Destinations</h3>
        <v-btn v-on:click="selectAll" color="#3f51b5">
          <h6 style="color: white">Select All</h6>
        </v-btn>
      </div>
      <v-divider class="photo-header-divider"></v-divider>
      <div v-for="(item) in destinations" :value="item.value" :key="item.value">
        <v-checkbox
          v-model="selectedDestinations"
          :label="item.name"
          :value="item"
          @change="onCheckBoxChange"
        ></v-checkbox>
        <v-divider class="photo-header-divider"></v-divider>
      </div>
    </div>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

h2 {
  align-self: flex-end;
}

alligned-h3-text {
  align-self: center;
}

hr {
  margin-bottom: 25px;
}
.dest-name div {
  text-align: start;
}
.dest-sub-info p {
  margin-bottom: 0px;
  color: grey;
}

.dest-name button {
  margin-right: 20px;
}

.horizontal-details li {
  display: inline-block;
  padding-right: 20px;
  color: white;
}

.top-destination-content span {
  padding: 10px 20px;
}

.top-destination-content a {
  font-family: "Karla", sans-serif;
  font-size: 15px;
  margin-left: 10px;
}

ul {
  list-style: none;
}

.blue-background {
  background-color: #3f51b5;
  width: 15px;
}
.blue-background:hover {
  cursor: pointer;
}

.side-menu-button-size {
  height: 100%;
  width: 30px;
}
.side-menu-height {
  height: 100%;
  width: 100%;
}
.map-side-menu-container {
  width: 15%;
  display: flex;
  align-items: start;
  flex-direction: row;
}

.no-width {
  width: 0% !important;
}
</style>


<script>
export default {
  data() {
    return {
      offsetTop: 0,
      userId: this.$route.params.id,
      selectedDestinations: [],
      isShowingMenu: false
    };
  },
  props: {
    destinations: Array,
    updateDestinationsMarkers: Function
  },
  // child components
  components: {},
  methods: {
    /**
     * Updates the map markers based on checkboxes selected.
     */
    onCheckBoxChange() {
      this.updateDestinationsMarkers(this.selectedDestinations);
    },

    /**
     * Toggles the side selection menu.
     */
    onMenuClick() {
      this.isShowingMenu = !this.isShowingMenu;
    },

    /**
     * Scrolling for the side menu.
     * @param e The event that occured.
     */
    onScroll(e) {
      this.offsetTop = e.target.scrollTop;
    },

    /**
     * Checks and selects all of the destinations available to display on the map.
     */
    selectAll() {
      this.selectedDestinations = this.destinations;
      this.updateDestinationsMarkers(this.selectedDestinations);
    }
  },
  watch: {}
};
</script>
