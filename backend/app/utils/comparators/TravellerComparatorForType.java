package utils.comparators;

import models.Nationality;
import models.Traveller;
import models.TravellerType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TravellerComparatorForType implements Comparator<Traveller> {
    /**
     * Compares two Traveller objects using the sorted first value of their nationalities list
     * It also sorts the nationalities lists for both travellers
     *
     * @param t1 Traveller object
     * @param t2 Traveller object
     * @return the values to compare
     */
    @Override
    public int compare(Traveller t1, Traveller t2) {
        List<TravellerType> types1 = t1.types;
        List<TravellerType> types2 = t2.types;
        Collections.sort(types1, new TravellerTypeComparator());
        Collections.sort(types2, new TravellerTypeComparator());
        t1.setTypes(types1);
        t2.setTypes(types2);
        return types1.get(0).tType.compareTo(types2.get(0).tType);
    }
}
