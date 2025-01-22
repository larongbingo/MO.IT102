import java.util.LinkedList;
import java.util.Scanner;

public class Week2GroupActivity {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        System.out.print("Number of students: ");
        var numberOfStudents = scanner.nextInt();

        var listOfStudents = new Student[numberOfStudents];

        for(var i = 0; i < numberOfStudents; i++) {

            System.out.print("Name: ");
            var newStudentName = scanner.next();

            System.out.print("GRADE: ");
            var grade = scanner.nextInt();

            listOfStudents[i] = new Student(newStudentName, grade);
        }

        for(var student : listOfStudents) {
            System.out.println(String.format("%s, %s", student.name, student.grade));
        }
    }

    public static void main2(String[] args) {
        var music = new LinkedList<String>();
        music.add("Test3");
        music.add("Test2");

        System.out.print("Index of the music you want to remove: ");

    }
}

class Student {
    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    public String name;
    public int grade;
}
