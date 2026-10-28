package model;
public class SavingsAccount extends Account implements Premium {
    private static final long serialVersionUID = 1L;
    private final long minBalance;
    public SavingsAccount(String ownerName, long openingBalance) {
        super(ownerName, openingBalance);
        minBalance = 500;
    }
    public SavingsAccount(String accountNumber, String ownerName, long openingBalance) {
        super(accountNumber, ownerName, openingBalance);
        minBalance = 500;
    }
    @Override
    public double interestRate() { return 4.0; }
    @Override
    public boolean canWithdraw(long amount) {
        return getBalance() - amount >= minBalance;
    }
}
