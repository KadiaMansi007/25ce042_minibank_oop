package model;
public interface InterestBearing {
    double interestRate();
    default double yearlyInterest() {
        return getBalanceForInterest() * interestRate() / 100.0;
    }
    long getBalanceForInterest();
}
