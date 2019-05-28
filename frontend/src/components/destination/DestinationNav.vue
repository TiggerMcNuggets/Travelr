<template>
  <v-card height="830px" flat>
    <v-layout row wrap>
      <v-flex xs12>
        <v-layout row>
          <v-flex col xs12>
            <v-flex mt-2 mb-2 pa-3 class="collapse-menu" @click="closeDestinationNav">
              <v-icon>arrow_back_ios</v-icon>Collapse Browse
            </v-flex>

            <v-tabs v-model="tab">
              <v-tab :key="1" class="dest-list-tabs">Private</v-tab>
              <v-tab :key="2" class="dest-list-tabs">Public</v-tab>
              <v-tab-item mt-2 :key="1">
                <v-flex mt-3>
                  <DestinationNavItem
                    class="destination-nav-item"
                    v-bind:key="index"
                    v-for="(destination, index) in privateDestinations"
                    :destination="destination"
                    @click.native="toggleDestination(destination)"
                    @dblclick="focusDestination(destination)"
                  />
                </v-flex>
              </v-tab-item>
              <v-tab-item mt-2 :key="2">
                <v-flex mt-3>
                  <DestinationNavItem
                    v-bind:key="index"
                    v-for="(destination, index) in publicDestinations"
                    v-bind:class="{showing: destination.isShowing}"
                    :destination="destination"
                    @click.native="toggleDestination(destination)"
                    @dblclick="focusDestination(destination)"
                  />
                </v-flex>
              </v-tab-item>
            </v-tabs>
          </v-flex>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<style>
.collapse-menu {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.collapse-menu:hover {
  background-color: rgba(0, 0, 0, 0.04);
  cursor: pointer;
}

.dest-list-tabs,
.dest-list-tabs div {
  width: 50%;
}


.destination-nav-item {
  cursor: pointer;
}
</style>


<script>
import DestinationNavItem from "./DestinationNavItem";

export default {
  components: {
    DestinationNavItem
  },
  data() {
    return {
      tab: 1,
      browseActive: false
    };
  },
  methods: {},

  props: {
    destinations: Array,
    focusDestination: Function,
    toggleDestination: Function,
    closeDestinationNav: Function
  },
  watch: {},

  computed: {
    privateDestinations() {
      return this.destinations.filter(x => !x.data.isPublic);
    },
    publicDestinations() {
      return this.destinations.filter(x => x.data.isPublic);
    }
  }
};
</script>

