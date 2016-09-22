package CourseRecom;
import java.sql.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;


public class NewCourseEdition_Interface extends JFrame {
	private static final String PROGRAM_NAME = "new_course_edition_interface";
	
	Connection connection;
	String username;
	String current_edition = "Not Chosen";
	private int pointer = 0;  //which course is selected
	private int pointer2 = 0; //which row in edition is selected
	private List<String> current = new ArrayList<String>(); //edition list of courses
	private List<Integer> course_id_list = new ArrayList<Integer>(); //course_id list
	private List<Integer> edition_id_list = new ArrayList<Integer>();
	
	private Map<Integer, Integer> ce_list = new HashMap<Integer, Integer>(); 
	
	private String newCourses = "SELECT * FROM new_course";
	
	private JTable newcourses;
	private JTable course_edition;
	
	private JTextField tcourse_year;
	private JComboBox<String> dcourseTime;
	private JComboBox<String> dsemester;
	private JTextField tstudentsN;
	
	private JButton new_edition;
	
	
	private String[] semester_list = {"", "Fall", "Winter", "Summer"};
	private String[] course_time = {"", "Morning", "Afternoon", "Evening"};
	
	
	Object[] st_course_list = {"Department", "Course Number"};
	Object[] edition_info_list = {"Session Number", "Year", "Semester", "Total Students", "Time of Day"};
	
