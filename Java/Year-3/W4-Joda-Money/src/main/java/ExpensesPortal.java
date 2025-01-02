/**
 * The main "portal" which manages an employee's expenses
 * -> All expenses on the portal are automatically converted to EURO
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExpensesPortal{
    private List<Expense> expenses;
    private Money expensesTotal = Money.of(CurrencyUnit.EUR, 0);

    /**
     * Instantiate a new ExpensesPortal for an employee without any expenses
     */
    public ExpensesPortal(){
        this.expenses = new ArrayList<>();
    }

    /**
     * Instantiate a new ExpensesPortal for an employee with expenses pre-defined
     * -> Calculate total of all pre-defined expenses
     */
    public ExpensesPortal(List<Expense> expenses){
        this.expenses = new ArrayList<>(expenses);
        for(Expense expense : expenses){
            if (expense.getAmount().getCurrencyUnit().equals(CurrencyUnit.USD)){
                // Convert every USD expense to EURO
                expense.setAmount(expense.getAmount().convertedTo(CurrencyUnit.EUR, new BigDecimal("0.85"), RoundingMode.HALF_UP));
            }
            addExpenseToTotal(expense);
        }
    }

    /**
     * Prints all expenses using the specified ExpensePrinter's print() method
     *
     * @param printer The specified ExpensePrinter interface
     */
    public void printExpenses(ExpensePrinter printer) {
        printer.print(expenses);
    }

    /**
     * Add an expense to the employee's expense list
     * -> Recalculate total by adding new expense to current total
     *
     * @param expense The employee's expense to be added
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
        addExpenseToTotal(expense);
    }

    /**
     * Adds an expense amount to an employee's total expense amount on the portal
     *
     * @param expense The employee's expense
     */
    private void addExpenseToTotal(Expense expense) {
        expensesTotal = expensesTotal.plus(expense.getAmount());
    }

    /**
     * Remove an expense from the employee's expense list
     * -> Recalculate total by subtracting new expense from current total
     *
     * @param expense The employee's expense to be removed
     */
    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        removeExpenseFromTotal(expense);
    }

    /**
     * Removes an expense amount from an employee's total expense amount on the portal
     *
     * @param expense The employee's expense
     */
    private void removeExpenseFromTotal(Expense expense) {
        expensesTotal = expensesTotal.minus(expense.getAmount());
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public int getNumberOfExpenses() {
        return expenses.size();
    }

    public Money getExpensesTotal() {
        return expensesTotal;
    }
}
