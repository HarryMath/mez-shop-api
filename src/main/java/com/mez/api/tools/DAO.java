package com.mez.api.tools;

import com.mez.api.models.Engine;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
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

    public Connection getExtraConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
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

    public void executeUpdate(String query, Connection conn) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    public long countQuery(String query) {
        ScalarHandler<Long> longHandler = new ScalarHandler<>();
        ScalarHandler<Integer> integerHandler = new ScalarHandler<>();
        try {
            try {
                return queryRunner.query(connection, query, longHandler);
            } catch (ClassCastException e) {
                return queryRunner.query(connection, query, integerHandler);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public <Type> List<Type> columnQuery(String query) {
        try {
            return queryRunner.query(connection, query, new ColumnListHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
