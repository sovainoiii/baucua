package Client;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import DAO.PlayerConnection;
import Model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField userField;
    private JTextField ipField;
    private JTextField portField;
    private JPasswordField passField;
    private Socket client;
    private boolean done = false;
    private static Login frame;
    private final String titleMess = "Đăng nhập";

    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Login() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 355);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 62, 62));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("Đăng nhập");
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.PLAIN, 20));
        title.setBounds(115, 15, 98, 25);
        contentPane.add(title);

        JLabel userLabel = new JLabel("Tài khoản");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userLabel.setBounds(35, 55, 70, 22);
        contentPane.add(userLabel);

        userField = new JTextField();
        userField.setToolTipText("Username");
        userField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userLabel.setLabelFor(userField);
        userField.setBounds(115, 55, 180, 25);
        contentPane.add(userField);
        userField.setColumns(10);

        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passLabel.setBounds(35, 100, 70, 22);
        contentPane.add(passLabel);

        passField = new JPasswordField();
        passField.setToolTipText("Password");
        passField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passLabel.setLabelFor(passField);
        passField.setBounds(115, 100, 180, 25);
        contentPane.add(passField);

        JLabel ipLabel = new JLabel("IP Server");
        ipLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setBounds(35, 145, 70, 22);
        contentPane.add(ipLabel);

        ipField = new JTextField();
        ipLabel.setLabelFor(ipField);
        ipField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        try {
            ipField.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        ipField.setBounds(115, 145, 180, 25);
        contentPane.add(ipField);
        ipField.setColumns(10);

        JLabel portLabel = new JLabel("Port server");
        portLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portLabel.setForeground(Color.WHITE);
        portLabel.setBounds(35, 190, 70, 22);
        contentPane.add(portLabel);

        portField = new JTextField();
        portLabel.setLabelFor(portField);
        portField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portField.setBounds(115, 190, 80, 25);
        contentPane.add(portField);
        portField.setColumns(10);

        JButton loginBtn = new JButton("Đăng nhập");
        loginBtn.addActionListener(e -> {
            if(!portField.getText().isEmpty()) {
                try {
                    int port = Integer.parseInt(portField.getText());
                    String ip = ipField.getText();
                    LoginBtn(ip, port);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(contentPane, "Hãy nhập đúng định dạng", titleMess, JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginBtn.setBounds(115, 235, 108, 25);
        contentPane.add(loginBtn);

        JLabel lblNewLabel = new JLabel("Bạn chưa có tài khoản?");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(62, 270, 144, 25);
        contentPane.add(lblNewLabel);

        JLabel signupLabel = new JLabel("Đăng ký");
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
                new Signup().setVisible(true);
            }
        });
        signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        signupLabel.setForeground(new Color(0, 255, 255));
        signupLabel.setBounds(218, 270, 52, 25);
        contentPane.add(signupLabel);
    }

    public void close() {
        dispose();
    }

    private void LoginBtn(String ip, int port) {
        String user = userField.getText();
        String pass = new String(passField.getPassword());
        String data = "{\"function\":\"login\",\"username\":\"" + user + "\", \"password\":\"" + pass + "\"}";
        if(!user.isEmpty() || !pass.isEmpty()) {
            Thread thread = new Thread(() -> {
                try {
                    this.client = new Socket(ip, port);
                    LoginHandler loginHandler = new LoginHandler(data);
                    Thread th = new Thread(loginHandler);
                    th.start();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(contentPane, "Server không hoạt động", titleMess, JOptionPane.WARNING_MESSAGE);
                }
            });
            thread.start();
        } else {
            JOptionPane.showMessageDialog(contentPane, "Hãy nhập đầy đủ thông tin", titleMess, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void shutdown() {
        done = true;
        try {
            if(client != null) client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class LoginHandler implements Runnable {
        private String data;
        public LoginHandler(String data) {
            this.data = data;
        }
        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while(!done) {
                    writer.println(data);
                    JsonParser js = new JsonParser();
                    String getData = reader.readLine();
                    JsonObject value = (JsonObject) js.parse(getData);
                    boolean status = Boolean.parseBoolean(value.get("status").getAsString());
                    if(!status) {
                        JOptionPane.showMessageDialog(contentPane, value.get("message").getAsString(), titleMess, JOptionPane.WARNING_MESSAGE);
                        return;
                    } else {
                        done = true;
                        JsonObject dataPlayer = (JsonObject) value.get("data");
                        Gson gson = new Gson();
                        Player player = gson.fromJson(dataPlayer, Player.class);
                        new Main(client, player).setVisible(true);
                        frame.dispose();
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }

}
