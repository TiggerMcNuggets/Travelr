package repository;

import controllers.UserController;
import controllers.dto.User.CreateUserReq;
import controllers.dto.User.NationalityReq;
import controllers.dto.User.UpdateUserReq;
import finders.UserFinder;
import io.ebean.ExpressionList;
import models.Nationality;
import models.TravellerType;
import models.User;
import models.UserNationality;
import play.db.ebean.Transactional;
import utils.Moment;

import javax.inject.Inject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class UserRepository {

    private DatabaseExecutionContext context;

    private UserFinder userFinder = new UserFinder();

    @Inject
    public UserRepository(DatabaseExecutionContext context) {
        this.context = context;
    }

    /**
     * Gets user by their email
     * @param email the user email
     * @return completable future of user
     */
    public CompletableFuture<User> getUserByEmail(String email) {
        return supplyAsync(() -> userFinder.findByEmail(email), context);
    }

    /**
     * Gets all users
     * @return completable future of list of users
     */
    public CompletableFuture<List<User>> getAllUsers() {
        return supplyAsync(() -> userFinder.findAll(), context);
    }

    /**
     * Displays all travellers meeting the (optional) request query parameters
     *
     * @return CompletionStage<List<Traveller>>
     */
    @Transactional
    public CompletionStage<List<User>> getFilteredUsers(String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities, List<String> traveller_types, String orderBy) {
        return supplyAsync(() -> userFinder.findUsersByParams(fname, lname, gender, minAge, maxAge, nationalities, traveller_types, orderBy), context);
    };


    /**
     * Creates a new user
     * @param request the request DTO
     * @return completable future of new user id
     */
    public CompletableFuture<Long> createNewUser(CreateUserReq request) {
        return supplyAsync(() -> {
            // Insert user
            User user = new User(request);
            // int in Java defaults to 0
            user.accountType = request.accountType;
            user.insert();

            // Insert nationalities
            for(NationalityReq nationality: request.nationalities) {
                Nationality nationalityNew = Nationality.find.byId(nationality.id);
                UserNationality userNationality = new UserNationality(user, nationalityNew, nationality.hasPassport);
                userNationality.insert();
            }
           // user.travellerTypes = new ArrayList<TravellerType>();

            // Insert traveller types
            for(long i: request.travellerTypes) {
                System.out.println(TravellerType.find.byId(i));
                System.out.println(user.firstName);
                System.out.println(user.travellerTypes);
                user.travellerTypes.add(TravellerType.find.byId(i));
            }
            user.save();

            return user.id;
        }, context);
    }

    /**
     * Gets one user by id
     * @param id the user id
     * @return completable future of user
     */
    public CompletableFuture<User> getUser(Long id) {
        return supplyAsync(() -> userFinder.findById(id), context);
    }

    /**
     * Updates the details of a user that matches their header and the id
     * @param request the request DTO
     * @param id the user id
     * @return completable future of user id
     */
    public CompletableFuture<Long> updateUser(UpdateUserReq request, Long id) {
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

            for(NationalityReq nationality: request.nationalities) {
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
