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

    private static final Logger logger = LogManager.getLogger(StudentFormServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        logger.info("Начата обработка GET-запроса");

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            logger.info("Получено соединение с базой MySql");
            String sql = "SELECT * FROM student_db";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                String studentName = resultSet.getString("имя");
                logger.info("Имя студента: " + studentName);
            }

        } catch (SQLException sqlException) {
            logger.error("Ошибка при подключении к базе данных", sqlException);
            request.setAttribute("errorMessage", ErrorMessages.ERROR_CONNECT_DATABASE);

            try {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ServletException e) {
                logger.error("Ошибка при перенаправлении на страницу index.jsp", e);
                throw new ServletException(ErrorMessages.ERROR_REDIRECT_TO_FORM_PAGE, e);
            } catch (IOException e) {
                logger.error("Ошибка при обработке запроса", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorMessages.ERROR_PROCESSING_REQUEST);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        logger.info("Начата обработка POST-запроса");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String courseNumber = request.getParameter("courseNumber");
        logger.info(String.format("Собраны следующие данные студента: %s %s %s курс", firstName, lastName, courseNumber));

        DataWorker dataWorker = new DataWorker();
        String validationMessage = dataWorker.validateStudentData(firstName, lastName, courseNumber);

        if (validationMessage.equals("validationSucceed")) {
            logger.info("Валидация данных студента прошла успешно, начато сохранение в базу данных");
            int course = Integer.parseInt(courseNumber);
            Student newStudent = new Student(firstName, lastName, course);
            dataWorker.saveStudentInDB(newStudent);
            request.setAttribute("successMessage", SuccessesMessages.STUDENT_ADDED);
        } else {
            logger.warn("Валидация данных студента не удалась: " + validationMessage);
                request.setAttribute("validationMessage", validationMessage);
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}