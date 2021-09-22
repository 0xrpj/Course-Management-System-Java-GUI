import javax.swing.*;
import java.awt.*;
import java.awt.Component.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;

public class Administrator extends JFrame {
    JPanel panel;
    DefaultTableModel tableModel;
    Object[] rowData = {};
    JTextField moduleIDTextField;
    JTextField moduleTextField;
    JTextField mlTextField;
    JComboBox<String> courseField;
    Vector<String> allCourses;
    JComboBox<String> course;
    UserRegistration reg;
    int rowNo = -1;

    public Administrator() {
        setTitle("Welcome administrator!");
        setSize(1000, 650);
        setLocationRelativeTo(null); // for the window to appear on the middle
        add(getPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to completely close the application on exit
        setVisible(true);
    }

    private JPanel getPanel() {
        reg = new UserRegistration();
        Font sans = new Font("SansSerif", Font.PLAIN, 17);
        Font ariel = new Font("Ariel", Font.PLAIN, 20);
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2)); // 2 rows and 2 columns

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(1, 0));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Prakash Shrestha"));
        // tablePanel.add(new JLabel("All events"));

        // Table for working with data
        JTable table = new JTable();
        Object[] columnNames = { "Module ID", "Name", "Course", "Module Leader" };

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        table.setModel(tableModel);

        // to make data to table uneditable
        table.setDefaultEditor(Object.class, null);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rowNo = table.getSelectedRow();
                moduleTextField.setText((String) tableModel.getValueAt(rowNo, 1));
                // courseField.setText((String) tableModel.getValueAt(rowNo, 2));
                courseField.setSelectedItem((Object) tableModel.getValueAt(rowNo, 2));
                mlTextField.setText((String) tableModel.getValueAt(rowNo, 3));
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

        // ScrollPane to add the table to
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All modules and Instructors"));
        tablePanel.add(scrollPane);

        JButton getInstructors = new JButton("Instructors");
        getInstructors.setFont(ariel);
        getInstructors.setBackground(Color.BLACK);
        getInstructors.setForeground(Color.WHITE);
        // panel.add(getInstructors);

        // tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        try {
            ResultSet resultSet = reg.getAllModules();
            while (resultSet.next()) {
                rowData = new Object[] { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4) };

                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        panel.add(tablePanel);

        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel moduleLabel = new JLabel("Module Name");
        inputPanel.add(moduleLabel);

        moduleTextField = new JTextField();
        moduleTextField.setFont(sans);
        inputPanel.add(moduleTextField);

        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());

        JLabel courseLabell = new JLabel("Course");
        inputPanel.add(courseLabell);

        allCourses = new Vector<String>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("txtfiles/courses.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.trim().equals(""))
                    allCourses.add(str);
            }
        } catch (IOException eii) {
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }

        courseField = new JComboBox<String>(allCourses);
        courseField.setFont(sans);
        inputPanel.add(courseField);

        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());

        JLabel mlLabel = new JLabel("Module Leader");
        inputPanel.add(mlLabel);

        mlTextField = new JTextField();
        mlTextField.setFont(sans);
        inputPanel.add(mlTextField);

        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(ariel);
        clearBtn.setBackground(Color.BLACK);
        clearBtn.setForeground(Color.WHITE);
        inputPanel.add(clearBtn);

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // moduleIDTextField.setText("");
                moduleTextField.setText(" ");
                mlTextField.setText("");
                // courseField.setText("");
            }
        });

        JButton updateBtn = new JButton("Update");
        updateBtn.setFont(ariel);
        updateBtn.setBackground(Color.BLACK);
        updateBtn.setForeground(Color.WHITE);
        inputPanel.add(updateBtn);

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reg.updateModule();
                rowNo = table.getSelectedRow();

                if (rowNo != -1 && (moduleTextField.getText().trim().length() > 0
                        && mlTextField.getText().trim().length() > 0)) {
                    reg.updateModule((String) tableModel.getValueAt(rowNo, 0), moduleTextField.getText(),
                            (String) courseField.getSelectedItem(), mlTextField.getText());

                    tableModel.setValueAt(moduleTextField.getText(), rowNo, 1);
                    tableModel.setValueAt((String) courseField.getSelectedItem(), rowNo, 2);
                    tableModel.setValueAt(mlTextField.getText(), rowNo, 3);
                    clearBtn.doClick();
                } else {

                }

            }
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(ariel);
        deleteBtn.setBackground(Color.BLACK);
        deleteBtn.setForeground(Color.WHITE);
        inputPanel.add(deleteBtn);

        JButton adddBtn = new JButton("Add");
        adddBtn.setFont(ariel);
        adddBtn.setBackground(Color.BLACK);
        adddBtn.setForeground(Color.WHITE);
        inputPanel.add(adddBtn);

        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new GridLayout(2, 0));
        // buttonPanel1.setBorder(BorderFactory.createTitledBorder("Course Related"));

        buttonPanel1.add(new JLabel());

        JPanel buttonPanel1i = new JPanel();
        buttonPanel1i.setLayout(new GridLayout(2, 4));
        buttonPanel1i.setBorder(BorderFactory.createTitledBorder("Course Related"));

        // buttonPanel1i.add(new JLabel());
        // buttonPanel1i.add(new JLabel());
        // buttonPanel1i.add(new JLabel());
        // buttonPanel1i.add(new JLabel());

        JButton addBtn = new JButton("Add");
        addBtn.setFont(ariel);
        addBtn.setBackground(Color.BLACK);
        addBtn.setForeground(Color.WHITE);
        buttonPanel1i.add(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String course_name = JOptionPane.showInputDialog(null, "What is the course called?");
                System.out.println(course_name);
                PrintWriter out = null;
                try {
                    out = new PrintWriter(new BufferedWriter(new FileWriter("txtfiles/courses.txt", true)));
                    if (course_name != null) {

                        File file = new File("txtfiles/cancelled-courses.txt");
                        try {
                            List<String> cancelledlist = Files.lines(file.toPath()).collect(Collectors.toList());

                            if (cancelledlist.contains(course_name)) {
                                cancelledlist.remove(course_name);

                                Files.write(file.toPath(), cancelledlist, StandardOpenOption.WRITE,
                                        StandardOpenOption.TRUNCATE_EXISTING);

                            }
                        } catch (IOException ioex) {
                            ioex.printStackTrace();
                        }

                        out.println(course_name);
                        JOptionPane.showMessageDialog(null, "Successfully Added.", "Adding a course",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe);
                    JOptionPane.showMessageDialog(null, "An exception occured.", "Adding a course",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        });

        JButton editBtn = new JButton("Edit");
        editBtn.setFont(ariel);
        editBtn.setBackground(Color.BLACK);
        editBtn.setForeground(Color.WHITE);

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame editframe = new JFrame();
                editframe.setTitle("Editing a course");
                editframe.setSize(500, 280);
                editframe.setLocationRelativeTo(panel); // for the window to appear on the middle

                JPanel edit_panel = new JPanel();
                edit_panel.setLayout(new GridLayout(5, 2));
                editframe.add(edit_panel);

                edit_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // for

                JLabel slt = new JLabel("Select a course to edit");
                slt.setFont(ariel);
                edit_panel.add(slt);

                allCourses = new Vector<String>();

                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader("txtfiles/courses.txt"));
                    String str;
                    while ((str = in.readLine()) != null) {
                        if (!str.trim().equals(""))
                            allCourses.add(str);
                    }
                } catch (IOException eii) {
                } finally {
                    try {
                        in.close();
                    } catch (Exception ex) {
                    }
                }

                course = new JComboBox<String>(allCourses);
                course.setFont(sans);
                edit_panel.add(course);

                edit_panel.add(new JLabel());
                edit_panel.add(new JLabel());

                JLabel cnt = new JLabel("Change name to: ");
                cnt.setFont(ariel);
                edit_panel.add(cnt);

                JTextField tf = new JTextField();
                tf.setFont(ariel);

                edit_panel.add(tf);
                edit_panel.add(new JLabel());
                edit_panel.add(new JLabel());

                JButton cancelEditbtn = new JButton("Cancel");
                cancelEditbtn.setFont(ariel);
                cancelEditbtn.setBackground(Color.BLACK);
                cancelEditbtn.setForeground(Color.WHITE);
                cancelEditbtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editframe.dispose();
                    }
                });
                edit_panel.add(cancelEditbtn);

                JButton submitEditbtn = new JButton("Submit");
                submitEditbtn.setFont(ariel);
                submitEditbtn.setBackground(Color.BLACK);
                submitEditbtn.setForeground(Color.WHITE);
                submitEditbtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {

                            List<String> fileContent = new ArrayList<>(
                                    Files.readAllLines(Paths.get("txtfiles/courses.txt"), StandardCharsets.UTF_8));

                            String oldvalue = String.valueOf(course.getSelectedItem());
                            String newvalue = tf.getText();

                            for (int i = 0; i < fileContent.size(); i++) {
                                if (fileContent.get(i).equals(oldvalue)) {
                                    fileContent.set(i, newvalue);
                                    JOptionPane.showMessageDialog(null, "Successfully updated.",
                                            "Updating a course name", JOptionPane.INFORMATION_MESSAGE);
                                    cancelEditbtn.doClick();
                                    break;
                                }
                            }

                            Files.write(Paths.get("txtfiles/courses.txt"), fileContent, StandardCharsets.UTF_8);
                        } catch (Exception ioef) {
                            ioef.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Couldnot update the course name. Exception occured.",
                                    "Updating a course name", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });
                edit_panel.add(submitEditbtn);

                editframe.setVisible(true);
            }
        });

        buttonPanel1i.add(editBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(ariel);
        cancelBtn.setBackground(Color.BLACK);
        cancelBtn.setForeground(Color.WHITE);

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame cancelframe = new JFrame();
                cancelframe.setTitle("Cancelling a course");
                cancelframe.setSize(500, 180);
                cancelframe.setLocationRelativeTo(panel); // for the window to appear on the middle

                JPanel temp_panel = new JPanel();
                temp_panel.setLayout(new GridLayout(3, 2));
                cancelframe.add(temp_panel);

                temp_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // for

                JLabel slt = new JLabel("Select a course");
                slt.setFont(ariel);
                temp_panel.add(slt);

                allCourses = new Vector<String>();

                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader("txtfiles/courses.txt"));
                    String str;
                    while ((str = in.readLine()) != null) {
                        if (!str.trim().equals(""))
                            allCourses.add(str);
                    }
                } catch (IOException eii) {
                } finally {
                    try {
                        in.close();
                    } catch (Exception ex) {
                    }
                }

                course = new JComboBox<String>(allCourses);
                course.setFont(sans);
                temp_panel.add(course);

                temp_panel.add(new JLabel());
                temp_panel.add(new JLabel());
                temp_panel.add(new JLabel());

                JButton submit_cancel = new JButton("Submit");
                submit_cancel.setFont(ariel);
                submit_cancel.setBackground(Color.BLACK);
                submit_cancel.setForeground(Color.WHITE);

                submit_cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String x = String.valueOf(course.getSelectedItem());
                        System.out.println(x);

                        File file = new File("txtfiles/courses.txt");
                        File file2 = new File("txtfiles/cancelled-courses.txt");
                        try {
                            List<String> out = Files.lines(file.toPath()).filter(line -> !line.contains(x))
                                    .collect(Collectors.toList());
                            List<String> out1 = Files.lines(file.toPath()).filter(line -> line.contains(x))
                                    .collect(Collectors.toList());
                            Files.write(file.toPath(), out, StandardOpenOption.WRITE,
                                    StandardOpenOption.TRUNCATE_EXISTING);
                            Files.write(file2.toPath(), out1, StandardOpenOption.WRITE, StandardOpenOption.APPEND);

                            JOptionPane.showMessageDialog(null, "Successfully cancelled.", "Deleting a course",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ioex) {
                            ioex.printStackTrace();
                        }
                        cancelframe.dispose();
                    }
                });
                temp_panel.add(submit_cancel);
                cancelframe.setVisible(true);
            }
        });

        buttonPanel1i.add(cancelBtn);

        JButton removeBtn = new JButton("Remove");
        removeBtn.setFont(ariel);
        removeBtn.setBackground(Color.BLACK);
        removeBtn.setForeground(Color.WHITE);
        buttonPanel1i.add(removeBtn);

        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame removeframe = new JFrame();
                removeframe.setTitle("Removing a course permanently");
                removeframe.setSize(500, 180);
                removeframe.setLocationRelativeTo(panel); // for the window to appear on the middle

                JPanel temp_panel = new JPanel();
                temp_panel.setLayout(new GridLayout(3, 2));
                removeframe.add(temp_panel);

                temp_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // for the

                JLabel slt = new JLabel("Select a course");
                slt.setFont(ariel);
                temp_panel.add(slt);

                allCourses = new Vector<String>();

                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader("txtfiles/courses.txt"));
                    String str;
                    while ((str = in.readLine()) != null) {
                        if (!str.trim().equals(""))
                            allCourses.add(str);
                    }
                } catch (IOException eii) {
                } finally {
                    try {
                        in.close();
                    } catch (Exception ex) {
                    }
                }

                course = new JComboBox<String>(allCourses);
                course.setFont(sans);
                temp_panel.add(course);

                temp_panel.add(new JLabel());
                temp_panel.add(new JLabel());
                temp_panel.add(new JLabel());

                JButton submit_del = new JButton("Submit");
                submit_del.setFont(ariel);
                submit_del.setBackground(Color.BLACK);
                submit_del.setForeground(Color.WHITE);

                submit_del.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String x = String.valueOf(course.getSelectedItem());
                        System.out.println(x);

                        File file = new File("txtfiles/courses.txt");
                        try {
                            List<String> out = Files.lines(file.toPath()).filter(line -> !line.contains(x))
                                    .collect(Collectors.toList());
                            Files.write(file.toPath(), out, StandardOpenOption.WRITE,
                                    StandardOpenOption.TRUNCATE_EXISTING);
                            JOptionPane.showMessageDialog(null, "Successfully Deleted.", "Deleting a course",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ioex) {
                            ioex.printStackTrace();
                        }
                        removeframe.dispose();
                    }
                });

                temp_panel.add(submit_del);

                removeframe.setVisible(true);

                // String course_name = JOptionPane.showInputDialog(null, "What is the course
                // called?");
                // System.out.println(course_name);
                // PrintWriter out = null;
                // try {
                // out = new PrintWriter(new BufferedWriter(new
                // FileWriter("txtfiles/courses.txt", true)));
                // out.println(course_name);
                // JOptionPane.showMessageDialog(null, "Successfully Added.", "Adding a course",
                // JOptionPane.INFORMATION_MESSAGE);
                // } catch (IOException ioe) {
                // System.err.println(ioe);
                // JOptionPane.showMessageDialog(null, "An exception occured.", "Adding a
                // course",
                // JOptionPane.ERROR_MESSAGE);
                // } finally {
                // if (out != null) {
                // out.close();
                // }
                // }
            }
        });

        buttonPanel1.add(buttonPanel1i);
        buttonPanel.add(buttonPanel1);

        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(2, 0));
        // buttonPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel2.add(new JLabel());

        JPanel buttonPanel2i = new JPanel();
        buttonPanel2i.setLayout(new GridLayout(3, 0));
        buttonPanel2i.setBorder(BorderFactory.createTitledBorder("Other Actions"));

        JButton viewDelBtn = new JButton("Cancelled courses");
        viewDelBtn.setFont(ariel);
        viewDelBtn.setBackground(Color.BLACK);
        viewDelBtn.setForeground(Color.WHITE);
        viewDelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame detailsFrame = new JFrame();
                detailsFrame.setTitle("Cancelled courses");
                detailsFrame.setSize(500, 180);
                detailsFrame.setLocationRelativeTo(panel); // for the window to appear on the middle

                JTextArea textArea = new JTextArea("These are the cancelled courses:\n");
                textArea.setFont(ariel);
                textArea.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader("txtfiles/cancelled-courses.txt"));
                    String str;
                    while ((str = in.readLine()) != null) {
                        textArea.append(str + "\n");
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (Exception ex) {
                    }
                }

                JScrollPane scrollPanetextArea = new JScrollPane(textArea);
                textArea.setEditable(false);
                textArea.setCaretPosition(0);
                detailsFrame.add(scrollPanetextArea);
                detailsFrame.setVisible(true);

            };
        });

        buttonPanel2i.add(viewDelBtn);

        JButton studDetBtn = new JButton("Student Details");
        studDetBtn.setFont(ariel);
        studDetBtn.setBackground(Color.BLACK);
        studDetBtn.setForeground(Color.WHITE);
        // studDetBtn

        buttonPanel2i.add(studDetBtn);

        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setFont(ariel);
        logOutBtn.setBackground(Color.BLACK);
        logOutBtn.setForeground(Color.WHITE);
        buttonPanel2i.add(logOutBtn);

        logOutBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }

        });

        buttonPanel2.add(buttonPanel2i);
        buttonPanel.add(buttonPanel2);

        secondPanel.add(inputPanel);
        secondPanel.add(buttonPanel);
        secondPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // for
                                                                                // the

        panel.add(secondPanel);

        return panel;
    }

    public static void main(String[] args) {
        new Administrator();
    }
}
