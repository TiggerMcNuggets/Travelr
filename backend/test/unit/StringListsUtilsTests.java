package unit;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.lists.StringListsUtils;

import java.util.ArrayList;

public class StringListsUtilsTests {

    private ArrayList<String> first = new ArrayList<>();
    private ArrayList<String> second = new ArrayList<>();

    @Before
    public void initialSetup() {
        first.add("A");
        first.add("B");
        first.add("C");
        first.add("D");

        second.add("C");
        second.add("D");
        second.add("E");

    }

    @After
    public void teardown() {
        first.clear();
        second.clear();
    }

    // edge case
    @Test
    public void testSubtractionWithEmptyListAsFirstElement() {
        first.clear();
        ArrayList<String> difference = StringListsUtils.subtraction(first, second);
        assertEquals(0, difference.size());
    }

    // edge case
    @Test
    public void testSubtractionWithEmptyListAsSecondElement() {
        second.clear();
        ArrayList<String> difference = StringListsUtils.subtraction(first, second);
        assertEquals(4, difference.size());
        assertEquals(difference.get(0), "A");
        assertEquals(difference.get(1), "B");
        assertEquals(difference.get(2), "C");
        assertEquals(difference.get(3), "D");
    }

    // frequent behaviour
    @Test
    public void testSubtractionWithMixedValuesListsWithFirstAndSecond() {
        ArrayList<String> difference = StringListsUtils.subtraction(first, second);
        assertEquals(2, difference.size());
        assertEquals(difference.get(0), "A");
        assertEquals(difference.get(1), "B");
    }

    // frequent behaviour
    @Test
    public void testSubtractionWithMixedValuesListsWithSecondAndFirst() {
        ArrayList<String> difference = StringListsUtils.subtraction(second, first);
        assertEquals(1, difference.size());
        assertEquals(difference.get(0), "E");
    }

    // frequent behaviour
    @Test
    public void testSubtractionWithListsWithNoValuesInCommon() {
        first.clear();
        first.add("Z");
        first.add("Y");
        ArrayList<String> difference = StringListsUtils.subtraction(first, second);
        assertEquals(2, difference.size());
        assertEquals(difference.get(0), "Z");
        assertEquals(difference.get(1), "Y");
    }
}