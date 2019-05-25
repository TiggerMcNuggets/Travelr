package controllers.dto.Destination;

import models.Destination;
import models.DestinationEditRequest;
import models.TravellerType;
import models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for receiving destination edit requests
 */
public class DestinationEditRequestResponse {

    public List<DestinationEditRequestItem> requests = new ArrayList<>();

    public DestinationEditRequestResponse(List<DestinationEditRequest> requests) {
        for (DestinationEditRequest request : requests) {
            this.requests.add(new DestinationEditRequestItem(request));
        }
    }

    private class DestinationEditRequestItem {

        public DestinationItem destination;
        public List<TravellerTypeItem> travellerTypes = new ArrayList<>();
        public UserItem user;
        public Long id;

        public DestinationEditRequestItem(DestinationEditRequest request) {

            this.destination = new DestinationItem(request.getDestination());
            this.id = request.getId();
            this.user = new UserItem(request.getUser());

            for (TravellerType type : request.getTravellerTypes()) {
                this.travellerTypes.add(new TravellerTypeItem(type));
            }
        }
    }

    private class DestinationItem {

        public long id;
        public String name;
        public List<TravellerTypeItem> travellerTypes = new ArrayList<>();

        public DestinationItem(Destination destination) {
            this.id = destination.getId();
            this.name = destination.getName();
            for(TravellerType type : destination.getTravellerTypes()) {
                travellerTypes.add(new TravellerTypeItem(type));
            }
        }

    }

    private class TravellerTypeItem {

        public long id;
        public String name;

        public TravellerTypeItem(TravellerType type) {
            this.id = type.getId();
            this.name = type.name;

        }

    }

    private class UserItem {

        public long id;
        public String email;

        public UserItem(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
        }
    }


}
