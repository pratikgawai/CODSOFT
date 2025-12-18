import java.io.*;
import java.util.ArrayList;

public class StudentManagementSystem {

    private ArrayList<Student> students;
    private final String FILE_NAME = "students.dat";

    public StudentManagementSystem() {
        students = loadFromFile();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
        System.out.println("Student added successfully.");
    }

    public void removeStudent(int rollNo) {
        for (Student s : students) {
            if (s.getRollNo() == rollNo) {
                students.remove(s);
                saveToFile();
                System.out.println("Student removed successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void searchStudent(int rollNo) {
        for (Student s : students) {
            if (s.getRollNo() == rollNo) {
                System.out.println(s);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void displayAll() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    private ArrayList<Student> loadFromFile() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ArrayList<Student>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
