package service;
import exception.*;
import model.*;
import util.Validator;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
public class BankService {
    private final Repository<Account> repository = new Repository<>();
    private final AccountStore store = new AccountStore();
    private final Path dataFile = Path.of("data", "accounts.dat");
    private final Path logFile = Path.of("logs", "transactions.log");

    public BankService() {
        try {
            for (Account account : StatePersister.load(dataFile)) {
                repository.save(account);
                store.add(account);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Saved data could not be loaded.");
        }
    }
    public Account openAccount(String name, long amount, String type) throws BankException {
        if (name == null || name.isBlank()) throw new BankException("Owner name is required");
        if (amount < 0) throw new InvalidAmountException("Opening balance cannot be negative");
        Account account = switch (type.toUpperCase()) {
            case "CURRENT" -> new CurrentAccount(name, amount);
            case "FIXED" -> new FixedDepositAccount(name, amount);
            default -> new SavingsAccount(name, amount);
        };
        repository.save(account);
        store.add(account);
        record("OPEN " + account.getAccountNumber() + " " + amount);
        return account;
    }
    public Account find(String number) throws AccountNotFoundException {
        Account account = repository.findById(number);
        if (account == null) throw new AccountNotFoundException(number);
        return account;
    }
    public void deposit(String number, long amount) throws BankException {
        Account account = find(number);
        account.deposit(amount);
        record("DEPOSIT " + number + " " + amount);
    }
    public void withdraw(String number, long amount) throws BankException {
        Account account = find(number);
        account.withdraw(amount);
        record("WITHDRAW " + number + " " + amount);
    }
    public void transfer(String from, String to, long amount) throws BankException {
        if (amount <= 0) throw new InvalidAmountException("Transfer amount must be positive");
        Account source = find(from);
        Account target = find(to);
        Account first = from.compareTo(to) < 0 ? source : target;
        Account second = first == source ? target : source;
        synchronized (first) {
            synchronized (second) {
                source.withdraw(amount);
                try {
                    target.deposit(amount);
                } catch (InvalidAmountException e) {
                    source.deposit(amount);
                    throw e;
                }
            }
        }
        record("WITHDRAW " + from + " " + amount);
        record("DEPOSIT " + to + " " + amount);
        record("TRANSFER " + from + " " + to + " " + amount);
    }
    public String statement(String number) throws BankException {
        Account account = find(number);
        return "MiniBank Statement\nAccount: " + account.getAccountNumber() +
            "\nOwner: " + account.getOwnerName() + "\nBalance: " + account.getBalance() +
            "\nInterest rate: " + account.interestRate() + "%";
    }
    public ArrayList<Account> listAccounts() { return store.sortedByBalance(); }
    public String report() throws IOException {
        return ReportGenerator.generate(Path.of("logs"), Path.of("data", "report.txt"));
    }
    public void batchDeposits(String number, int count, long amount) throws BankException {
        Account account = find(number);
        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 0; i < count; i++) {
            processor.submit(() -> {
                try { account.deposit(amount); }
                catch (InvalidAmountException e) { throw new RuntimeException(e); }
            });
        }
        processor.stop();
        record("DEPOSIT " + number + " " + (count * amount));
    }
    public void save() {
        try { StatePersister.save(store.getAll().toArray(new Account[0]), dataFile); }
        catch (IOException e) { System.out.println("Could not save account data."); }
    }
    private void record(String line) {
        store.addHistory(line);
        try { TransactionLog.append(logFile, line); }
        catch (IOException e) { System.out.println("Could not write transaction log."); }
    }
}
