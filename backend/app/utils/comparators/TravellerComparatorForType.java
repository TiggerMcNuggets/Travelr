package utils.comparators;

import models.User;
import models.TravellerType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TravellerComparatorForType implements Comparator<User> {
    /**
     * Compares two Traveller objects using the sorted first value of their nationalities list
     * It also sorts the nationalities lists for both travellers
     *
     * @param t1 Traveller object
     * @param t2 Traveller object
     * @return the values to compare
     */
    @Override
    public int compare(User t1, User t2) {
        List<TravellerType> types1 = t1.travellerTypes;
        List<TravellerType> types2 = t2.travellerTypes;
        Collections.sort(types1, new TravellerTypeComparator());
        Collections.sort(types2, new TravellerTypeComparator());
        return types1.get(0).name.compareTo(types2.get(0).name);
    }
}
