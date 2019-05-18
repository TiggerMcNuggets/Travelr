import RollbackStack from '../../../src/tools/rollback/RollbackStack';

describe('Tests the rollback stack data structure', () => {

    let rollbackStack;
    // variables used only when inside behaviour is irrelevant (i.e. pure data structure logic)
    const mockAction = () => 'action';
    const mockReaction = () => 'reaction';
    const mockActionReaction = {action: mockAction, reaction: mockReaction};

    beforeEach(() => {
        rollbackStack = new RollbackStack();
    });

    it('ensures the stack is initialised as expected', () => {
        expect(rollbackStack.stack.length).toBe(0);
        expect(rollbackStack.pointer).toBe(-1);
    });

    it('ensures the stack is at initial state when flushed', () => {

        rollbackStack.push(mockActionReaction);
        rollbackStack.push(mockActionReaction);
        rollbackStack.flush();

        expect(rollbackStack.stack.length).toBe(0);
        expect(rollbackStack.pointer).toBe(-1);
    });


    /**
     * Successful undo: the reaction stored at the pointer place is returned, pointer is decreased, said reaction is returned
     */
    it('ensures when undo is called successfully, the data structure integrity is preserved', () => {

        rollbackStack.push(mockActionReaction);
        rollbackStack.push(mockActionReaction);
        expect(rollbackStack.pointer).toBe(1);

        const undoRes = rollbackStack.undo();
        expect(typeof undoRes === "function").toBe(true);
        expect(undoRes()).toBe('reaction');
        expect(rollbackStack.pointer).toBe(0);
        expect(rollbackStack.stack.length).toBe(2);
    });

    /**
     * Successful undo: the reaction stored at the pointer place is returned, pointer is decreased, said reaction is returned
     */
    it('ensures when undo is called successfully, the data structure integrity is preserved', () => {

        const undoRes = rollbackStack.undo();
        expect(undoRes === undefined).toBe(true);
        expect(rollbackStack.pointer).toBe(-1);
        expect(rollbackStack.stack.length).toBe(0);
    });
});
