package banking;

public class SavingsAccount extends Account {
    private static double interestRate = 2.5; // Default %

    public SavingsAccount(int ownerId, int accountNumber, double initialDeposit) {
        super(ownerId, accountNumber, initialDeposit);
    }

    @Override
    public boolean makeDeposit(double amount, String description) {
        if (amount <= 0) return false;
        balance += amount;
        recordTransaction(description, amount, balance);
        return true;
    }

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
        if (deductions.isEmpty()) {
            System.out.println("No deductions.");
        }
        for (Deduction d : deductions) {
            System.out.println(d);
        }
    }

    public void calculateInterest() {
        double interest = (balance * interestRate) / 100;
        balance += interest;
        recordTransaction("Interest", interest, balance);
        System.out.println("Interest applied: " + interest);
    }

    public static void setInterestRate(double rate) {
        interestRate = rate;
    }
}
