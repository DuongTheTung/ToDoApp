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
public class SignUp extends JFrame {

    private JPanel panel;
    private JLabel lbltitle, lblName, lblEmail, lblUsername, lblPassword;
    private JTextField name, email, username;
    private JPasswordField password;
    private JButton sign_in, register;

    public SignUp() {
        initComponents();

        setTitle("SignUp");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    public void initComponents() {

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(220, 220, 220)); // Màu nền xám nhạt
        panel.setBounds(0, 0, 400, 500);
        add(panel);

        // ===== Label =====
        lbltitle = new JLabel("New User Register");
        lblName = new JLabel("Name");
        lblEmail = new JLabel("Email id");
        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");

        lbltitle.setBounds(100, 40, 300, 30);
        lbltitle.setFont(lbltitle.getFont().deriveFont(25f));
        lblName.setBounds(50, 100, 100, 30);
        lblUsername.setBounds(50, 150, 100, 30);
        lblEmail.setBounds(50, 200, 100, 30);
        lblPassword.setBounds(50, 250, 100, 30);

        // ===== Text Fields =====
        name = new JTextField();
        username = new JTextField();
        email = new JTextField();
        password = new JPasswordField();

        name.setBounds(150, 100, 150, 30);
        username.setBounds(150, 150, 150, 30);
        email.setBounds(150, 200, 150, 30);
        password.setBounds(150, 250, 150, 30);

        name.setFont(name.getFont().deriveFont(16f));
        username.setFont(username.getFont().deriveFont(16f));
        email.setFont(email.getFont().deriveFont(16f));
        password.setFont(password.getFont().deriveFont(16f));

        // ===== Buttons =====
        sign_in = new JButton("Sign in");
        sign_in.setBounds(80, 350, 80, 30);
        sign_in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login l = new Login();
                l.setLocationRelativeTo(null);
                l.setVisible(true);
            }
        });

        register = new JButton("Register");
        register.setBounds(200, 350, 100, 30);
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Name = name.getText();
                String emailId = email.getText();
                String Username = username.getText();
                String Password = password.getText();

                try (Connection con = DatabaseConnection.getCon()) {
                    String checkQuery = "SELECT COUNT(*) FROM details WHERE username = ? AND password = ?";
                    PreparedStatement pst = con.prepareStatement(checkQuery);
                    pst.setString(1, Username);
                    pst.setString(2, Password);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(null, "Already Registered");
                    } else {
                        String insertQuery = "INSERT INTO details(Name, emailId, Username, Password) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertPst = con.prepareStatement(insertQuery);
                        insertPst.setString(1, Name);
                        insertPst.setString(2, emailId);
                        insertPst.setString(3, Username);
                        insertPst.setString(4, Password);

                        insertPst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Information Inserted.");

                        dispose();
                        Login l = new Login();
                        l.setLocationRelativeTo(null);
                        l.setVisible(true);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });

        // ===== Add to panel =====
        panel.add(lbltitle);
        panel.add(lblName);
        panel.add(lblEmail);
        panel.add(lblUsername);
        panel.add(lblPassword);
        panel.add(name);
        panel.add(email);
        panel.add(username);
        panel.add(password);
        panel.add(register);
        panel.add(sign_in);

    }

}
