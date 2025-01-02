/**
 * This class performs the necessary setup
 * for the tests of the application
 * -> Reads in UserAccounts from a pre-defined CSV file
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserReader {
    /**
     * Performs I/O operations on a "users.csv" file to
     * read in some users of the application (testing purposes)
     *
     * @return The list of UserAccounts extracted from the CSV
     */
    public List<UserAccount> readUsers() {
        List<UserAccount> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;

            // Parse each line of the file and extract/transfer the data into UserAccount objects
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 3) {
                    users.add(new UserAccount(Long.parseLong(parts[0]), parts[1], parts[2]));
                } else {
                    throw new IOException();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }
}
