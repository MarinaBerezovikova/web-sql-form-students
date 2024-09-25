package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataWorker {

    public String validateStudentData(String firstName, String lastName, String courseStr) {
        if (firstName == null || firstName.trim().isEmpty() || firstName.length() > 50) {
            return "Имя не должно быть пустым и не более 50 символов.";
        }
        if (lastName == null || lastName.trim().isEmpty() || lastName.length() > 50) {
            return "Фамилия не должна быть пустой и не более 50 символов.";
        }
        int course;
        try {
            course = Integer.parseInt(courseStr);
            if (course < 1 || course > 5) {
                return "Курс должен быть от 1 до 5.";
            }
        } catch (NumberFormatException e) {
            return "Курс должен быть числом.";
        }
        return null; // Все проверки пройдены
    }


    public void saveStudentInDB(Student student) {
        String sql = "INSERT INTO student_db (first_name, last_name, course) VALUES (?, ?, ?)";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getCourse());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Обработка ошибок
        }
    }
}