import RollbackManager from '../../../src/tools/rollback/RollbackManager';


/**
 * NOTE: no need to test undo, redo, canUndo, canRedo for RollbackManager as same functionality is tested on the stack, these 2 methods
 * only provide a further level of abstraction but perform no change on the raw calls made on the stack
 */
describe('Tests the rollback stack data structure', () => {

    let rollbackManager;
    const actionBody = {url: 'action', body: {}};
    const reactionBody = {url: 'reaction', body: {}};

    beforeEach(() => {
        rollbackManager = new RollbackManager();
    });

    it('checks the stack is updated properly after performing a checkpoint', () => {
        rollbackManager.checkpoint('PUT', actionBody, reactionBody);
        expect(rollbackManager.stack.pointer).toBe(0);
        expect(rollbackManager.stack.stack.length).toBe(1);
        expect(rollbackManager.stack.canUndo()).toBe(true);
    });
});
