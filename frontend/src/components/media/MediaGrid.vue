<template>
  <v-container v-bind="{ [`grid-list-xl`]: true }" fluid pt-2 pl-0 pr-0>
    <v-layout row wrap>
      <v-flex v-for="(item, index) in allowedToViewMedia" :key="index" xs12 sm6 md4 lg3 xl2>
        <AlbumElement
          v-if="item.content"
          :album="item"
          :getImgFromUrl="getImgFromUrl"
          :fillerImageURL="fillerImageURL"
          :getAllAlbums="getAllAlbums"
          :openEditAlbumDialog="openEditAlbumDialog"
          :openElement="() => openElement(item)"
        ></AlbumElement>
        <MediaElement
          v-else
          :media="item"
          :getImgFromUrl="getImgFromUrl"
          :fillerImageURL="fillerImageURL"
          :openElement="() => openElement(item)"
        ></MediaElement>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import AlbumElement from "./AlbumElement";
import MediaElement from "./MediaElement";
import base_url from "../../repository/BaseUrl";
import {store} from "../../store";

export default {
  name: "MediaGrid",
  store,
  props: {
    filteredMedia: Array,
    viewingAlbum: Boolean,
    openElement: Function,
    getAllAlbums: Function,
    openEditAlbumDialog: Function
  },

  components: {
    AlbumElement,
    MediaElement
  },

  data() {
    return {
      fillerImageURL:
        "https://scontent.fwlg1-1.fna.fbcdn.net/v/t1.15752-9/67378175_348500139423638_1827459196619063296_n.png?_nc_cat=110&_nc_oc=AQk_S4fCW-LL3HlYKvsLYrv3qvB1o1FSUcC7wRMsRw0PNysZF8yNa7EAsOalje3gDeg&_nc_ht=scontent.fwlg1-1.fna&oh=d80948deee840eaf09db4ad4b37ee3c4&oe=5DED9897"
    };
  },

  computed: {

    /**
     * Returns true if user is admin, owner or media is public
     */
    allowedToViewMedia() {
      return this.filteredMedia.filter((media) => {
        const isMyProfile = parseInt(store.getters.getUser.id) === parseInt(this.$route.params.id);
        const isAdminUser = store.getters.getIsUserAdmin;
        const publicMedia = media.content ? media.isPublic : media.is_public;
        return isMyProfile || isAdminUser || publicMedia;
      })
    }
  },

  methods: {

    /**
     * Gets the image src url for the server
     * @param filename The photo item to get the image for.
     * @returns {string} A url of where to find the photo to set as src
     */
    getImgFromUrl(filename) {
      return base_url + `/api/users/${this.$route.params.id}/media/` + filename;
    }
  }
};
</script>