/**
 * This enum specifies pre-defined categories of expenses
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

public enum ExpenseCategory {
    TRAVEL_AND_SUBSISTENCE("TRAVEL & SUBSISTENCE"),
    SUPPLIES("SUPPLIES"),
    ENTERTAINMENT("ENTERTAINMENT"),
    EQUIPMENT("EQUIPMENT"),
    OTHER("OTHER");

    private String name;

    ExpenseCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
