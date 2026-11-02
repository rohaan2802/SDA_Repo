package banking;

import java.util.Scanner;

public class ConsoleUI {
    private Bank bank;
    private Scanner sc = new Scanner(System.in);

    public ConsoleUI(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        System.out.println("Welcome to " + bank.getBankName() + " Account Management System");
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

    // ------------ Main Menu ------------
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

    // ------------ Account Creation ------------
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

        System.out.println("Customer created with ID: " + c.getCustomerId());
        if (c.getSavingsAccount() != null)
            System.out.println("  Savings Account: " + c.getSavingsAccount().getAccountNumber());
        if (c.getCheckingAccount() != null)
            System.out.println("  Checking Account: " + c.getCheckingAccount().getAccountNumber());
    }

    private void closeAccountMenu() {
        int custId = readInt("Enter customer ID to close: ");
        boolean ok = bank.closeAccount(custId);
        System.out.println(ok ? "Account closed." : "Customer not found.");
    }

    // ------------ Login ------------
    private void loginMenu() {
        int custId = readInt("Enter customer ID: ");
        Customer c = bank.getCustomer(custId);
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

    // ------------ Account Ops ------------
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

    // ------------ Admin Tools ------------
    private void adjustSavingsInterest() {
        double r = readDouble("New savings interest rate: ");
        SavingsAccount.setInterestRate(r);
        System.out.println("Updated interest rate: " + r + "%");
    }

    private void displayAllAccounts() {
        for (Account a : bank.getAllAccounts()) {
            Customer c = bank.getCustomer(a.getOwnerId());
            System.out.println("------------------------------------------------");
            System.out.println("Account Number: " + a.getAccountNumber());
            System.out.println("Type: " + a.getClass().getSimpleName());
            System.out.println("Owner: " + (c != null ? c.getName() : "Unknown"));
            System.out.println("Balance: " + a.getBalance());
        }
    }

    private void displayAllDeductions() {
        for (Account a : bank.getAllAccounts()) {
            Customer c = bank.getCustomer(a.getOwnerId());
            System.out.println("------------------------------------------------");
            System.out.println("Account: " + a.getAccountNumber() + " (" + a.getClass().getSimpleName() + ")");
            System.out.println("Owner: " + (c != null ? c.getName() : "Unknown"));
            a.displayAllDeductions();
        }
    }

    private void advanceMonth() {
        for (Account a : bank.getAllAccounts()) {
            if (a instanceof CheckingAccount) {
                ((CheckingAccount) a).resetMonthlyFreeTransactions();
            }
        }
        System.out.println("Month advanced. Counters reset.");
    }

    // ------------ Helpers ------------
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

    private String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private boolean yesNo(String prompt) {
        System.out.print(prompt);
        String ans = sc.nextLine().trim().toLowerCase();
        return ans.equals("y") || ans.equals("yes");
    }
}