	public NewCourseEdition_Interface(Connection conn, String username)throws SQLException{
		super("Course Recommendation System");
		
		this.connection = conn;
		this.username = username;
		PreparedStatement stmt = null;
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(connection);
	          System.exit(0);
	        }
	    });
		
		try{
			stmt = conn.prepareStatement(newCourses);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Courses c = new Courses();
				c.setDept_code(rs.getString("dept_code"));
				c.setCourse_number(rs.getInt("course_number"));
				if(!c.checkExistence(connection)) c.addNewCourse(conn);
				course_id_list.add(c.getCourse_id());
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		
		//System.out.print(course_id_list);
		
		NewCourseAdapter nc = new NewCourseAdapter();
		int count = nc.countCourse(conn);
		for(int i = 0;i < count;i++){
			current.add("Not Chosen");
		}
		
		for(int i = 0;i < course_id_list.size();i++){
			ce_list.put(course_id_list.get(i), -1);
		}
		
		//System.out.print(count);
		
		
		/*******************Initialization*************************/
		Dimension d = new Dimension(200, 30);
		Dimension dropdown = new Dimension(100, 30);
		
		//course edition year
		JLabel course_year = new JLabel("Attended year: ");
		tcourse_year = new JTextField(20);
		tcourse_year.setEnabled(false);
		tcourse_year.setPreferredSize(d);
		
		//semester
		JLabel semester = new JLabel("Course Semester: ");
		dsemester = new JComboBox<String>(semester_list);
		dsemester.setPreferredSize(dropdown);
		dsemester.setEnabled(false);
		dsemester.setSelectedIndex(0);
		
		//total students
		JLabel studentsN = new JLabel("Total students enrolled: ");
		tstudentsN = new JTextField(20);
		tstudentsN.setPreferredSize(d);
		tstudentsN.setEnabled(false);
		JLabel sHints = new JLabel("An approximate number is enough.");
		
		//time session
		JLabel courseTime = new JLabel("Time Session: ");
		dcourseTime = new JComboBox<String>(course_time);
		dcourseTime.setPreferredSize(dropdown);
		dcourseTime.setEnabled(false);
		dcourseTime.setSelectedIndex(0);
		
		JLabel currentEdition = new JLabel("Current Edition Chosen: Not Chosen");
		
		JButton confirm = new JButton("Confrim");
		confirm.setEnabled(false);
		
		JButton add = new JButton("Add New Edition");
		add.setEnabled(false);
		
		new_edition = new JButton("Confirm New Edition");
		new_edition.setEnabled(false);
		
		//button section
		JButton finish = new JButton("Next");
		
		JButton exit = new JButton("Exit");
		
		/*******************Initialization*************************/
		
		Container contentPane = getContentPane();
		contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        
        JLabel title = new JLabel("Your New Courses");
        c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		contentPane.add(title, c);
		
		newcourses = new JTable();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 2;
		c.weighty = 2;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		contentPane.add(new JScrollPane(newcourses), c);
		refreshTable();
		
		newcourses.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					clearInput();
					confirm.setEnabled(false);
					add.setEnabled(true);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer = row;
					currentEdition.setText("Current Edition Chosen: " + current.get(pointer));
					String dept_code = (String)target.getValueAt(row, 0);
					int course_number = Integer.parseInt(target.getValueAt(row, 1).toString());
					try{
						refreshInfo(dept_code, course_number);
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		JLabel title1 = new JLabel("Course Edition Info");
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		contentPane.add(title1, c);
		
		course_edition = new JTable();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		contentPane.add(new JScrollPane(course_edition), c);
		
		course_edition.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					confirm.setEnabled(true);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer2 = row;
					clearInput();
				}
			}
		});
		
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		contentPane.add(confirm, c);
		
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				add.setEnabled(false);
				current.set(pointer, Integer.toString(pointer2 + 1));
				currentEdition.setText("Current Edition Chosen: " + current.get(pointer));
				//Course_editions ce = new Course_editions();
				//ce.setCourse_id(course_id_list.get(pointer));
				//System.out.print(course_id_list.get(pointer));
				//ce.setEdition_id(edition_id_list.get(pointer2));
				//System.out.print(edition_id_list.get(pointer2));
				ce_list.put(course_id_list.get(pointer), edition_id_list.get(pointer2));
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		contentPane.add(add, c);
		
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				confirm.setEnabled(false);
				tcourse_year.setEnabled(true);
				dsemester.setEnabled(true);
				tstudentsN.setEnabled(true);
				dcourseTime.setEnabled(true);
				new_edition.setEnabled(true);
			}
		});
		
	
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		contentPane.add(currentEdition, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		contentPane.add(course_year, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		contentPane.add(tcourse_year, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		contentPane.add(semester, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		contentPane.add(dsemester, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		contentPane.add(studentsN, c);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		contentPane.add(tstudentsN, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		contentPane.add(sHints, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 1;
		contentPane.add(courseTime, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 10;
		c.gridwidth = 1;
		contentPane.add(dcourseTime, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 11;
		c.gridwidth = 1;
		contentPane.add(new_edition, c);
		
		new_edition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(tcourse_year.getText().equals("") || dsemester.getSelectedItem().equals("") 
						|| tstudentsN.getText().equals("") || dcourseTime.getSelectedItem().equals("")){
					JOptionPane.showMessageDialog(null, "Please enter full edition information.");
				}else{
					Course_editions ce = new Course_editions();
					
					ce.setCourse_id(course_id_list.get(pointer));
					ce.setYear(Integer.parseInt(tcourse_year.getText()));
					
					if(dsemester.getSelectedItem().equals("Fall")) ce.setSemester("f");
					else if(dsemester.getSelectedItem().equals("Winter")) ce.setSemester("w");
					else ce.setSemester("s");
					
					ce.setTotal_student(Integer.parseInt(tstudentsN.getText()));
					
					if(dcourseTime.getSelectedItem().equals("Morning")) ce.setTime_day("m");
					else if(dcourseTime.getSelectedItem().equals("Afternoon")) ce.setTime_day("a");
					else ce.setTime_day("e");
					
					String select = "SELECT dept_code, course_number FROM courses WHERE course_id = ?";
					PreparedStatement stmt = null;
					try{
						int input = JOptionPane.showConfirmDialog(contentPane, "Confirm your entry? You will not be able to change it after submission.","Confirm Data Entry",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(input == JOptionPane.YES_OPTION){
							ce.insertNewEdition(conn);
							stmt = conn.prepareStatement(select);
							stmt.setInt(1, course_id_list.get(pointer));
							refreshInfo(stmt.executeQuery().getString("dept_code"), stmt.executeQuery().getInt("course_number"));
							clearInput();
						}
					}catch(SQLException ex){
						SQLError.show(ex);
					}
					//add.setEnabled(false);
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 12;
		c.gridwidth = 1;
		contentPane.add(finish, c);
		
		finish.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean go_next = true;
				if(checkConfirm(ce_list)){
					try{
						for(int i = 0;i < course_id_list.size();i++){
							Enrollments en = new Enrollments();
							en.setEditionId(ce_list.get(course_id_list.get(i)));
							en.setUserName(username);
							if(en.checkEdition(conn)){
								JOptionPane.showMessageDialog(null, "You've already entered the course info for No." + i + 1 + " in the list, please choose another edition for that course.");
								go_next = false;
								break;
							}else{
								en.insertNewEdition(connection);
							}
						}
						if(go_next){
							FinalInfoCollection fc = new FinalInfoCollection(connection, username, ce_list, course_id_list);
							setVisible(false);
							fc.setSize(480, 600);
							fc.setLocationRelativeTo(null);
							fc.setVisible(true);
							dispose();
						}
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please enter edition information for all the courses.");
				}
			}
		});

		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 12;
		c.gridwidth = 1;
		contentPane.add(exit, c);
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int input = JOptionPane.showConfirmDialog(contentPane, "Want to quit the system without saving course info?","Exit Alert",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(input == JOptionPane.YES_OPTION){
					setVisible(false);
					JOptionPane.showMessageDialog(null, "Thank you for using our system.");
					System.exit(0);
				}
			}
		});
		
	}
	
	public void refreshTable()throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], st_course_list);
		java.util.List<NewCourseAdapter> nc = NewCourseAdapter.getAllNewCourses(connection);
		for(NewCourseAdapter n : nc){
			Object[] o = new Object[2];
			o[0] = n.getDeptCode();
			o[1] = n.getCourseNumber();
			model.addRow(o);
		}
		newcourses.setModel(model);
		newcourses.repaint();
	}
	
	public void refreshInfo(String dept_code, int course_number)throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], edition_info_list);
		int count = 1;
		java.util.List<Course_editions> ces = Course_editions.getAllInfo(connection, dept_code, course_number);
		edition_id_list.clear();
		for(Course_editions ce : ces){
			Object[] o = new Object[5];
			o[0] = count++;
			o[1] = ce.getYear();
			
			if(ce.getSemester().equals("f")) o[2] = "Fall";
			else if(ce.getSemester().equals("s")) o[2] = "Summer";
			else o[2] = "Winter";
			
			o[3] = ce.getTotal_student();
			
			if(ce.getTime_day().equals("m")) o[4] = "Morning";
			else if(ce.getTime_day().equals("a")) o[4] = "Afternoon";
			else o[4] = "Evening";
			
			edition_id_list.add(ce.getEdition_id());
			//System.out.print(ce.getEdition_id() + "\n");
			model.addRow(o);
		}
		course_edition.setModel(model);
		course_edition.repaint();
	}
	
	public void clearInput(){
		tcourse_year.setText("");
		tcourse_year.setEnabled(false);
		dsemester.setSelectedIndex(0);
		dsemester.setEnabled(false);
		tstudentsN.setText("");
		tstudentsN.setEnabled(false);
		dcourseTime.setSelectedIndex(0);
		dcourseTime.setEnabled(false);
		new_edition.setEnabled(false);
	}
	
	public boolean checkConfirm(Map<Integer, Integer> ce_list){
		boolean rs = true;
		for(Map.Entry<Integer, Integer> entry : ce_list.entrySet()){
			if(entry.getValue().equals(-1)) rs = false;
		}
		return rs;
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
    	
    	NewCourseEdition_Interface app = new NewCourseEdition_Interface(conn, "CC");
    	app.setSize(480, 600);
    	app.setLocationRelativeTo(null);
        app.setVisible(true);
        
	}
	*/
	
	
}
