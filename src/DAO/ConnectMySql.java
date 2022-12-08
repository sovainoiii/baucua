package DAO;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectMySql {

    public Connection getConnect() {

        final String DB = "baucuatomca";
        final String url = "jdbc:mysql://localhost:3306/"+DB;
        final String user = "root";
        final String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
        }

        return null;
    }

    public static void main(String[] args) {
        new ConnectMySql().getConnect();
    }

}
