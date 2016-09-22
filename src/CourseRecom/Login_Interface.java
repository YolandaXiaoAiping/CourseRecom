package CourseRecom;
import java.sql.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import CourseRecom.DBConnection;
import CourseRecom.Login_Interface;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Login_Interface extends JFrame {
	public static final String PROGRAM_NAME = "Login_Interface";
	Connection connection;
	boolean correct = false;
	String username;
	
	//JTable Login;
	JLabel student;
	JTextField student_userid;
	JButton button_signin;
	JButton button_signup;
	
	
	public Login_Interface(Connection conn)throws SQLException{
		super("Course Recommendation System");
		
		this.connection = conn;
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(connection);
	          System.exit(0);
	        }
	    });
		
		//Login = new JTable();
		student = new JLabel("UserID:");
		student_userid = new JTextField(20);
		student_userid.setPreferredSize(new Dimension(80, 40));
		button_signin = new JButton("Sign In");
		button_signin.setPreferredSize(new Dimension(80, 40));
		button_signup = new JButton("Sign Up");
		button_signup.setPreferredSize(new Dimension(80, 40));
		
		Container contentPane = getContentPane();
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
       
        
        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        contentPane.add(student, c);
        
        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        contentPane.add(student_userid, c);
        
        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        contentPane.add(button_signin, c);
        
        button_signin.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Students sid = new Students();
        		boolean rs = false;	
        		username = student_userid.getText();
        		sid.setUsername(student_userid.getText());
        		try{
        			rs = sid.checkUserid(connection);
        			if(rs){
        				setVisible(false);
        				JOptionPane.showMessageDialog(null, "Welcome to Course Recommendation System.");
        				username = student_userid.getText();
        				CollectUserCourse cc = new CollectUserCourse(connection, username, false);
        				cc.setSize(400,600);
        				cc.setLocationRelativeTo(null);
        				cc.setVisible(true);
        				dispose();
        			}
        			else{
        				JOptionPane.showMessageDialog(null, "Account does not exist. Please sign up for a new account!");
        			}
        			clearInputs ();
        		} catch(SQLException ex){
        			SQLError.show(ex);
        		} 
        	}
        });
        
        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        contentPane.add(button_signup, c);
        
        button_signup.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		//for new sign up interface
        		try{
        			SignUp_Interface si = new SignUp_Interface(connection);
        			setVisible(false);
        			si.setSize(400,600);
        			si.setLocationRelativeTo(null);
        			si.setVisible(true);
        			dispose();
        		}catch(SQLException ex){
        			SQLError.show(ex);
        		}
        	}
        });
        
		
	}
	
	private void clearInputs () {
		student_userid.setText("");
	}
	
	public boolean check_status(){
		return this.correct;
	}
	
	public String getUserid(){
		return this.username;
	}
	
	/* used for test
	public static void main(String [] args) throws IOException, SQLException {
		if (args.length == 0){
    		System.out.println("Usage: "+PROGRAM_NAME+" <name of properties file>");
    		System.exit(1);
    	}
		
		Properties props = new Properties();
    	FileInputStream in = new FileInputStream(args[0]);
    	props.load(in);
    	in.close();
    	java.sql.Connection conn = DBConnection.getConnection (props);	
		
		conn.createStatement().execute("PRAGMA foreign_keys = ON");
    	
    	if (conn == null)
    		System.exit(1);
    	
    	Login_Interface app = new Login_Interface(conn);
    	app.setSize(300, 500);
        app.setVisible(true);
        
	}
	*/
	
}
