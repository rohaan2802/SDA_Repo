import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Account Management System (Console-Based)
 *
 * This program simulates a bank's account management system.
 * - Supports two account types: Savings and Checking
 * - Allows deposits, withdrawals, transfers, interest, zakat
 * - Tracks transactions and deductions (fees, zakat)
 * - Admin console to open/close accounts, manage all customers
 *
 * Author: Mohammad Rohaan (22i-2327)
 */
public class AccountManagementSystem {
    public static void main(String[] args) {
        // Create a bank and start the admin console UI
        Bank bank = new Bank("PakTrust Bank", "Mr. Ahmad Khan");
        ConsoleUI ui = new ConsoleUI(bank);
        ui.start();
    }
}

/* ================================================
   BANK & ADMIN CONSOLE
   ================================================ */

/**
 * Bank class: manages all customers and accounts
 */
class Bank {
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<Integer, Account> accounts = new HashMap<>();
    private static int nextAccountNumber = 1001;  // unique generator

    private String bankName;
    private String bankOwner;

    public Bank(String name, String owner) {
        this.bankName = name;
        this.bankOwner = owner;
    }

    // Generate a unique account number
    public int generateAccountNumber() {
        return nextAccountNumber++;
    }

    // Create a new customer and open their accounts
    public Customer openAccount(String name, String address, String phone,
                                boolean openSavings, boolean openChecking, double initialDeposit) {
        int accNo = generateAccountNumber();
        Customer c = new Customer(name, address, phone, accNo);
        customers.put(accNo, c);

        // Open savings account
        if (openSavings) {
            SavingsAccount s = new SavingsAccount(accNo, initialDeposit);
            accounts.put(s.getAccountNumber(), s);
            c.setSavingsAccount(s);
        }
        // Open checking account
        if (openChecking) {
            CheckingAccount ch = new CheckingAccount(accNo, initialDeposit);
            accounts.put(ch.getAccountNumber(), ch);
            c.setCheckingAccount(ch);
        }
        return c;
    }

    // Close a customer and remove their accounts
    public boolean closeAccount(int accountNumber) {
        Customer c = customers.remove(accountNumber);
        if (c != null) {
            if (c.getSavingsAccount() != null)
                accounts.remove(c.getSavingsAccount().getAccountNumber());
            if (c.getCheckingAccount() != null)
                accounts.remove(c.getCheckingAccount().getAccountNumber());
            return true;
        }
        return false;
    }

    // Lookup functions
    public Account getAccount(int accNo) { return accounts.get(accNo); }
    public Customer getCustomerByAccountNumber(int accNo) { return customers.get(accNo); }
    public Collection<Account> getAllAccounts() { return accounts.values(); }
    public Collection<Customer> getAllCustomers() { return customers.values(); }

    // Show summary of bank
    public void displayBankDetails() {
        System.out.println("Bank Name : " + bankName);
        System.out.println("Bank Owner: " + bankOwner);
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Total Accounts: " + accounts.size());
    }
}

/**
 * ConsoleUI class: handles all menus and user interaction
 */
class ConsoleUI {
    private Bank bank;
    private Scanner sc = new Scanner(System.in);

    public ConsoleUI(Bank bank) {
        this.bank = bank;
    }

