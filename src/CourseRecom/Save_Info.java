package CourseRecom;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import javax.swing.*;

public class Save_Info extends JFrame{
	private static final String PROGRAM_NAME = "Save_Info";
			
	Connection conn;
	String username;
	
	JLabel userLabel;
	
	JButton finishButton;
	
	JCheckBox ageBox;
	JCheckBox genderBox;
	JCheckBox countryBox;
	
	public Save_Info(Connection conn,String username)throws SQLException{
		super("Choose the data you want to save");
		
		this.conn = conn;
		this.username = username;
		
		String getStudent = "SELECT * FROM students WHERE username='"+username+"'";
		Students students = new Students();
		students.setUsername(username);
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(conn);
	          System.exit(0);
	        }
	    });
		
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(getStudent);
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				students.setAge(res.getInt("age"));
				students.setGender(res.getString("gender"));
				students.setNative_country(res.getString("native_country"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		
		/****************************Set Up UI********************************/
		//JLabel title = new JLabel("Check the information you want to save.");
		userLabel = new JLabel("Username: "+students.getUsername());
		
		ageBox = new JCheckBox("Age: "+Integer.toString(students.getAge()));
		ageBox.setSelected(true);
		genderBox = new JCheckBox("Gender: "+students.getGender());
		genderBox.setSelected(true);
		countryBox = new JCheckBox("Native Country: "+students.getNative_country());
		countryBox.setSelected(true);
		
		finishButton = new JButton("Finish");
		finishButton.setPreferredSize(new Dimension(120, 35));
		
		/******************************Initialization***************************/
		Container contentPane = getContentPane();
		contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 0, 10, 0);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		//c.gridheight = 1;
		c.gridwidth = 1;
		contentPane.add(userLabel, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		//c.gridheight = 1;
		c.gridwidth = 1;
		contentPane.add(ageBox, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		//c.gridheight = 1;
		c.gridwidth = 1;
		contentPane.add(genderBox, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		//c.gridheight = 1;
		c.gridwidth = 1;
		contentPane.add(countryBox, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 4;
		//c.gridheight = 1;
		c.gridwidth = 1;
		contentPane.add(finishButton, c);
		
		finishButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				/************************remove the not-selected data**************************/
				if(!ageBox.isSelected()){
					String deleteAge = "UPDATE students SET age = NULL WHERE username='"+username+"'";
					PreparedStatement stmt1 = null;
					try{
						stmt1 = conn.prepareStatement(deleteAge);
						stmt1.executeUpdate();
					}catch(SQLException ex){
						SQLError.show(ex);
					}finally{
						if(stmt1 != null){
							try {
								stmt1.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				if(!genderBox.isSelected()){
					String deleteGender = "UPDATE students SET gender = NULL WHERE username='"+username+"'";
					PreparedStatement stmt2 = null;
					try{
						stmt2 = conn.prepareStatement(deleteGender);
						stmt2.executeUpdate();
					}catch(SQLException ex){
						SQLError.show(ex);
					}finally{
						if(stmt2 != null){
							try {
								stmt2.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				if(!countryBox.isSelected()){
					String deleteCountry = "UPDATE students SET native_country = NULL WHERE username='"+username+"'";
					PreparedStatement stmt3 = null;
					try{
						stmt3 = conn.prepareStatement(deleteCountry);
						stmt3.executeUpdate();
					}catch(SQLException ex){
						SQLError.show(ex);
					}finally{
						if(stmt3 != null){
							try {
								stmt3.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				
				/********************check if there are any new courses for this students***********************/
				String checkCourse = "SELECT * FROM new_course";
				PreparedStatement stmt4 = null;
				try{
					stmt4 = conn.prepareStatement(checkCourse);
					ResultSet res = stmt4.executeQuery();
					if(res.next()){
						/***************move to next page******************/
						int output = JOptionPane.showConfirmDialog(contentPane, "Data has been saved into the database!Do you want to rate your new courses?","Course Rate",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(output == JOptionPane.YES_OPTION){
							/***********Then go to the new page**************/
							NewCourseEdition_Interface fc = new NewCourseEdition_Interface(conn, username);
							setVisible(false);
							fc.setSize(480, 600);
							fc.setLocationRelativeTo(null);
							fc.setVisible(true);
							dispose();
						}else if(output == JOptionPane.NO_OPTION){
							//do nothing
							JOptionPane.showMessageDialog(contentPane, "Thank you for using this system!","Data Save",JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						}

					}else{
						JOptionPane.showMessageDialog(contentPane, "Data has been saved into the database! Thank you for using this system!","Data Save",JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}
				}catch(SQLException ex){
					SQLError.show(ex);
				}finally{
					if(stmt4 != null){
						try {
							stmt4.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	/*
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
    	
    	Save_Info app = new Save_Info(conn, "CC");
    	app.setSize(300, 400);
        app.setVisible(true);
        
	}
	*/
	
}
