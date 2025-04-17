import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.txt";
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n== Expense Tracker ==");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> viewExpenses();
                case 3 -> deleteExpense(scanner);
                case 4 -> saveExpenses();
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter description: ");
        String desc = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter category (e.g., Food, Travel, Bills): ");
        String category = scanner.nextLine();

        expenses.add(new Expense(desc, amount, category));
        System.out.println("Expense added!");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        double total = 0;
        System.out.println("\n--- All Expenses ---");
        int index = 1;
        for (Expense exp : expenses) {
            System.out.println(index++ + ". " + exp);
            total += exp.getAmount();
        }

        System.out.printf("\nTotal Spent: Rs %.2f\n", total);
    }

    private static void deleteExpense(Scanner scanner) {
        viewExpenses();
        if (expenses.isEmpty()) return;

        System.out.print("Enter the number of the expense to delete: ");
        int index = scanner.nextInt();
        if (index >= 1 && index <= expenses.size()) {
            expenses.remove(index - 1);
            System.out.println("Expense deleted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense exp : expenses) {
                writer.println(exp.getDescription() + "," + exp.getAmount() + "," + exp.getCategory());
            }
            System.out.println("Expenses saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    private static void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length == 3) {
                    String desc = parts[0];
                    double amt = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    expenses.add(new Expense(desc, amt, category));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
