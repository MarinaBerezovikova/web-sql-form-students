package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFormServlet extends HttpServlet {

    private static final String SQL_SELECT_STUDENTS = "SELECT * FROM student_db";
    private static final Logger logger = LogManager.getLogger(StudentFormServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("Начата обработка GET-запроса");

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try (Connection connection = getDBConnection()) {
            logger.info("Получено соединение с базой MySql");
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_STUDENTS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String studentName = resultSet.getString("имя");
                logger.info("Имя студента: " + studentName);
            }

        } catch (SQLException sqlException) {
            logger.error("Ошибка при подключении к базе данных", sqlException);
            request.setAttribute("errorMessage", ErrorMessages.ERROR_DB_CONNECTION);

            forwardToIndexPage(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("Начата обработка POST-запроса");

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        String courseNumber = request.getParameter("courseNumber");
        logger.info(String.format("Собраны следующие данные студента: %s %s %s курс", firstName, lastName, courseNumber));

        DataWorker dataWorker = new DataWorker();
        String validationMessage = dataWorker.validateStudentData(firstName, lastName, courseNumber);

        if (validationMessage.equals("validationSucceed")) {
            logger.info("Валидация данных студента прошла успешно");
            try (Connection connection = getDBConnection()) {
                logger.info("Соединение с базой прошло успешно");
                Student newStudent = createStudent(firstName, lastName, courseNumber);
                dataWorker.saveStudentInDB(newStudent, connection);
                request.setAttribute("successMessage", SuccessesMessages.STUDENT_ADDED);
            } catch (SQLException e) {
                logger.error(ErrorMessages.ERROR_DB_CONNECTION, e);
                request.setAttribute("errorMessage", ErrorMessages.ERROR_DB_CONNECTION);
            }
        } else {
            logger.warn("Валидация данных студента не удалась: " + validationMessage);
            request.setAttribute("validationMessage", validationMessage);
        }
        forwardToIndexPage(request, response);
    }

    private void forwardToIndexPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error(ErrorMessages.ERROR_REDIRECT, e);
            throw new ServletException(ErrorMessages.ERROR_REDIRECT, e);
        } catch (IOException e) {
            logger.error("Ошибка при обработке запроса", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessages.ERROR_PROCESSING_REQUEST);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    private Student createStudent(String firstName, String lastName, String courseNumber) {
        int course = Integer.parseInt(courseNumber);
        Student student = new Student(firstName, lastName, course);
        logger.info(String.format("Создан студент: %s %s %s курс", firstName, lastName, courseNumber));
        return student;
    }

    private Connection getDBConnection() throws SQLException {
        return DataSourceFactory.getDataSource().getConnection();
    }
}