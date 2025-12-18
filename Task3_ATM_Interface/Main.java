import java.util.* ;
class BankAccount {
    private double accountBalance;

    // Constructor to initialize the account with an initial balance
    public BankAccount(double initialBalance) {
        this.accountBalance = initialBalance;
    }

    // Method to deposit money into the account
    public void deposit(double transactionAmount) {
        if (transactionAmount > 0) {
            accountBalance += transactionAmount;
            System.out.println("Successfully deposited ₹" + transactionAmount);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    // Method to withdraw money from the account
    public void withdraw(double transactionAmount) {
        if (transactionAmount > 0 && transactionAmount <= accountBalance) {
            accountBalance -= transactionAmount;
            System.out.println("Successfully withdrew ₹" + transactionAmount);
        } else {
            System.out.println("Insufficient balance or invalid amount!");
        }
    }

    // Method to retrieve the current account balance
    public double getBalance() {
        return accountBalance;
    }
}

// Class representing an ATM machine
class ATM {
    private BankAccount userAccount;
    private Scanner scanner = new Scanner(System.in);

    // Constructor to initialize the ATM with a linked bank account
    public ATM(BankAccount account) {
        this.userAccount = account;
    }

    // Display the ATM menu to the user
    public void displayMenu() {
        int userChoice;

        do {
            System.out.println(" ----- ATM Menu -----");
                    System.out.println("1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            userChoice = scanner.nextInt();

            switch (userChoice) {
                case 1:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawalAmount = scanner.nextDouble();
                    userAccount.withdraw(withdrawalAmount);
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    userAccount.deposit(depositAmount);
                    break;

                case 3:
                    System.out.println("Current Balance: ₹" + userAccount.getBalance());
                    break;

                case 4:
                    System.out.println("Thank you for using ATM!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (userChoice != 4);
    }
}

// Main class to run the ATM application
public class Main {
    public static void main(String[] args) {
        // Create a bank account with an initial balance of ₹5000
        BankAccount userAccount = new BankAccount(5000);


        ATM atmMachine = new ATM(userAccount);

        atmMachine.displayMenu();
    }
}
