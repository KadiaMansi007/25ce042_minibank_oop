package model;
import exception.*;
import model.annotation.Id;
import model.annotation.Positive;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;
    private static final AtomicInteger counter = new AtomicInteger(1);
    @Id
    private final String accountNumber;
    private final String ownerName;
    @Positive
    private long balance;
    private boolean active = true;

    protected Account(String ownerName, long openingBalance) {
        this("AC" + String.format("%04d", counter.getAndIncrement()), ownerName, openingBalance);
    }
    protected Account(String accountNumber, String ownerName, long openingBalance) {
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public synchronized long getBalance() { return balance; }
    public synchronized long getBalanceForInterest() { return balance; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive");
        balance += amount;
    }
    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive");
        if (!canWithdraw(amount)) throw new InsufficientFundsException(Math.max(0, amount - balance));
        balance -= amount;
        return true;
    }
    public abstract boolean canWithdraw(long amount);
    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account other)) return false;
        return accountNumber.equals(other.accountNumber);
    }
    @Override
    public int hashCode() { return Objects.hash(accountNumber); }
    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " | " + (active ? "Active" : "Inactive");
    }
}
