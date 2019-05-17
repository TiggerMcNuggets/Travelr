import Repository from "../../repository/Repository";

export default class RollbackWorker {
    
    /**
     * 
     * @param {action: function, reaction: function} body 
     */
    putActionReaction(body) {
        const actionBody = body.actionBody;
        const reactionBody = body.reactionBody;
        return {
            action: () => Repository.put(actionBody.url, actionBody.body),
            reaction: () => Repository.put(reactionBody.url, reactionBody.body)
        }
    }
}