<template>
  <div class="textarea-emoji-picker">
    <picker
      class="emoji-picker"
      v-show="showEmojiPicker"
      title="Pick your emoji..."
      emoji="point_up"
      :include="['custom', 'people']"
      :sheetSize="32"
      :custom="customEmojis"
      @select="addEmoji"
      set="messenger"
    />

    <span
      class="emoji-trigger"
      :class="{ 'triggered': showEmojiPicker }"
      @mousedown.prevent="toggleEmojiPicker"
    >
      <svg style="width:20px;height:20px" viewBox="0 0 24 24">
        <path
          fill="#888888"
          d="M20,12A8,8 0 0,0 12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20A8,8 0 0,0 20,12M22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2A10,10 0 0,1 22,12M10,9.5C10,10.3 9.3,11 8.5,11C7.7,11 7,10.3 7,9.5C7,8.7 7.7,8 8.5,8C9.3,8 10,8.7 10,9.5M17,9.5C17,10.3 16.3,11 15.5,11C14.7,11 14,10.3 14,9.5C14,8.7 14.7,8 15.5,8C16.3,8 17,8.7 17,9.5M12,17.23C10.25,17.23 8.71,16.5 7.81,15.42L9.23,14C9.68,14.72 10.75,15.23 12,15.23C13.25,15.23 14.32,14.72 14.77,14L16.19,15.42C15.29,16.5 13.75,17.23 12,17.23Z"
        ></path>
      </svg>
    </span>
  </div>
</template>

<script>
import { Picker } from "emoji-mart-vue";
export default {
  components: { Picker },
  props: {
    commentId: Number,
    commentIndex: Number,
    sendEmojiForComment: Function
  },
  data() {
    return {
      showEmojiPicker: false,
      customEmojis: [
        {
          name: "Kappa",
          short_names: ["kappa"],
          text: "kappa",
          emoticons: [],
          keywords: ["kappa"],
          imageUrl:
            "https://www.dictionary.com/e/wp-content/uploads/2019/04/Screen-Shot-2019-07-05-at-12.20.52-PM.png"
        },
        {
          name: "Coppat",
          short_names: ["coppat"],
          text: "coppat",
          emoticons: [],
          keywords: ["coppat"],
          imageUrl: "https://i.imgur.com/nEwGDuQ.png"
        },
        {
          name: "Moffy",
          short_names: ["moffy"],
          text: "moffy",
          emoticons: [],
          keywords: ["moffy"],
          imageUrl: "https://i.imgur.com/x5o4ryo.png"
        },
        {
          name: "Fabby",
          short_names: ["fabby"],
          text: "fabby",
          emoticons: [],
          keywords: ["fabby"],
          imageUrl: "https://i.imgur.com/BUnwIv3.png"
        }
      ]
    };
  },
  methods: {
    toggleEmojiPicker() {
      this.showEmojiPicker = !this.showEmojiPicker;
    },
    addEmoji(emoji) {
      this.sendEmojiForComment(
        this.commentId,
        emoji.native || emoji.imageUrl,
        this.commentIndex
      );
      this.toggleEmojiPicker();
    }
  }
};
</script>

<style scoped>
.emoji-picker {
  z-index: 1;
  position: absolute;
  top: 40px;
  right: 10px;
}

.emoji-trigger {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  height: 20px;
}
</style>