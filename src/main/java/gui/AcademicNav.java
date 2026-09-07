package gui;

import java.lang.reflect.Field;
import java.util.List;

import gui.nav.NavLevel;
import gui.nav.NavRow;

import model.AcademicLevel;
import model.Group;
import model.Section;
import model.Student;

import orm.Table;
import orm.Table.Range;
import orm.reflect.Model;
import orm.reflect.Reflected;

/**
 * Builds the day-to-day drill-down tree:
 *
 *   Specialty + Year  ->  Section  ->  Group  ->  Student
 *
 * "Specialty + Year" is rendered as a single synthetic row per
 * {@link AcademicLevel} (e.g. "ST L1"), matching how a professor actually
 * thinks about their assignments, even though it's really the combination
 * of two model entities.
 *
 * NOTE: this deliberately does not touch the Course/grading path yet.
 * "Physique II" as a Course (grading, progress-platform export) and
 * "Physique II" as a day-to-day Group concept are two different fork
 * points in the data model (Course -> SemesterCourse -> Semester ->
 * AcademicLevel, vs. Section -> Group), and that ambiguity should get its
 * own navigator/level rather than being folded into this one silently.
 */
public class AcademicNav {

    public static NavLevel root() {
        List<AcademicLevel> levels = Table.all(AcademicLevel.class);

        List<NavRow> rows = levels.stream()
                .map(level -> NavLevel.row(level.toString(), () -> sectionsLevel(level)))
                .toList();

        return new NavLevel("Spécialités", rows);
    }

    private static NavLevel sectionsLevel(AcademicLevel level) {
        List<Section> sections = Table.search(new Section().setAcademicLevel(level));

        List<NavRow> rows = sections.stream()
                .map(section -> NavLevel.row(section.toString(), () -> groupsLevel(section)))
                .toList();

        return new NavLevel(level.toString(), rows);
    }

    private static NavLevel groupsLevel(Section section) {
        List<Group> groups = Table.search(new Group().setSection(section));

        List<NavRow> rows = groups.stream()
                .map(group -> NavLevel.row(group.toString(), () -> studentsLevel(group)))
                .toList();

        return new NavLevel(section.toString(), rows);
    }

    private static NavLevel studentsLevel(Group group) {
        List<Student> students = Table.search(new Student().setGroup(group));

        List<NavRow> rows = students.stream()
                .map(student -> NavLevel.row(student.getSurname() + " " + student.getName(), null))
                .toList();

        return new NavLevel(group.toString(), rows);
    }
}
