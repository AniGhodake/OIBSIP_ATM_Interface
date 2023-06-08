import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class BankAccount {
    private String accountNumber;
    private String password;
    private double balance;
    private ArrayList<Transaction> transactions;

    public BankAccount(String accountNumber, String password) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }

        balance += amount;
        Transaction transaction = new Transaction("Deposit", amount);
        transactions.add(transaction);

        System.out.println("$" + amount + " deposited successfully.");
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }

        if (balance < amount) {
            System.out.println("Insufficient funds.");
            return;
        }

        balance -= amount;
        Transaction transaction = new Transaction("Withdrawal", amount);
        transactions.add(transaction);

        System.out.println("$" + amount + " withdrawn successfully.");
    }

    public void transfer(BankAccount destinationAccount, double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }

        if (balance < amount) {
            System.out.println("Insufficient funds.");
            return;
        }

        balance -= amount;
        Transaction transaction = new Transaction("Transfer to " + destinationAccount.getAccountNumber(), amount);
        transactions.add(transaction);

        destinationAccount.balance += amount;
        Transaction destinationTransaction = new Transaction("Transfer from " + accountNumber, amount);
        destinationAccount.transactions.add(destinationTransaction);

        System.out.println("$" + amount + " transferred successfully to " + destinationAccount.getAccountNumber());
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getType() + ": $" + transaction.getAmount());
        }
    }
}

class ATM {
    private HashMap<String, BankAccount> accounts;
    private BankAccount currentAccount;
    private Scanner scanner;

    public ATM() {
        this.accounts = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to the ATM!");

        boolean quit = false;
        while (!quit) {
            displayMenu();

            int option = getOption();

            switch (option) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    if (currentAccount != null) {
                        currentAccount.showTransactionHistory();
                    } else {
                        System.out.println("No account selected. Please login first.");
                    }
                    break;
                case 4:
                    if (currentAccount != null) {
                        withdraw();
                    } else {
                        System.out.println("No account selected. Please login first.");
                    }
                    break;
                case 5:
                    if (currentAccount != null) {
                        deposit();
                    } else {
                        System.out.println("No account selected. Please login first.");
                    }
                    break;
                case 6:
                    if (currentAccount != null) {
                        transfer();
                    } else {
                        System.out.println("No account selected. Please login first.");
                    }
                    break;
                case 7:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        System.out.println("Thank you for using the ATM!");
    }

    private void displayMenu() {
        System.out.println("------------------------------");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Transactions History");
        System.out.println("4. Withdraw");
        System.out.println("5. Deposit");
        System.out.println("6. Transfer");
        System.out.println("7. Quit");
        System.out.println("------------------------------");
    }

    private int getOption() {
        System.out.print("Enter your option: ");
        return scanner.nextInt();
    }

    private void createAccount() {
        System.out.print("Enter a new account number: ");
        String accountNumber = scanner.next();

        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account number already exists.");
            return;
        }

        System.out.print("Enter a password for the account: ");
        String password = scanner.next();

        BankAccount account = new BankAccount(accountNumber, password);
        accounts.put(accountNumber, account);

        System.out.println("Account created successfully.");
    }

    private void login() {
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.next();

        BankAccount account = accounts.get(accountNumber);

        if (account != null) {
            System.out.print("Enter your password: ");
            String password = scanner.next();

            if (account.getPassword().equals(password)) {
                currentAccount = account;
                System.out.println("Login successful. Account selected: " + currentAccount.getAccountNumber());
            } else {
                System.out.println("Invalid password.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private void withdraw() {
        System.out.print("Enter the withdrawal amount: ");
        double amount = scanner.nextDouble();
        currentAccount.withdraw(amount);
    }

    private void deposit() {
        System.out.print("Enter the deposit amount: ");
        double amount = scanner.nextDouble();
        currentAccount.deposit(amount);
    }

    private void transfer() {
        System.out.print("Enter the destination account number: ");
        String destinationAccountNumber = scanner.next();

        BankAccount destinationAccount = accounts.get(destinationAccountNumber);

        if (destinationAccount != null) {
            System.out.print("Enter the transfer amount: ");
            double amount = scanner.nextDouble();
            currentAccount.transfer(destinationAccount, amount);
        } else {
            System.out.println("Destination account not found.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.run();
    }
}
