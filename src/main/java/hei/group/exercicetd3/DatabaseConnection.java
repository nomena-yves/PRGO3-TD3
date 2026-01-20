package hei.group.exercicetd3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    private static final String URL =
            "jdbc:postgresql://localhost:5432/mini_dish_db";
    private static final String USER = "mini_dish_manager";
    private static final String PASSWORD = "harena";
   public Connection getConnection() throws SQLException {
       return DriverManager.getConnection(URL,USER,PASSWORD);
   }
}
