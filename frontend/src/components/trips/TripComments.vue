<template>
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex mt-3 mb-3>
        <h2>Comments ({{commentsLength}})</h2>
      </v-flex>

      <v-flex class="user-comment">
        <v-layout class="post-comment-container">
          <v-list-tile-avatar>
            <img :src="getProfileImageURL()">
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
              <img :src="getProfileImageURL(comment.profilePhoto, comment.userId)">
            </v-list-tile-avatar>
            <v-flex>
              <p>{{`${comment.userFirstName} ${comment.userLastName}`}}</p>
              <p class="sub-text">{{formatTimeStamp(comment.timestamp)}}</p>
            </v-flex>
            <v-icon color="red lighten-1" @click="deleteComment(comment.id)">delete</v-icon>
          </v-layout>
          <v-divider></v-divider>

          <v-flex>
            <p class="subtext">{{comment.comment}}</p>
          </v-flex>
        </v-card>
      </v-flex>
    </v-flex>

    <v-flex v-if="userComments.length < commentsLength">
      <v-progress-circular
        :indeterminate="loading"
        :rotate="0"
        :size="32"
        :value="0"
        :width="4"
        color="light-blue"
      ></v-progress-circular>
    </v-flex>

    <div ref="commentEnd"></div>
  </v-layout>
</template>

<script>
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";
import dateTime from "../common/dateTime/dateTime";

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
      loading: false,
      commentsLength: 0,
      page: 0,
      userComments: []
    };
  },

  computed: {},

  methods: {
    /**
     * Checks if the user has scrolled to the bottom of the comments.
     */
    bottomVisible() {
      if (this.$refs.commentEnd) {
        var rect = this.$refs.commentEnd.getBoundingClientRect();
        var elemTop = rect.top;
        var elemBottom = rect.bottom;

        // Element is in view on the screen:
        var isVisible = elemTop >= 0 && elemBottom <= window.innerHeight;
        return isVisible;
      }
      return false;
    },

    /**
     * Returns the timestamp as a formatted string
     */
    formatTimeStamp(timestamp) {
      return dateTime.convertTimestampToWordString(timestamp);
    },

    /**
     * Deletes a comment from the comment list.
     */
    deleteComment(commentId) {
      commentRepository
        .deleteComment(
          this.$store.getters.getUser.id,
          this.trip.trip.id,
          commentId
        )
        .then(() => {
          this.userComments = this.userComments.filter(comment => {
            if (comment.id != commentId) {
              return comment;
            }
          });
          this.commentsLength -= 1;
        });
    },

    /**
     * Get all user comments
     */
    getComments() {
      commentRepository
        .getComments(this.$store.getters.getUser.id, this.trip.trip.id, {
          page: this.page,
          comments: 5
        })
        .then(response => {
          this.commentsLength = response.data.commentsLength;
          this.userComments = this.userComments.concat(response.data.comments);
          this.loading = false;
          this.page += 1;
        });
    },

    /**
     * Calls API to post a users commment.
     */
    postComment() {
      let commentBody = { message: this.commentText };
      commentRepository
        .postComment(
          this.$store.getters.getUser.id,
          this.trip.trip.id,
          commentBody
        )
        .then(() => {
          this.commentText = "";
          this.page = 0;
          this.userComments = [];
        })
        .then(() => {
          this.getComments();
        });
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
  },

  watch: {
    trip: function() {
      this.page = 0;
      this.userComments = [];
      this.getComments();
    },
    loading: function(loading) {
      if (loading && this.userComments.length < this.commentsLength) {
        this.getComments();
      }
    }
  },

  mounted() {
    this.getComments();
    window.addEventListener("scroll", () => {
      this.loading = this.bottomVisible();
    });
  }
};
</script>
