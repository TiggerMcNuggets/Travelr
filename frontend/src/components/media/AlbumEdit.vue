<template>
  <AlbumForm title="Edit Album" :album="album" :closeAlbumDialog="closeEditAlbumDialog" :submitAlbum="editAlbumHandler"/>
</template>
 
<script>
  import AlbumForm from "./AlbumForm"
  export default {
    props: {
      closeEditAlbumDialog: Function,
      editAlbum: Function,
      album: Object,
    },
    components: {
      AlbumForm
    },
    methods: {
      /**
       * Calls the edit album method that eventually sends an update album put request
       * @param name The album name
       */
      editAlbumHandler(name) {
        this.editAlbum(name, this.album.id);
      }
    }
  };
</script>