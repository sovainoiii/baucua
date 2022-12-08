package Server;

import Model.Player;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.net.*;
import java.util.ArrayList;

public class Main extends JFrame {

    private JPanel contentPane;
    private JTextField ipField;
    private JTextField portField;
    private JTable playerTable;
    private static DefaultTableModel model;
    private static JLabel numberOnline;
    private static Server server;
    private Thread thread;
    private static int port = 1234;

    /**
     * Launch the application.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
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
    public Main() {

        setTitle("Server");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 670, 400);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Server");
        lblNewLabel.setForeground(new Color(0, 0, 0));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(290, 10, 70, 25);
        contentPane.add(lblNewLabel);

        ipField = new JTextField();
        ipField.setEditable(false);
        ipField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        try {
            ipField.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ipField.setBounds(95, 50, 150, 25);
        contentPane.add(ipField);
        ipField.setColumns(10);

        JLabel ipLabel = new JLabel("Địa chỉ IP");
        ipLabel.setLabelFor(ipField);
        ipLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        ipLabel.setBounds(25, 50, 60, 25);
        contentPane.add(ipLabel);

        JLabel portLabel = new JLabel("Port");
        portLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portLabel.setBounds(275, 50, 40, 25);
        contentPane.add(portLabel);

        portField = new JTextField();
        portLabel.setLabelFor(portField);
        portField.setText(String.valueOf(port));
        portField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        portField.setBounds(315, 50, 70, 25);
        contentPane.add(portField);
        portField.setColumns(10);

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new TitledBorder(null, "Thông tin", TitledBorder.CENTER, TitledBorder.TOP, null, Color.GRAY));
        infoPanel.setBounds(420, 45, 210, 105);
        infoPanel.setBackground(Color.WHITE);
        contentPane.add(infoPanel);
        infoPanel.setLayout(null);

        JLabel statusLabel = new JLabel("Trạng thái:");
        statusLabel.setBounds(20, 25, 80, 25);
        infoPanel.add(statusLabel);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel status = new JLabel("TẮT");
        status.setBounds(110, 25, 78, 25);
        infoPanel.add(status);
        status.setForeground(Color.RED);
        status.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setLabelFor(status);

        JLabel onlineLabel = new JLabel("Online:");
        onlineLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        onlineLabel.setBounds(20, 66, 80, 25);
        infoPanel.add(onlineLabel);

        numberOnline = new JLabel("0");
        numberOnline.setForeground(Color.BLUE);
        numberOnline.setFont(new Font("Tahoma", Font.PLAIN, 14));
        numberOnline.setBounds(110, 65, 78, 25);
        infoPanel.add(numberOnline);

        JPanel optionPanel = new JPanel();
        optionPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thao t\u00E1c", TitledBorder.CENTER, TitledBorder.TOP, null, Color.GRAY));
        optionPanel.setBounds(25, 86, 361, 64);
        contentPane.add(optionPanel);

        JButton startBtn = new JButton("Run server");
        optionPanel.add(startBtn);
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.setIcon(new ImageIcon(Main.class.getResource("/Image/connect.png")));
        startBtn.setFocusable(false);
        startBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JButton stopBtn = new JButton("Stop server");
        optionPanel.add(stopBtn);
        stopBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        stopBtn.setIcon(new ImageIcon(Main.class.getResource("/Image/stop.png")));
        stopBtn.setFocusable(false);
        stopBtn.setEnabled(false);
        stopBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 160, 602, 180);
        contentPane.add(scrollPane);

        playerTable = new JTable();
        playerTable.setEnabled(false);
        playerTable.setShowGrid(false);
        playerTable.setRowSelectionAllowed(false);
        playerTable.setFocusable(false);
        model = (DefaultTableModel) playerTable.getModel();
        Object[] columns = {"Tên", "IP"};
        model.setColumnIdentifiers(columns);
        scrollPane.setViewportView(playerTable);

        startBtn.addActionListener(e -> {
            try {
                String portTxt = portField.getText();
                if(!portTxt.isEmpty()) {
                    port = Integer.parseInt(portTxt);
                    server = new Server(port);
                    thread = new Thread(server);
                    thread.start();
                    status.setText("<html><font color='green'>ĐANG CHẠY</font></html>");
                    stopBtn.setEnabled(true);
                    startBtn.setEnabled(false);
                    portField.setEditable(false);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Bạn chưa nhập PORT cho server!", "Server", JOptionPane.WARNING_MESSAGE);
                }
            }catch (Exception e1) { }
        });

        stopBtn.addActionListener(e -> {
            status.setText("<html><font color='red'>TẮT</font></html>");
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            portField.setEditable(true);
            server.shutdown();
            thread.interrupt();
        });

    }

    public static void updateNumberOnline() {
        ArrayList<Player> list = server.getAllUser();
        numberOnline.setText(Integer.toString(list.size()));
        displayUser();
    }

    public static void displayUser() {
        ArrayList<Player> list = server.getAllUser();
        if(model.getRowCount() > 0) {
            for(int i = model.getRowCount()-1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
        for (Player player : list) {
            model.addRow(new Object[] {player.getName(), player.getIP()});
        }
    }
}

