package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataWorker {

    public String validateStudentData(String firstName, String lastName, String courseStr) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return ValidationMessages.EMPTY_FIRST_NAME;
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return ValidationMessages.EMPTY_LAST_NAME;
        }
        if (firstName.length() > 50 || lastName.length() > 50) {
            return ValidationMessages.WRONG_NUMBER_CHARACTERS;
        }
        int course;
        try {
            course = Integer.parseInt(courseStr);
            if (course < 1 || course >= 4) {
                return ValidationMessages.WRONG_NUMBER_COURSE;
            }
        } catch (NumberFormatException e) {
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
            System.out.println("Студент добавлен: " + student.getFirstName() + " " + student.getLastName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}