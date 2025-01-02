package main;

/**
 * An exception representing an instance where a negative balance is set to an account
 * @author Adrian Clear
 * @author Vlad Florea 22409144
 */
public class NegativeBalanceException extends Exception {
	public NegativeBalanceException(String message) {
		super(message);
	}
}
