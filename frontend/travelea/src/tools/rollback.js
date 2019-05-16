/**
 * Stack used for undoing and redoing actions
 */
class Stack {
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

    flush = () => {
        this.stack = [];
        this.pointer = -1;
    }

    undo = () => {
        if (!this.canUndo()) return;
        const reaction = this.stack[index].reaction;
        this.pointer -= 1;
    }

    redo = () => {
        if (!this.canRedo()) return;
        this.pointer += 1;
        const action = this.stack[index].action;
    }

    canUndo = () => {
        if (this.pointer === -1) return false;
        return true;
    }

    canRedo = () => {
        if (this.pointer + 1 === this.stack.length) return false;
        return true;
    }

    push = (action, reaction) => {
        this.pointer += 1;
        this.stack = this.stack.slice(0, this.pointer);
        this.stack.push({action, reaction});
    }

}