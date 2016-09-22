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
import javax.swing.table.DefaultTableModel;

public class Recom_Interface extends JFrame {
	private final static String PROGRAM_NAME = "recom_interface";
	
	Connection connection;
	String username;
	boolean new_user;
	
	JTable showPanel = new JTable();
	
	Object[] topskill_column = {"Department Code", "Course Number", "Average Skill Improvement"};
	Object[] topinterest_column = {"Department Code", "Course Number", "Average Interest Increasing"};
	Object[] topscore_column = {"Department Code", "Course Number", "Predicted Score"};
	Object[] topsatisfaction_column = {"Department Code", "Course Number", "Satisfaction Ranking"};
	Object[] course_list = {"Department Code", "Course Number"};
	
	public Recom_Interface(Connection conn, String username, boolean new_user)throws SQLException{
		super("Course Recommendation System");
		
		this.connection = conn;
		this.username = username;
		this.new_user = new_user;
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(connection);
	          System.exit(0);
	        }
	    });
		
		//=======================Initialization===================================//
		JLabel title = new JLabel("Choose your recommendation method.");

		Dimension button_size = new Dimension(200, 40);
		
		JButton hsat = new JButton("Highest Course Satisfaction");
		hsat.setPreferredSize(button_size);
		JButton hskillimp = new JButton("Higest Skill Improvements");
		hskillimp.setPreferredSize(button_size);
		JButton hinterest = new JButton("Highest Interest Rising");
		hinterest.setPreferredSize(button_size);
		JButton hgrade = new JButton("Higest Expected Grades");
		hgrade.setPreferredSize(button_size);
		
		showPanel = new JTable();
		JButton finish = new JButton("Finish");
		finish.setPreferredSize(button_size);
		//=======================Initialization===================================//
		
		Container contentPane = getContentPane();
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 10, 2, 10);
        
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        contentPane.add(title, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		contentPane.add(hgrade, c);
		
		hgrade.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					refreshTable(0);
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		contentPane.add(hsat, c);
		
		hsat.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					refreshTable(1);
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		contentPane.add(hskillimp, c);
		
		hskillimp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					refreshTable(2);
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.75;
        c.weighty = 0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		contentPane.add(hinterest, c);
		
		hinterest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					refreshTable(3);
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 5;
		c.weighty = 5;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		contentPane.add(new JScrollPane(showPanel), c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		contentPane.add(finish, c);
		
		finish.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(new_user == false){
					String checkCourse = "SELECT * FROM new_course";
					PreparedStatement stmt4 = null;
					try{
						stmt4 = conn.prepareStatement(checkCourse);
						ResultSet res = stmt4.executeQuery();
						if(res.next()){
							/***************move to next page******************/
							int output = JOptionPane.showConfirmDialog(contentPane, "Do you want to rate your new courses?","Course Rate",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(output == JOptionPane.YES_OPTION){
								/***********Then go to the new page**************/
								NewCourseEdition_Interface fc = new NewCourseEdition_Interface(conn, username);
								setVisible(false);
								fc.setSize(300, 600);
								fc.setLocationRelativeTo(null);
								fc.setVisible(true);
								dispose();
							}else if(output == JOptionPane.NO_OPTION){
								//do nothing
								JOptionPane.showMessageDialog(contentPane, "Thank you for using this system!","Data Save",JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}

						}else{
							JOptionPane.showMessageDialog(contentPane, "Thank you for using this system!","Data Save",JOptionPane.INFORMATION_MESSAGE);
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
				}else{
					//System.out.print(username);
					/***********************ask if the user want to record data********************************/
					int output = JOptionPane.showConfirmDialog(contentPane, "Do you want to add your own data into the CEA database?","Data Record",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(output == JOptionPane.YES_OPTION){
						//System.out.print("Yes option");
						/***********Then we will save this student info into students table********/
						try {
							Save_Info si = new Save_Info(conn,username);
							si.setSize(300, 400);
							si.setLocationRelativeTo(null);
							si.setVisible(true);
							dispose();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}else if(output == JOptionPane.NO_OPTION){
						System.out.print("No option");
						/**************remove the user***************************/
						String deleteUser = "DELETE FROM students WHERE username='"+username+"'";
						PreparedStatement s = null;
						try{
							s = conn.prepareStatement(deleteUser);
							s.executeUpdate();
						}catch(SQLException ex){
							SQLError.show(ex);
						}
						JOptionPane.showMessageDialog(contentPane, "Your data has been removed from the CEA database.Thank you for using this system!","Data Remove",JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}else if(output == JOptionPane.CANCEL_OPTION){
						/*******Do nothing********/
					}
				}

			}
		});
        
	}
	
	public void refreshTable(int button_number)throws SQLException{
		if(button_number == 0){
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_list);
			java.util.List<CourseRecomAdapter> getLetterGrade = CourseRecomAdapter.getLetterGrade(connection);
			for(CourseRecomAdapter cra : getLetterGrade){
				Object[] o = new Object[2];
				o[0] = cra.getDeptCode();
				o[1] = cra.getCourseN();
				//o[2] = cra.getLetterGrade();
				model.addRow(o);
			}
			showPanel.setModel(model);
			showPanel.repaint();
		}
		else if(button_number == 1){
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_list);
			java.util.List<CourseRecomAdapter> getHighestRanking = CourseRecomAdapter.getHighestRanking(connection);
			for(CourseRecomAdapter cra : getHighestRanking){
				Object[] o = new Object[2];
				o[0] = cra.getDeptCode();
				o[1] = cra.getCourseN();
				//o[2] = cra.getAvgRanking();
				model.addRow(o);
			}
			showPanel.setModel(model);
			showPanel.repaint();
		}
		else if(button_number == 2){
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_list);
			java.util.List<CourseRecomAdapter> getAverageSkill = CourseRecomAdapter.getAverageSkill(connection);
			for(CourseRecomAdapter cra : getAverageSkill){
				Object[] o = new Object[2];
				o[0] = cra.getDeptCode();
				o[1] = cra.getCourseN();
				//o[2] = cra.getAvgSkill();
				model.addRow(o);
			}
			showPanel.setModel(model);
			showPanel.repaint();
		}
		else{
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_list);
			java.util.List<CourseRecomAdapter> getAverageInterest = CourseRecomAdapter.getAverageInterest(connection);
			for(CourseRecomAdapter cra : getAverageInterest){
				Object[] o = new Object[2];
				o[0] = cra.getDeptCode();
				o[1] = cra.getCourseN();
				//o[2] = cra.getAvgInterest();
				model.addRow(o);
			}
			showPanel.setModel(model);
			showPanel.repaint();
		}
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
    	
    	Recom_Interface app = new Recom_Interface(conn, "CC", false);
    	app.setSize(480, 600);
    	app.setLocationRelativeTo(null);
        app.setVisible(true);
        
	}
	*/

}
