import RollbackStack from "./RollbackStack";
import RollbackWorker from "./RollbackWorker"

export const Types = {
    PUT: 'PUT'
}

export default class RollbackManager {


    constructor() {
        this.stack = new RollbackStack();
        this.rollbackWorker = new RollbackWorker();
    }

    /**
     * 
     * @param string type 
     * @param {url: string, body: object} actionBody 
     * @param {url: string, body: object} reactionBody 
     */
    checkpoint(type, actionBody, reactionBody) {
        if (type === Types.PUT) {
            const response = 
                this.rollbackWorker.putActionReaction({
                    actionBody: actionBody,
                    reactionBody: reactionBody
                });
            this.stack.push(response);
        }
    }
 
    /** 
     * 
    */
    async undo() {
        const reaction = this.stack.undo();
        return await reaction();
    }

    async redo() {
        const action = this.stack.redo();
        return await action();
    }

    canUndo() {
        return this.stack.canUndo();
    }

    canRedo() {
        return this.stack.canRedo();
    }

}