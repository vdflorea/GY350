/**
 * This class performs various tests to
 * demonstrate the usage/manipulation of
 * collections of Users
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package test;

import main.UserAccount;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountTest extends BaseTest {
    /**
     * Helper function for tests
     * -> Prints users to the console using toString()
     */
    private void printUsers() {
        for (UserAccount user : users) {
            System.out.println(user.toString());
        }
    }

    /**
     * Tests the natural ordering of User objects
     * -> Verifies that the compareTo function compares Users lexicographically (by email)
     */
    @Test
    public void testNaturalOrdering() {
        System.out.println(users.getFirst().getEmailAddress() + " comes before (is less than) " + users.getLast().getEmailAddress());
        assertTrue(users.getFirst().compareTo(users.getLast()) < 0);
    }

    /**
     * Tests the equality between User objects
     * -> Verifies that the equals function returns true if the IDs of two Users are equal
     */
    @Test
    public void testEquality() {
        UserAccount alexClone = new UserAccount(users.getFirst().getUserID(), "Alex (clone)", users.getFirst().getEmailAddress());
        assertTrue(users.getFirst().equals(alexClone));
    }

    /**
     * Tests the correct hashing of User objects
     * -> Hashes two identical User objects to the same hash code
     */
    @Test
    public void testHashCode() {
        UserAccount u1 = new UserAccount(users.getFirst().getUserID(), users.getFirst().getName(), users.getFirst().getEmailAddress());

        // Hash codes should be equal for both objects
        assertEquals(u1.hashCode(), users.getFirst().hashCode());

        // Hash codes should not be equal for two different objects
        assertNotEquals(u1.hashCode(), users.getLast().hashCode());
    }

    /**
     * Tests for the correct toString format output for User objects
     */
    @Test
    public void testToString() {
        String testString = "UserID: "  + users.getFirst().getUserID() + "\n" +
                            "Name: " + users.getFirst().getName() + "\n" +
                            "Email Address: " + users.getFirst().getEmailAddress() + "\n";

        assertEquals(testString, users.getFirst().toString());
    }

    /**
     * Tests various functions of the Collections class with multiple User objects
     * -> Sorting by email (natural ordering)
     * -> Custom sorting using a Comparator (anonymous inner class)
     * -> Custom sorting using a Comparator (lambda function)
     * -> Searching for a UserAccount in a pre-sorted list (Collections.binarySearch)
     */
    @Test
    public void testSortingAndSearching() {
        // New user (Aaron) will be at the bottom of the list of users
        UserAccount a = new UserAccount(736226, "Aaron", "aaron@gmail.com");
        UserAccount z = new UserAccount(344892, "Zachary", "zachary@gmail.com");
        users.add(a);
        users.add(z);

        // Current list (before sorting)
        System.out.println("-------------- Before Sorting --------------");
        printUsers();

        // Aaron will now be at the top of the users list (alphabetical order of email addresses)
        Collections.sort(users);
        System.out.println("\n-------------- Sorted by Email --------------");
        printUsers();

        // Users will now be sorted based on their UserIDs (ascending order)
        Collections.sort(users, new Comparator<UserAccount>() {
            @Override
            public int compare(UserAccount o1, UserAccount o2) {
                return Long.compare(o1.getUserID(), o2.getUserID());
            }
        });
        System.out.println("\n-------------- Sorted by UserID --------------");
        printUsers();

        // Users will now be sorted based on their names (descending order)
        Collections.sort(users, (u1, u2) -> u1.getName().compareTo(u2.getName()));
        System.out.println("\n-------------- Sorted by Name --------------");
        printUsers();

        // Sorting again based on natural ordering (email address)
        Collections.sort(users);

        System.out.println("\n-------------- Binary Search --------------");

        // Finding "Aaron" using Binary Search
        System.out.println("Searching for \"Aaron\"");
        int index = Collections.binarySearch(users, a);
        System.out.println("Aaron's index in the Users list: " + index);
        System.out.println("Aaron's details: " + users.get(index).toStringCSV());

        // Finding "Zachary" using Binary Search
        System.out.println("\nSearching for \"Zachary\"");
        index = Collections.binarySearch(users, z);
        System.out.println("Zachary's index in the Users list: " + index);
        System.out.println("Zachary's details: " + users.get(index).toStringCSV());
    }

}
