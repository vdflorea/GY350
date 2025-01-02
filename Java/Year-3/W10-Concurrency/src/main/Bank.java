package main;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Represents a Bank that manages accounts and processes transactions
 * -> Uses a thread pool to handle multiple transactions concurrently
 * @author Vlad Florea 22409144
 */
public class Bank {
    private Map<Integer, Account> accounts;
    private final LinkedBlockingQueue<Transaction> transactionQueue;
    private final RandomTransactionGenerator generator;
    private final ExecutorService executor;
    public static final int NUM_THREADS = 2;

    /**
     * Creates a Bank instance
     * -> Initialises the accounts map, transaction queue and thread pool
     */
    public Bank() {
        accounts = new ConcurrentHashMap<>();
        transactionQueue = new LinkedBlockingQueue<>();
        executor = Executors.newFixedThreadPool(NUM_THREADS);
        generator = new RandomTransactionGenerator(this);
    }

    /**
     * Adds a new account to the bank
     * @param account the account to be added
     */
    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    /**
     * Retrieves a specific account based on the account number
     * @param accountNumber the account number to look up
     * @return the account associated with the given account number
     */
    public Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Retrieves the RandomTransactionGenerator associated with this bank
     * @return the RandomTransactionGenerator instance
     */
    public RandomTransactionGenerator getRandomTransactionGenerator() {
        return generator;
    }

    /**
     * Submits a given transaction to this bank's transaction queue
     * @param transaction the transaction to be submitted
     * @throws InterruptedException if the thread is interrupted while waiting to put the transaction in the queue
     */
    public void submitTransaction(Transaction transaction) throws InterruptedException {
        System.out.println("Bank (Added to Queue): " + transaction);
        transactionQueue.put(transaction);
    }

    /**
     * Prints details of all accounts in the bank
     */
    public void printAllAccountDetails() {
        for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
            System.out.println("Account number: " + entry.getKey() + ", Balance: " + entry.getValue().getBalance() + " " + entry.getValue().getBalance().getCurrencyUnit());
        }
    }

    /**
     * Retrieves the numbers of all accounts in the bank
     * @return a Collection of all account numbers in the bank
     */
    public Collection<Integer> getAllAccountNumbers() {
        return accounts.keySet();
    }

    /**
     * Opens the bank by starting a number of AutomatedBankClerk instances
     */
    public void openBank() {
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(new AutomatedBankClerk("ABC" + (i+1), this));
        }
    }

    /**
     * Closes the bank by shutting down the executor service
     */
    public void closeBank() {
        executor.shutdown();
        try {
            // Force shutdown if executor service has not terminated after 10 seconds
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // Force shutdown if an interruption occurs
            executor.shutdownNow();
        }
    }

    /**
     * Inner class representing an AutomatedBankClerk that processes transactions
     * -> Multiple instances may be instantiated per Bank
     */
    private class AutomatedBankClerk implements Runnable {
        private final String name;
        private final Bank bank;

        private int numDeposits;
        private int numWithdrawals;

        /**
         * Constructs a new AutomatedBankClerk
         * @param name the name of the clerk
         * @param bank the bank associated with the clerk
         */
        public AutomatedBankClerk(String name, Bank bank) {
            this.name = name;
            this.bank = bank;
        }

        /**
         * Runs the clerk's transaction processor loop
         */
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Wait for 5 seconds only for a transaction to be retrieved from the queue
                    Transaction transaction = transactionQueue.poll(5, TimeUnit.SECONDS);

                    // Transaction was NOT retrieved after 5 seconds
                    if (transaction == null) {
                        break;
                    }

                    // Reached poison pill; Print summary & shutdown clerk
                    if (transaction.getAccountNumber() == -1) {
                        break;
                    }

                    System.out.println(name + " --> Processing a " + (transaction.getAmount() > 0 ? "DEPOSIT" : "WITHDRAWAL") + " of " +transaction.getAmount()+ " from ACCOUNT " + transaction.getAccountNumber());
                    Account account = bank.getAccount(transaction.getAccountNumber()); // Retrieve the account

                    if (account != null) {
                        if (transaction.getAmount() > 0) { // Deposit
                            Money amt = Money.of(CurrencyUnit.EUR, transaction.getAmount(), RoundingMode.HALF_EVEN);
                            account.makeDeposit(amt);
                            numDeposits++;
                        } else if (transaction.getAmount() < 0) { // Withdraw
                            Money amt = Money.of(CurrencyUnit.EUR, transaction.getAmount(), RoundingMode.HALF_EVEN);
                            account.makeWithdrawal(amt);
                            numWithdrawals++;
                        }
                    }

                    Thread.sleep(ThreadLocalRandom.current().nextLong(1000)); // Sleep for random amount of time (0->1 seconds)
                } catch (InterruptedException e) {
                    System.out.println(name + " was interrupted, exiting.");
                } catch (InsufficientFundsException e) {
                    System.out.println(name + " encountered account with insufficient funds: " + e.getMessage());
                }
            }

            printSummary();
        }

        /**
         * Prints a summary of the transactions processed by this clerk
         */
        private void printSummary() {
            System.out.println(name + " finished. Processed " +numDeposits + " deposits, and " +numWithdrawals + " withdrawals");
        }
    }
}
