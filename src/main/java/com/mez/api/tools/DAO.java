package com.mez.api.tools;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class DAO {

    @Value("${datasource.url}")
    private String connectionString;
    private Connection connection;
    private QueryRunner queryRunner;

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(connectionString);
        queryRunner = new QueryRunner();
    }

    @SuppressWarnings("unused")
    public void closeConnection() throws SQLException {
        connection.close();
    }


    public void executeUpdate(String query) throws SQLException {
        if(connection.isClosed()) openConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    public <Type> Type executeQuery(String query, Class<Type> type) {
        ResultSetHandler<Type> handler = new BeanHandler<>(type);
        try {
            return queryRunner.query(connection, query, handler);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
            return null;
        }
    }

    public int countQuery(String query) {
        ScalarHandler<Long> handler = new ScalarHandler<>();
        try {
            return queryRunner.query(connection, query, handler).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public <Type> List<Type> executeListQuery(String query, Class<Type> type) {
        ResultSetHandler<List<Type>> handler = new BeanListHandler<>(type);
        try {
            return queryRunner.query(connection, query, handler);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
            return null;
        }
    }
}
