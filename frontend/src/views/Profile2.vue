
<template>
  <v-layout d-block>
    <div>
      <h1>Profile</h1>
      <h4>Joined</h4>
    </div>

    <div class="profile-feature" d-flex justify-center>
      <div>
        <v-avatar size="250">
          <img src="https://randomuser.me/api/portraits/men/45.jpg" />
        </v-avatar>
        <h2 headline>Person Name</h2>
      </div>
    </div>

    <v-layout>
      <v-flex> <div class="field-section">
        <ul class="chip-list">
          <li v-for="type in nationalities" :value="type.value" :key="type.value">
            <div v-if="!type.old">
              <v-chip class="nationality">{{type.name}}</v-chip>
            </div>
            <div v-else>
              <v-chip color="amber lighten-3" class="nationality">
                {{type.name}}
                <v-tooltip top>
                  <template v-slot:activator="{ on }">
                    <v-avatar v-on="on">
                      <v-icon color="gray" right>info</v-icon>
                    </v-avatar>
                  </template>
                  <span>Outdated Nationality</span>
                </v-tooltip>
              </v-chip>
            </div>
          </li>
        </ul>
        <p class="caption font-weight-light field-title">NATIONALITIES</p>
      </div>
      <v-divider></v-divider></v-flex>
      <v-flex></v-flex>
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
export default {};
</script>