    // Start main menu loop
    public void start() {
        System.out.println("Welcome to " + bank.getClass() + " Account Management System");
        boolean exit = false;

        while (!exit) {
            showMainMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1: newAccountMenu(); break;
                case 2: closeAccountMenu(); break;
                case 3: loginMenu(); break;
                case 4: adjustSavingsInterest(); break;
                case 5: bank.displayBankDetails(); break;
                case 6: displayAllAccounts(); break;
                case 7: displayAllDeductions(); break;
                case 8: advanceMonth(); break;
                case 9: exit = true; break;
                default: System.out.println("Invalid choice."); break;
            }
            System.out.println();
        }
        System.out.println("Exiting. Goodbye!");
    }

    // ----------- Main Menu -----------
    private void showMainMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println("1. Open New Account");
        System.out.println("2. Close Account");
        System.out.println("3. Login to Account");
        System.out.println("4. Set Savings Interest Rate");
        System.out.println("5. Display Bank Details");
        System.out.println("6. Display All Accounts");
        System.out.println("7. Display All Deductions");
        System.out.println("8. Advance Month (reset checking counters)");
        System.out.println("9. Exit");
        System.out.println("-------------------------------------------------");
    }

    // ----------- Account Creation / Closure -----------
    private void newAccountMenu() {
        System.out.println("-- Open New Account --");
        String name = readString("Customer name: ");
        String addr = readString("Address: ");
        String phone = readString("Phone: ");
        boolean openSaving = yesNo("Open Savings account? (y/n): ");
        boolean openChecking = yesNo("Open Checking account? (y/n): ");

        if (!openSaving && !openChecking) {
            System.out.println("Must open at least one account.");
            return;
        }

        double initial = readDouble("Initial deposit: ");
        Customer c = bank.openAccount(name, addr, phone, openSaving, openChecking, initial);

        System.out.println("Customer created with ID: " + c.getAccountNumber());
        if (c.getSavingsAccount() != null)
            System.out.println("  Savings Account: " + c.getSavingsAccount().getAccountNumber());
        if (c.getCheckingAccount() != null)
            System.out.println("  Checking Account: " + c.getCheckingAccount().getAccountNumber());
    }

    private void closeAccountMenu() {
        int acc = readInt("Enter customer ID to close: ");
        boolean ok = bank.closeAccount(acc);
        System.out.println(ok ? "Account closed." : "Customer not found.");
    }

    // ----------- Login & Customer Menu -----------
    private void loginMenu() {
        int acc = readInt("Enter customer ID: ");
        Customer c = bank.getCustomerByAccountNumber(acc);
        if (c == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Welcome, " + c.getName());
        boolean logout = false;

        while (!logout) {
            showCustomerMenu();
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1: depositMenu(c); break;
                case 2: withdrawMenu(c); break;
                case 3: transferMenu(c); break;
                case 4: checkBalanceMenu(c); break;
                case 5: printStatementMenu(c); break;
                case 6: zakatMenu(c); break;
                case 7: interestMenu(c); break;
                case 8: showDeductionsMenu(c); break;
                case 9: logout = true; break;
                default: System.out.println("Invalid."); break;
            }
            System.out.println();
        }
    }

    private void showCustomerMenu() {
        System.out.println("---- Account Operations ----");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Check Balance");
        System.out.println("5. Print Statement");
        System.out.println("6. Apply Zakat (Savings)");
        System.out.println("7. Apply Interest (Savings)");
        System.out.println("8. Show Deductions");
        System.out.println("9. Logout");
    }

    // ----------- Account Operations -----------
    private void depositMenu(Customer c) {
        Account a = chooseAccount(c, "deposit into");
        if (a == null) return;

        double amt = readDouble("Amount: ");
        if (a.makeDeposit(amt, "Deposit")) {
            System.out.println("Deposit successful. Balance: " + a.getBalance());
        } else {
            System.out.println("Deposit failed.");
        }
    }

    private void withdrawMenu(Customer c) {
        Account a = chooseAccount(c, "withdraw from");
        if (a == null) return;

        double amt = readDouble("Amount: ");
        if (a.makeWithdrawal(amt, "Withdrawal")) {
            System.out.println("Withdrawal successful. Balance: " + a.getBalance());
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    private void transferMenu(Customer c) {
        Account from = chooseAccount(c, "transfer from");
        if (from == null) return;

        int toAcc = readInt("Destination account number: ");
        Account to = bank.getAccount(toAcc);
        if (to == null) {
            System.out.println("Destination not found.");
            return;
        }

        double amt = readDouble("Amount: ");
        if (from.transferAmount(to, amt, "Transfer")) {
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Transfer failed.");
        }
    }

    private void checkBalanceMenu(Customer c) {
        Account a = chooseAccount(c, "check balance for");
        if (a != null) a.checkBalance(c.getName());
    }

    private void printStatementMenu(Customer c) {
        Account a = chooseAccount(c, "print statement for");
        if (a != null) a.printStatement(c);
    }

    private void zakatMenu(Customer c) {
        if (c.getSavingsAccount() != null) {
            c.getSavingsAccount().calculateZakat();
        } else {
            System.out.println("No savings account.");
        }
    }

    private void interestMenu(Customer c) {
        if (c.getSavingsAccount() != null) {
            c.getSavingsAccount().calculateInterest();
        } else {
            System.out.println("No savings account.");
        }
    }

    private void showDeductionsMenu(Customer c) {
        if (c.getSavingsAccount() != null) {
            System.out.println("Savings deductions:");
            c.getSavingsAccount().displayAllDeductions();
        }
        if (c.getCheckingAccount() != null) {
            System.out.println("Checking deductions:");
            c.getCheckingAccount().displayAllDeductions();
        }
    }

    // ----------- Admin Tools -----------
    private void adjustSavingsInterest() {
        double r = readDouble("New savings interest rate: ");
        SavingsAccount.setInterestRate(r);
        System.out.println("Updated interest rate: " + r + "%");
    }

    private void displayAllAccounts() {
        for (Account a : bank.getAllAccounts()) {
            Customer c = bank.getCustomerByAccountNumber(a.getOwnerId());
            System.out.println("------------------------------------------------");
            System.out.println("Account Number: " + a.getAccountNumber());
            System.out.println("Type: " + a.getClass().getSimpleName());
            System.out.println("Owner: " + (c != null ? c.getName() : "Unknown"));
            System.out.println("Balance: " + a.getBalance());
        }
    }

    private void displayAllDeductions() {
        for (Account a : bank.getAllAccounts()) {
            Customer c = bank.getCustomerByAccountNumber(a.getOwnerId());
            System.out.println("------------------------------------------------");
            System.out.println("Account: " + a.getAccountNumber() + " (" + a.getClass().getSimpleName() + ")");
            System.out.println("Owner: " + (c != null ? c.getName() : "Unknown"));
            a.displayAllDeductions();
        }
    }

    private void advanceMonth() {
        // Resets checking accounts' free transaction counters
        for (Account a : bank.getAllAccounts()) {
            if (a instanceof CheckingAccount) {
                ((CheckingAccount) a).resetMonthlyFreeTransactions();
            }
        }
        System.out.println("Month advanced. Counters reset.");
    }

    // ----------- Helper Methods -----------
    private Account chooseAccount(Customer c, String action) {
        if (c.getSavingsAccount() != null && c.getCheckingAccount() != null) {
            System.out.println("Choose account to " + action + ":");
            System.out.println("1. Savings");
            System.out.println("2. Checking");
            int choice = readInt("Choice: ");
            return (choice == 1) ? c.getSavingsAccount() : c.getCheckingAccount();
        } else if (c.getSavingsAccount() != null) {
            return c.getSavingsAccount();
        } else if (c.getCheckingAccount() != null) {
            return c.getCheckingAccount();
        }
        System.out.println("No accounts available.");
        return null;
    }

    // Input helpers
    private String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) { System.out.println("Invalid number."); }
        }
    }
    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) { System.out.println("Invalid number."); }
        }
    }
    private boolean yesNo(String prompt) {
        System.out.print(prompt);
        String ans = sc.nextLine().trim().toLowerCase();
        return ans.equals("y") || ans.equals("yes");
    }
}

