package hei.group.exercicetd3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    @Value("${db.url}") String url;
    @Value("${db.username}") String user;
    @Value("${db.password}") String password;
   public Connection getConnection() throws SQLException {
       return DriverManager.getConnection(url,user,password);
   }
}
