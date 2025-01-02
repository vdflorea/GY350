package main;

/**
 * An exception representing the fact that an account has insufficient funds
 * for a given transaction.
 * @author Adrian Clear
 * @author Vlad Florea 22409144
 */
public class InsufficientFundsException extends Exception {
	public InsufficientFundsException(String message) {
        super(message);
    }
}
