package repository;

import models.UserGroup;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserGroupRepository {

    private DatabaseExecutionContext context;

    @Inject
    public UserGroupRepository(DatabaseExecutionContext context) {
        this.context = context;
    }


    /**
     * Deletes a group member
     * @param groupId The group id
     * @param memberId The id of the group member to be removed
     * @return The id of the deleted group member otherwise null if not found.
     */
    public CompletableFuture<Long> remove(Long groupId, Long memberId) {
        return supplyAsync(() -> {
            UserGroup userGroup = UserGroup.find.query().where().eq("user_id", memberId).eq("group_id", groupId).findOne();
            if (userGroup != null) {
                userGroup.delete();
                return userGroup.user.getId();
            }
            return null;
        }, context);
    }




}
