

<template>
  <v-container fluid>
    <PageHeader
      title="User Search"
      :canRedo="rollbackCanRedo"
      :canUndo="rollbackCanUndo"
      :undo="undo"
      :redo="redo"
    />
    <v-layout row wrap>
      <v-flex xs12 sm8 md9>
        <userTable :search="search" :deleteUser="deleteUser" :isError="isError" />
      </v-flex>
      <v-flex xs12 sm4 md3>
        <v-flex xs12 pb-4 pa-3>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-flex>
        <searchFilter />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import userTable from "./userTable";
import searchFilter from "./searchFilter";
import PageHeader from "../common/header/PageHeader";
import RollbackMixin from "../mixins/RollbackMixin.vue";

export default {
  mixins: [RollbackMixin],
  data() {
    return {
      search: "",
      isError: false
    };
  },
  components: {
    userTable,
    searchFilter,
    PageHeader
  },

  methods: {
    /**
     * Updates the store with the users gotten from the backend
     */
    getUsers() {
      this.$store
        .dispatch("getUsers", false)
        .then(() => {
          return;
        })
        .catch(err => {
          console.error(err);
        });
    },

    /**
     * Takes in a users ID, Deletes the user then regets all users from the database into the this.$store.state
     * @param userId
     */
    async deleteUser(userId) {
      this.isError = false;
      try {
        await this.$store.dispatch("toggleUserDeleted", userId);
      } catch (err) {
        this.isError = true;
        console.error(err);
        return;
      }

      const url = `/travellers/${userId}/toggle_deleted`;
      this.rollbackCheckpoint(
        "DELETE",
        {
          url: url
        },
        {
          url: url
        }
      );

      this.getUsers();
    },

    /**
     * Undoes the last action and gets users afterwards
     */
    undo: function() {
      this.isError = false;
      const actions = [this.getUsers];
      this.rollbackUndo(actions);
    },

    /**
     * Redoes the last action and gets users afterwards
     */
    redo: function() {
      this.isError = false;
      const actions = [this.getUsers];
      this.rollbackRedo(actions);
    }
  },

  /**
   * Gets users on component creation and sets user administration status.
   */
  created: async function() {
    await this.$store.dispatch("getUsers", false);
    if (this.$store.getters.getIsUserAdmin) {
      this.isAdmin = true;
    }
  }
};
</script>
