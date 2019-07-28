<template>
  <v-flex xs12>
    <v-card flat>
      <v-layout v-bind:class="{showing: destination.isShowing}">
        <div class="dest-element">
          <div class="dest-element-details" d-flex justify-start align-center>
            <div>
              <div class="dest-small-img-container">
                <img
                  class="dest-small-image"
                  src="https://perkinsdesigns.files.wordpress.com/2015/11/jan15_mountains_galore3.png"
                />
              </div>
            </div>
            <div>
              <strong>{{ destination.data.name }}</strong>
              <br />
              <span class="caption">{{ destination.data.district}}, {{ destination.data.country }}</span>
            </div>
          </div>

          <div>
            <v-checkbox disabled v-model="destination.isShowing"></v-checkbox>
          </div>
        </div>
      </v-layout>
      <v-divider></v-divider>
    </v-card>
  </v-flex>
</template>

<style>
.dest-element {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.dest-element-details {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.dest-small-image {
  height: 30px !important;
}

.dest-small-img-container {
  height: 30px !important;
  width: 30px !important;
  overflow: hidden !important;
  border-radius: 30px !important;
  margin: 0px 10px !important;
}

.dest-small-img-container-container {
  width: 30px !important;
}
</style>

<script>
export default {
  data() {
    return {
      checkbox: false
    };
  },

  props: {
    destination: Object
  },

  methods: {}
};
</script>

