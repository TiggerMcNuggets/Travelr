<template>
  <!-- <v-hover v-slot:default="{ hover }">
    <v-card
      class="file-card"
      :elevation="hover ? 12 : 2"
    >
      <v-img
        height="100px"
        src="https://www.canterbury.ac.nz/engineering/contact-us/people/1485116691697_Moffat-Mathews-low.jpg"
      >
      </v-img>
      <v-card-actions
        class="align-end justify-end">
        <div>
          <h4>{{ file.name }}</h4>
        </div>

        <div class="crud-options">
          <v-tooltip top>
            <template v-slot:activator="{ on }">
              <v-btn icon dark v-on="on">
                <v-icon color="primary">picture_as_pdf</v-icon>
              </v-btn>
            </template>
            <span>Download trip pdf</span>
          </v-tooltip>
        </div>
      </v-card-actions>
    </v-card>
  </v-hover> -->

  <v-card>
    <v-img
      height="100px"
      src="https://www.canterbury.ac.nz/engineering/contact-us/people/1485116691697_Moffat-Mathews-low.jpg"
    >
    </v-img>
    <v-card-actions>
      <span class="file-name">{{file.name}}</span>
    </v-card-actions>
  </v-card>
</template>

<style>
.file-name {
  text-overflow: ellipsis !important;
  white-space: nowrap;
  overflow: hidden;
}
</style>

<script>
  export default {
    name: "UserFile",

    props: {
      file: Object
    },

    components: {},

    data() {
      return {};
    },

    methods: {},
  };
</script>
