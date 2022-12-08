package Client;

import Model.Player;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main extends JFrame {

    private JPanel contentPane;
    private JPanel panel1, panel2;
    private JLabel datNai, datBau,datGa , datCa, datCua,datTom, coin;
    private JLabel time;
    private JLabel tienNai, tienBau, tienGa, tienCa, tienCua, tienTom;
    private JLabel xucxac1, xucxac2, xucxac3;
    private JTextField textField;
    private Socket client;
    private MainHandler handler;
    private Player player;
    private boolean done = false;
    private final String[] dataIcon = {"/Image/nai.jpg", "/Image/bau.jpg", "/Image/ga.jpg", "/Image/ca.jpg", "/Image/cua.jpg", "/Image/tom.jpg"};

    public Main(Socket socketClient, Player playerData) {
        this.client = socketClient;
        this.player = playerData;

        setTitle("Bầu cua tôm cá");
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    handler = new MainHandler(client, player);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                handler.start();
                panel2.setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            handler.sendData("{\"function\":\"disconnect\",\"data\":" + player.toString() + "}");
            }
        });

        setBackground(new Color(255, 62, 62));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 616, 670);
        contentPane = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();

        contentPane.setBackground(new Color(255, 62, 62));
        panel1.setBackground(new Color(255, 62, 62));
        panel2.setBackground(new Color(255, 62, 62));

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);

        setContentPane(contentPane);
        panel1.setLayout(null);
        panel2.setLayout(null);
        contentPane.setLayout(null);

        contentPane.add(panel1);
        contentPane.add(panel2);

        contentPane.add(panel1);
        panel1.setBounds(0, 0, 600, 670);
        panel2.setBounds(0, 0, 600, 670);

        JButton startGame = new JButton("Chơi game");
        startGame.setBounds(200, 300,180, 50);
        startGame.addActionListener(e -> {
            panel1.setVisible(false);
            panel2.setVisible(true);
            String data = "{\"function\":\"play\"}";
            handler.sendData(data);
        });
        startGame.setFont(new Font("Tahoma", Font.PLAIN, 30));
        panel1.add(startGame);
        contentPane.add(panel1);


        JLabel title = new JLabel("Bầu cua tôm cá");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 10, 600, 37);
        title.setFont(new Font("Tahoma", Font.PLAIN, 30));
        title.setForeground(Color.WHITE);
        panel2.add(title);

        time = new JLabel("0:30");
        time.setIcon(new ImageIcon(Main.class.getResource("/Image/clock.png")));
        time.setForeground(Color.WHITE);
        time.setFont(new Font("Tahoma", Font.PLAIN, 40));
        time.setBounds(50, 90, 132, 56);
        panel2.add(time);

        JPanel panel = new JPanel();
        panel.setBounds(200, 58, 320, 111);
        panel2.add(panel);

        xucxac1 = new JLabel("");
        xucxac1.setIcon(new ImageIcon(Main.class.getResource("/Image/bau.jpg")));
        panel.add(xucxac1);

        xucxac2 = new JLabel("");
        xucxac2.setIcon(new ImageIcon(Main.class.getResource("/Image/bau.jpg")));
        panel.add(xucxac2);

        xucxac3 = new JLabel("");
        xucxac3.setIcon(new ImageIcon(Main.class.getResource("/Image/bau.jpg")));
        panel.add(xucxac3);

        tienNai = new JLabel("0");
        tienNai.setForeground(Color.YELLOW);
        tienNai.setFont(new Font("Tahoma", Font.BOLD, 14));
        tienNai.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        tienNai.setBounds(100, 200, 100, 20);
        panel2.add(tienNai);

        tienBau = new JLabel("0");
        tienBau.setForeground(Color.YELLOW);
        tienBau.setFont(new Font("Tahoma", Font.BOLD, 14));
        tienBau.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        tienBau.setBounds(250, 200, 100, 20);
        panel2.add(tienBau);

        tienGa = new JLabel("0");
        tienGa.setFont(new Font("Tahoma", Font.BOLD, 14));
        tienGa.setForeground(Color.YELLOW);
        tienGa.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        tienGa.setBounds(400, 200, 100, 20);
        panel2.add(tienGa);

        tienCa = new JLabel("0");
        tienCa.setForeground(Color.YELLOW);
        tienCa.setFont(new Font("Tahoma", Font.BOLD, 14));
        tienCa.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        tienCa.setBounds(100, 360, 100, 20);
        panel2.add(tienCa);

        tienCua = new JLabel("0");
        tienCua.setForeground(Color.YELLOW);
        tienCua.setFont(new Font("Tahoma", Font.BOLD, 14));
        tienCua.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        tienCua.setBounds(250, 360, 100, 20);
        panel2.add(tienCua);

        tienTom = new JLabel("0");
        tienTom.setFont(new Font("Tahoma", Font.BOLD, 14));
        tienTom.setForeground(Color.YELLOW);
        tienTom.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        tienTom.setBounds(400, 360, 100, 20);
        panel2.add(tienTom);

        JLabel nai = new JLabel("");
        nai.setIcon(new ImageIcon(Main.class.getResource(dataIcon[0])));
        nai.setBounds(100, 225, 100, 100);
        panel2.add(nai);

        datNai = new JLabel("Đặt");
        datNai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                datCuoc(0);
            }
        });
        datNai.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datNai.setForeground(Color.WHITE);
        datNai.setFont(new Font("Tahoma", Font.BOLD, 14));
        datNai.setHorizontalAlignment(SwingConstants.CENTER);
        datNai.setBounds(100, 330, 100, 20);
        panel2.add(datNai);

        JLabel bau = new JLabel("");
        bau.setIcon(new ImageIcon(Main.class.getResource(dataIcon[1])));
        bau.setBounds(250, 225, 100, 100);
        panel2.add(bau);

        datBau = new JLabel("Đặt");
        datBau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                datCuoc(1);
            }
        });
        datBau.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datBau.setHorizontalAlignment(SwingConstants.CENTER);
        datBau.setForeground(Color.WHITE);
        datBau.setFont(new Font("Tahoma", Font.BOLD, 14));
        datBau.setBounds(250, 330, 100, 20);
        panel2.add(datBau);

        JLabel ga = new JLabel("");
        ga.setIcon(new ImageIcon(Main.class.getResource(dataIcon[2])));
        ga.setBounds(400, 225, 100, 100);
        panel2.add(ga);

        datGa = new JLabel("Đặt");
        datGa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                datCuoc(2);
            }
        });
        datGa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datGa.setHorizontalAlignment(SwingConstants.CENTER);
        datGa.setForeground(Color.WHITE);
        datGa.setFont(new Font("Tahoma", Font.BOLD, 14));
        datGa.setBounds(400, 329, 100, 20);
        panel2.add(datGa);

        JLabel ca = new JLabel("");
        ca.setIcon(new ImageIcon(Main.class.getResource(dataIcon[3])));
        ca.setBounds(100, 385, 100, 100);
        panel2.add(ca);

        datCa = new JLabel("Đặt");
        datCa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                datCuoc(3);
            }
        });
        datCa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datCa.setHorizontalAlignment(SwingConstants.CENTER);
        datCa.setForeground(Color.WHITE);
        datCa.setFont(new Font("Tahoma", Font.BOLD, 14));
        datCa.setBounds(100, 490, 100, 20);
        panel2.add(datCa);

        JLabel cua = new JLabel("");
        cua.setIcon(new ImageIcon(Main.class.getResource(dataIcon[4])));
        cua.setBounds(250, 385, 100, 100);
        panel2.add(cua);

        datCua = new JLabel("Đặt");
        datCua.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                datCuoc(4);
            }
        });
        datCua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datCua.setHorizontalAlignment(SwingConstants.CENTER);
        datCua.setForeground(Color.WHITE);
        datCua.setFont(new Font("Tahoma", Font.BOLD, 14));
        datCua.setBounds(250, 490, 100, 20);
        panel2.add(datCua);

        JLabel tom = new JLabel("");
        tom.setIcon(new ImageIcon(Main.class.getResource(dataIcon[5])));
        tom.setBounds(400, 385, 100, 100);
        panel2.add(tom);

        datTom = new JLabel("Đặt");
        datTom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                datCuoc(5);
            }
        });
        datTom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datTom.setHorizontalAlignment(SwingConstants.CENTER);
        datTom.setForeground(Color.WHITE);
        datTom.setFont(new Font("Tahoma", Font.BOLD, 14));
        datTom.setBounds(400, 490, 100, 20);
        panel2.add(datTom);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(Main.class.getResource("/Image/user.png")));
        lblNewLabel.setBounds(100, 530, 65, 65);
        panel2.add(lblNewLabel);

        JLabel name = new JLabel(player.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Tahoma", Font.PLAIN, 20));
        name.setBounds(175, 540, 100, 20);
        panel2.add(name);

        coin = new JLabel(String.valueOf(player.getMoney()));
        coin.setForeground(Color.YELLOW);
        coin.setFont(new Font("Tahoma", Font.BOLD, 14));
        coin.setIcon(new ImageIcon(Main.class.getResource("/Image/coin.png")));
        coin.setBounds(175, 565, 100, 20);
        panel2.add(coin);

        textField = new JTextField("100");
        textField.setToolTipText("Nhập xu muốn cược");
        textField.setBounds(300, 545, 100, 40);
        panel2.add(textField);
        textField.setColumns(10);

        JLabel thuhoi = new JLabel("Thu hồi");
        thuhoi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handler.sendData("{\"function\":\"recall\"}");
            }
        });
        thuhoi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        thuhoi.setForeground(Color.WHITE);
        thuhoi.setHorizontalAlignment(SwingConstants.CENTER);
        thuhoi.setFont(new Font("Tahoma", Font.BOLD, 16));
        thuhoi.setBounds(410, 555, 65, 20);
        panel2.add(thuhoi);
    }

    public void updateSecond(int second) {
        if(second < 10) {
            time.setText("0:0" + second);
//            if (second == 0) {
//                handler.sendData("{\"function\":\"play\"}");
//            }
        } else {
            time.setText("0:" + second);
        }
    }

    public void datCuoc(int oDat) {
        try {
            int xu = Integer.parseInt(textField.getText());
            String data = "{\"function\":\"dat\", \"data\":{\"o\":\"" + oDat + "\", \"tien\":\"" + xu + "\"}}";
            handler.sendData(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPane, "Nhập đúng định dạng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateDatCuoc(boolean dat) {
        datNai.setEnabled(dat);
        datBau.setEnabled(dat);
        datGa.setEnabled(dat);
        datCa.setEnabled(dat);
        datCua.setEnabled(dat);
        datTom.setEnabled(dat);
    }

    public void updateXucXac(int o1, int o2, int o3) {
        xucxac1.setIcon(new ImageIcon(Main.class.getResource(dataIcon[o1])));
        xucxac2.setIcon(new ImageIcon(Main.class.getResource(dataIcon[o2])));
        xucxac3.setIcon(new ImageIcon(Main.class.getResource(dataIcon[o3])));
    }

    private void shutdown() {
        done = true;
        try {
            if(client != null) client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MainHandler extends Thread {
        private Socket connect;
        private Player player;
        private PrintWriter writer;
        private BufferedReader reader;

        public MainHandler(Socket socket, Player player) throws IOException {
            this.connect = socket;
            this.player =player;
            writer = new PrintWriter(connect.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String fun;
                while (!done) {
                    JsonParser js = new JsonParser();
                    String getData = reader.readLine();
                    System.out.println(getData);
                    JsonObject jo = (JsonObject) js.parse(getData);
                    fun = jo.get("function").getAsString();
                    JsonObject dataObj = (JsonObject) jo.get("data");
                    switch (fun) {
                        case "play":
                            int second = dataObj.get("second").getAsInt();
                            boolean dat = dataObj.get("dat").getAsBoolean();
                            updateDatCuoc(dat);
                            updateSecond(second);
                            break;
                        case "roll":
                            int o1 = dataObj.get("o1").getAsInt();
                            int o2 = dataObj.get("o2").getAsInt();
                            int o3 = dataObj.get("o3").getAsInt();
                            updateXucXac(o1, o2, o3);
                            break;
                        case "notice":
                            boolean status = dataObj.get("status").getAsBoolean();
                            String mess = dataObj.get("message").getAsString();
                            if (!status) {
                                JOptionPane.showMessageDialog(contentPane, mess, "Lỗi", JOptionPane.WARNING_MESSAGE);
                            }
                            break;
                        case "update":
                            String tien = dataObj.get("tien").getAsString();
                            JsonArray tongCuoc = dataObj.get("cuoc").getAsJsonArray();
                            tienNai.setText(String.valueOf(tongCuoc.get(0)));
                            tienBau.setText(String.valueOf(tongCuoc.get(1)));
                            tienGa.setText(String.valueOf(tongCuoc.get(2)));
                            tienCa.setText(String.valueOf(tongCuoc.get(3)));
                            tienCua.setText(String.valueOf(tongCuoc.get(4)));
                            tienTom.setText(String.valueOf(tongCuoc.get(5)));
                            coin.setText(tien);
                            break;
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }

        public void sendData(String data) {
            writer.println(data);
        }
    }
}
