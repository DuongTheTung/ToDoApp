package todoapp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Todolist extends JFrame {

    private String username;
    private JTable table;
    private DefaultTableModel model;
    private JPanel panel1, panel2;
    private JLabel lblTitle, lblDescription, lblDeadline;
    private JTextField Title, Description, Deadline;
    private JButton Logout, Add, Delete;

    public Todolist(String username) {
        this.username = username;

        initComponents();
        loadTasksFromDatabase();

        setTitle("To-Do App - User: " + username);
        setSize(1200, 700);
        getContentPane().setBackground(new Color(220, 220, 220));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    private void initComponents() {
        // ===== Panel 1 =====
        panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setBackground(new Color(220, 220, 220));
        panel1.setBounds(0, 50, 450, 660);
        panel1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(java.awt.Color.BLACK, 1),
                "Add Task",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        add(panel1);

        // ===== Panel 2 =====
        panel2 = new JPanel();
        panel2.setLayout(null);
        panel2.setBackground(new Color(220, 220, 220));
        panel2.setBounds(460, 0, 720, 660);
        panel2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(java.awt.Color.BLACK, 1),
                "Task List",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        add(panel2);

        // ===== Label =====
        lblTitle = new JLabel("Title:");
        lblTitle.setBounds(50, 250, 100, 30);

        lblDescription = new JLabel("Description:");
        lblDescription.setBounds(50, 300, 100, 30);

        lblDeadline = new JLabel("Deadline (YYYY-MM-DD):");
        lblDeadline.setBounds(50, 350, 200, 30);

        // ===== TextField =====
        Title = new JTextField();
        Title.setBounds(200, 250, 200, 30);

        Description = new JTextField();
        Description.setBounds(200, 300, 200, 30);

        Deadline = new JTextField();
        Deadline.setBounds(200, 350, 200, 30);

        // ===== Button: Add =====
        Add = new JButton("Add");
        Add.setBounds(300, 400, 80, 30);
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTaskToDatabase();
            }
        });

        // ===== Add components to panel1 =====
        panel1.add(lblTitle);
        panel1.add(lblDescription);
        panel1.add(lblDeadline);
        panel1.add(Title);
        panel1.add(Description);
        panel1.add(Deadline);
        panel1.add(Add);

        // ===== Table for panel2 =====
        String[] columns = {"Date", "Task", "Description", "Due Date"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 40, 680, 550);
        panel2.add(scrollPane);

        // ===== Button: Delete =====
        Delete = new JButton("Delete");
        Delete.setBounds(600, 600, 100, 30);
        Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedTask();
            }
        });
        panel2.add(Delete);

        // ===== Button: Logout =====
        Logout = new JButton("Logout");
        Logout.setBounds(10, 20, 100, 30);
        Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login l = new Login();
                l.setVisible(true);
                l.setLocationRelativeTo(null);
            }
        });
        add(Logout);

    }

    private void addTaskToDatabase() {
        String title = Title.getText();
        String desc = Description.getText();
        String deadline = Deadline.getText();

        if (title.isEmpty() || desc.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        try (Connection conn = DatabaseConnection.getCon(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Task (date, task, discription, dueDate, username) VALUES (CURDATE(), ?, ?, ?, ?)")) {

            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, deadline);
            ps.setString(4, username);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Task added successfully!");
            model.setRowCount(0); // Đảm bảo không trùng dữ liệu 
            loadTasksFromDatabase();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding task: " + e.getMessage());
        }
    }

    private void loadTasksFromDatabase() {
        try (Connection conn = DatabaseConnection.getCon(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM Task WHERE username = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0); // clear table
            while (rs.next()) {
                String date = rs.getString("date");
                String task = rs.getString("task");
                String desc = rs.getString("discription");
                String due = rs.getString("dueDate");

                model.addRow(new Object[]{date, task, desc, due});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage());
        }
    }

    private void deleteSelectedTask() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete!");
            return;
        }

        String task = (String) model.getValueAt(selectedRow, 1);

        try (Connection conn = DatabaseConnection.getCon(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Task WHERE task = ? AND username = ?")) {

            ps.setString(1, task);
            ps.setString(2, username);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Task deleted!");
            model.removeRow(selectedRow);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting task: " + e.getMessage());
        }
    }

}
