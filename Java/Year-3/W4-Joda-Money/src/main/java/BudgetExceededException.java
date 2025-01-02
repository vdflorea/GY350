/**
 * This exception is thrown when an employee's expense exceeds
 * the company's allocated budget
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

public class BudgetExceededException extends RuntimeException {
    public BudgetExceededException(String message) {
        super(message);
    }
}
