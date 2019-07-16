package service;

import dto.trip.TripDTO;
import dto.trip.TripDestinationDTO;
import models.Trip;
import models.TripDestination;
import models.User;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A service which holds the business logic relating to Trips
 */
public class TripService {

    private DatabaseExecutionContext context;

    @Inject
    public TripService(DatabaseExecutionContext context) { this.context = context; }

    public CompletableFuture<Trip> createTrip(TripDTO tripDTO, User user) {

        return supplyAsync(() -> {
            Trip trip = new Trip(tripDTO.name, tripDTO.description, user);

            List<TripDestination> tripDestinationList = new ArrayList<>();

            for(TripDestinationDTO dto : tripDTO.destinations) {
                tripDestinationList.addAll(FetchAllTripDestinations(dto, null));
            }

            for(TripDestination td : tripDestinationList) {
                System.out.println(td);
            }

            return trip;
        });
    }


    private List<TripDestination> FetchAllTripDestinations(TripDestinationDTO dto, TripDestination parent) {

        TripDestination td = new TripDestination(parent, dto.ordinal, dto.customName, dto.arrivalDate, dto.departureDate);

        if(parent != null) {
            td.parent = parent;
        }

        ArrayList<TripDestination> list  = new ArrayList<>();

        list.add(td);



        if (dto.children == null && dto.children.size() == 0) return list;

        for(TripDestinationDTO child : dto.children) {
            list.addAll(FetchAllTripDestinations(child, td));
        }

        return list;
    }


}
