<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex mt-3 mb-3>
        <h2>Comments ({{commentsLength}})</h2>
      </v-flex>

      <v-flex>
        <v-layout class="post-comment-container">
          <v-list-tile-avatar>
            <img :src="getProfileImageURL()">
          </v-list-tile-avatar>

          <v-layout>
            <emoji-picker v-model="commentText" />
            <v-btn icon @click="postComment">
              <v-icon color="primary lighten-1" :disabled="commentText.length < 1">send</v-icon>
            </v-btn>
          </v-layout>
        </v-layout>
      </v-flex>

      <v-flex mt-4 mb-2 v-for="(comment, index) in userComments" :key="comment.id" @mouseover="hoverIndex = index" @mouseout="hoverIndex = undefined">
        <v-card class="user-comment">
          <v-layout class="comment-header">
            <v-list-tile-avatar>
              <img :src="getProfileImageURL(comment.profilePhoto, comment.userId)">
            </v-list-tile-avatar>
            <v-flex>
              <p>{{`${comment.userFirstName} ${comment.userLastName}`}}</p>
              <p class="sub-text">{{formatTimeStamp(comment.timestamp)}}</p>
            </v-flex>
          </v-layout>
          <v-divider></v-divider>



          <div class=" d-flex justify-space-between">
            <p class="subtext">{{comment.comment}}</p>
            <v-icon class="justified-end" color="red lighten-1" @click="deleteComment(comment.id)">delete</v-icon>
          </div>


          <icon-emoji-picker
                  v-show="hoverIndex === index"
                  :commentId="comment.id"
                  :sendEmojiForComment="postCommentEmoji"/>
        </v-card>
        <div class="d-flex">
          <div v-for="(emoji, index) in comment.emojis" class="width-restriction" v-bind:key="index">
            
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <div v-on="on" class="comment-emoji-box hoverable">
                  <div class="d-flex align-center">
                    <h3>{{emoji.emoji}}</h3>
                    <h4>{{emoji.users.length}}</h4>
                  </div>
                </div>
              </template>
              <span>{{getListOfReactionAuthors(emoji.users)}}</span>
            </v-tooltip>

          </div>
        </div>
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


<style>
  .comment-emoji-box {
    background-color: #c5cae9;
    border-radius: 4px;
    padding: 3px;
    margin-right: 5px;
    max-width: 30px
  }
  .width-restriction {
    max-width: 35px !important;
  }

  .justified-end {
    justify-content: flex-end;
  }
</style>


<script>
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";
import dateTime from "../common/dateTime/dateTime";

import { RepositoryFactory } from "../../repository/RepositoryFactory";
let commentRepository = RepositoryFactory.get("comment");
import EmojiPicker from "../comment/emoji/EmojiPicker";
import IconEmojiPicker from "../comment/emoji/IconEmojiPicker";

export default {
  name: "TripComments",

  components: {
      EmojiPicker,
      IconEmojiPicker
  },

  props: {
    trip: Object
  },

  data() {
    return {
      hoverIndex: undefined,
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

    getListOfReactionAuthors(users) {
        let listOfNames = "";
        users.forEach((u, index) => {
            listOfNames += `${u}${index === (users.length - 1)  ? '' : ', '}`;
        });
        return listOfNames;
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

    postCommentEmoji(commentId, emoji) {
        const e = {
            "emoji": emoji
        };
        console.log(e);
        commentRepository.addEmojiToComment(
            this.$store.getters.getUser.id,
            this.trip.trip.id,
            commentId,
            e
        ).then(res => {
            console.log(res);
            this.getComments();
        } )
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
