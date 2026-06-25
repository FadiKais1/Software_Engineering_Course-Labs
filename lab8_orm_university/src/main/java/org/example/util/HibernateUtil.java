package org.example.util;

import org.example.entities.Course;
import org.example.entities.CourseClass;
import org.example.entities.Lecturer;
import org.example.entities.Student;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {
    private static final String HIBERNATE_PROPERTIES_FILE = "hibernate.properties";

    private HibernateUtil() {
    }

    public static SessionFactory buildSessionFactory(String[] args) throws HibernateException, IOException {
        Properties settings = loadHibernateProperties();
        applyRuntimeOverrides(settings, args);

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);

        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Lecturer.class);
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(CourseClass.class);

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static Properties loadHibernateProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = HibernateUtil.class.getClassLoader()
                .getResourceAsStream(HIBERNATE_PROPERTIES_FILE)) {
            if (inputStream == null) {
                throw new IOException("Missing " + HIBERNATE_PROPERTIES_FILE + " in src/main/resources");
            }
            properties.load(inputStream);
        }
        return properties;
    }

    private static void applyRuntimeOverrides(Properties settings, String[] args) {
        if (args == null) {
            return;
        }

        for (String arg : args) {
            if (arg == null || arg.isBlank()) {
                continue;
            }

            if (arg.startsWith("--db-password=")) {
                settings.setProperty("hibernate.connection.password", valueAfterEquals(arg));
            } else if (arg.startsWith("--db-user=")) {
                settings.setProperty("hibernate.connection.username", valueAfterEquals(arg));
            } else if (arg.startsWith("--db-url=")) {
                settings.setProperty("hibernate.connection.url", valueAfterEquals(arg));
            } else if (arg.startsWith("--db-name=")) {
                setDatabaseName(settings, valueAfterEquals(arg));
            } else if (!arg.startsWith("--")) {
                settings.setProperty("hibernate.connection.password", arg);
            }
        }
    }

    private static String valueAfterEquals(String arg) {
        int index = arg.indexOf('=');
        if (index == -1 || index == arg.length() - 1) {
            return "";
        }
        return arg.substring(index + 1);
    }

    private static void setDatabaseName(Properties settings, String databaseName) {
        String currentUrl = settings.getProperty("hibernate.connection.url");
        String parameters = "?serverTimezone=UTC&createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true";
        if (currentUrl != null && currentUrl.contains("?")) {
            parameters = currentUrl.substring(currentUrl.indexOf('?'));
        }
        settings.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/" + databaseName + parameters);
    }
}
