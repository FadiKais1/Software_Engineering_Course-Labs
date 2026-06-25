package org.example;

import org.example.data.DataSeeder;
import org.example.entities.Course;
import org.example.entities.CourseClass;
import org.example.entities.Lecturer;
import org.example.entities.Student;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        Session session = null;

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(args)) {
            session = sessionFactory.openSession();
            session.beginTransaction();

            new DataSeeder(session).populate();

            session.getTransaction().commit();
            session.clear();

            session.beginTransaction();
            printCourses(session);
            printStudents(session);
            printClassesByCourse(session);
            printLecturers(session);
            session.getTransaction().commit();

            System.out.println("\nDone. Data was saved and printed successfully.");
        } catch (Exception exception) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred. Changes were rolled back.");
            exception.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void printCourses(Session session) {
        printTitle("Courses");
        List<Course> courses = getAll(session, Course.class);
        for (Course course : courses) {
            System.out.printf(
                    "Id: %d | Name: %s | Code: %d | Credits: %d | Lecturer: %s%n",
                    course.getId(),
                    course.getCourseName(),
                    course.getCourseCode(),
                    course.getCredits(),
                    course.getLecturer().getFullName()
            );
        }
    }

    private static void printStudents(Session session) {
        printTitle("Students");
        List<Student> students = getAll(session, Student.class);
        for (Student student : students) {
            String courseNames = student.getCourses().stream()
                    .map(Course::getCourseName)
                    .collect(Collectors.joining(", "));

            System.out.printf(
                    "Id: %d | Student number: %d | Name: %s | Email: %s | Courses: [%s]%n",
                    student.getId(),
                    student.getStudentNumber(),
                    student.getFullName(),
                    student.getEmail(),
                    courseNames
            );
        }
    }

    private static void printClassesByCourse(Session session) {
        printTitle("Classes by course");
        List<Course> courses = getAll(session, Course.class);
        for (Course course : courses) {
            System.out.printf("Course: %s (%d)%n", course.getCourseName(), course.getCourseCode());
            for (CourseClass courseClass : course.getClasses()) {
                System.out.printf(
                        "  Class id: %d | Date and time: %s | Duration: %d minutes | Location: %s%n",
                        courseClass.getId(),
                        courseClass.getFormattedDateTime(),
                        courseClass.getDurationMinutes(),
                        courseClass.getClassroom()
                );
            }
        }
    }

    private static void printLecturers(Session session) {
        printTitle("Lecturers");
        List<Lecturer> lecturers = getAll(session, Lecturer.class);
        for (Lecturer lecturer : lecturers) {
            System.out.printf(
                    "Id: %d | Name: %s | Specialization: %s | Email: %s | Number of courses: %d%n",
                    lecturer.getId(),
                    lecturer.getFullName(),
                    lecturer.getSpecialization(),
                    lecturer.getEmail(),
                    lecturer.getCourses().size()
            );
        }
    }

    private static <T> List<T> getAll(Session session, Class<T> entityClass) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        query.from(entityClass);
        return session.createQuery(query).getResultList();
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("==================== " + title + " ====================");
    }
}
