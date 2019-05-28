package repository;

import controllers.dto.Destination.CreateDestinationEditReq;
import controllers.dto.TravellerType.CreateTravellerTypeReq;
import models.Destination;
import models.DestinationEditRequest;
import models.TravellerType;
import models.User;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class DestinationEditRequestRepository {

    private DatabaseExecutionContext context;

    @Inject
    public DestinationEditRequestRepository(DatabaseExecutionContext context) { this.context = context; }


    public  CompletableFuture<List<DestinationEditRequest>> getAllEditRequests() {
        return supplyAsync(() -> DestinationEditRequest.find.all(), context);
    }

    public CompletableFuture<Long> createRequest(CreateDestinationEditReq request, User user) {
        return supplyAsync(() -> {
            List<TravellerType> travellerTypes = new ArrayList<>();

            if(request.travellerTypeIds != null) {
                for (CreateTravellerTypeReq createTravellerTypeReq : request.travellerTypeIds) {
                    travellerTypes.add(TravellerType.find.byId(createTravellerTypeReq.id));
                }
            }


            Destination destination = Destination.find.byId(request.destinationId);

            if (destination == null || user == null) {
                //TODO throw error from repository layer to controller
                return 0L;
            }


            DestinationEditRequest destinationEditRequest = new DestinationEditRequest();

            destinationEditRequest.setUser(user);
            destinationEditRequest.setTravellerTypes(travellerTypes);
            destinationEditRequest.setDestination(destination);

            destinationEditRequest.insert();

            return destinationEditRequest.id;


        }, context);
    }


    public CompletableFuture<Boolean> acceptRequest(long id) {
        return supplyAsync(() -> {
            DestinationEditRequest req = DestinationEditRequest.find.byId(id);

            //TODO Throw errors up from repository layer
            if(req == null) {
                return false;
            }

            if(req.getTravellerTypes() == null) {
                req.setTravellerTypes(new ArrayList<>());
            }

            Destination destination = req.getDestination();

            destination.getTravellerTypes().clear();

            destination.getTravellerTypes().addAll(req.getTravellerTypes());

            destination.update();

            req.deletePermanent();

            return true;
        }, context);
    }

    public CompletableFuture<Boolean> deleteRequest(long id) {
        return supplyAsync(() -> {
            DestinationEditRequest req = DestinationEditRequest.find.byId(id);

            //TODO Throw errors up from repository layer

            if(req == null) {
                return false;
            }

            req.deletePermanent();

            return true;

        }, context);
    }

}
