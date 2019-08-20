<template>
  <v-flex xs12>
    <v-layout>
      <v-flex
        :class="isSelected ? 'usergroup-element usergroup-selected' : 'usergroup-element'"
        pa-2
        @click="selectUserGroup(usergroup)"
      >
        <div class="usergroup-element-details" d-flex justify-start align-center>
          <div>
            <strong>{{ usergroup.name }}</strong>
            <p>{{usergroup.members.length == 1 ? usergroup.members.length + ' member' : usergroup.members.length + ' members'}}</p>

            <p class="usergroup-element-details-description">{{usergroup.description}}</p>
          </div>
        </div>
        <div v-if="isUserAdminOrOwner">
          <v-btn icon flat small color="primary" @click="() => editDialogActive = true">
            <v-icon>edit</v-icon>
          </v-btn>
          <v-btn icon flat small color="error" @click="deleteUserGroup">
            <v-icon>delete</v-icon>
          </v-btn>
        </div>
      </v-flex>
    </v-layout>
    <v-divider class="no-margin"></v-divider>
    <v-dialog v-model="editDialogActive" width="500">
      <UpdateGroupForm
        :name="usergroup.name"
        :description="usergroup.name"
        :usergroupId="usergroup.id"
        :closeDialog="closeEditDialog"
        :rollbackCheckpoint="rollbackCheckpoint"
      />
    </v-dialog>
  </v-flex>
</template>

<script>
import UpdateGroupForm from "./UpdateGroupForm";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let usergroupRepository = RepositoryFactory.get("userGroup");

export default {
  components: {
    UpdateGroupForm
  },

  data() {
    return {
      editDialogActive: false
    };
  },

  props: {
    usergroup: Object,
    isSelected: Boolean,
    selectUserGroup: Function,
    updateUserGroups: Function,
    rollbackCheckpoint: Function,
    checkIfUserIsOwner: Function
  },

  computed: {
    isUserAdminOrOwner() {
      return (this.checkIfUserIsOwner(this.$store.getters.getUser.id, this.usergroup) || this.$store.getters.getIsUserAdmin)
    }
  },

  methods: {
    /**
     * Closes the dialog and updates the user group in case of updating.
     */
    closeEditDialog() {
      this.updateUserGroups();
      this.editDialogActive = false;
    },

    /**
     * Deletes the user group
     */
    deleteUserGroup() {
      const url =  `/users/${this.$store.getters.getUser.id}/group/${this.usergroup.id}/toggle_deleted`;
      usergroupRepository.deleteSingleUserGroup(
        this.$store.getters.getUser.id,
        this.usergroup.id
      ).then(() => {
        this.updateUserGroups();
        this.rollbackCheckpoint(
          "DELETE",
          {
              url: url
          },
          {
              url: url
          }
        );
      });
    }
  }
};
</script>

