package model;
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long overdraftLimit;
    public CurrentAccount(String ownerName, long openingBalance) {
        super(ownerName, openingBalance);
        overdraftLimit = 2000;
    }
    public CurrentAccount(String accountNumber, String ownerName, long openingBalance) {
        super(accountNumber, ownerName, openingBalance);
        overdraftLimit = 2000;
    }
    @Override
    public double interestRate() { return 0.0; }
    @Override
    public boolean canWithdraw(long amount) {
        return getBalance() - amount >= -overdraftLimit;
    }
}
