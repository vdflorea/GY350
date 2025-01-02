/**
 * This class implements the ExpensePrinter interface and
 * therefore defines its own method of printing expenses
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

import java.util.List;

public class PrinterByLabel implements ExpensePrinter{
    /**
     * Print out each expense category along with
     * the expenses pertaining to each
     *
     * @param expenses
     */
    @Override
    public void print(List<Expense> expenses) {
        // Print out "Travel & Subsistence" expenses
        System.out.println(ExpenseCategory.TRAVEL_AND_SUBSISTENCE.getName());
        for (Expense expense : expenses) {
            if (expense.getCategory() == ExpenseCategory.TRAVEL_AND_SUBSISTENCE) {
                System.out.println(expense.toString());
            }
        }

        // Print out "Supplies" expenses
        System.out.println(ExpenseCategory.SUPPLIES.getName());
        for (Expense expense : expenses) {
            if (expense.getCategory() == ExpenseCategory.SUPPLIES) {
                System.out.println(expense.toString());
            }
        }

        // Print out "Entertainment" expenses
        System.out.println(ExpenseCategory.ENTERTAINMENT.getName());
        for (Expense expense : expenses) {
            if (expense.getCategory() == ExpenseCategory.ENTERTAINMENT) {
                System.out.println(expense.toString());
            }
        }

        // Print out "Equipment" expenses
        System.out.println(ExpenseCategory.EQUIPMENT.getName());
        for (Expense expense : expenses) {
            if (expense.getCategory() == ExpenseCategory.EQUIPMENT) {
                System.out.println(expense.toString());
            }
        }

        // Print out "Other" expenses
        System.out.println(ExpenseCategory.OTHER.getName());
        for (Expense expense : expenses) {
            if (expense.getCategory() == ExpenseCategory.OTHER) {
                System.out.println(expense.toString());
            }
        }
    }
}
