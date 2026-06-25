package org.example.data;

import org.example.entities.Course;
import org.example.entities.CourseClass;
import org.example.entities.Lecturer;
import org.example.entities.Student;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DataSeeder {
    private final Session session;

    public DataSeeder(Session session) {
        this.session = session;
    }

    public void populate() {
        Lecturer lecturer1 = new Lecturer("Dana", "Levi", "Software Engineering", "dana.levi@university.ac.il");
        Lecturer lecturer2 = new Lecturer("Amir", "Cohen", "Databases", "amir.cohen@university.ac.il");
        Lecturer lecturer3 = new Lecturer("Noa", "Mizrahi", "Algorithms", "noa.mizrahi@university.ac.il");
        Lecturer lecturer4 = new Lecturer("Yosef", "Katz", "Computer Systems", "yosef.katz@university.ac.il");

        Course course1 = new Course("Object Oriented Programming", 234114, 3, lecturer1);
        Course course2 = new Course("Databases and ORM", 236314, 3, lecturer2);
        Course course3 = new Course("Data Structures", 234218, 4, lecturer3);
        Course course4 = new Course("Operating Systems", 234123, 4, lecturer4);

        addThreeClasses(course1, 1, "Taub 3");
        addThreeClasses(course2, 2, "Taub 7");
        addThreeClasses(course3, 3, "Ullmann 301");
        addThreeClasses(course4, 4, "Taub 9");

        Student student1 = new Student(100001, "Adam", "Bar", "adam.bar@campus.ac.il");
        Student student2 = new Student(100002, "Maya", "Aviv", "maya.aviv@campus.ac.il");
        Student student3 = new Student(100003, "Omer", "Tal", "omer.tal@campus.ac.il");
        Student student4 = new Student(100004, "Lior", "Nave", "lior.nave@campus.ac.il");
        Student student5 = new Student(100005, "Yael", "Shahar", "yael.shahar@campus.ac.il");
        Student student6 = new Student(100006, "Itay", "Mor", "itay.mor@campus.ac.il");
        Student student7 = new Student(100007, "Roni", "Erez", "roni.erez@campus.ac.il");
        Student student8 = new Student(100008, "Tamar", "Gal", "tamar.gal@campus.ac.il");

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course1);
        student2.addCourse(course3);
        student3.addCourse(course1);
        student3.addCourse(course4);
        student4.addCourse(course2);
        student4.addCourse(course3);
        student5.addCourse(course2);
        student5.addCourse(course4);
        student6.addCourse(course3);
        student6.addCourse(course4);
        student7.addCourse(course1);
        student7.addCourse(course2);
        student7.addCourse(course3);
        student8.addCourse(course2);
        student8.addCourse(course3);
        student8.addCourse(course4);

        List<Lecturer> lecturers = Arrays.asList(lecturer1, lecturer2, lecturer3, lecturer4);
        List<Course> courses = Arrays.asList(course1, course2, course3, course4);
        List<Student> students = Arrays.asList(
                student1, student2, student3, student4, student5, student6, student7, student8
        );

        lecturers.forEach(session::save);
        session.flush();

        courses.forEach(session::save);
        session.flush();

        students.forEach(session::save);
        session.flush();
    }

    private void addThreeClasses(Course course, int dayOffset, String classroom) {
        LocalDateTime firstClass = LocalDateTime.of(2026, 3, 1 + dayOffset, 10, 30);
        course.addClass(new CourseClass(firstClass, 90, course, classroom));
        course.addClass(new CourseClass(firstClass.plusWeeks(1), 90, course, classroom));
        course.addClass(new CourseClass(firstClass.plusWeeks(2), 90, course, classroom));
    }
}
