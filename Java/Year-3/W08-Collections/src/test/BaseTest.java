/**
 * This class performs various set-up
 * operations for its two child tests:
 * UserAccountTest and WorkspaceTest
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package test;

import main.UserAccount;
import main.UserReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class BaseTest {
    public static List<UserAccount> users;
    public static File usersFile;

    /**
     * Set up for both User and Workspace Tests
     */
    @BeforeAll
    public static void setUpAll() {
        users = new ArrayList<>();
        usersFile = new File("users.csv");

        if (!usersFile.exists()) {
            System.out.println("Users file for testing does not exist");
            fail();
        } else {
            UserReader ur = new UserReader();
            users = ur.readUsers();
        }
    }

    /**
     * Test reading Users from pre-defined CSV into UserAccount Objects
     * -> Ensure correct UserReader.readUsers() functionality
     */
    @Test
    public void testCorrectUsersFileReadOperation() {
        assertEquals(users.size(), 12);
        assertEquals(users.get(0).getUserID(), 713197);
        assertEquals(users.get(0).getName(), "Alex");
        assertEquals(users.get(0).getEmailAddress(), "alex@gmail.com");

        assertEquals(users.get(1).getUserID(), 903720);
        assertEquals(users.get(1).getName(), "Bob");
        assertEquals(users.get(1).getEmailAddress(), "bob@gmail.com");

        assertEquals(users.get(2).getUserID(), 616914);
        assertEquals(users.get(2).getName(), "Charlie");
        assertEquals(users.get(2).getEmailAddress(), "charlie@gmail.com");

        assertEquals(users.get(3).getUserID(), 554959);
        assertEquals(users.get(3).getName(), "Diana");
        assertEquals(users.get(3).getEmailAddress(), "diana@gmail.com");

        assertEquals(users.get(4).getUserID(), 521577);
        assertEquals(users.get(4).getName(), "Eveline");
        assertEquals(users.get(4).getEmailAddress(), "eveline@gmail.com");

        assertEquals(users.get(5).getUserID(), 948350);
        assertEquals(users.get(5).getName(), "Fiona");
        assertEquals(users.get(5).getEmailAddress(), "fiona@gmail.com");

        assertEquals(users.get(6).getUserID(), 899792);
        assertEquals(users.get(6).getName(), "Gary");
        assertEquals(users.get(6).getEmailAddress(), "gary@gmail.com");

        assertEquals(users.get(7).getUserID(), 937899);
        assertEquals(users.get(7).getName(), "Henry");
        assertEquals(users.get(7).getEmailAddress(), "henry@gmail.com");

        assertEquals(users.get(8).getUserID(), 828923);
        assertEquals(users.get(8).getName(), "Isaac");
        assertEquals(users.get(8).getEmailAddress(), "isaac@gmail.com");

        assertEquals(users.get(9).getUserID(), 884561);
        assertEquals(users.get(9).getName(), "Josephine");
        assertEquals(users.get(9).getEmailAddress(), "josephine@gmail.com");

        assertEquals(users.get(10).getUserID(), 158630);
        assertEquals(users.get(10).getName(), "Kate");
        assertEquals(users.get(10).getEmailAddress(), "kate@gmail.com");

        assertEquals(users.get(11).getUserID(), 833916);
        assertEquals(users.get(11).getName(), "Lucy");
        assertEquals(users.get(11).getEmailAddress(), "lucy@gmail.com");
    }
}
