package controllers.dto.Destination;

import models.Destination;
import models.DestinationEditRequest;
import models.TravellerType;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class DestinationEditRequestReponse {


    public List<DestinationEditRequestItem> requests = new ArrayList<>();

    public DestinationEditRequestReponse(List<DestinationEditRequest> requests) {
        for (DestinationEditRequest request : requests) {
            this.requests.add(new DestinationEditRequestItem(request));
        }
    }

    private class DestinationEditRequestItem {
        public DestinationItem destination;
        public List<TravellerTypeItem> travellerTypes = new ArrayList<>();
        public UserItem user;

        public DestinationEditRequestItem(DestinationEditRequest request) {

            this.destination = new DestinationItem(request.getDestination());

            this.user = new UserItem(request.getUser());

            for (TravellerType type : request.getTravellerTypes()) {
                this.travellerTypes.add(new TravellerTypeItem(type));
            }
        }
    }

    private class DestinationItem {

        public long id;
        public String name;


        public DestinationItem(Destination destination) {
            this.id = destination.id;
            this.name = destination.name;
        }

    }

    private class TravellerTypeItem {

        public long id;
        public String name;

        public TravellerTypeItem(TravellerType type) {
            this.id = type.id;
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
