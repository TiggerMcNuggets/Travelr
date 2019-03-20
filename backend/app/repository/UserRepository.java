package repository;

import controllers.UserController;
import models.Nationality;
import models.TravellerType;
import models.User;
import models.UserNationality;

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
        return supplyAsync(() -> User.find.all(), context);
    }

    public CompletableFuture<Long> addUser(User user) {
        return supplyAsync(() -> {
            user.insert();
            return user.id;
        }, context);
    }

    public CompletableFuture<Long> createNewUser(UserController.CreateUserRequest request) {
        return supplyAsync(() -> {
            User user = new User(request);

            for(UserController.NationalityRequest nationality: request.nationalities) {

                UserNationality userNationality = new UserNationality(user, Nationality.find.byId(nationality.id), nationality.hasPassport );
                userNationality.insert();

            }

            for(long i: request.travellerTypes) {
                user.travellerTypes.add(TravellerType.find.byId(i));
                user.save();
            }

            System.out.println(user.nationalities);
            return user.id;
        }, context);
    }
}
