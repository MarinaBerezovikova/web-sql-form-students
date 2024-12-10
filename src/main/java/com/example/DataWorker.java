package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataWorker {

    private static final Logger logger = LogManager.getLogger(DataWorker.class);

    public String validateStudentData(String firstName, String lastName, String courseStr) {
        if (firstName == null || firstName.trim().isEmpty()) {
            logger.warn("Имени студента не существует");
            return ValidationMessages.EMPTY_FIRST_NAME;
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            logger.warn("Фамилии студента не существует");
            return ValidationMessages.EMPTY_LAST_NAME;
        }
        if (firstName.length() > 50 || lastName.length() > 50) {
            logger.warn("Имя или фамилия длиннее 50 символов");
            return ValidationMessages.WRONG_NUMBER_CHARACTERS;
        }
        int course;
        try {
            course = Integer.parseInt(courseStr);
            if (course < 1 || course > 5) {
                logger.warn("Число курса студента вне диапазона (course < 1 || course > 5)");
                return ValidationMessages.WRONG_NUMBER_COURSE;
            }
        } catch (NumberFormatException e) {
            logger.warn("Символ курса не число");
            return ValidationMessages.INVALID_COURSE;
        }
        return "validationSucceed";
    }


    public void saveStudentInDB(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, course) VALUES (?, ?, ?)";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getCourse());
            statement.executeUpdate();
            logger.info("Студент добавлен в базу данных: " + student.getFirstName() + " " + student.getLastName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}