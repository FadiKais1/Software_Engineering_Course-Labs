package org.example.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_code", nullable = false, unique = true)
    private int courseCode;

    @Column(nullable = false)
    private int credits;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    @ManyToMany(mappedBy = "courses")
    @OrderBy("lastName ASC, firstName ASC")
    private Set<Student> students = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lessonDateTime ASC")
    private Set<CourseClass> classes = new LinkedHashSet<>();

    public Course() {
    }

    public Course(String courseName, int courseCode, int credits, Lecturer lecturer) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        setLecturer(lecturer);
    }

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
        if (lecturer != null) {
            lecturer.getCourses().add(this);
        }
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<CourseClass> getClasses() {
        return classes;
    }

    public void addClass(CourseClass courseClass) {
        classes.add(courseClass);
        courseClass.setCourse(this);
    }
}
