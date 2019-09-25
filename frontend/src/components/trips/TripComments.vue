<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-layout row wrap justify-content-center>
    <v-flex xs12 ma-2>
      <v-flex v-if="selectedTrip && selectedTrip.trip.usergroup.length !== 0" mt-3 mb-3>
        <h2>Comments ({{commentsLength}})</h2>
      </v-flex>
      <v-flex v-else>
        <h2>Comments</h2>
        <p>Please add a group first.</p>
      </v-flex>

      <v-flex v-if="selectedTrip && selectedTrip.trip.usergroup.length !== 0">
        <v-layout class="post-comment-container">
          <v-list-tile-avatar>
            <img :src="getProfileImageURL()" />
          </v-list-tile-avatar>

          <v-layout>
            <emoji-picker v-model="commentText" />
            <v-btn icon @click="postComment">
              <v-icon color="primary lighten-1" :disabled="commentText.length < 1">send</v-icon>
            </v-btn>
          </v-layout>
        </v-layout>
      </v-flex>

      <div id="scrollableComments" class="scrollable-y">
        <v-flex
          mt-4
          mb-2
          v-for="(comment, commentIndex) in userComments"
          :key="comment.id"
          @mouseover="hoverIndex = commentIndex"
          @mouseout="hoverIndex = undefined"
        >
          <v-card class="user-comment">
            <v-layout class="comment-header">
              <v-list-tile-avatar>
                <img :src="getProfileImageURL(comment.profilePhoto, comment.userId)" />
              </v-list-tile-avatar>
              <v-flex>
                <p>{{`${comment.userFirstName} ${comment.userLastName}`}}</p>
                <p class="sub-text">{{formatTimeStamp(comment.timestamp)}}</p>
              </v-flex>
            </v-layout>
            <v-divider></v-divider>

            <div class="d-flex justify-space-between">
              <p class="subtext">{{comment.comment}}</p>
              <v-icon
                class="justified-end"
                color="red lighten-1"
                @click="deleteComment(comment.id)"
              >delete</v-icon>
            </div>

            <icon-emoji-picker
              v-show="hoverIndex === commentIndex"
              :commentId="comment.id"
              :commentIndex="commentIndex"
              :sendEmojiForComment="reactWithCommentEmoji"
            />
          </v-card>
          <div class="d-flex">
            <div
              v-for="(emoji, index) in comment.emojis"
              class="width-restriction"
              v-bind:key="index"
            >
              <v-tooltip top>
                <template v-slot:activator="{ on }">
                  <div v-on="on" class="comment-emoji-box hoverable">
                    <div
                      v-on:click="() => reactWithCommentEmoji(comment.id, emoji.emoji, commentIndex)"
                      class="d-flex align-center"
                    >
                      <h3 v-if="emoji.emoji.length < 10">{{emoji.emoji}}</h3>
                      <img v-else :src="emoji.emoji" width="24" height="24" />
                      <h4>{{emoji.users.length}}</h4>
                    </div>
                  </div>
                </template>
                <span>{{getListOfReactionAuthors(emoji.users)}}</span>
              </v-tooltip>
            </div>
          </div>
        </v-flex>
        <v-progress-circular indeterminate v-if="loading" color="primary" small></v-progress-circular>
      </div>
    </v-flex>
  </v-layout>
</template>


<style>
.comment-emoji-box {
  border: 1px solid #c5cae9;
  border-radius: 4px;
  padding: 3px;
  margin-right: 5px;
  max-width: 50px;
}

.comment-emoji-box:hover {
  background-color: #ced3f0;
}

.width-restriction {
  max-width: 50px !important;
}

.justified-end {
  justify-content: flex-end;
}
</style>


<script>
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";
import dateTime from "../common/dateTime/dateTime";
import StoreTripsMixin from "../mixins/StoreTripsMixin";

import { RepositoryFactory } from "../../repository/RepositoryFactory";
let commentRepository = RepositoryFactory.get("comment");
import EmojiPicker from "../comment/emoji/EmojiPicker";
import IconEmojiPicker from "../comment/emoji/IconEmojiPicker";

import { deepCopy } from "../../tools/deepCopy";

export default {
  name: "TripComments",

  mixins: [StoreTripsMixin],
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

  computed: {
    isAdminOrOwner() {
      let isOwner = false;
      if (this.selectedTrip) {
        this.selectedTrip.trip.usergroup.forEach(user => {
          if (this.$store.getters.getUser.id === user.userId) {
            isOwner = user.owner;
          }
        });
      }
      return isOwner || this.$store.getters.getIsUserAdmin;
    }
  },

  methods: {
    /**
     * Checks if the user has scrolled to the bottom of the comments.
     */
    bottomVisible() {
      let myDiv = document.getElementById("scrollableComments");
      return myDiv.offsetHeight + myDiv.scrollTop >= myDiv.scrollHeight;
    },

    getListOfReactionAuthors(users) {
      let listOfNames = "";
      users.forEach((u, index) => {
        listOfNames += `${u.firstName} ${u.lastName}${
          index === users.length - 1 ? "" : ", "
        }`;
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
          this.selectedTrip.trip.id,
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
        .getComments(
          this.$store.getters.getUser.id,
          this.selectedTrip.trip.id,
          {
            page: this.page,
            comments: 5
          }
        )
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
          this.selectedTrip.trip.id,
          commentBody
        )
        .then(() => {
          this.commentText = "";
          this.page = 0;
          this.userComments = [];
          this.getComments();
        });
    },

    reactWithCommentEmoji(commentId, emoji, postIndex) {
      const e = { emoji: emoji };
      commentRepository
        .addEmojiToComment(
          this.$store.getters.getUser.id,
          this.trip.trip.id,
          commentId,
          e
        )
        .then(() => {
          return commentRepository.getComment(
            this.$store.getters.getUser.id,
            this.trip.trip.id,
            commentId
          );
        })
        .then(res => {
          let commentsCopy = deepCopy(this.userComments);
          commentsCopy[postIndex] = res.data;
          this.userComments = commentsCopy;
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
    selectedTrip: function(oldTrip, newTrip) {
      if (oldTrip.trip.id !== newTrip.trip.id) {
        this.page = 0;
        this.userComments = [];
        this.getComments();
      }
    },
    loading: function(loading) {
      if (loading && this.userComments.length < this.commentsLength) {
        this.getComments();
      }
    }
  },

  mounted() {
    this.getComments();
    document
      .getElementById("scrollableComments")
      .addEventListener("scroll", () => {
        this.loading = this.bottomVisible();
      });
  }
};
</script>
