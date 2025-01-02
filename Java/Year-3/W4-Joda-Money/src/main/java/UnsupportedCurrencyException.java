/**
 * This exception is thrown when an employee's expense
 * is instantiated in an unsupported currency
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String message) {
        super(message);
    }
}
