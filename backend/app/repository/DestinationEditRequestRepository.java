package repository;

import controllers.dto.Destination.CreateDestinationEditReq;
import cucumber.api.java.hu.De;
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
        return supplyAsync(() -> {
            return DestinationEditRequest.find.all();
        }, context);
    }

    public CompletableFuture<DestinationEditRequest> createRequest(CreateDestinationEditReq request, User user) {
        return supplyAsync(() -> {
            List<TravellerType> travellerTypes = new ArrayList<>();

            for(Long id : request.travellerTypeIds) {

                travellerTypes.add(TravellerType.find.byId(id));
            }

            Destination destination = Destination.find.byId(request.destinationId)

            DestinationEditRequest destinationEditRequest = new DestinationEditRequest();

            destinationEditRequest.user = user;
            destinationEditRequest.travellerTypes = travellerTypes;
            destinationEditRequest.destination = destination;

            destinationEditRequest.save();

            return destinationEditRequest.id;
        }, context);
    }


    public CompletableFuture<Boolean> acceptRequest(long id) {
        return supplyAsync(() -> {
            DestinationEditRequest req = DestinationEditRequest.find.byId(id);
            Destination destination = req.destination;

            destination.travellerTypes = req.travellerTypes;

            destination.save();

            return true;
        }, context);
    }

    public CompletableFuture<Boolean> deleteRequest(long id) {
        return supplyAsync(() -> {
            DestinationEditRequest req = DestinationEditRequest.find.byId(id);

            req.deletePermanent();

            return true;

        }, context);
    }

}
