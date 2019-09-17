<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-layout row wrap>
    <v-flex xs12 ma-2>
      <v-flex mt-3 mb-3>
        <h2>Comments (3)</h2>
      </v-flex>

      <v-flex>
        <v-layout class="post-comment-container">
          <v-list-tile-avatar>
            <img :src="getProfileImageURL()" />
          </v-list-tile-avatar>

          <v-layout>
            <emoji-picker v-model="commentText" />
            <v-btn icon @click="postComment">
              <v-icon color="primary lighten-1">send</v-icon>
            </v-btn>
          </v-layout>
        </v-layout>
      </v-flex>

      <v-flex mt-4 mb-2 v-for="comment in userComments" :key="comment.id">
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
          <icon-emoji-picker
                  :commentId="comment.id"
                  :sendEmojiForComment="postCommentEmoji"/>
        </v-card>
        <div class="d-flex">
          <div v-for="emoji in comment.emojis" class="width-restriction">
            
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
</style>


<script>
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";

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
      commentText: "",
      userComments: [
        {
          id: 1,
          userId: 2,
          firstName: "Bob",
          lastName: "Ross",
          comment: "The joy of painting.",
          profilePic: "defaultPic.png",
          date: "3 days ago",
          emojis: []
        },
        {
          id: 2,
          userId: 1,
          firstName: "Joe",
          lastName: "Bloggs",
          comment: "Some really awesome reply to the above comment.",
          profilePic: "defaultPic.png",
          date: "4 days ago",
          emojis: [{emoji: "🤤", users: ["Lorenzo Fasano", "Dana Lambert"]},{emoji: "🤤", users: ["Lorenzo Fasano", "Dana Lambert"]},{emoji: "🤤", users: ["Lorenzo Fasano", "Dana Lambert"]},{emoji: "🤤", users: ["Lorenzo Fasano", "Dana Lambert"]}, {emoji: "😡", users: ["Lorenzo Fasano"]}]
        },
        {
          id: 3,
          userId: 3,
          firstName: "Joe",
          lastName: "Bloggs",
          comment:
            "This comment is a placeholder to show some text in this area. Ran out of creativity.",
          profilePic: "defaultPic.png",
          date: "a week ago",
          emojis: [{emoji: "😡", users: ["Lorenzo Fasano"]}]
        }
      ]
    };
  },

  computed: {},

  methods: {

    getListOfReactionAuthors(users) {
        let listOfNames = "";
        users.forEach((u, index) => {
            listOfNames += `${u}${index === (users.length - 1)  ? '' : ', '}`;
        });
        return listOfNames;
    },

    postCommentEmoji(commentId, emoji) {
        console.log(emoji.toString());

        // TODO: uncomment when the GET request comes in
        // commentRepository.postCommentEmoji(commentId, emoji).then(res => console.log(res) )
    },

    /**
     * Calls API to post a users commment.
     */
    postComment() {
      let commentBody = {"message": this.commentText};
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
