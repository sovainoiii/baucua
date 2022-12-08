package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import Model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Signup extends JFrame {

    private JPanel contentPane;
    private JTextField userField;
    private JPasswordField passField;
    private JTextField nameField;
    private JPasswordField rePassField;
    private JTextField ipField;
    private JTextField portField;
    private Socket client;
    private boolean done = false;
    private final String titleMess = "Đăng ký";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Signup frame = new Signup();
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
    public Signup() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 435);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 62, 62));

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("Đăng ký");
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.PLAIN, 20));
        title.setBounds(128, 15, 87, 25);
        contentPane.add(title);

        JLabel userLabel = new JLabel("Tài khoản");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userLabel.setBounds(35, 55, 70, 22);
        contentPane.add(userLabel);

        userField = new JTextField();
        userField.setToolTipText("Tài khoản");
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
        passField.setToolTipText("Mật khẩu");
        passField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passLabel.setLabelFor(passField);
        passField.setBounds(115, 100, 180, 25);
        contentPane.add(passField);

        JLabel lblNewLabel_1 = new JLabel("Nhập lại");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(35, 145, 70, 25);
        contentPane.add(lblNewLabel_1);

        rePassField = new JPasswordField();
        rePassField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rePassField.setBounds(115, 145, 180, 25);
        contentPane.add(rePassField);

        JLabel nameLabel = new JLabel("Tên game");
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(35, 190, 70, 25);
        contentPane.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(115, 190, 180, 25);
        contentPane.add(nameField);
        nameField.setColumns(10);

        JLabel ipLabel = new JLabel("IP Server");
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        ipLabel.setBounds(35, 235, 70, 25);
        contentPane.add(ipLabel);

        ipField = new JTextField();
        ipField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        try {
            ipField.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        ipField.setBounds(115, 235, 180, 25);
        contentPane.add(ipField);

        JLabel portLabel = new JLabel("Port server");
        portLabel.setForeground(Color.WHITE);
        portLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portLabel.setBounds(35, 280, 70, 25);
        contentPane.add(portLabel);

        portField = new JTextField();
        portField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portField.setBounds(115, 280, 80, 25);
        contentPane.add(portField);

        JButton SignupBtn = new JButton("Đăng ký");
        SignupBtn.addActionListener(e -> {
            if(!portField.getText().isEmpty()) {
                try {
                    int port = Integer.parseInt(portField.getText());
                    String ip = ipField.getText();
                    signUp(ip, port);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(contentPane, "Hãy nhập đúng định dạng", titleMess, JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        SignupBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
        SignupBtn.setBounds(128, 325, 95, 30);
        contentPane.add(SignupBtn);

        JLabel lblNewLabel = new JLabel("Bạn đã có tài khoản?");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(62, 360, 131, 25);
        contentPane.add(lblNewLabel);

        JLabel loginLabel = new JLabel("Đăng nhập");
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
                Login login = new Login();
                login.setVisible(true);
            }
        });
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginLabel.setForeground(new Color(0, 255, 255));
        loginLabel.setBounds(207, 360, 70, 25);
        contentPane.add(loginLabel);
    }

    public void close() {
        dispose();
    }

    private void signUp(String ip, int port) {
        String user = userField.getText();
        String name = nameField.getText();
        String pass =  new String(passField.getPassword());
        String rePass = new String(rePassField.getPassword());
        String IP;
        try {
            IP = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        if(!user.isEmpty()) {
            if(user.length() > 4) {
                if (!name.isEmpty()) {
                    if(name.length() > 4) {
                        if(pass.equals(rePass)) {
                            Thread thread = new Thread(() -> {
                                try {
                                    this.client = new Socket(ip, port);

                                    String data = "{\"function\":\"signup\",\"username\":\"" + user + "\", \"password\":\"" + pass + "\", \"IP\":\"" +IP + "\", \"name\":\"" + name + "\"}";
                                    Signup.SignupHandler signupHandler = new SignupHandler(data);
                                    Thread th = new Thread(signupHandler);
                                    th.start();
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(contentPane, "Server không hoạt động", titleMess, JOptionPane.WARNING_MESSAGE);
                                }
                            });
                            thread.start();
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Mật khẩu nhập lại không đúng!", titleMess, JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Tên phải lớn hơn 5 ký tự!", titleMess, JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Không được để trống tên!", titleMess, JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(contentPane, "Tài khoản phải lớn hơn 5 ký tự!", titleMess, JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, "Không được để trống tài khoản!", titleMess, JOptionPane.WARNING_MESSAGE);
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

    class SignupHandler implements Runnable {

        private String data;

        public SignupHandler(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

                while ( !done ) {
                    writer.println(data);
                    JsonParser js = new JsonParser();

                    String getData = reader.readLine();
                    JsonObject value = (JsonObject) js.parse(getData);
                    boolean status = Boolean.parseBoolean(value.get("status").getAsString());
                    if(status) {
                        done = true;
                        JsonObject dataPlayer = (JsonObject) value.get("data");
                        Gson gson = new Gson();
                        Player player = gson.fromJson(dataPlayer, Player.class);
                        new Main(client, player).setVisible(true);
                        close();
                    } else {
                        JOptionPane.showMessageDialog(contentPane, value.get("message").getAsString(), titleMess, JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }
}
