package Server;

import DAO.PlayerConnection;
import Model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private int port;
    private static ArrayList<Player> playerList;

    public Server(int port) {
        connections = new ArrayList<>();
        playerList = new ArrayList<>();
        done = false;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }
    public void updateList(Player player) {
        playerList.add(player);
    }

    public void decList(Player player) {
        for (Player p : playerList) {
            if (p.getUsername().equals(player.getUsername())) {
                playerList.remove(p);
                return;
            }
        }
    }

    public ArrayList<Player> getAllUser() {
        return playerList;
    }
    public void shutdown() {
        try {
            done = true;
            if (pool != null)
                pool.shutdown();
            if(!server.isClosed())
                server.close();
            for( ConnectionHandler ch : connections) {
                ch.shutdown();
            }
            playerList.clear();
            Main.updateNumberOnline();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class ConnectionHandler implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private Timer timer;
        private Player player;
        private static int second;
        private static boolean isPlay = false;
        private static boolean dat;
        private Random random;
        private static int[] xucxac, totalCuoc = {0,0,0,0,0,0};

        public ConnectionHandler(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            try {
                String outData;
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PlayerConnection conn = new PlayerConnection();
                JsonParser js = new JsonParser();
                Gson gs = new Gson();
                timer = new Timer();
                random = new Random();
                int[] cuoc = {0,0,0,0,0,0};
                while (!done) {
                    String getData = in.readLine();
//                    System.out.println(getData);
                    JsonObject value = (JsonObject) js.parse(getData);
                    String func = value.get("function").getAsString();
                    switch (func) {
                        case "login":
                            String user = value.get("username").getAsString();
                            String pass = value.get("password").getAsString();
                            if(conn.checkPlayer(user, pass)){
                                player = conn.getPlayer(user, pass);
                                outData = "{\"status\":\"true\", \"data\":" + player.toString() + ",\"message\":\"\"}";
                                out.println(outData);
                                updateList(player);
                                Main.updateNumberOnline();
                            } else {
                                outData = "{\"status\":\"false\", \"data:\":\"\",\"message\":\"Tài khoản hoặc mật khẩu không đúng!\"}";
                                out.println(outData);
                            }
                            break;
                        case "signup":
                            String userReg = value.get("username").getAsString();
                            String passReg = value.get("password").getAsString();
                            String IPReg = value.get("IP").getAsString();
                            String nameReg = value.get("name").getAsString();

                            if(!conn.checkPlayer(userReg, passReg)) {
                                Player player = new Player(userReg, passReg, IPReg, nameReg);
                                conn.addPlayer(player);
                                outData = "{\"status\":\"true\", \"data\":" + player.toString() + ",\"message\":\"\"}";
                                out.println(outData);
                                updateList(player);
                                Main.updateNumberOnline();
                            } else {
                                outData = "{\"status\":\"false\", \"data:\":\"\",\"message\":\"Tài khoản đã tồn tại!\"}";
                                out.println(outData);
                            }
                            break;
                        case "disconnect":
                            JsonObject dataPlayer = (JsonObject) value.get("data");
                            player = gs.fromJson(dataPlayer, Player.class);
                            decList(player);
                            Main.updateNumberOnline();
                            break;
                        case "play":
                            resetTongCuoc();
                            if (!isPlay) {
                                playGame(30, 1000);
                            } else {
                                playGame(second, 0);
                            }
                            break;
                        case "dat":
                            JsonObject data = (JsonObject) value.get("data");
                            int o = data.get("o").getAsInt();
                            int tien = data.get("tien").getAsInt();
                            if(player.getMoney() < tien) {
                                outData = "{\"function\":\"notice\", \"data\": {\"status\":\"false\",\"message\":\"Không đủ tiền đặt\"}}";
                                out.println(outData);
                            } else {
                                player.setMoney(player.getMoney() - tien);
                                cuoc[o] += tien;
                                totalCuoc[o] += tien;
                                outData = "{\"function\":\"update\", \"data\": {\"tien\":\""+ player.getMoney() +"\",\"cuoc\":["+ totalCuoc[0] +", "+ totalCuoc[1] +", "+ totalCuoc[2] +", "+ totalCuoc[3] +", "+ totalCuoc[4] +", "+ totalCuoc[5] +"]}}";
                                out.println(outData);
                            }
                            break;
                        case "recall":
                            for (int i = 0; i < 6; i++) {
                                totalCuoc[i] -= cuoc[i];
                            }
                            for (int i = 0; i < 6; i++) {
                                player.setMoney(player.getMoney() + cuoc[i]);
                            }
                            cuoc = new int[]{0, 0, 0, 0, 0, 0};
                            break;
                    }
                }
            } catch (IOException | SQLException e) {
                shutdown();
            }
        }

        public void playGame(int sc, int delay) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public int secondCr = sc;
                @Override
                public void run() {
                    isPlay = true;
                    dat = true;
                    if (secondCr <= 1) {
                        timer.cancel();
                        isPlay = false;
                        dat = false;
                        xucxac = randomXucXac();
                        out.println("{\"function\":\"roll\",\"data\":{\"o1\":\""+ xucxac[0] +"\";\"o2\":\""+ xucxac[1] +"\",\"o3\":\""+ xucxac[2] +"\"}}");
                        }
                    secondCr --;
                    second = secondCr;
                    out.println("{\"function\":\"update\", \"data\": {\"tien\":\""+ player.getMoney() +"\",\"cuoc\":["+ totalCuoc[0] +", "+ totalCuoc[1] +", "+ totalCuoc[2] +", "+ totalCuoc[3] +", "+ totalCuoc[4] +", "+ totalCuoc[5] +"]}}");
                    out.println("{\"function\":\"play\",\"data\":{\"second\":\"" + secondCr + "\", \"dat\":\""+ dat +"\"}}");
                }
            }, delay, 1000);
        }

        public int[] randomXucXac() {
            int o1 = random.nextInt(6);
            int o2 = random.nextInt(6);
            int o3 = random.nextInt(6);
            int[] xucxac = {o1, o2, o3};
            return xucxac ;
        }

        public void resetTongCuoc(){
            for (int i = 0; i < 6; i++) {
                 totalCuoc[i] = 0;
            }
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()){
                    client.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
