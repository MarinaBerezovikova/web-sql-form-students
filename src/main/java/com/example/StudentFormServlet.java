package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentFormServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            String sql = "SELECT * FROM student_db";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Обработка результата
                System.out.println("Имя: " + resultSet.getString("имя"));
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String courseNumber = request.getParameter("courseNumber");

        DataWorker dataWorker = new DataWorker();
        String validationMessage = dataWorker.validateStudentData(firstName,lastName,courseNumber);

        if (validationMessage != null) {
            request.setAttribute("validationMessage",validationMessage);
            request.getRequestDispatcher("index.jsp").forward(request,response);
        }

        int course = Integer.parseInt(courseNumber);
        Student newStudent = new Student(firstName,lastName,course);
        dataWorker.saveStudentInDB(newStudent);
        request.setAttribute("successMessages", SuccessesMessages.STUDENT_ADDED);
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}