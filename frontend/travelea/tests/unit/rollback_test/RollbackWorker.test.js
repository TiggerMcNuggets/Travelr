import RollbackWorker from '../../../src/tools/rollback/RollbackWorker';

describe('Tests the putActionReaction function', () => {

    const manager = new RollbackWorker();

    it('Returns an object containing action and reaction properties which are functions', () => {
        const actionBody = {url: 'action', body: {}};
        const reactionBody = {url: 'reaction', body: {}};
        const putActionReactionRes = manager.putActionReaction(actionBody, reactionBody);
        expect(typeof putActionReactionRes.action === "function").toBe(true);
        expect(typeof putActionReactionRes.reaction === "function").toBe(true);

    })
});