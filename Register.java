import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Register extends JFrame {
    JPanel panel;
    JLabel uniID;
    JLabel phoneNum;
    JTextField uniIDfield;
    JTextField namefield;
    JTextField phoneNumfield;
    JLabel password;
    JLabel fullName;
    JLabel emailLabel;
    JTextField email;
    JPasswordField passfield;
    JLabel userRoleLabel;
    JLabel courseLabel;
    JComboBox<String> userRole;
    JComboBox<String> course;
    Vector<String> allCourses;
    JButton registerBtn;
    JButton backBtn;
    String uni_id;
    String phone;
    String name;
    String pass;
    String e_mail;
    String user_role;
    String selected_course;
    UserRegistration reg;

    Register() {
        setTitle("Register");
        setSize(500, 430);
        setLocationRelativeTo(null); // for the window to appear on the middle
        add(getPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to completely close the application on exit
        setVisible(true);
    }

    JPanel getPanel() {
        Font sans = new Font("SansSerif", Font.PLAIN, 17);
        Font ariel = new Font("Ariel", Font.PLAIN, 20);
        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));

        fullName = new JLabel("Full Name");
        fullName.setFont(sans);
        panel.add(fullName);
        namefield = new JTextField();
        namefield.setFont(sans);
        panel.add(namefield);

        uniID = new JLabel("University ID");
        uniID.setFont(sans);
        panel.add(uniID);

        uniIDfield = new JTextField();
        uniIDfield.setFont(sans);
        panel.add(uniIDfield);

        password = new JLabel("Password");
        password.setFont(sans);
        panel.add(password);

        passfield = new JPasswordField();
        passfield.setFont(sans);
        panel.add(passfield);

        emailLabel = new JLabel("Email address: ");
        emailLabel.setFont(sans);
        panel.add(emailLabel);
        email = new JTextField();
        email.setFont(sans);
        panel.add(email);

        phoneNum = new JLabel("Phone number: ");
        phoneNum.setFont(sans);
        panel.add(phoneNum);
        phoneNumfield = new JTextField();
        phoneNumfield.setFont(sans);
        panel.add(phoneNumfield);

        courseLabel = new JLabel("Course");
        courseLabel.setFont(sans);
        panel.add(courseLabel);

        allCourses = new Vector<String>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("txtfiles/courses.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.trim().equals(""))
                    allCourses.add(str);
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }

        course = new JComboBox<String>(allCourses);
        course.setFont(sans);
        panel.add(course);

        userRoleLabel = new JLabel("User Role");
        userRoleLabel.setFont(sans);
        panel.add(userRoleLabel);

        String[] roles = { "Student", "Instructor", "Administrator" };
        userRole = new JComboBox<String>(roles);
        userRole.setFont(sans);
        panel.add(userRole);

        panel.add(new JLabel());
        panel.add(new JLabel());

        backBtn = new JButton("Back");
        backBtn.setFont(ariel);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        panel.add(backBtn);

        registerBtn = new JButton("Register");
        registerBtn.setFont(ariel);
        registerBtn.setBackground(Color.BLACK);
        registerBtn.setForeground(Color.WHITE);
        panel.add(registerBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // for the
        // padding on all sides

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();

            }
        });

        registerBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                uni_id = uniIDfield.getText();
                name = namefield.getText();
                pass = String.valueOf(passfield.getPassword());
                e_mail = email.getText();
                phone = phoneNumfield.getText();
                user_role = (String) userRole.getSelectedItem();
                selected_course = (String) course.getSelectedItem();

                // if (uni_id.trim().isEmpty() || name.trim().isEmpty() || pass.trim().isEmpty()
                // || e_mail.trim().isEmpty()) { // whitespaces
                // JOptionPane.showMessageDialog(null, "All fields are required!");
                // } else {
                // reg.register(uni_id, name, pass, e_mail, user_role);
                // backBtn.doClick();
                // }
                if (uni_id.trim().isEmpty() || name.trim().isEmpty() || pass.trim().isEmpty() || e_mail.trim().isEmpty()
                        || phone.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All details are required");

                } else {
                    if (user_role.equalsIgnoreCase("Student")) {
                        new StudentRegister(uni_id, name, pass, e_mail, phone, user_role.toLowerCase(),
                                selected_course);
                        dispose();

                    } else if (user_role.equalsIgnoreCase("Administrator")) {
                        // if (optionPanePass() == 1) {
                        new AdminRegister(uni_id, name, pass, e_mail, phone, user_role.toLowerCase(), selected_course);
                        dispose();
                        // }
                    } else if (user_role.equalsIgnoreCase("Instructor")) {
                        // if (optionPanePass() == 1) {
                        new InstructorRegister(uni_id, name, pass, e_mail, phone, user_role.toLowerCase(),
                                selected_course);
                        dispose();
                        // }
                    }
                }

            }
        });

        return panel;

    }

    public static void main(String[] args) {
        new Register();
    }

}

