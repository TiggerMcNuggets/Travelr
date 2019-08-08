package dto.trip;

import java.util.List;

public class GetTripResponse {

    public List<NavigationDTO> navigation;

    public GetTripDTO trip;

    public RootNodeDTO root;

    public List<NavigationDTO> getNavigation() {
        return navigation;
    }

    public void setNavigation(List<NavigationDTO> navigation) {
        this.navigation = navigation;
    }

    public GetTripDTO getTrip() {
        return trip;
    }

    public void setTrip(GetTripDTO trip) {
        this.trip = trip;
    }

    public RootNodeDTO getRoot() {
        return root;
    }

    public void setRoot(RootNodeDTO root) {
        this.root = root;
    }
}
