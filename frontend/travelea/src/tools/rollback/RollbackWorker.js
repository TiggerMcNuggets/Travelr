import Repository from "../../repository/Repository";

/**
 * Provides methods to convert data from the manager to data to be put on the stack
 */
export default class RollbackWorker {
    
    /**
     * Converts an actionBody and reactionBody into and action put
     * request function and a reaction put request function
     * @param {url: string, body: Object} actionBody The url and json body for the action request
     * @param {url: string, body: Object} reactionBody The url and json body for the reaction request
     * @return {action: Function, reaction: Function} An object consisting of the function to undo and redo the action
     */
    putActionReaction(actionBody, reactionBody) {
        return {
            action: () => Repository.put(actionBody.url, actionBody.body),
            reaction: () => Repository.put(reactionBody.url, reactionBody.body)
        }
    }

    /**
     * Converts an actionBody and reactionBody into and action delete
     * request function and a reaction delete request function
     * @param {url: string, body: Object} actionBody The url and json body for the action request
     * @param {url: string, body: Object} reactionBody The url and json body for the reaction request
     * @return {action: Function, reaction: Function} An object consisting of the function to undo and redo the action
     */
    postDeleteActionReaction(actionBody, reactionBody) {
        return {
            action: () => Repository.put(actionBody.url),
            reaction: () => Repository.put(reactionBody.url)
        }
    }

}