class StudentRegister extends Register {
    JPanel student_panel;
    JPanel student_panel1;
    JPanel student_panel2;
    JPanel student_panel2i;
    Vector<String> opt_modules;
    Vector<String> levels;
    JComboBox<String> userLevel;

    public StudentRegister(String uni_id, String name, String pass, String email, String phone, String user_role,
            String selected_course) {
        getContentPane().removeAll();
        setSize(820, 500);
        setTitle("Choose course!");
        add(studentPanel(uni_id, name, pass, email, phone, user_role, selected_course));
        setLocationRelativeTo(null); // for the window to appear on the middle
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to completely close the application on exit
        // repaint();
        setVisible(true);
    }

    JPanel studentPanel(String uni_id, String name, String pass, String email, String phone, String user_role,
            String selected_course) {
        Font sans = new Font("SansSerif", Font.PLAIN, 10);
        Font ariel = new Font("Ariel", Font.PLAIN, 17);

        student_panel = new JPanel();
        student_panel.setLayout(new GridLayout(2, 0));
        student_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        student_panel1 = new JPanel();
        student_panel1.setLayout(new GridLayout(1, 0));
        student_panel1.setBorder(BorderFactory.createTitledBorder("Level details"));
        student_panel.add(student_panel1);
        // Optional: None
        JTextArea textArea = new JTextArea("");
        textArea.setFont(ariel);
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        opt_modules = new Vector<String>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("details.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.contains(","))
                    textArea.append(str.substring(0, str.indexOf(",")) + "\t\t"
                            + str.substring(str.indexOf(",") + 1, str.length()) + "\n");
                // textArea.append(str + "\n");
                else
                    textArea.append(str + "\n");

                if (str.contains("Optional:") && str.contains(",")) {
                    String module1 = str.substring(str.indexOf(":") + 1, str.indexOf(","));
                    String module2 = str.substring(str.indexOf(",") + 1, str.length());
                    opt_modules.add(module1);
                    opt_modules.add(module2);
                    // System.out.println(module2);
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }

        JScrollPane scrollPanetextArea = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setCaretPosition(0);

        student_panel1.add(scrollPanetextArea);

        student_panel2 = new JPanel();
        student_panel2.setLayout(new GridLayout(2, 3));
        // student_panel2.setBorder(BorderFactory.createTitledBorder("Student Panel
        // 2"));
        student_panel.add(student_panel2);

        // student_panel2.add(new JLabel());

        JPanel chooseLevelPanel = new JPanel();
        chooseLevelPanel.setLayout(new GridLayout(2, 0));

        student_panel2.add(chooseLevelPanel);

        // chooseLevelPanel.setBorder(BorderFactory.createTitledBorder(""));

        JLabel chooseLevel = new JLabel("             Choose your level");
        chooseLevel.setFont(ariel);

        chooseLevelPanel.add(chooseLevel);
        // String[] roles = { "Student", "Instructor", "Administrator" };
        levels = new Vector<String>();
        levels.add("Level 4");
        levels.add("Level 5");
        levels.add("Level 6");
        userLevel = new JComboBox<String>(levels);
        userLevel.setFont(ariel);

        userLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userLevel.getSelectedItem().equals("Level 6"))
                    userRole.setEnabled(true);
                else
                    userRole.setEnabled(false);
            }
        });

        chooseLevelPanel.add(userLevel);

        student_panel2.add(new JLabel());

        student_panel2i = new JPanel();
        student_panel2i.setLayout(new GridLayout(2, 0));
        // student_panel2i.setBorder(BorderFactory.createTitledBorder("student_panel2i"));

        student_panel2.add(student_panel2i);

        // student_panel2i.setBorder(BorderFactory.createTitledBorder(""));

        JLabel chooseLabel = new JLabel("             Choose a course");
        chooseLabel.setFont(ariel);

        student_panel2i.add(chooseLabel);
        // String[] roles = { "Student", "Instructor", "Administrator" };
        userRole = new JComboBox<String>(opt_modules);
        userRole.setEnabled(false);
        userRole.setFont(ariel);
        student_panel2i.add(userRole);

        // student_panel2.add(new JLabel());

        JPanel panelbackbtn = new JPanel();
        panelbackbtn.setLayout(new GridLayout(2, 2));
        // panelbackbtn.setBorder(BorderFactory.createTitledBorder("panelbackbtn"));

        panelbackbtn.add(new JLabel());
        panelbackbtn.add(new JLabel());
        panelbackbtn.add(backBtn);
        panelbackbtn.add(new JLabel());

        student_panel2.add(panelbackbtn);

        student_panel2.add(new JLabel());

        JPanel panelenrollbtn = new JPanel();
        panelenrollbtn.setLayout(new GridLayout(2, 2));
        // panelenrollbtn.setBorder(BorderFactory.createTitledBorder("panelenrollbtn"));

        panelenrollbtn.add(new JLabel());
        panelenrollbtn.add(new JLabel());
        panelenrollbtn.add(new JLabel());

        JButton enrollBtn = new JButton("Enroll");
        enrollBtn.setFont(ariel);
        enrollBtn.setBackground(Color.BLACK);
        enrollBtn.setForeground(Color.WHITE);

        enrollBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reg = new UserRegistration();
                reg.register(uni_id, name, pass, email, phone, selected_course, user_role);
                backBtn.doClick();
            }
        });

        panelenrollbtn.add(enrollBtn);

        student_panel2.add(panelenrollbtn);

        // JLabel level = new JLabel("Level 4");
        // student_panel.add(level);

        // JButton button = new JButton("0");
        // student_panel.add(button);

        // JButton button1 = new JButton("1");
        // student_panel.add(button1);

        // JButton button2 = new JButton("2");
        // student_panel.add(button2);

        // JButton button3 = new JButton("3");
        // student_panel.add(button3);

        return student_panel;
    }
}

