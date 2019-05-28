/**
 * Stack used for undoing and redoing actions
 */
export default class RollbackStack {
    /**
     * Constructor
     */
    constructor() {
        this.stack = [];
        this.pointer = -1;
    }

    /**
     * Resets the stack so there are no actions to undo or redo
     */
    flush = () => {
        this.stack = [];
        this.pointer = -1;
    };

    /**
     * Returns a reaction to undo original action
     * @return {Function} a function that undoes original action
     */
    undo = () => {
        if (!this.canUndo()) return;
        const reaction = this.stack[this.pointer].reaction;
        this.pointer -= 1;
        return reaction;

    };

    /**
     * Returns an action to redo original action
     * @return {Function} a function that redoes original action
     */
    redo = () => {
        if (!this.canRedo()) return;
        this.pointer += 1;
        return this.stack[this.pointer].action
    };

    /**
     * Checks if stack can be undone
     * @return {boolean} true if the stack can be undone, false if not
     */
    canUndo = () => {
        if (this.pointer === -1) return false;
        return true;
    };

    /**
     * Checks if stack can be redone
     * @return {boolean} true if the stack can be redone, false if not
     */
    canRedo = () => {
        return this.pointer + 1 !== this.stack.length;

    };

    /**
     * Adds an item to the stack
     * Replaces anything in front of the current position (pointer) with the new item
     * @param {action: Function, reaction: Function} item an object containing the action and reaction function
     */
    push = (item) => {
        this.pointer += 1;
        this.stack = this.stack.slice(0, this.pointer);
        this.stack.push(item);
    };
}