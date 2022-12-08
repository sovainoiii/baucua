package Model;

public class Player {

    private String username;
    private String password;
    private String IP;
    private String name;
    private int money;

    public Player() { }

    public Player(String username, String password, String IP, String name) {
        this.username = username;
        this.password = password;
        this.IP = IP;
        this.name = name;
        this.money = 1000;
    }

    public Player(String username, String password, String IP, String name, int money) {
        this.username = username;
        this.password = password;
        this.IP = IP;
        this.name = name;
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "{\"username\":\"" + username + "\", \"password\":\"" + password + "\", \"IP\":\"" + IP + "\", \"name\":\"" + name + "\", \"money\":\"" + money + "\"}";
    }

}
