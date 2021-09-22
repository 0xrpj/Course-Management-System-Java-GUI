import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {
    JPanel panel;
    JLabel id;
    JTextField idfield;
    JLabel password;
    JPasswordField passfield;
    JLabel roleLabel;
    JComboBox<String> role;
    JButton loginBtn;
    JButton registerBtn;
    UserRegistration reg;

    public Login() {
        setTitle("Login");
        setSize(500, 300);
        setLocationRelativeTo(null);
        add(getPanel());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        reg = new UserRegistration();
    }

    private JPanel getPanel() {
        Font sans = new Font("SansSerif", Font.PLAIN, 17);

        // the main container
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        id = new JLabel("University ID");
        id.setFont(sans);
        panel.add(id);

        idfield = new JTextField();
        idfield.setFont(sans);
        panel.add(idfield);

        password = new JLabel("Password");
        password.setFont(sans);
        panel.add(password);

        passfield = new JPasswordField();
        passfield.setFont(sans);
        panel.add(passfield);

        roleLabel = new JLabel("User Role");
        roleLabel.setFont(sans);
        panel.add(roleLabel);

        String[] roles = { "Student", "Instructor", "Administrator" };
        role = new JComboBox<String>(roles);
        role.setFont(sans);
        panel.add(role);

        panel.add(new JLabel());
        panel.add(new JLabel());

        registerBtn = new JButton("Register");
        registerBtn.setFont(sans);
        registerBtn.setBackground(Color.BLACK);
        registerBtn.setForeground(Color.WHITE);
        panel.add(registerBtn);

        loginBtn = new JButton("Login");
        loginBtn.setFont(sans);
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setForeground(Color.WHITE);
        panel.add(loginBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uni_id = idfield.getText();
                String pass = String.valueOf(passfield.getPassword());
                String user_role = (String) role.getSelectedItem();
                if (uni_id.trim().isEmpty() || pass.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are mandatory.", "Error logging in",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    reg.verify(uni_id, pass, user_role);
                    dispose();
                }
            }
        });

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register();
                dispose();
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        new Login();
    }
}
