package CourseRecom;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import javax.swing.*;
import CourseRecom.DBConnection;

public class SignUp_Interface extends JFrame{
	public static final String PROGRAM_NAME = "signup_interface";
	Connection connection;
	String username;
	
	//section for username
	JLabel UserId;
	JTextField tUserId;
	
	//section for user gender
	JLabel Gender;
	JRadioButton male;
	boolean bmale = true;
	JRadioButton female;
	boolean bfemale = false;
	//JRadioButton not_given;
	//boolean bnotgiven = true;
	
	//section for user age
	JLabel Age;
	JTextField tAge;
	
	//section for country
	JLabel NC;
	JTextField tNC;
	
	//section for submit
	JButton submit;
	JButton cancel;
	
	public SignUp_Interface(Connection conn)throws SQLException{
		super("Course Recommendation System");
		
		this.connection = conn;
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(connection);
	          System.exit(0);
	        }
	    });
		
		
		//initialization-----------------------------------//
		Dimension d = new Dimension(100, 30);
		UserId = new JLabel("User Name:");
		tUserId = new JTextField(20);
		tUserId.setPreferredSize(d);
		
		Gender = new JLabel("Gender:");
		male = new JRadioButton("Male", true);
		female = new JRadioButton("Female");
		//not_given = new JRadioButton("Not Given", true);
		
		//group the radio buttons
		ButtonGroup group = new ButtonGroup();
		group.add(male);
		group.add(female);
		//group.add(not_given);
		
		Age = new JLabel("Age:");
		tAge = new JTextField(20);
		tAge.setPreferredSize(d);
		
		NC = new JLabel("Native Country:");
		tNC = new JTextField(20);
		tNC.setPreferredSize(d);
		
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		//initialization-----------------------------------//
		
		
		Container contentPane = getContentPane();
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.PAGE_START;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(20,20,20,20);
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        contentPane.add(UserId, c);
        
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,10,10);
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        contentPane.add(tUserId, c);
        
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        contentPane.add(Gender, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        contentPane.add(male, c);
        
        male.addItemListener(new ItemListener(){
        	public void itemStateChanged(ItemEvent e){
        		if(e.getStateChange() == 1)
        			bmale = true;
        		else
        			bmale = false;
        	}
        });
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        contentPane.add(female, c);
        
        female.addItemListener(new ItemListener(){
        	public void itemStateChanged(ItemEvent e){
        		if(e.getStateChange() == 1)
        			bfemale = true;
        		else
        			bfemale = false;
        	}
        });
        
        /*
        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 1;
        contentPane.add(not_given, c);
        
        not_given.addItemListener(new ItemListener(){
        	public void itemStateChanged(ItemEvent e){
        		if(e.getStateChange() == 1)
        			bnotgiven = true;
        		else
        			bnotgiven = false;
        	}
        });
        */
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        contentPane.add(Age, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        contentPane.add(tAge, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        contentPane.add(NC, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 2;
        contentPane.add(tNC, c);
		
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        contentPane.add(submit, c);
        
        submit.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		try{
        			Students s = new Students();
        			if(tUserId.getText().equals("") || tAge.getText().equals("") || tNC.getText().equals("")){
        				JOptionPane.showMessageDialog(null, "Please enter all information for better recommendation experience!");
        			}
        			else{
	        			s.setUsername(tUserId.getText());
	        			boolean res = s.checkUserid(conn);
	        			if(res){
	        				JOptionPane.showMessageDialog(null, "User name already exists.");
	        				clearInputs();
	        			}
	        			else{
	        				username = tUserId.getText();
		        			s.setAge(Integer.parseInt(tAge.getText()));
		        			if(bmale) s.setGender("m");
		        			else s.setGender("f");
		        			//else s.setGender("");
		        			s.setNative_country(tNC.getText());
		        			s.insertNewUser(connection);
		        			JOptionPane.showMessageDialog(null, "Sign Up Successfully!");
		        			setVisible(false);
		        			CollectUserCourse c = new CollectUserCourse(connection, username, true);
		        			c.setSize(480,800);
		        			c.setLocationRelativeTo(null);
		        			c.setVisible(true);
		        			dispose();
	        			}
        			}
        		}catch(SQLException ex){
        			SQLError.show(ex);
        		}
        	}
        });
        
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        contentPane.add(cancel, c);
        
        cancel.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		try{
	        		setVisible(false);
	        		Login_Interface li = new Login_Interface(connection);
	        		li.setSize(300,500);
	        		li.setVisible(true);
	        		dispose();
        		}catch(SQLException ex){
        			SQLError.show(ex);
        		}
        	}
        });
		
	}
	
	public void clearInputs(){
		tUserId.setText("");
		//tAge.setText("");
		//tNC.setText("");
	}
	
}
