/**
 * This class represents a User's account on
 * an online application
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package main;

import java.util.Objects;

public class UserAccount implements Comparable<UserAccount> {
    private long userID;
    private String name;
    private String emailAddress;

    /**
     * @param userID The identifier of the user
     * @param name The name of the user
     * @param emailAddress The email address of the user
     */
    public UserAccount(long userID, String name, String emailAddress) {
        this.userID = userID;
        this.name = name;
        this.emailAddress = emailAddress;
    }

    /**
     * Creates a formatted String representation
     * of a User's account details
     *
     * @return String representation of a UserAccount
     */
    @Override
    public String toString() {
        return String.format("UserID: %d\nName: %s\nEmail Address: %s\n", userID, name, emailAddress);
    }

    /**
     * Alternative toString() method
     * -> Formatted in a single line like for "users.csv"
     *
     * @return String representation of a UserAccount (CSV Format)
     */
    public String toStringCSV() {
        return userID + ", " + name + ", " + emailAddress;
    }

    /**
     * Defines the natural ordering between UserAccount objects
     * -> Lexicographically by email address
     *
     * @param o The object to be compared
     * @return -1, 0 or 1 (Less than, equal, greater than)
     */
    @Override
    public int compareTo(UserAccount o) {
        return this.emailAddress.compareTo(o.emailAddress);
    }

    /**
     * Defines the equality criteria for UserAccount objects
     * -> Equal if identifiers are equal
     *
     * @param o The object to be tested for equality (with this object)
     * @return Equal/Not Equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserAccount other = (UserAccount) o;

        return this.userID == other.userID;
    }

    /**
     * Converts this UserAccount object into a hash code
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        // Combine hash codes of userID, name and emailAddress
        return Objects.hash(userID, name, emailAddress);
    }

    public long getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
