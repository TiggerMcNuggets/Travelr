package utils.comparators;

import models.TravellerType;

import java.util.Comparator;

public class TravellerTypeComparator implements Comparator<TravellerType> {
    /**
     *
     * Compares two Nationality objects depending on their nationality parameter
     *
     * @param t1 TravellerType object
     * @param t2 TravellerType object
     * @return the values to compare
     */
    @Override
    public int compare(TravellerType t1, TravellerType t2) {
        return t1.name.toLowerCase().compareTo(t2.name.toLowerCase());
    }
}
