

<template>
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex mt-3 mb-3>
        <h2>Comments (3)</h2>
      </v-flex>

      <v-layout>
        <v-list-tile-avatar>
          <img :src="getProfileImageURL()">
        </v-list-tile-avatar>
        <v-textarea name="input-7-4" placeholder="Add your thoughts..."></v-textarea>
      </v-layout>

      <v-flex mt-2 mb-2 v-for="comment in userComments" :key="comment.id">
        <v-layout align-text-center>
          <v-list-tile-avatar>
            <img :src="getProfileImageURL(comment.profilePic)">
          </v-list-tile-avatar>
          <p>{{`${comment.firstName} ${comment.lastName}`}}</p>
        </v-layout>
        <v-divider></v-divider>

        <v-flex>
          <p class="subtext">{{comment.comment}}</p>
        </v-flex>
      </v-flex>
    </v-flex>
  </v-layout>
</template>

<script>
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";

export default {
  name: "TripComments",

  props: {},

  data() {
    return {
      userComments: [
        {
          id: 1,
          userId: 2,
          firstName: "Bob",
          lastName: "Ross",
          comment: "The joy of painting.",
          profilePic: "defaultPic.png",
          date: "3 days ago"
        },
        {
          id: 2,
          userId: 1,
          firstName: "Joe",
          lastName: "Bloggs",
          comment: "Some really awesome reply to the above comment.",
          profilePic: "defaultPic.png",
          date: "4 days ago"
        },
        {
          id: 3,
          userId: 3,
          firstName: "Joe",
          lastName: "Bloggs",
          comment:
            "This comment is a placeholder to show some text in this area. Ran out of creativity.",
          profilePic: "defaultPic.png",
          date: "a week ago"
        }
      ]
    };
  },

  computed: {},

  methods: {
    getProfileImageURL(userProfilePhoto, userId) {
      userId = userId ? userId : this.$store.getters.getUser.id;
      userProfilePhoto = userProfilePhoto
        ? userProfilePhoto
        : this.$store.getters.getUser.userProfilePhoto;
      if (!userProfilePhoto || userProfilePhoto == "defaultPic.png") {
        return DefaultPic;
      } else {
        return base_url + "/api/travellers/profile-photo/" + userId;
      }
    }
  }
};
</script>
