# Lab 8 - ORM / Hibernate University Course Management System

This project implements the Lab 8 requirements using Java, Maven, MySQL, JPA annotations and Hibernate.

## What is implemented

- 4 entities: `Student`, `Lecturer`, `Course`, `CourseClass`.
- Student-Course many-to-many relation using the `student_courses` join table.
- Course-Lecturer many-to-one relation: each course has exactly one responsible lecturer.
- Course-CourseClass one-to-many relation: each class belongs to one course.
- Seed data: 4 courses, 4 lecturers, 8 students and 12 classes.
- Each student is registered to at least 2 courses.
- Each course has 3 classes.
- Required prints:
  - Courses with all fields and the responsible lecturer name.
  - Students with all fields and registered courses.
  - Classes for each course with date, time, duration and location.
  - Lecturers with all fields and number of responsible courses.
- Runtime option to change DB password without editing code.

## Database name

The default database name is exactly:

```sql
myFirstDataBase
```

The JDBC URL also includes `createDatabaseIfNotExist=true`, but you can create it manually if needed:

```sql
CREATE DATABASE IF NOT EXISTS myFirstDataBase;
```

## How to build the executable JAR

From the project root folder:

```bash
mvn clean package
```

Maven creates:

```text
target/lab8-orm-university.jar
```

The generated JAR is executable and also includes the source files under `source-code/`, as requested.

## How to run

Default username is `root` and default password is `password`.

```bash
java -jar target/lab8-orm-university.jar
```

To change only the password at runtime:

```bash
java -jar target/lab8-orm-university.jar --db-password=YOUR_PASSWORD
```

Shortcut: the first plain argument is also treated as the DB password:

```bash
java -jar target/lab8-orm-university.jar YOUR_PASSWORD
```

To change username too:

```bash
java -jar target/lab8-orm-university.jar --db-user=root --db-password=YOUR_PASSWORD
```

To change the database URL:

```bash
java -jar target/lab8-orm-university.jar --db-url=jdbc:mysql://localhost:3306/myFirstDataBase?serverTimezone=UTC\&createDatabaseIfNotExist=true\&useSSL=false\&allowPublicKeyRetrieval=true
```

## Submission packaging

The official submission ZIP should contain exactly:

1. `Lab8_Report.pdf`
2. `lab8-orm-university.jar`

Name the final ZIP with the two student ID numbers separated by an underscore, for example:

```text
123456789_987654321.zip
```
