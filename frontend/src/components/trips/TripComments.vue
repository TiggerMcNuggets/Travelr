

<template>
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex mt-3 mb-3>
        <h2>Comments (3)</h2>
      </v-flex>

      <v-flex class="user-comment">
        <v-layout class="post-comment-container">
          <v-list-tile-avatar>
            <img :src="getProfileImageURL()" />
          </v-list-tile-avatar>

          <v-layout>
            <v-textarea name="input-6-2" v-model="commentText" placeholder="Add your thoughts..."></v-textarea>
            <v-btn icon @click="postComment">
              <v-icon color="primary lighten-1">send</v-icon>
            </v-btn>
          </v-layout>
        </v-layout>
      </v-flex>

      <v-flex mt-2 mb-2 v-for="comment in userComments" :key="comment.id">
        <v-card class="user-comment">
          <v-layout class="comment-header">
            <v-list-tile-avatar>
              <img :src="getProfileImageURL(comment.profilePic)" />
            </v-list-tile-avatar>
            <p>{{`${comment.firstName} ${comment.lastName}`}}</p>
          </v-layout>
          <v-divider></v-divider>

          <v-flex>
            <p class="subtext">{{comment.comment}}</p>
          </v-flex>
        </v-card>
      </v-flex>
    </v-flex>
  </v-layout>
</template>

<script>
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";

import { RepositoryFactory } from "../../repository/RepositoryFactory";
let commentRepository = RepositoryFactory.get("comment");

export default {
  name: "TripComments",

  props: {
    trip: Object
  },

  data() {
    return {
      commentText: "",
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

    /**
     * Calls API to post a users commment.
     */
    postComment() {
      let commentBody = {"message": this.commentText}
      commentRepository.postComment(this.$store.getters.getUser.id, this.trip.trip.id, commentBody).then(response => console.log(response))
    },

    /**
     * Returns a users profile image file given the profile photo filename and user id.
     */
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
