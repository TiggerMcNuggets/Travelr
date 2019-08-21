
<script>
import RollbackManager from "../../tools/rollback/RollbackManager";

/**
 * Mixin to be included anywhere that needs undo or redo functionatliy
 */
export default {
  data() {
    return {
      rollbackManager: undefined,
      rollbackPreviousBody: {}
    };
  },

  /**
   * Create a new rollback manager to interface between this mixin and both the rollback stack and worker
   */
  created() {
    this.rollbackManager = new RollbackManager();
  },
  methods: {
    /**
     * Adds a new checkpoint to the stack
     * @param {string} type The original action http method
     * @param {url: string, body: Object} actionBody The url and json body for the action request
     * @param {url: string, body: Object} reactionBody The url and json body for the reaction request
     */
    rollbackCheckpoint: function(type, actionBody, reactionBody) {
      this.rollbackManager.checkpoint(type, actionBody, reactionBody);
    },

    /**
     * Pushes payload to stack
     * @param payload The payload
     */
    pushStack: function(payload) {
      this.rollbackManager.pushStack(payload);
    },

    /**
     * Sets the previous body to be used for a future reaction
     * @param {Object} previousBody The object to set the previous body to
     */
    rollbackSetPreviousBody: function(previousBody) {
      this.rollbackPreviousBody = { ...previousBody };
    },
    /**
     * Calls undo and call a list of given functions to be performed afterwards
     * @param {Function[]} actions A list of functions to perform after the undo
     */
    rollbackUndo: async function(actions) {
      await this.rollbackManager.undo();
      for (let action of actions) await action();
    },

    /**
     * Calls redo and call a list of given functions to be performed afterwards
     * @param {Function[]} actions A list of functions to perform after redo
     */
    rollbackRedo: async function(actions) {
      await this.rollbackManager.redo();
      for (let action of actions) await action();
    }, 

    /**
     * Calls the rollback manager canUndo method to return whether there is something to undo in the stack
     * @return {boolean} true if there is an action to be undone, false if not
     */
    rollbackCanUndo: function() {
        return this.rollbackManager.canUndo();
    },

    /**
     * Calls the rollback manager canRedo method to return whether there is something to redo in the stack
     * @return {boolean} true if there is an action to be redone, false if not
     */
    rollbackCanRedo: function() {
        return this.rollbackManager.canRedo();
    },

    /**
     * TODO
     */
    rollbackFlush: function() {
        return this.rollbackManager.flush();
    }
  }
};
</script>
