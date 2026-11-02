package banking;

public class CheckingAccount extends Account {
    private static final double transactionFee = 10.0; // Fixed fee
    private int freeTransactions = 2;   // Free transactions per month
    private int usedTransactions = 0;   // Counter

    public CheckingAccount(int ownerId, int accountNumber, double initialDeposit) {
        super(ownerId, accountNumber, initialDeposit);
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
        if (amount <= 0 || amount > balance + 5000) return false; // overdraft allowed
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
        System.out.println("Zakat not applicable for Checking.");
    }

    @Override
    public void displayAllDeductions() {
        if (deductions.isEmpty()) {
            System.out.println("No deductions.");
        }
        for (Deduction d : deductions) {
            System.out.println(d);
        }
    }

    private void applyTransactionFee() {
        usedTransactions++;
        if (usedTransactions > freeTransactions) {
            balance -= transactionFee;
            recordTransaction("Transaction Fee", -transactionFee, balance);
            addDeduction("Transaction Fee", transactionFee);
        }
    }

    public void resetMonthlyFreeTransactions() {
        usedTransactions = 0;
    }
}
