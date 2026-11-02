package banking;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
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

    @Override
    public String toString() {
        return String.format("%s | %s | Amount: %.2f | Balance: %.2f",
                timestamp, type, amount, balanceAfter);
    }
}
