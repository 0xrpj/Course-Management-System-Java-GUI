import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.table.DefaultTableModel;

public class Student extends JFrame {
    JPanel panel;
    DefaultTableModel tableModel;
    Object[] rowData = {};
    UserRegistration reg;
    String id;

    public Student(String id) {
        this.id = id;
        setTitle("Welcome ");
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
            String filepath = "images/" + id + ".png";
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

        String std_name = reg.getName(id, "Student");
        JLabel stdNameLbl = new JLabel("Name: " + std_name);
        stdNameLbl.setFont(ariel);
        subPanelTop.add(stdNameLbl);

        String course_name = reg.getCourse(id, "Student");
        JLabel courseNameLbl = new JLabel("Course: " + course_name);
        courseNameLbl.setFont(ariel);
        subPanelTop.add(courseNameLbl);

        // subPanelTop.add(new JLabel());
        // subPanelTop.add(new JLabel());

        panelTop.add(subPanelTop);
        panel.add(panelTop);

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1, 0));
        // panelBottom.setBorder(BorderFactory.createTitledBorder("Bottom Panel"));

        JTextArea textArea = new JTextArea("Modules you are enrolled in:\n\n");
        for (String s : reg.getStudModules(id)) {
            textArea.append(s + "\n");
        }
        textArea.setFont(ariel);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPanetextArea = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setCaretPosition(0);
        panelBottom.add(scrollPanetextArea);

        panel.add(panelBottom);

        // JScrollPane scrollPane = new JScrollPane();
        // scrollPane.setBorder(BorderFactory.createTitledBorder("Details about you"));
        // panelTop.add(scrollPane);

        return panel;
    }

    public static void main(String[] args) {
        new Student("2");
        // UserRegistration ree = new UserRegistration();
        // System.out.println(ree.getModules("2", "Student"));
    }
}
