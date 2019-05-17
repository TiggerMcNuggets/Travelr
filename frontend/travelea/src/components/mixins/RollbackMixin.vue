
<script>
import RollbackManager from '../../tools/rollback/RollbackManager';
import { Types } from "../../tools/rollback/RollbackManager";

export default {
    data() {
        return {
            _rollbackManager: undefined,
            _previousBody: {}
        }
    },
    created() {
        this._rollbackManager = new RollbackManager();
        console.log("rollback mixin started");
    },
    methods: {
        _setPreviousBody: function(previousBody) {
            this._previousBody = {...previousBody};
        }, 
        /**
         * Array<Function> actions,contains a list of actions the component needs to perform to keep the frontend synced
         */
        _undo: async function(actions) {
            try {
                let res = await this._rollbackManager.undo();
                for (let action of actions) action();
            } catch(e) {
                console.error(e);
            }
        },

        _canUndo: function() {
            return this._rollbackManager.canUndo();
        },

    }
}
</script>
