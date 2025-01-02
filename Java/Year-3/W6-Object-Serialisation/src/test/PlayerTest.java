/**
 * This class performs various tests to ensure
 * correct Object serialisation functionality
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package test;

import main.Achievement;
import main.Country;
import main.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    ArrayList<Player> players;
    private File achievementsFile;
    private File playersFile;

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Neo", Country.ARGENTINA, LocalDate.of(2023, 12, 10)));
        players.add(new Player("Morpheus", Country.IRELAND, LocalDate.of(2023, 10, 15)));
        players.add(new Player("Zion", Country.POLAND, LocalDate.of(2021, 2, 3)));
        players.add(new Player("Trinity", Country.DENMARK, LocalDate.of(2020, 9, 29)));
        players.add(new Player("Cypher", Country.AUSTRALIA, LocalDate.of(2019, 12, 5)));

        // Set Player 1 achievements
        players.get(0).addAchievement(Achievement.QUANTUM_LEAP);

        // Set Player 2 achievements
        players.get(1).addAchievement(Achievement.QUANTUM_LEAP);
        players.get(1).addAchievement(Achievement.MEGASTRUCTURE_MASTERMIND);

        // Set Player 3 achievements
        players.get(2).addAchievement(Achievement.MEGASTRUCTURE_MASTERMIND);

        // Set Player 4 achievements
        players.get(3).addAchievement(Achievement.MEGASTRUCTURE_MASTERMIND);
        players.get(3).addAchievement(Achievement.COSMIC_EXPLORER);

        // Set Player 5 achievements
        players.get(4).addAchievement(Achievement.QUANTUM_LEAP);
        players.get(4).addAchievement(Achievement.MEGASTRUCTURE_MASTERMIND);
        players.get(4).addAchievement(Achievement.COSMIC_EXPLORER);

        // Files used strictly for testing purposes
        achievementsFile = new File("achievements.csv");
        playersFile = new File("players.ser");

        // Ensure that files are deleted if they already exist
        // (Prior to test execution)
        if (achievementsFile.exists()) {
            achievementsFile.delete();
        }
        if (playersFile.exists()) {
            playersFile.delete();
        }
    }

    /**
     * Ensure that a Player object's fields are
     * instantiated correctly
     */
    @Test
    public void testCorrectPlayerInstantiation() {
        Player p = players.getFirst();

        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals(p.getUsername(), "Neo");
        assertEquals(p.getCountry(), Country.ARGENTINA);
        assertEquals(p.getJoinDate(), LocalDate.of(2023, 12, 10));
        assertEquals(p.getAchievements().getFirst(), Achievement.QUANTUM_LEAP);
    }


    /**
     * Ensure that every Achievement that is instantiated is
     * assigned a DateOfAward automatically on the date of instantiation
     */
    @Test
    public void testCorrectPlayerAchievementDateOfAward() {
        Player p = players.getFirst();
        p.addAchievement(Achievement.COSMIC_EXPLORER);

        assertEquals(p.getAchievements().getLast().getDateOfAward(), LocalDate.now());
    }

    /**
     * Ensure that the 5 Player objects are serialised correctly and
     * remain intact by subsequently de-serialising them
     */
    @Test
    public void testMultiplePlayerObjectsSerialisation() {
        // Serialise the Player objects
        try {
            FileOutputStream out = new FileOutputStream(playersFile.getName());
            ObjectOutputStream oos = new ObjectOutputStream(out);

            // Serialise each Player object to players.ser file
            for (Player player : players) {
                oos.writeObject(player);
            }

            oos.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Deserialise the Player objects
        try {
            FileInputStream in = new FileInputStream(playersFile.getName());
            ObjectInputStream ois = new ObjectInputStream(in);

            Player p;

            // NOTE: Implemented it this way rather than:
            // while ((p = (Player)s.readObject()) != null)
            // -> To prevent EOFException on the final iteration of the loop
            int expectedPlayers = players.size();
            for (int i = 0; i < expectedPlayers; i++) {
                // De-serialise each Player object from players.ser file
                p = (Player)ois.readObject();

                // Ensure that deserialised object is a Player instance (not null)
                assertNotNull(p);

                // Ensure that Player object has been deserialised correctly
                assertEquals(p.getId(), players.get(i).getId());
                assertEquals(p.getUsername(), players.get(i).getUsername());
                assertEquals(p.getCountry().getName(), players.get(i).getCountry().getName());
                assertEquals(p.getJoinDate(), players.get(i).getJoinDate());
                assertEquals(p.getAchievements(), players.get(i).getAchievements());
            }

            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }

}