/* ================================================
   CUSTOMER
   ================================================ */

class Customer {
    private String name;
    private String address;
    private String phone;
    private int accountNumber; // Unique customer ID
    private SavingsAccount savings;
    private CheckingAccount checking;

    public Customer(String name, String address, String phone, int accountNumber) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.accountNumber = accountNumber;
    }

    // Getters
    public String getName() { return name; }
    public int getAccountNumber() { return accountNumber; }
    public SavingsAccount getSavingsAccount() { return savings; }
    public CheckingAccount getCheckingAccount() { return checking; }

    // Setters
    public void setSavingsAccount(SavingsAccount s) { this.savings = s; }
    public void setCheckingAccount(CheckingAccount c) { this.checking = c; }
}

/* ================================================
   ABSTRACT ACCOUNT
   ================================================ */

abstract class Account {
    protected int accountNumber;
    protected double balance;
    protected String dateCreated;
    protected int ownerId; // Customer ID
    protected List<Transaction> transactions = new ArrayList<>();
    protected List<Deduction> deductions = new ArrayList<>();

    protected SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Account(int ownerId, double initialDeposit) {
        this.ownerId = ownerId;
        this.accountNumber = new Random().nextInt(9000) + 1000; // 4-digit ID
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

    // Helper: record transactions and deductions
    protected void recordTransaction(String type, double amount, double afterBalance) {
        transactions.add(new Transaction(type, amount, afterBalance));
    }
    protected void addDeduction(String type, double amount) {
        deductions.add(new Deduction(type, amount, dtf.format(new Date())));
    }

    // Default transfer receiver
    public void receiveTransfer(double amount, String description) {
        balance += amount;
        recordTransaction("Transfer: " + description, amount, balance);
    }
}

/* ================================================
   TRANSACTION & DEDUCTION
   ================================================ */

class Transaction {
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String toString() {
        return String.format("%s | %s | Amount: %.2f | Balance: %.2f",
                timestamp, type, amount, balanceAfter);
    }
}

class Deduction {
    private String type;
    private double amount;
    private String date;

