/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package todoapp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author Tung Duong
 */
public class Login extends JFrame {

    private JPanel panel;
    private JLabel lbltitle, lblUsername, lblPassword;
    private JTextField username;
    private JPasswordField password;
    private JButton login, sign_up;

    public Login() {
        initComponents();

        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    private void initComponents() {

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(220, 220, 220)); // Màu nền xám nhạt
        panel.setBounds(0, 0, 400, 300); // phủ toàn bộ JFrame
        add(panel);

        // ===== Label =====
        lbltitle = new JLabel("Login");
        lbltitle.setBounds(150, 40, 100, 30);
        lbltitle.setFont(lbltitle.getFont().deriveFont(25f));

        lblUsername = new JLabel("Username");
        lblUsername.setBounds(50, 100, 100, 30);

        lblPassword = new JLabel("Password");
        lblPassword.setBounds(50, 150, 100, 30);

        // ===== TextField =====
        username = new JTextField();
        username.setBounds(150, 100, 150, 30);
        username.setFont(username.getFont().deriveFont(16f));

        password = new JPasswordField();
        password.setBounds(150, 150, 150, 30);
        password.setFont(password.getFont().deriveFont(16f));

        // ===== Button: Login =====
        login = new JButton("Login");
        login.setBounds(100, 200, 80, 30);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Username = username.getText().trim();
                String Password = password.getText().trim();

                if (Username.isEmpty() || Password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both username and password.", "Alert", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (Connection con = DatabaseConnection.getCon()) {
                    String query = "SELECT * FROM details WHERE username = ? AND password = ?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, Username);
                    pst.setString(2, Password);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Welcome!", "Alert", JOptionPane.INFORMATION_MESSAGE);
                        int userId = rs.getInt("Id");
                        dispose();

                        Todolist tda = new Todolist(Username);
                        tda.setLocationRelativeTo(null);
                        tda.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Incorrect username or password.\nPlease reset your password or create an account.",
                                "Alert", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ===== Button: Sign Up =====
        sign_up = new JButton("Sign up");
        sign_up.setBounds(230, 200, 80, 30);
        sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SignUp su = new SignUp();
                su.setVisible(true);
                su.setLocationRelativeTo(null);
            }
        });

        // ===== Add Components to Panel =====
        panel.add(lbltitle);
        panel.add(lblUsername);
        panel.add(lblPassword);
        panel.add(username);
        panel.add(password);
        panel.add(login);
        panel.add(sign_up);
    }

    public void infoMessage(String message, String tittle) {
        JOptionPane.showMessageDialog(null, message, tittle, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Login();
    }

}
