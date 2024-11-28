package com.example;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASSWORD = "inmoodformysql";

    private static final HikariDataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Failed to load MySQL JDBC Driver");
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
//оформить как лог?
//try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
//        if (connection != null) {
//        System.out.println("Соединение успешно установлено!");
//        }
//        } catch (SQLException e) {
//        e.printStackTrace();
//        }