
import orm.ORM;
import gui.MainApp;

public class Main {
    public static void main(String[] args) {
        // args (containing the model names) must always be forwared
        ORM.initializeInstance(new String[] { "Semester", "SemesterCourse", "Specialty", "TeachingAssistant", "Student", "Group", "Enrollment", "AcademicLevel", "Section", "Course" });
        // Test.init();

        MainApp.launch(MainApp.class);
    }
}
