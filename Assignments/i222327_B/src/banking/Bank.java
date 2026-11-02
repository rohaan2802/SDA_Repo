package banking;

import java.util.*;

public class Bank {
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<Integer, Account> accounts = new HashMap<>();
    private static int nextCustomerId = 5001;   // Unique Customer IDs
    private static int nextAccountNumber = 1001; // Unique Account Numbers

    private String bankName;
    private String bankOwner;

    public Bank(String name, String owner) {
        this.bankName = name;
        this.bankOwner = owner;
    }

    // Generate unique IDs
    public int generateCustomerId() { return nextCustomerId++; }
    public int generateAccountNumber() { return nextAccountNumber++; }

    // Open account(s) for a new customer
    public Customer openAccount(String name, String address, String phone,
                                boolean openSavings, boolean openChecking, double initialDeposit) {
        int custId = generateCustomerId();
        Customer c = new Customer(custId, name, address, phone);
        customers.put(custId, c);

        if (openSavings) {
            int accNo = generateAccountNumber();
            SavingsAccount s = new SavingsAccount(custId, accNo, initialDeposit);
            accounts.put(accNo, s);
            c.setSavingsAccount(s);
        }
        if (openChecking) {
            int accNo = generateAccountNumber();
            CheckingAccount ch = new CheckingAccount(custId, accNo, initialDeposit);
            accounts.put(accNo, ch);
            c.setCheckingAccount(ch);
        }
        return c;
    }

    // Close a customer and remove their accounts
    public boolean closeAccount(int customerId) {
        Customer c = customers.remove(customerId);
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
    public Customer getCustomer(int custId) { return customers.get(custId); }
    public Collection<Account> getAllAccounts() { return accounts.values(); }
    public Collection<Customer> getAllCustomers() { return customers.values(); }

    // Getters
    public String getBankName() { return bankName; }
    public String getBankOwner() { return bankOwner; }

    // Show summary
    public void displayBankDetails() {
        System.out.println("Bank Name : " + bankName);
        System.out.println("Bank Owner: " + bankOwner);
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Total Accounts: " + accounts.size());
    }
}
