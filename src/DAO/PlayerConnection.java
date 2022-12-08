package DAO;

import Model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerConnection {

    private Connection conn = new ConnectMySql().getConnect();

    public void createTablePlayer() throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS `baucuatomca`.`player` ( `username` VARCHAR(255) NOT NULL , `password` VARCHAR(255) NOT NULL, `IP` VARCHAR(255) NOT NULL , `name` VARCHAR(255) NOT NULL , `money` INT NOT NULL , PRIMARY KEY (`username`)) ENGINE = InnoDB;";

        Statement stmt =conn.createStatement();

        stmt.executeUpdate(sql);

    }

    public void getAllPlayer() throws SQLException {

        String sql = "SELECT * FROM `player`";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            String name = rs.getString("name");
            int money = rs.getInt("money");
            String IP = rs.getString("IP");
            Player p = new Player(username, password, name, IP, money);
        }

    }

    public Player getPlayer(String user, String pass) throws SQLException {

        String sql = "SELECT * FROM `player` WHERE `username` = '" + user + "' AND password = '" + pass + "' LIMIT 1";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        Player p = new Player();
        while(rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            String name = rs.getString("name");
            int money = rs.getInt("money");
            String IP = rs.getString("IP");
            p = new Player(username, password, IP, name, money);
        }
        return p;
    }

    public Boolean checkPlayer(String user, String pass) throws SQLException {

        createTablePlayer();

        String sql = "SELECT * FROM `player` WHERE `username` = '" + user + "' AND password = '" + pass + "' LIMIT 1";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        return rs.next();
    }

    public void addPlayer(Player player) throws SQLException {

        createTablePlayer();

        String sql = "INSERT INTO `player` (`username`, `password`, `IP`,`name`, `money`) VALUES (?, ?, ?, ?, ?);";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, player.getUsername());
        stmt.setString(2, player.getPassword());
        stmt.setString(3, player.getIP());
        stmt.setString(4, player.getName());
        stmt.setInt(5, player.getMoney());

        stmt.executeUpdate();

    }

    public void changeMoney(String username, int money) throws SQLException {

        String sql = "UPDATE `player` SET money = ? WHERE `username` = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, money);
        stmt.setString(2, username);

        stmt.executeUpdate();

    }

    public void deletePlayer(String name) throws SQLException {

        String sql = "DELETE FROM Player WHERE `username`=?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);

        stmt.executeUpdate();

    }

}
