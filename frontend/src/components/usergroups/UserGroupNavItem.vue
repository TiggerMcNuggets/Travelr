<template>
  <v-flex xs12>
    <v-layout>
      <div class="usergroup-element" @click="selectUserGroup(usergroup)">
        <div class="usergroup-element-details" d-flex justify-start align-center>
          <div>
            <strong>{{ usergroup.name }}</strong>
            <p>{{usergroup.description}}</p>
          </div>
        </div>

        <div>
          <v-checkbox disabled v-model="isSelected"></v-checkbox>
        </div>
      </div>
    </v-layout>
    <v-divider></v-divider>
  </v-flex>
</template>

<script>
export default {
  props: {
    usergroup: Object,
    isSelected: Boolean,
    selectUserGroup: Function
  }
};
</script>