    public Deduction(String type, double amount, String date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String toString() {
        return String.format("%s | %s | %.2f", date, type, amount);
    }
}

/* ================================================
   SAVINGS ACCOUNT
   ================================================ */

class SavingsAccount extends Account {
    private static double interestRate = 2.5; // default %

    public SavingsAccount(int ownerId, double initialDeposit) {
        super(ownerId, initialDeposit);
    }

    // Deposit: always allowed
    @Override
    public boolean makeDeposit(double amount, String description) {
        if (amount <= 0) return false;
        balance += amount;
        recordTransaction(description, amount, balance);
        return true;
    }

    // Withdraw: cannot exceed balance
    @Override
    public boolean makeWithdrawal(double amount, String description) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        recordTransaction(description, -amount, balance);
        return true;
    }

    @Override
    public void checkBalance(String name) {
        System.out.println("Customer: " + name + " | Savings Balance: " + balance);
    }

    @Override
    public void printStatement(Customer owner) {
        System.out.println("----- Statement (Savings #" + accountNumber + ") -----");
        System.out.println("Customer: " + owner.getName());
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    @Override
    public boolean transferAmount(Account toAccount, double amount, String description) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        recordTransaction("Transfer to " + toAccount.getAccountNumber(), -amount, balance);
        toAccount.receiveTransfer(amount, "from Savings " + accountNumber);
        return true;
    }

    // Zakat: if balance >= 20,000
    @Override
    public void calculateZakat() {
        if (balance >= 20000) {
            double zakat = (balance * 2.5) / 100;
            balance -= zakat;
            recordTransaction("Zakat", -zakat, balance);
            addDeduction("Zakat", zakat);
            System.out.println("Zakat deducted: " + zakat);
        } else {
            System.out.println("Balance < 20,000. No zakat.");
        }
    }

    @Override
    public void displayAllDeductions() {
        if (deductions.isEmpty()) System.out.println("No deductions.");
        for (Deduction d : deductions) System.out.println(d);
    }

    // Interest
    public void calculateInterest() {
        double interest = (balance * interestRate) / 100;
        balance += interest;
        recordTransaction("Interest", interest, balance);
        System.out.println("Interest applied: " + interest);
    }

    // Static setter for interest
    public static void setInterestRate(double rate) {
        interestRate = rate;
    }
}

/* ================================================
   CHECKING ACCOUNT
   ================================================ */

class CheckingAccount extends Account {
    private int freeTransactions = 2;   // 2 free each month
    private int usedTransactions = 0;   // counter
    private double transactionFee = 10; // fee after free

    public CheckingAccount(int ownerId, double initialDeposit) {
        super(ownerId, initialDeposit);
    }

    @Override
    public boolean makeDeposit(double amount, String description) {
        if (amount <= 0) return false;
        balance += amount;
        recordTransaction(description, amount, balance);
        applyTransactionFee();
        return true;
    }

    @Override
    public boolean makeWithdrawal(double amount, String description) {
        // Can withdraw beyond balance, but not more than overdraft limit
        if (amount <= 0 || amount > balance + 5000) return false;
        balance -= amount;
        recordTransaction(description, -amount, balance);
        applyTransactionFee();
        return true;
    }

    @Override
    public void checkBalance(String name) {
        System.out.println("Customer: " + name + " | Checking Balance: " + balance);
    }

    @Override
    public void printStatement(Customer owner) {
        System.out.println("----- Statement (Checking #" + accountNumber + ") -----");
        System.out.println("Customer: " + owner.getName());
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    @Override
    public boolean transferAmount(Account toAccount, double amount, String description) {
        if (amount <= 0 || amount > balance + 5000) return false;
        balance -= amount;
        recordTransaction("Transfer to " + toAccount.getAccountNumber(), -amount, balance);
        toAccount.receiveTransfer(amount, "from Checking " + accountNumber);
        applyTransactionFee();
        return true;
    }

    @Override
    public void calculateZakat() {
        // Not applicable
        System.out.println("Zakat not applicable for Checking.");
    }

    @Override
    public void displayAllDeductions() {
        if (deductions.isEmpty()) System.out.println("No deductions.");
        for (Deduction d : deductions) System.out.println(d);
    }

    // Helper: transaction fees
    private void applyTransactionFee() {
        usedTransactions++;
        if (usedTransactions > freeTransactions) {
            balance -= transactionFee;
            recordTransaction("Transaction Fee", -transactionFee, balance);
            addDeduction("Transaction Fee", transactionFee);
        }
    }

    // Reset counters every month
    public void resetMonthlyFreeTransactions() {
        usedTransactions = 0;
    }
}