class AdminRegister extends Register {

    JPanel admin_panel;
    JPanel masterpass_panel;
    JFrame passframe;

    public AdminRegister(String uni_id, String name, String pass, String email, String phone, String user_role,
            String selected_course) {
        getContentPane().removeAll();
        setSize(820, 500);
        setTitle("Continuing to register!");
        add(adminPanel(uni_id, name, pass, email, phone, user_role, selected_course));
        setLocationRelativeTo(null); // for the window to appear on the middle
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to completely close the application on exit
        setVisible(true);
    }

    int optionPanePass() {
        int return_num;
        passframe = new JFrame();
        JPanel passpanel = new JPanel();
        JLabel label = new JLabel("Enter master password:");
        JPasswordField pass = new JPasswordField(10);
        passpanel.add(label);
        passpanel.add(pass);
        String[] options = new String[] { "OK", "Cancel" };
        int option = JOptionPane.showOptionDialog(null, passpanel, "Authentication", JOptionPane.NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (option == 0) // pressing OK button
        {
            String password = new String(pass.getPassword());
            if (password.equals("123")) {
                return_num = 1;
            } else {
                return_num = 0;
            }
            return return_num;
        } else {
            return -1;
        }
    }

    JTextArea textarea(String file) {
        Font ariel = new Font("Ariel", Font.PLAIN, 17);

        JTextArea newTextArea = new JTextArea("");
        newTextArea.setFont(ariel);
        newTextArea.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                newTextArea.append(str + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }
        return newTextArea;
    }

    JPanel adminPanel(String uni_id, String name, String pass, String email, String phone, String user_role,
            String selected_course) {
        Font ariel = new Font("Ariel", Font.PLAIN, 17);

        admin_panel = new JPanel();
        admin_panel.setLayout(new GridLayout(2, 0));
        admin_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea adminTextArea = textarea("admindetails.txt");

        JScrollPane scrollPanetextArea = new JScrollPane(adminTextArea);
        adminTextArea.setEditable(false);
        adminTextArea.setCaretPosition(0);
        admin_panel.add(scrollPanetextArea);

        JPanel admin_panel2 = new JPanel();
        admin_panel2.setLayout(new GridLayout(2, 3));
        // admin_panel2.setBorder(BorderFactory.createTitledBorder("Admin Panel 2"));

        admin_panel.add(admin_panel2);

        admin_panel2.add(new JLabel());
        JLabel per_level = new JLabel(
                "<html>By clicking continue, you take a responsibility to the mentioned tasks and agree to the terms. :)</html>");
        per_level.setFont(ariel);
        admin_panel2.add(per_level);
        admin_panel2.add(new JLabel());
        // admin_panel2.add(new JLabel());

        // chooseLevelPanel.setBorder(BorderFactory.createTitledBorder(""));

        // JPanel admin_panel2i = new JPanel();
        // admin_panel2i.setLayout(new GridLayout(2, 2));
        // //
        // student_panel2i.setBorder(BorderFactory.createTitledBorder("student_panel2i"));

        // admin_panel2.add(admin_panel2i);

        JPanel panelbackbtn = new JPanel();
        panelbackbtn.setLayout(new GridLayout(2, 2));
        // panelbackbtn.setBorder(BorderFactory.createTitledBorder("panelbackbtn"));

        panelbackbtn.add(new JLabel());
        panelbackbtn.add(new JLabel());
        panelbackbtn.add(backBtn);
        panelbackbtn.add(new JLabel());

        admin_panel2.add(panelbackbtn);

        admin_panel2.add(new JLabel());

        JPanel panelcontbtn = new JPanel();
        panelcontbtn.setLayout(new GridLayout(2, 2));

        panelcontbtn.add(new JLabel());
        panelcontbtn.add(new JLabel());
        panelcontbtn.add(new JLabel());

        JButton continuebtn = new JButton("Continue");
        continuebtn.setFont(ariel);
        continuebtn.setBackground(Color.BLACK);
        continuebtn.setForeground(Color.WHITE);

        panelcontbtn.add(continuebtn);

        admin_panel2.add(panelcontbtn);

        continuebtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int val = optionPanePass();
                if (val == 1) {
                    reg = new UserRegistration();
                    reg.register(uni_id, name, pass, email, phone, selected_course, user_role);
                    backBtn.doClick();
                    dispose();
                }
                if (val == 0) {
                    JOptionPane.showMessageDialog(passframe, "Wrong password!!!", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return admin_panel;
    }
}

class InstructorRegister extends AdminRegister {
    JPanel instructor_panel;

    public InstructorRegister(String uni_id, String name, String pass, String email, String phone, String user_role,
            String selected_course) {
        super(uni_id, name, pass, email, phone, user_role, selected_course);
        getContentPane().removeAll();
        setSize(1220, 600);
        setTitle("Continuing to register!");
        add(instructorPanel(uni_id, name, pass, email, phone, user_role, selected_course));
        setLocationRelativeTo(null); // for the window to appear on the middle
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to completely close the application on exit
        setVisible(true);
    }

    JPanel instructorPanel(String uni_id, String name, String pass, String email, String phone, String user_role,
            String selected_course) {
        Font ariel = new Font("Ariel", Font.PLAIN, 17);

        instructor_panel = new JPanel();
        instructor_panel.setLayout(new GridLayout(2, 0));
        instructor_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel instructor_panel_top = new JPanel();
        instructor_panel_top.setLayout(new GridLayout(0, 3));
        instructor_panel_top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        instructor_panel.add(instructor_panel_top);

        JTextArea instructorTextArea1 = textarea("txtfiles/Level4subs.txt");
        JScrollPane scrollPanetextArea1 = new JScrollPane(instructorTextArea1);
        instructorTextArea1.setEditable(false);
        instructorTextArea1.setCaretPosition(0);
        instructorTextArea1.setFont(ariel);
        instructor_panel_top.add(scrollPanetextArea1);

        JTextArea instructorTextArea2 = textarea("txtfiles/Level5subs.txt");
        JScrollPane scrollPanetextArea2 = new JScrollPane(instructorTextArea2);
        instructorTextArea2.setEditable(false);
        instructorTextArea2.setCaretPosition(0);
        instructorTextArea2.setFont(ariel);
        instructor_panel_top.add(scrollPanetextArea2);

        JTextArea instructorTextArea3 = textarea("txtfiles/Level6subs.txt");
        JScrollPane scrollPanetextArea3 = new JScrollPane(instructorTextArea3);
        instructorTextArea3.setEditable(false);
        instructorTextArea3.setCaretPosition(0);
        instructorTextArea3.setFont(ariel);
        instructor_panel_top.add(scrollPanetextArea3);

        JPanel instructor_panel_bottom = new JPanel();
        instructor_panel_bottom.setLayout(new GridLayout(2, 3));
        instructor_panel_bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        instructor_panel.add(instructor_panel_bottom);

        instructor_panel_bottom.add(new JLabel());
        JLabel per_level2 = new JLabel(
                "<html>You will be assigned upto 4 modules out of these by the administrator. :)</html>");
        per_level2.setFont(ariel);
        instructor_panel_bottom.add(per_level2);
        instructor_panel_bottom.add(new JLabel());

        JPanel panelbackbtn = new JPanel();
        panelbackbtn.setLayout(new GridLayout(2, 2));
        // panelbackbtn.setBorder(BorderFactory.createTitledBorder("panelbackbtn"));

        panelbackbtn.add(new JLabel());
        panelbackbtn.add(new JLabel());
        panelbackbtn.add(backBtn);
        panelbackbtn.add(new JLabel());

        instructor_panel_bottom.add(panelbackbtn);

        instructor_panel_bottom.add(new JLabel());

        JPanel panelcontbtn = new JPanel();
        panelcontbtn.setLayout(new GridLayout(2, 2));

        panelcontbtn.add(new JLabel());
        panelcontbtn.add(new JLabel());
        panelcontbtn.add(new JLabel());

        JButton continuebtn = new JButton("Continue");
        continuebtn.setFont(ariel);
        continuebtn.setBackground(Color.BLACK);
        continuebtn.setForeground(Color.WHITE);

        panelcontbtn.add(continuebtn);

        instructor_panel_bottom.add(panelcontbtn);

        continuebtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int val = optionPanePass();
                if (val == 1) {
                    reg = new UserRegistration();
                    reg.register(uni_id, name, pass, email, phone, selected_course, user_role);
                    backBtn.doClick();
                    dispose();
                }
                if (val == 0) {
                    JOptionPane.showMessageDialog(passframe, "Wrong password!!!", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return instructor_panel;
    }
}
