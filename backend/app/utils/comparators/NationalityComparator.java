package utils.comparators;

import models.Nationality;

import java.util.Comparator;

public class NationalityComparator implements Comparator<Nationality> {
    /**
     *
     * Compares two Nationality objects depending on their nationality parameter
     *
     * @param n1 Nationality object
     * @param n2 Nationality object
     * @return the values to compare
     */
    @Override
    public int compare(Nationality n1, Nationality n2) {
        return n1.nationality.toLowerCase().compareTo(n2.nationality.toLowerCase());
    }
}
