
<script>
import RollbackManager from '../../tools/rollback/RollbackManager';
import { Types } from "../../tools/rollback/RollbackManager";

/**
 * Mixin to be included anywhere that needs undo or redo functionatliy
 */
export default {
    data() {
        return {
            mRollbackManager: undefined,
            mPreviousBody: {},
        }
    },

    /**
     * Create a new rollback manager to interface between this mixin and both the rollback stack and worker
     */
    created() {
        this.mRollbackManager = new RollbackManager();
    },
    methods: {
        /**
         * Adds a new checkpoint to the stack
         * @param {}
         */
        mCheckpoint: function(type, actionBody, reactionBody) {
            this.mRollbackManager.checkpoint(type, actionBody, reactionBody);
        },

        mSetPreviousBody: function(previousBody) {
            this.mPreviousBody = {...previousBody};
        }, 
        /**
         * Array<Function> actions,contains a list of actions the component needs to perform to keep the frontend synced
         */
        mUndo: async function(actions) {
            try {
                let res = await this.mRollbackManager.undo();
                for (let action of actions) action();
            } catch(e) {
                console.error(e);
            }
        },

        mRedo: async function(actions) {
            try {
                let res = await this.mRollbackManager.redo();
                for (let action of actions) action();
            } catch (e) {
                console.error(e);
            }
        },

        mCanUndo: function() {
            return this.mRollbackManager.canUndo();
        },

        mCanRedo: function() {
            return this.mRollbackManager.canRedo();
        }
    }
}
</script>
