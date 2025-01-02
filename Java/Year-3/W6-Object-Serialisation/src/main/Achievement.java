/**
 * This enum represents an Achievement and stores
 * information on multiple different game achievements
 *
 * NOTE:
 * Enums are serializable by default in Java; However I felt that
 * my solution would look much cleaner by treating Achievements as an enum
 * and to mark a player's Achievements list as transient anyway
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package main;

import java.time.LocalDate;

public enum Achievement {
    QUANTUM_LEAP("Quantum Leap",
            "Complete a mission without taking damage from enemies or hazards."),

    MEGASTRUCTURE_MASTERMIND("Megastructure Mastermind",
            "Design and construct a megastructure with a total mass exceeding 100 billion tons."),

    COSMIC_EXPLORER("Cosmic Explorer",
            "Visit every planet and moon in the game's solar system within a single playthrough.");

    private String name;
    private String description;
    private LocalDate dateOfAward;

    Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.dateOfAward = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateOfAward() {
        return dateOfAward;
    }
}
