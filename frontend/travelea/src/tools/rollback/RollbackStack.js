/**
 * Stack used for undoing and redoing actions
 */
export default class RollbackStack {
    constructor() {
        /**
         * [(
         *  action: function,
         *  reaction: function
         * )]
         */
        this.stack = [];
        this.pointer = -1;
    };

    /**
     * Resets the stack so there are no actions to undo or redo
     */
    flush = () => {
        this.stack = [];
        this.pointer = -1;
    } 

    /**
     * Returns / Performs a reaction to undo original action
     * Gets current item's reaction and then decreases pointer
     */
    undo = () => {
        if (!this.canUndo()) return;
        const reaction = this.stack[this.pointer].reaction;
        this.pointer -= 1;
        return reaction;

    }

    /**
     * Returns / Performs an action to redo orgininal action
     * Increases pointer first and then gets reaction
     */
    redo = () => {
        if (!this.canRedo()) return;
        this.pointer += 1;
        const action = this.stack[this.pointer].action;
        return action
    }

    /**
     * Checks if stack can be undone
     * Checks if pointer is -1 (original pointer value not pointing to any reaction)
     * @return true if the stack can be undone, false if not
     */
    canUndo = () => {
        if (this.pointer === -1) return false;
        return true;
    }

    /**
     * Checks if stack can be redone
     * Checks if 
     */
    canRedo = () => {
        if (this.pointer + 1 === this.stack.length) return false;
        return true;
    }

    push = (item) => {
        this.pointer += 1;
        this.stack = this.stack.slice(0, this.pointer);
        this.stack.push(item);
    }

}