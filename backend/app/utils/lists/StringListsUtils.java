package utils.lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to gather utils for lists of String objects
 */
public class StringListsUtils {

    /**
     *
     * @param firstList list of String
     * @param secondList list of String
     * @return firstList minus all the elements that are shared by both lists
     */
    public static ArrayList<String> subtraction(List<String> firstList, List<String> secondList) {
        ArrayList<String> old = new ArrayList<>();
        for (String str : firstList) {
            if (!secondList.contains(str)) old.add("" + str);
        }
        return old;
    }

}
