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
        return supplyAsync(() -> User.find.query().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").findList(), context);
    }

    public CompletableFuture<Long> createNewUser(UserController.CreateUserRequest request) {
        return supplyAsync(() -> {
            // Insert user
            User user = new User(request);
            user.insert();

            // Insert nationalities
            for(UserController.NationalityRequest nationality: request.nationalities) {
                Nationality nationalityNew = Nationality.find.byId(nationality.id);
                UserNationality userNationality = new UserNationality(user, nationalityNew, nationality.hasPassport);
                userNationality.insert();
            }

            // Insert traveller types
            for(long i: request.travellerTypes) {
                user.travellerTypes.add(TravellerType.find.byId(i));
            }
            user.save();

            return user.id;
        }, context);
    }

    public CompletableFuture<User> getUser(Long id) {
        return supplyAsync(() -> User.find.query().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").where().eq("id", id).findOneOrEmpty().orElse(null), context);
    }

    public CompletableFuture<Long> updateUser(UserController.UpdateUserRequest request, Long id) {
        return supplyAsync(() -> {
            User user = User.find.byId(id);

            user.firstName = request.firstName;
            user.middleName = request.middleName;
            user.lastName = request.lastName;
            user.dateOfBirth = request.dateOfBirth;
            user.gender = request.gender;
            user.nationalities.clear();
            user.travellerTypes.clear();

            List<UserNationality> userNationalities = UserNationality.find.query().where().eq("user.id", id).findList();
            for (UserNationality un : userNationalities) {
                un.delete();
            }

            for(UserController.NationalityRequest nationality: request.nationalities) {
                Nationality nationalityNew = Nationality.find.byId(nationality.id);
                UserNationality userNationality = new UserNationality(user, nationalityNew, nationality.hasPassport);
                userNationality.insert();
                user.nationalities.add(userNationality);
            }

            for(long i: request.travellerTypes) {
                user.travellerTypes.add(TravellerType.find.byId(i));
            }

            user.save();
            return user.id;
        }, context);
    }
}
