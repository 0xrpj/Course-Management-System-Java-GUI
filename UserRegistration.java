import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import Helpers.DBUtils;

public class UserRegistration {
    private Connection con;
    // private String foundpass;

    public UserRegistration() {
        try {
            con = DBUtils.getdbConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            // e.printStackTrace();
            JOptionPane.showMessageDialog(null, "We're having trouble connecting to the database!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void register(String uni_id, String name, String pass, String email, String phone, String selected_course,
            String userrole) {
        try {
            String role = userrole;
            String insert = "insert into " + role + "(id,name,email,password,phone,course) values(?,?,?,?,?,?) ";
            PreparedStatement statement = con.prepareStatement(insert);
            statement.setString(1, uni_id);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, pass);
            statement.setString(5, phone);
            statement.setString(6, selected_course);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "you are successfully registered! :) ", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "We're having trouble inserting data to the database!\n Possible Causes:\n   -Duplication of university ID.\n -Database connectivity problem.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    String getName(String id, String userrole) {
        String role = userrole;
        String name = "Undefined";
        System.out.println(id + "\t" + userrole);
        try {
            String query = "SELECT name from " + role + " WHERE id= ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next() != false) {
                name = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    String getCourse(String id, String userrole) {
        String role = userrole;
        String name = "Undefined";
        System.out.println(id + "\t" + userrole);
        try {
            String query = "SELECT course from " + role + " WHERE id= ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next() != false) {
                name = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    ResultSet setMarksData(String module_name) {
        try {
            String select = "select c.stu_id,name,marks from (select a.stu_id, marks from (select * from student_module where mod_id=(select id from modules where name=?)) as a left join (select * from student_marks where mod_id=(select id from modules where name=?)) as b on a.stu_id=b.stu_id) as c inner join student as d on c.stu_id=d.id;";
            PreparedStatement statement = con.prepareStatement(select);
            statement.setString(1, module_name);
            statement.setString(2, module_name);
            return statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "We're having trouble fetching data from the database!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    void insertMarks(String id, String name, String marks, String mod_id) {
        try {
            String query = " select marks from student_marks where stu_id=? and mod_id=?;";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, mod_id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                // Marks need to be added because it doesnot exist already.
                String query1 = "insert into student_marks values (?,?,?);";
                PreparedStatement statement1 = con.prepareStatement(query1);
                statement1.setString(1, id);
                statement1.setString(2, mod_id);
                statement1.setString(3, marks);
                statement1.executeUpdate();
                System.out.println("Inserted successfully");
            } else {
                // Marks need to be updated here because it already exists.
                String query2 = "update student_marks set marks=? where stu_id=? and mod_id=?";
                PreparedStatement statement2 = con.prepareStatement(query2);
                statement2.setString(1, marks);
                statement2.setString(2, id);
                statement2.setString(3, mod_id);
                statement2.executeUpdate();
                System.out.println("Updated successfully");

            }
            // while (resultSet.next() false) {
            // // name = resultSet.getString(1);
            // // modules.add(resultSet.getString(1));
            // }
        } catch (SQLException e) {

        }
    }

    void updateModule(String id, String name, String course, String mod_lead) {
        // System.out.println(id + " " + name + " " + course + " " + mod_lead);
        try {
            String query = "update modules set name=?,course=?,module_leader=? where id=?;";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, course);
            statement.setString(3, mod_lead);
            statement.setString(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String getModuleId(String moduleName) {
        String modID = null;
        try {
            String query = " select id from modules where name=?;";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, moduleName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                modID = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modID;
    }

    ArrayList<String> getStudModules(String id) {
        ArrayList<String> modules = new ArrayList<String>();
        try {
            String query = " select name from modules where id in (select mod_id from student_module where stu_id=?);";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() != false) {
                // name = resultSet.getString(1);
                modules.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }

    ArrayList<String> getInstModules(String id) {
        ArrayList<String> modules = new ArrayList<String>();
        try {
            String query = " select name from modules where id in (select mod_id from instructor_module where ins_id=?);";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() != false) {
                // name = resultSet.getString(1);
                modules.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }

    ResultSet getAllModules() {
        try {
            String select = "select * from modules;";
            PreparedStatement statement = con.prepareStatement(select);
            return statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "We're having trouble fetching data from the database!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    void verify(String id, String password, String userrole) {
        // System.out.println(id + " " + password + " " + userrole);
        int flag = 0;
        String role = userrole;

        try {
            String querypass = "SELECT password from " + role + " WHERE id= ?";
            PreparedStatement statement = con.prepareStatement(querypass);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next() == false) {
                JOptionPane.showMessageDialog(null, "Cannot verify credentials :( ", "Could not log in",
                        JOptionPane.ERROR_MESSAGE);
                new Login();
            } else {
                do {
                    String foundpass = resultSet.getString(1);

                    if (foundpass.equals(password)) {
                        flag = 1;
                        System.out.println("Password match");

                        if (userrole.equals("Student")) {
                            new Student(id); // Should provide id here.
                        } else if (userrole.equals("Instructor")) {
                            new Instructor(id);
                        } else if (userrole.equals("Administrator")) {
                            new Administrator();
                        }
                    }
                } while (resultSet.next());

                if (flag == 0) {
                    JOptionPane.showMessageDialog(null, "Cannot verify credentials :( ", "Could not log in",
                            JOptionPane.ERROR_MESSAGE);
                    new Login();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "You cannot log in. :( ", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new UserRegistration();
    }
}