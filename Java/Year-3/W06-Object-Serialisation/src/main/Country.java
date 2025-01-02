/**
 * This enum represents a Country and stores
 * information on multiple different countries
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package main;

import java.io.Serializable;

public enum Country implements Serializable {
    ARGENTINA("Argentina", 45700000),
    IRELAND("Ireland", 5300000),
    POLAND("Poland", 38500000),
    DENMARK("Denmark", 6000000),
    AUSTRALIA("Australia", 26700000);

    private String name;
    private int population;

    Country(String name, int population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }
}
