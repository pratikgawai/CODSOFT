import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManagementSystem sms = new StudentManagementSystem();
        int choice;

        do {
            System.out.println("\n=== STUDENT MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Roll No: ");
                    int roll = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Grade: ");
                    String grade = sc.nextLine();

                    if (name.isEmpty() || grade.isEmpty()) {
                        System.out.println("Fields cannot be empty.");
                    } else {
                        sms.addStudent(new Student(roll, name, grade));
                    }
                    break;

                case 2:
                    System.out.print("Enter Roll No to remove: ");
                    sms.removeStudent(sc.nextInt());
                    break;

                case 3:
                    System.out.print("Enter Roll No to search: ");
                    sms.searchStudent(sc.nextInt());
                    break;

                case 4:
                    sms.displayAll();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}
