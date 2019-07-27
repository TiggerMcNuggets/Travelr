<template>
    <v-card flat tile hover class="media-element">
        <!--<v-icon v-if="albumContent.is_public" class="lock-icon" left>lock_open</v-icon>-->
        <v-icon class="lock-icon" left>photo_library</v-icon>

        <!--<div v-if="albumContent.is_public" class="triangle"></div>-->
        <div class="triangle"></div>
        <div class="grid-4-container">
            <v-img class="box"
                    :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"
    </v-img>
            <v-img class="box"
                    :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"

            ></v-img>
            <v-img class="box"
                    :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"

            ></v-img>
            <v-img class="box"
                    :src="`https://unsplash.it/400/400?image=${Math.floor(Math.random() * 100) + 1}`"

            ></v-img>
        </div>
    </v-card>
</template>>

<style scoped>
    @import "../../assets/css/style.css";

    .grid-4-container {
        display: grid;
        grid-template-columns: 100px 100px 100px;
        grid-gap: 10px;
    }

    .box {
        color: #fff;
        border-radius: 5px;
        padding: 20px;
        font-size: 150%;
    }
</style>

<script>
  export default {
    name: "Album",

    props: {
      album: Object
    },

    data() {
      return {}
    },

    methods: {}
  }
</script>