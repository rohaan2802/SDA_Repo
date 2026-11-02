package banking;

public class Deduction {
    private String type;
    private double amount;
    private String date;

    public Deduction(String type, double amount, String date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %.2f", date, type, amount);
    }
}
