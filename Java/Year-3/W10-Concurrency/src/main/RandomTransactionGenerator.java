package main;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates random transactions for a bank simulation
 * @author Vlad Florea 22409144
 */
public class RandomTransactionGenerator implements Runnable {
    private final Random rand;
    private final Bank bank;
    private List<Integer> accountNums;

    /**
     * Constructs a new RandomTransactionGenerator
     * @param bank the bank for which to generate transactions
     */
    public RandomTransactionGenerator(Bank bank) {
        this.bank = bank;
        rand = new Random();
    }

    /**
     * Runs the random transaction generation process
     */
    @Override
    public void run() {
        // Instantiate a new list containing all the account numbers in the bank
        accountNums = new ArrayList<>(bank.getAllAccountNumbers());

        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Generate a random transaction
                Transaction t = new Transaction(getRandomAccountNumber(), generateRandomTransactionAmount());

                // Submit random transaction to bank
                System.out.println("RandomTransactionGenerator (Submit To Bank): " + t);
                bank.submitTransaction(t);

                Thread.sleep(ThreadLocalRandom.current().nextLong(1000)); // Sleep for random amount of time (0->1 seconds)
            }
        }
        catch (InterruptedException e) {
            System.out.println("RandomTransactionGenerator was interrupted.");
        } finally {
            System.out.println("RandomTransactionGenerator is beginning shutdown process:");
            try {
                // Submit 1 poison pill to the queue per AutomatedBankClerk (stop all clerks)
                for (int i = 0; i < Bank.NUM_THREADS; i++) {
                    bank.submitTransaction(new Transaction(-1, 0));
                    System.out.println("Poison pill " +(i+1)+ " submitted.");
                }
            } catch (InterruptedException e) {
                System.out.println("Failed to submit poison pill: " + e.getMessage());
            }
        }
    }

    /**
     * Generates a random transaction amount
     * @return a random float value between -10000 and 10000
     */
    private float generateRandomTransactionAmount() {
        return (rand.nextFloat() * 20000) - 10000;
    }

    /**
     * Selects a random account number from the list of all bank accounts
     * @return a randomly selected account number
     */
    private Integer getRandomAccountNumber() {
        return accountNums.get(rand.nextInt(accountNums.size()));
    }
}
