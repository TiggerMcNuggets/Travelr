package repository;

import controllers.UserController;
import models.Nationality;
import models.TravellerType;
import models.User;
import models.UserNationality;

import javax.inject.Inject;


import java.util.List;
import java.util.Optional;
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

    public CompletableFuture<Long> createNewUser(UserController.CreateUserRequest request) {
        return supplyAsync(() -> {
            User user = new User(request);
            user.insert();
            for(UserController.NationalityRequest nationality: request.nationalities) {

                UserNationality userNationality = new UserNationality(user, Nationality.find.byId(nationality.id), nationality.hasPassport);

                userNationality.insert();
            }

            for(long i: request.travellerTypes) {
                user.travellerTypes.add(TravellerType.find.byId(i));
            }
            user.save();

            return user.id;
        }, context);
    }

    public CompletableFuture<Long> updateCurrentUser(UserController.UpdateUserRequest request, long userId) {
        return supplyAsync(() -> {
            User user = User.find.byId(userId);
            user.firstName = request.firstName;
            user.lastName = request.lastName;
            if (request.middleName != null) {
                user.middleName = request.middleName;
            }
            user.gender = request.gender;
            user.dateOfBirth = request.dateOfBirth;
            for(UserController.NationalityRequest nationality: request.nationalities) {


                Nationality tempNationality = Nationality.find.byId(nationality.id);
                UserNationality tempUserNationality;
                tempUserNationality = UserNationality.find.ByUserNationality(user, tempNationality);
                if (tempUserNationality == null) {
                    UserNationality userNationality = new UserNationality(user, Nationality.find.byId(nationality.id), nationality.hasPassport);
                    userNationality.insert();
                } else {
                    tempUserNationality = new UserNationality(user, Nationality.find.byId(nationality.id), nationality.hasPassport);
                    tempUserNationality.save();
                };
            }
            for(long i: request.travellerTypes) {
                if (!user.travellerTypes.contains(TravellerType.find.byId(i))) {
                    user.travellerTypes.add(TravellerType.find.byId(i));
                }
            }
            user.save();

            return user.id;
        });
    }
}
