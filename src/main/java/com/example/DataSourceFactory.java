package com.example;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataSourceFactory {

    private static final Logger logger = LogManager.getLogger(DataSourceFactory.class);

    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASSWORD = "inmoodformysql";

    private static final HikariDataSource dataSource;

    static {
        try {
            logger.info("Загрузка MySQL JDBC Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Не удалось загрузить MySQL JDBC Driver");
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