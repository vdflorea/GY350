/**
 * This class represents a Player
 * in an online game
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.UUID;

public class Player implements Serializable {
    private String id;
    private String username;
    private Country country;
    private LocalDate joinDate;
    private transient List<Achievement> achievements;

    /**
     *
     * @param username The chosen username of the player
     * @param country The country that the player account was created from
     * @param joinDate The date when the player joined the platform
     */
    public Player(String username, Country country, LocalDate joinDate) {
        id = UUID.randomUUID().toString().substring(0, 6); // Create a random 6-digit ID
        this.username = username;
        this.country = country;
        this.joinDate = joinDate;
        achievements = new ArrayList<>();
    }

    /**
     * Writes the serialised representation of a Player to the provided ObjectOutputStream
     * -> Uses custom serialisation logic to store Player achievements
     *
     * @param out The ObjectOutputStream to which the serialised representation should be written
     * @throws IOException If an I/O error occurs during serialisation
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        writeAchievementsToCsv();
    }

    /**
     * Custom serialisation logic which appends
     * a Player's achievements to a single CSV file
     * (containing multiple player achievements)
     * -> The Player's unique ID distinguishes their achievements within the file
     */
    private void writeAchievementsToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("achievements.csv", true))) { // True enables append mode
            for (Achievement achievement : achievements) {
                writer.append(id).append(",").append(achievement.getName()).append(",").append(achievement.getDescription()).append(",").append(achievement.getDateOfAward().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Reads the serialised representation of a Player from the provided ObjectInputStream
     * -> Uses custom de-serialisation logic to retrieve Player achievements
     *
     * @param in The ObjectInputStream from which the serialised representation should be retrieved
     * @throws IOException If an I/O error occurs during de-serialisation
     * @throws ClassNotFoundException If the Player class is not found during de-serialisation
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        readAchievementsFromCsv();
    }

    /**
     * Custom de-serialisation logic which retrieves
     * a Player's achievements from a single CSV file
     * (containing multiple player achievements)
     * -> The Player's unique ID distinguishes their achievements within the file
     */
    private void readAchievementsFromCsv() {
        achievements = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("achievements.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].equals(id)) {

                    // Convert the name (parts[1]) to the enum value
                    Achievement a = Achievement.valueOf(parts[1].toUpperCase().replaceAll(" ", "_"));
                    addAchievement(a);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Country getCountry() {
        return country;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }
}
