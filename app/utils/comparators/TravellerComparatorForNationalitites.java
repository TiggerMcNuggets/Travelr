package utils.comparators;

import models.User;
import models.UserNationality;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TravellerComparatorForNationalitites implements Comparator<User> {
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
        List<UserNationality> nationalities1 = t1.nationalities;
        List<UserNationality> nationalities2 = t2.nationalities;
        Collections.sort(nationalities1, new NationalityComparator());
        Collections.sort(nationalities2, new NationalityComparator());
        return nationalities1.get(0).nationality.name.compareTo(nationalities2.get(0).nationality.name);
    }
}
