<template>
    <div class="overlay">
        <v-container fill-height grid-list-md text-xs-center>
            <v-layout row wrap align-center>
                <v-flex>
                    <h1 class="display-4">Travelr</h1>
                    <v-btn round large color="primary" @click="$router.push('/login')">Login</v-btn>
                    <v-btn round large color="primary" @click="$router.push('/signup')">Signup</v-btn>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<style>

.overlay{
    position:fixed;
    width:100%;
    height:100%;
    top:0px;
    left:0px;
    background-image: url("https://farm9.staticflickr.com/8629/16141602477_82d6d91a2d_b.jpg");
    filter: grayscale(50%);
    background-size:100% 100%;
    background-repeat: no-repeat;
}


</style>

<script>
export default {
  name: "Home",

  data() {
    return {};
  },
};

</script>