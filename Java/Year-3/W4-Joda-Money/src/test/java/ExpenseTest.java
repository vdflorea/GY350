/**
 * This class contains all JUnit tests for the
 * Employee Expenses application
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class ExpenseTest {
    private ExpensesPortal expensesPortal;

    /**
     * Set up a list of employee expenses on the portal
     * to be used during test execution later
     *
     * @throws BudgetExceededException Thrown if company budget is exceeded during expense creation
     */
    @BeforeEach
    public void setUp() throws BudgetExceededException{
        try {
            List<Expense> expenses = Arrays.asList(
                    new Expense(LocalDate.now(), "New York Business Trip 2024", ExpenseCategory.TRAVEL_AND_SUBSISTENCE, Money.parse("EUR 5700.00")),
                    new Expense(LocalDate.now(), "Employee Stationery Supplies", ExpenseCategory.SUPPLIES, Money.parse("USD 450.00")),
                    new Expense(LocalDate.now(), "HR Department Social Night 2024", ExpenseCategory.ENTERTAINMENT, Money.parse("EUR 900.00")),
                    new Expense(LocalDate.now(), "x100 Dell 24-Inch IPS Monitors", ExpenseCategory.EQUIPMENT, Money.parse("USD 12300.00")),
                    new Expense(LocalDate.now(), "Misc. Office Renovations", ExpenseCategory.OTHER, Money.parse("EUR 760.00"))
            );
            expensesPortal = new ExpensesPortal(expenses);
        } catch (BudgetExceededException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensure that valid expenses are set as "approved"
     */
    @Test
    public void testValidExpenseIsApproved() {
        assertEquals(expensesPortal.getExpenses().get(0).isApproved(), true);
    }

    /**
     * Ensure that expenses exceeding the company's budget throw a BudgetExceededException
     */
    @Test
    public void testInvalidExpenseExceedingCompanyBudget() {
        assertThrows(BudgetExceededException.class, () -> {
            new Expense(LocalDate.now(), "Huge Purchase", ExpenseCategory.OTHER, Money.parse("EUR 500000.00"));
        });
    }

    /**
     * Ensure that expenses in an unsupported currency throw an UnsupportedCurrencyException
     */
    @Test
    public void testInvalidExpenseWithUnsupportedCurrency() {
        assertThrows(UnsupportedCurrencyException.class, () -> {
           new Expense(LocalDate.now(), "Purchase from Japan", ExpenseCategory.OTHER, Money.parse("JPY 100.00"));
        });
    }

    /**
     * Ensure that allocated company budget decreases upon valid expense creation
     */
    @Test
    public void testCompanyBudgetDecreases() {
        Money currCompanyBudget = Expense.getCompanyBudget();
        expensesPortal.addExpense(new Expense(LocalDate.now(), "Small Expense", ExpenseCategory.OTHER, Money.parse("EUR 100.00")));
        assertEquals(Expense.getCompanyBudget(), currCompanyBudget.minus(Money.parse("EUR 100.00")));
    }

    /**
     * Define an ExpensePrinter interface using a lambda expression and
     * ensure that the output stream contains the desired output
     *
     * @throws IOException Thrown if I/O operation is interrupted/fails
     */
    @Test
    public void testExpensePrinterInterfaceLambdaExpression() throws IOException {
        ExpensePrinter printer = (List<Expense> expenses) -> {
            expenses.forEach(expense -> {System.out.println(expense.toString());});
        };

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Capture System.out and redirect it to the byte array output stream
            PrintStream capture = new PrintStream(outputStream);
            System.setOut(capture);

            expensesPortal.printExpenses(printer);

            // Get the captured output as a trimmed string
            String capturedOutput = outputStream.toString().trim();

            // Ensure that the captured output matches the desired format/output
            for (Expense expense : expensesPortal.getExpenses()) {
                assertThat(capturedOutput).contains(expense.toString());
            }
        } finally {
            System.setOut(System.out); // Restore original System.out
        }
    }

    /**
     * Define an ExpensePrinter interface using an anonymous inner class and
     * ensure that the output stream contains the desired output using a regular expression
     *
     * @throws IOException Thrown if I/O operation is interrupted/fails
     */
    @Test
    public void testExpensePrinterInterfaceAnonymousInnerClass() throws IOException {
        ExpensePrinter printer = new ExpensePrinter() {
            public void print(List<Expense> expenses) {
                ExpensesPortal ePortal = new ExpensesPortal(expenses);
                Money total = ePortal.getExpensesTotal();
                int numExpenses = ePortal.getNumberOfExpenses();

                System.out.println("There are " + numExpenses + " expenses in the system totalling to a value of " + total.getCurrencyUnit() + " " + total.getAmount());
            }
        };

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Capture System.out and redirect it to the byte array output stream
            PrintStream capture = new PrintStream(outputStream);
            System.setOut(capture);

            expensesPortal.printExpenses(printer);

            // Get the captured output as a trimmed string
            String capturedOutput = outputStream.toString().trim();

            // Define a pattern to match the expected output format
            Pattern pattern = Pattern.compile("There are \\d+ expenses in the system totalling to a value of EUR \\d+.\\d\\d");

            // Check if the captured output matches the pattern
            Matcher matcher = pattern.matcher(capturedOutput.trim());
            assertTrue(matcher.find());
        } finally {
            System.setOut(System.out); // Restore original System.out
        }
    }

    /**
     * Ensure that PrinterByLabel print() method
     * contains the desired format/output
     *
     * @throws IOException Thrown if I/O operation is interrupted/fails
     */
    @Test
    public void testPrinterByLabel() throws IOException {
        PrinterByLabel printer = new PrinterByLabel();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Capture System.out and redirect it to the byte array output stream
            PrintStream capture = new PrintStream(outputStream);
            System.setOut(capture);

            // Use print method of PrinterByLabel object to print the expenses on the portal
            expensesPortal.printExpenses(printer);

            // Get the captured output as a trimmed string
            String capturedOutput = outputStream.toString().trim();

            // Ensure that the captured output matches the desired format/output
            assertThat(capturedOutput)
                    .contains(ExpenseCategory.TRAVEL_AND_SUBSISTENCE.getName())
                    .contains(expensesPortal.getExpenses().get(0).toString())
                    .contains(ExpenseCategory.SUPPLIES.getName())
                    .contains(expensesPortal.getExpenses().get(1).toString())
                    .contains(ExpenseCategory.ENTERTAINMENT.getName())
                    .contains(expensesPortal.getExpenses().get(2).toString())
                    .contains(ExpenseCategory.EQUIPMENT.getName())
                    .contains(expensesPortal.getExpenses().get(3).toString())
                    .contains(ExpenseCategory.OTHER.getName())
                    .contains(expensesPortal.getExpenses().get(4).toString());
        } finally {
            System.setOut(System.out); // Restore original System.out
        }
    }

}
