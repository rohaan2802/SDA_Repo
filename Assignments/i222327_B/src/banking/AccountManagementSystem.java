package banking;


public class AccountManagementSystem {
    public static void main(String[] args) {

        Bank bank = new Bank("PakTrust Bank", "Mr. Ahmad Khan");
        ConsoleUI ui = new ConsoleUI(bank);
        ui.start();
    }
}
