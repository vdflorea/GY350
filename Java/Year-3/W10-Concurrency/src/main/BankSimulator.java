package main;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.concurrent.TimeUnit;

/**
 * A class which simulates the operation of a bank using
 * random transactions
 * @author Vlad Florea 22409144
 */
public class BankSimulator {
    private static Bank bank;

    public static void main(String[] args) throws NegativeBalanceException {
        bank = new Bank();

        try {
            // Create three accounts in the bank with random balances
            bank.addAccount(new Account(100, Money.of(CurrencyUnit.EUR, 100000)));
            bank.addAccount(new Account(200, Money.of(CurrencyUnit.EUR, 150000)));
            bank.addAccount(new Account(300, Money.of(CurrencyUnit.EUR, 200000)));

            // Create and start the RandomTransactionGenerator thread
            Thread t = new Thread(bank.getRandomTransactionGenerator());
            t.start();

            // Open the bank (start the AutomatedBankClerk instances)
            bank.openBank();

            // Allow the generation & processing of transactions for 10 seconds
            System.out.println("Waiting for 10 seconds...");
            TimeUnit.SECONDS.sleep(10);
            System.out.println("10 seconds passed, interrupting generator...");

            // Interrupt the RandomTransactionGenerator thread and wait for 2 seconds
            // at most for the thread to terminate
            t.interrupt();
            t.join(2000);
            if (t.isAlive()) {
                System.out.println("RandomTransactionGenerator did not stop within 2 seconds.");
            } else {
                System.out.println("RandomTransactionGenerator stopped successfully.");
            }

            // Close the bank (shutdown the executor service)
            bank.closeBank();

            // Print out final account balances after transactions
            bank.printAllAccountDetails();
        } catch (NegativeBalanceException e) {
            throw new NegativeBalanceException("Balance cannot be negative");
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted: " + e.getMessage());
        }
    }
}
