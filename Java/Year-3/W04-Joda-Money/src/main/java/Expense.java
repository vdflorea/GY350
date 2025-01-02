/**
 * This class stores information about an
 * employee's particular expense within the company
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Expense {
    private LocalDate date;
    private String description;
    private ExpenseCategory category;

    private static Money companyBudget = Money.of(CurrencyUnit.EUR, 200000); // Per employee budget
    private Money amount;
    private boolean isApproved = false;

    /**
     * Constructor attempts to instantiate a new employee expense
     * -> It will fail to do so if:
     * --1> The expense exceeds the company's allocated budget for all employees
     * --2> The expense is instantiated in a currently unsupported currency
     *
     * @param date
     * @param description
     * @param category
     * @param amount
     */
    public Expense(LocalDate date, String description, ExpenseCategory category, Money amount) throws BudgetExceededException, UnsupportedCurrencyException {
        try {
            // Attempt to approve expense
            approveExpense(amount);

            // Expense has been approved
            isApproved = true;
            this.date = date;
            this.description = description;
            this.category = category;
            this.amount = amount;
        } catch (BudgetExceededException | UnsupportedCurrencyException e) {
            // Expense is invalid
            throw e;
        }
    }

    /**
     * @return a String representation of an employee's Expense
     */
    @Override
    public String toString() {
        return String.format("%s: %s - %s - %s %.2f",
                date.toString(),
                description,
                category.getName(),
                amount.getCurrencyUnit(),
                amount.getAmount());
    }

    /**
     * Approves whether an expense is valid by ensuring that
     * the expense does not exceed the company's allocated budget
     *
     * @param expenseAmount The amount of an expense in EURO
     * @throws BudgetExceededException Thrown if the expense exceed the company's budget
     */
    public void approveExpense(Money expenseAmount) throws BudgetExceededException, UnsupportedCurrencyException {
        // Convert expense amount to EURO temporarily (if in USD)
        if (expenseAmount.getCurrencyUnit().equals(CurrencyUnit.USD)) {
            expenseAmount = expenseAmount.convertedTo(CurrencyUnit.EUR, new BigDecimal("0.85"), RoundingMode.HALF_UP);
        }

        // Ensure that the expense amount is only in USD or EURO
        if (!expenseAmount.getCurrencyUnit().equals(CurrencyUnit.EUR)) {
            throw new UnsupportedCurrencyException("Expense defined in currently unsupported currency");
        }

        // Ensure that expense amount does not exceed allocated company budget
        if (expenseAmount.compareTo(companyBudget) > 0) {
            throw new BudgetExceededException("Expense exceeds company budget");
        } else {
            // Subtract expense amount from company budget
            companyBudget = companyBudget.minus(expenseAmount);
        }
    }

    public static Money getCompanyBudget() {
        return companyBudget;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }
}
