package repository;

import models.User;

import javax.inject.Inject;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;


public class UserRepository {

    private DatabaseExecutionContext context;

    @Inject
    public UserRepository(DatabaseExecutionContext context) {

        this.context = context;
    }


    public CompletableFuture<List<User>> getAllUsers() {
        return supplyAsync(() -> {
            return User.find.all();
        }, context);
    }

    public CompletableFuture<Long> addUser(User user) {
        return supplyAsync(() -> {
            user.insert();
            return user.id;
        }, context);
    }
}
