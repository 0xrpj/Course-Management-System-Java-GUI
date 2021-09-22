import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.image.BufferedImage;
import javax.swing.table.DefaultTableModel;

public class Instructor extends JFrame {
    JPanel panel;
    UserRegistration reg;
    String id;
    Object[] rowData;
    int rowNo;

    public Instructor(String id) {
        this.id = id;
        setTitle("Welcome instructor!");
        setSize(700, 550);
        setLocationRelativeTo(null); // for the window to appear on the middle
        add(getPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to completely close the application on exit
        setVisible(true);
        getContentPane().requestFocusInWindow();
    }

    private JPanel getPanel() {
        reg = new UserRegistration();
        Font sans = new Font("SansSerif", Font.PLAIN, 17);
        Font ariel = new Font("Ariel", Font.PLAIN, 18);

        // Font ariel = new Font("Ariel", Font.PLAIN, 20);
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(1, 0));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JPanel subPanelTop = new JPanel();
        subPanelTop.setLayout(new GridLayout(2, 2));

        // String name = "Roshan Parajuli";
        // subPanelTop.add(new JLabel("Name: " + name));
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 2));

        // Insert image here.
        try {
            String filepath = "images/" + id + "_i.png";

            Image myPicture = ImageIO.read(new File(filepath)).getScaledInstance(130, 130, Image.SCALE_SMOOTH);

            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            imagePanel.add(picLabel);
        } catch (IOException i) {
            try {
                Image myPicture = ImageIO.read(new File("images/default.png")).getScaledInstance(130, 130,
                        Image.SCALE_SMOOTH);

                JLabel picLabel = new JLabel(new ImageIcon(myPicture));
                imagePanel.add(picLabel);
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }

        }
        subPanelTop.add(imagePanel);
        // add(picLabel);
        JPanel logOutBtn = new JPanel();
        logOutBtn.setLayout(new GridLayout(2, 2));
        // panelbackbtn.setBorder(BorderFactory.createTitledBorder("panelbackbtn"));

        JButton logOut = new JButton("Log Out");
        logOut.setFont(ariel);
        logOut.setBackground(Color.BLACK);
        logOut.setForeground(Color.WHITE);

        logOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();
            }
        });

        logOutBtn.add(new JLabel());
        logOutBtn.add(logOut);
        logOutBtn.add(new JLabel());
        logOutBtn.add(new JLabel());

        subPanelTop.add(logOutBtn);

        String inst_name = reg.getName(id, "Instructor");
        JLabel stdNameLbl = new JLabel("Name: " + inst_name);
        stdNameLbl.setFont(ariel);
        subPanelTop.add(stdNameLbl);

        JPanel mtpanel = new JPanel();
        mtpanel.setLayout(new GridLayout(2, 1));

        int modules_teach = reg.getInstModules(id).size();
        JLabel modules_teachLbl = new JLabel("Modules teaching: " + modules_teach);
        modules_teachLbl.setFont(ariel);
        mtpanel.add(new JLabel());
        mtpanel.setBorder(BorderFactory.createEmptyBorder(10, 145, 40, 10));
        mtpanel.add(modules_teachLbl);

        subPanelTop.add(mtpanel);

        // subPanelTop.add(new JLabel());
        // subPanelTop.add(new JLabel());

        panelTop.add(subPanelTop);
        panel.add(panelTop);

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1, 2));
        // panelBottom.setBorder(BorderFactory.createTitledBorder("Bottom Panel"));

        JTextArea textArea = new JTextArea("Modules you are teaching:\n\n");
        for (String s : reg.getInstModules(id)) {
            textArea.append(s + "\n");
        }
        textArea.setFont(ariel);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPanetextArea = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setCaretPosition(0);
        panelBottom.add(scrollPanetextArea);

        JPanel gradePanel = new JPanel();
        gradePanel.setLayout(new GridLayout(3, 1));
        gradePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel gradeSt = new JLabel("<html>Grade students<br/><br/>Choose module</html>");
        gradeSt.setFont(ariel);
        gradeSt.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 10));
        gradePanel.add(gradeSt);

        // Vector<String> each_mod = new Vector<String>();
        // each_mod.add("AI and Stuff");
        // each_mod.add("Personal development");
        // each_mod.add("Web development");
        JComboBox<String> mod = new JComboBox<String>(new Vector<String>(reg.getInstModules(id)));
        mod.setFont(ariel);
        mod.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        gradePanel.add(mod);

        JPanel grade_btnp = new JPanel();
        grade_btnp.setLayout(new GridLayout(2, 2));
        // panelbackbtn.setBorder(BorderFactory.createTitledBorder("panelbackbtn"));

        JButton gradeBtn = new JButton("Grade");
        gradeBtn.setFont(ariel);
        gradeBtn.setBackground(Color.BLACK);
        gradeBtn.setForeground(Color.WHITE);

        gradeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame mainGradeFrame = new JFrame();
                mainGradeFrame.setTitle("Grading students!");
                mainGradeFrame.setSize(600, 450);
                mainGradeFrame.setLocationRelativeTo(null); // for the window to appear on the middle
                // add(gradePanelSub());
                mainGradeFrame.setVisible(true);
                mainGradeFrame.getContentPane().requestFocusInWindow();

                JPanel mainGradePanel = new JPanel();
                mainGradePanel.setLayout(new GridLayout(1, 2));

                JTable table = new JTable();
                Object[] columnNames = { "Student ID", "Student Name", "Marks" };

                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.setColumnIdentifiers(columnNames);
                table.setModel(tableModel);

                // to make data to table uneditable
                table.setDefaultEditor(Object.class, null);

                try {
                    ResultSet resultSet = reg.setMarksData((String) mod.getSelectedItem());
                    while (resultSet.next()) {
                        rowData = new Object[] { resultSet.getString(1), resultSet.getString(2),
                                resultSet.getString(3) };

                        tableModel.addRow(rowData);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // ScrollPane to add the table to
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBorder(BorderFactory.createTitledBorder("Details of every students on the module"));
                mainGradePanel.add(scrollPane);

                JPanel addMarksPanel = new JPanel();
                addMarksPanel.setLayout(new GridLayout(7, 2));
                addMarksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel stu_id = new JLabel("Student id: ");
                stu_id.setFont(ariel);
                addMarksPanel.add(stu_id);

                JTextField stu_id_text = new JTextField();
                stu_id_text.setEditable(false);
                addMarksPanel.add(stu_id_text);

                addMarksPanel.add(new JLabel());
                addMarksPanel.add(new JLabel());

                JLabel stu_name = new JLabel("Name: ");
                stu_name.setFont(ariel);

                addMarksPanel.add(stu_name);

                JTextField stu_name_text = new JTextField();

                addMarksPanel.add(stu_name_text);
                stu_name_text.setEditable(false);
                addMarksPanel.add(new JLabel());
                addMarksPanel.add(new JLabel());

                JLabel stu_marks = new JLabel("Marks: ");
                stu_marks.setFont(ariel);

                addMarksPanel.add(stu_marks);

                // addMarksPanel.add(new JLabel());
                JTextField stu_marks_text = new JTextField();
                addMarksPanel.add(stu_marks_text);
                addMarksPanel.add(new JLabel());
                addMarksPanel.add(new JLabel());

                table.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        rowNo = table.getSelectedRow();
                        stu_id_text.setText((String) tableModel.getValueAt(rowNo, 0));
                        stu_name_text.setText((String) tableModel.getValueAt(rowNo, 1));
                        stu_marks_text.setText((String) tableModel.getValueAt(rowNo, 2));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }
                });

                JPanel twobtnPanel = new JPanel();
                twobtnPanel.setLayout(new GridLayout(2, 1));
                // twobtnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JButton clearMarksButton = new JButton("Clear");
                clearMarksButton.setFont(ariel);
                clearMarksButton.setBackground(Color.BLACK);
                clearMarksButton.setForeground(Color.WHITE);
                clearMarksButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        stu_id_text.setText("");
                        stu_name_text.setText(" ");
                        stu_marks_text.setText("");
                    }
                });

                twobtnPanel.add(clearMarksButton);

                JButton submitMarksButton = new JButton("Submit");
                submitMarksButton.setFont(ariel);
                submitMarksButton.setBackground(Color.BLACK);
                submitMarksButton.setForeground(Color.WHITE);

                submitMarksButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String each_module_id = reg.getModuleId((String) mod.getSelectedItem());
                        reg.insertMarks(stu_id_text.getText(), stu_name_text.getText(), stu_marks_text.getText(),
                                each_module_id);
                        if (!stu_marks_text.getText().trim().isEmpty()) {
                            tableModel.setValueAt(stu_marks_text.getText(), rowNo, 2);
                        }
                        clearMarksButton.doClick();

                    }
                });

                twobtnPanel.add(submitMarksButton);

                addMarksPanel.add(new JLabel());
                addMarksPanel.add(twobtnPanel);
                mainGradePanel.add(addMarksPanel);

                mainGradeFrame.add(mainGradePanel);

                // JButton getInstructors = new JButton("Instructors");
                // getInstructors.setFont(ariel);
                // getInstructors.setBackground(Color.BLACK);
                // getInstructors.setForeground(Color.WHITE);
                // // panel.add(getInstructors);

                // // tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                // Object rowDataa = new Object[] { "ML and AI", "Prakash Dahal, Sheela Poudel",
                // "Deepson Shrestha", "4" };

                // tableModel.addRow(rowDataa);

            }
        });

        grade_btnp.add(new JLabel());
        grade_btnp.add(new JLabel());
        grade_btnp.add(new JLabel());
        grade_btnp.add(gradeBtn);

        gradePanel.add(grade_btnp);

        // gradePanel.add(new JLabel("Duh"));
        panelBottom.add(gradePanel);

        panel.add(panelBottom);
        return panel;
    }

    public static void main(String[] args) {
        new Instructor("7");
    }

}
