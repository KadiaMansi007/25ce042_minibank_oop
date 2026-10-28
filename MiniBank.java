import exception.BankException;
import model.Account;
import service.BankService;
import util.Validator;
import java.util.Scanner;
public class MiniBank {
    public record BankInfo(String name, String branch) {}
    public enum MenuOption { OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, STATEMENT, LIST_ACCOUNTS, REPORT, BATCH_DEPOSIT, BALANCE_CHECK, EXIT }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bank = new BankService();
        BankInfo info = new BankInfo("MiniBank", "Main Branch");
        System.out.println("================================");
        System.out.println(info.name() + " - " + info.branch());
        System.out.println("================================");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Open account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Statement");
            System.out.println("6. List accounts");
            System.out.println("7. Generate report");
            System.out.println("8. Batch deposits");
            System.out.println("9. Check balance");
            System.out.println("10. Exit");
            System.out.print("Choose: ");
            String input = sc.nextLine().trim();
            int choice;
            try { choice = Integer.parseInt(input); }
            catch (NumberFormatException e) { System.out.println("Enter a valid menu number."); continue; }
            try {
                MenuOption option = switch (choice) {
                    case 1 -> MenuOption.OPEN_ACCOUNT;
                    case 2 -> MenuOption.DEPOSIT;
                    case 3 -> MenuOption.WITHDRAW;
                    case 4 -> MenuOption.TRANSFER;
                    case 5 -> MenuOption.STATEMENT;
                    case 6 -> MenuOption.LIST_ACCOUNTS;
                    case 7 -> MenuOption.REPORT;
                    case 8 -> MenuOption.BATCH_DEPOSIT;
                    case 9 -> MenuOption.BALANCE_CHECK;
                    case 10 -> MenuOption.EXIT;
                    default -> null;
                };
                if (option == null) {
                    System.out.println("Invalid menu number.");
                    continue;
                }
                switch (option) {
                    case OPEN_ACCOUNT -> {
                        System.out.print("Owner name: ");
                        String name = sc.nextLine();
                        System.out.print("Opening balance: ");
                        long amount = Long.parseLong(sc.nextLine());
                        System.out.print("Type (SAVINGS/CURRENT/FIXED): ");
                        String type = sc.nextLine();
                        Account account = bank.openAccount(name, amount, type);
                        System.out.println("Account opened: " + account);
                    }
                    case DEPOSIT -> {
                        System.out.print("Account number: ");
                        String number = sc.nextLine();
                        System.out.print("Amount: ");
                        bank.deposit(number, Long.parseLong(sc.nextLine()));
                        System.out.println("Deposit successful.");
                    }
                    case WITHDRAW -> {
                        System.out.print("Account number: ");
                        String number = sc.nextLine();
                        System.out.print("Amount: ");
                        bank.withdraw(number, Long.parseLong(sc.nextLine()));
                        System.out.println("Withdrawal successful.");
                    }
                    case TRANSFER -> {
                        System.out.print("From account: ");
                        String from = sc.nextLine();
                        System.out.print("To account: ");
                        String to = sc.nextLine();
                        System.out.print("Amount: ");
                        bank.transfer(from, to, Long.parseLong(sc.nextLine()));
                        System.out.println("Transfer successful.");
                    }
                    case STATEMENT -> {
                        System.out.print("Account number: ");
                        System.out.println(bank.statement(sc.nextLine()));
                    }
                    case LIST_ACCOUNTS -> {
                        for (Account account : bank.listAccounts()) System.out.println(account);
                    }
                    case REPORT -> System.out.println(bank.report());
                    case BALANCE_CHECK -> {
                        System.out.print("Account number: ");
                        Account account = bank.find(sc.nextLine());
                        System.out.println("Current balance: " + account.getBalance());
                    }
                    case BATCH_DEPOSIT -> {
                        System.out.print("Account number: ");
                        String number = sc.nextLine();
                        System.out.print("How many deposits? ");
                        int count = Integer.parseInt(sc.nextLine());
                        System.out.print("Amount per deposit: ");
                        long amount = Long.parseLong(sc.nextLine());
                        bank.batchDeposits(number, count, amount);
                        System.out.println("Batch completed.");
                    }
                    case EXIT -> {
                        bank.save();
                        running = false;
                        System.out.println("Thank you for using MiniBank.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (BankException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Operation could not be completed: " + e.getMessage());
            } finally {
                if (running) System.out.println("Ready.");
            }
        }
        sc.close();
    }
}
