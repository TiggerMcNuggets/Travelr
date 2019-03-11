package utils.comparators;

import models.Nationality;
import models.Traveller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TravellerComparatorForNationalitites implements Comparator<Traveller> {
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
        List<Nationality> nationalities1 = t1.nationalities;
        List<Nationality> nationalities2 = t2.nationalities;
        Collections.sort(nationalities1, new NationalityComparator());
        Collections.sort(nationalities2, new NationalityComparator());
        t1.setNationalities(nationalities1);
        t2.setNationalities(nationalities2);
        return nationalities1.get(0).nationality.compareTo(nationalities2.get(0).nationality);
    }
}
