package banking;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Account {
    protected int accountNumber;
    protected double balance;
    protected String dateCreated;
    protected int ownerId; // Customer ID
    protected List<Transaction> transactions = new ArrayList<>();
    protected List<Deduction> deductions = new ArrayList<>();

    protected SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Account(int ownerId, int accountNumber, double initialDeposit) {
        this.ownerId = ownerId;
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
        this.dateCreated = dtf.format(new Date());
        if (initialDeposit > 0) {
            transactions.add(new Transaction("Opening Deposit", initialDeposit, balance));
        }
    }

    // Getters
    public int getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getDateCreated() { return dateCreated; }
    public int getOwnerId() { return ownerId; }

    // Abstract methods
    public abstract boolean makeDeposit(double amount, String description);
    public abstract boolean makeWithdrawal(double amount, String description);
    public abstract void checkBalance(String customerName);
    public abstract void printStatement(Customer owner);
    public abstract boolean transferAmount(Account toAccount, double amount, String description);
    public abstract void calculateZakat();
    public abstract void displayAllDeductions();

    // Helpers
    protected void recordTransaction(String type, double amount, double afterBalance) {
        transactions.add(new Transaction(type, amount, afterBalance));
    }

    protected void addDeduction(String type, double amount) {
        deductions.add(new Deduction(type, amount, dtf.format(new Date())));
    }

    public void receiveTransfer(double amount, String description) {
        balance += amount;
        recordTransaction("Transfer: " + description, amount, balance);
    }
}
