package banking;

public class Customer {
    private int customerId; // Unique customer ID
    private String name;
    private String address;
    private String phone;

    private SavingsAccount savings;
    private CheckingAccount checking;

    public Customer(int customerId, String name, String address, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    // Getters
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }

    // Accounts
    public SavingsAccount getSavingsAccount() { return savings; }
    public CheckingAccount getCheckingAccount() { return checking; }
    public void setSavingsAccount(SavingsAccount s) { this.savings = s; }
    public void setCheckingAccount(CheckingAccount c) { this.checking = c; }
}
