import RollbackStack from "./RollbackStack";
import RollbackWorker from "./RollbackWorker"

export const Types = {
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE'
};

/**
 * Provides an interface for the mixin to interact with the rollback stack and worker
 */
export default class RollbackManager {

    /**
     * Constructor
     */
    constructor() {
        this.stack = new RollbackStack();
        this.rollbackWorker = new RollbackWorker();
    }

    /**
     * Pushes a checkpoint onto the stack
     * Uses RollbackWorker to convert type, actionBody and reactionBody into an action 
     * and reaction function and then pushes result onto the rollback stack 
     * @param {string} type The original action http method
     * @param {url: string, body: Object} actionBody The url and json body for the action request
     * @param {url: string, body: Object} reactionBody The url and json body for the reaction request
     */
    checkpoint(type, actionBody, reactionBody) {
        let response;
        switch(type) {
            case Types.POST:
                response = this.rollbackWorker.postDeleteActionReaction(actionBody, reactionBody);
                break;
            case Types.PUT:
                response = this.rollbackWorker.putActionReaction(actionBody, reactionBody);
                break;
            case Types.DELETE:
                response = this.rollbackWorker.postDeleteActionReaction(actionBody, reactionBody);
                break;
            default:
                return;
        }

        this.stack.push(response);
    }
 
    /** 
     * Calls the stack undo method and calls the resulting function
     */
    async undo() {
        console.log("stack: ", this.stack.stack);
        console.log("pointer: ", this.stack.pointer);
        const reaction = this.stack.undo();
        await reaction();
        console.log("stack after: ", this.stack.stack);
        console.log("pointer after: ", this.stack.pointer);
    }

    /**
     * Calls the stack redo method and calls the resulting function
     */
    async redo() {
        console.log("stack: ", this.stack.stack);
        console.log("pointer: ", this.stack.pointer);
        const action = this.stack.redo();
        await action();
        console.log("stack after: ", this.stack.stack);
        console.log("pointer after: ", this.stack.pointer);
    }

    /**
     * Calls the stack canUndo method to return whether there is something to undo in the stack
     * @return {boolean} true if there is an action to be undone, false if not
     */
    canUndo() {
        return this.stack.canUndo();
    }

    /**
     * Calls the stack canRedo method to return whether there is something to redo in the stack
     * @return {boolean} true if there is an action to be redone, false if not
     */
    canRedo() {
        return this.stack.canRedo();
    }

    flush() {
        return this.stack.flush();
    }
}