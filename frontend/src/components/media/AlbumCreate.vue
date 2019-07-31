<template>
  <AlbumForm title="Create Album" :closeAlbumDialog="closeCreateAlbumDialog" :submitAlbum="createNewAlbum" />
</template>

<script>
  import AlbumForm from "./AlbumForm"
  export default {
    components: {
      AlbumForm
    },
    props: {
      closeCreateAlbumDialog: Function,
      createNewAlbum: Function
    },
  };
</script>
