package service;
import model.Account;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class AccountStore {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ArrayList<String> history = new ArrayList<>();
    public Account add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        return account;
    }
    public Account get(String number) { return accounts.get(number); }
    public ArrayList<Account> getAll() { return new ArrayList<>(accounts.values()); }
    public ArrayList<String> getHistory() { return new ArrayList<>(history); }
    public synchronized void addHistory(String line) { history.add(line); }
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }
    public void replaceAll(Account[] items) {
        accounts.clear();
        for (Account account : items) add(account);
    }
}
