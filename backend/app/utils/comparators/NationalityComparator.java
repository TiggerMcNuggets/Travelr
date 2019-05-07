package utils.comparators;


import models.UserNationality;

import java.util.Comparator;

public class NationalityComparator implements Comparator<UserNationality> {
    /**
     *
     * Compares two Nationality objects depending on their nationality parameter
     *
     * @param n1 Nationality object
     * @param n2 Nationality object
     * @return the values to compare
     */
    @Override
    public int compare(UserNationality n1, UserNationality n2) {
        return n1.nationality.name.toLowerCase().compareTo(n2.nationality.name.toLowerCase());
    }
}
