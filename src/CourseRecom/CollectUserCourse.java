package CourseRecom;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import CourseRecom.DBConnection;

public class CollectUserCourse extends JFrame{
	private static final String PROGRAM_NAME = "collectusercourse";
	Connection connection;
	String username;
	boolean new_user;
	//String dept_code;
	//int course_number;
	
	PreparedStatement stmt1 = null;
	PreparedStatement stmt2 = null;
	private String newTable = "DROP TABLE IF EXISTS new_course";
	private String createTable = "CREATE TABLE new_course("
									+ "dept_code TEXT,"
									+ "course_number INTEGER,"
									+ "PRIMARY KEY(dept_code, course_number)"
									+ ")";
	
	JTable courses;
	
	JLabel courseN;
	JLabel deptcode;
	JTextField tdeptcode;
	JTextField tcourseN;
	
	JButton addcourse;
	JButton delete;
	
	JButton next;
	JButton exit;
	
	JTable newCourse;
	
	Object[] course_column = {"Department Code","Course Number"};
	public CollectUserCourse(Connection conn, String username, boolean new_user)throws SQLException{
		super("Course Recommendation System");
		
		this.connection = conn;
		this.username = username;
		this.new_user = new_user;
		
		try{
			stmt1 = conn.prepareStatement(newTable);
			stmt1.execute();
			stmt2 = conn.prepareStatement(createTable);
			stmt2.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
		
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(conn);
	          System.exit(0);
	        }
	    }); 
		
		//initialization----------------------------------//
		courses = new JTable(); //display course table
		refreshTable();
		deptcode = new JLabel("Department Code: ");
		courseN = new JLabel("Course Number: ");
		tdeptcode = new JTextField(10);
		tcourseN = new JTextField(10);
		
		addcourse = new JButton("Add New Course");
		delete = new JButton("Delete New Course");
		delete.setEnabled(false);
		
		newCourse = new JTable();
		
		next = new JButton("Next");
		exit = new JButton("Exit");
		
		//initialization----------------------------------//
		
		Container contentPane = getContentPane();
		contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,10,5,10);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 5.0;
		c.weighty = 5.0;
		c.gridx = 0;
		c.gridy = 0;
		//c.gridheight = 1;
		c.gridwidth = 7;
		contentPane.add(new JScrollPane(courses), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		//c.gridwidth = 1;
		contentPane.add(deptcode,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		//c.gridwidth = 1;
		contentPane.add(tdeptcode,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		contentPane.add(courseN, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		contentPane.add(tcourseN, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 3;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		contentPane.add(new JScrollPane(newCourse), c);
		
		newCourse.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					delete.setEnabled(true);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					tdeptcode.setText(target.getValueAt(row, 0).toString());
					tcourseN.setText(Integer.toString((int)target.getValueAt(row, 1)));
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		contentPane.add(addcourse, c);
		
		addcourse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				NewCourseAdapter nc = new NewCourseAdapter();
				try{
					if(tdeptcode.getText().equals("") || tcourseN.getText().equals(""))
						JOptionPane.showMessageDialog(null, "Please input entire information about the course.");
					else{
						nc.setDeptCode(tdeptcode.getText());
						nc.setCourseNumber(Integer.parseInt(tcourseN.getText()));
						nc.addCourse(connection);
						refreshNewTable();
						clearInputs();
					}
					
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		contentPane.add(delete, c);
		
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				NewCourseAdapter nc = new NewCourseAdapter();
				nc.setDeptCode(tdeptcode.getText());
				nc.setCourseNumber(Integer.parseInt(tcourseN.getText()));
				try{
					nc.deleteCourse(connection);
					refreshNewTable();
					clearInputs();
					delete.setEnabled(false);
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		contentPane.add(next, c);
		
		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					setVisible(false);
					TopicSkill_Record tr = new TopicSkill_Record(connection, username, new_user);
					tr.setSize(480,600);
					tr.setLocationRelativeTo(null);
					tr.setVisible(true);
					dispose();
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.25;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		contentPane.add(exit, c);
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					setVisible(false);
					Login_Interface li = new Login_Interface(connection);
					li.setSize(400,600);
					li.setLocationRelativeTo(null);
					li.setVisible(true);
					dispose();
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		
	}
	
	private void refreshTable()throws SQLException{
		try{
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_column);
			java.util.List<Courses> getAllUserCourses = Courses.getUserCourses(connection, username);
			for(Courses c : getAllUserCourses){
				Object[] o = new Object[2];
				o[0] = c.getDept_code();
				o[1] = c.getCourse_number();
				model.addRow(o);
			}
			courses.setModel(model);
			courses.repaint();
		}catch(SQLException e){
			SQLError.show(e);
		}                                           
	}
	
	public void refreshNewTable()throws SQLException{
		try{
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_column);
			java.util.List<NewCourseAdapter> getAllNewCourses = NewCourseAdapter.getAllNewCourses(connection);
			for(NewCourseAdapter nc : getAllNewCourses){
				Object[] o = new Object[2];
				o[0] = nc.getDeptCode();
				o[1] = nc.getCourseNumber();
				model.addRow(o);
			}
			newCourse.setModel(model);
			newCourse.repaint();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void clearInputs(){
		tdeptcode.setText("");
		tcourseN.setText("");
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
    	
    	CollectUserCourse app = new CollectUserCourse(conn,"CC");
    	app.setSize(500, 1000);
        app.setVisible(true);
        
	}
	*/
	

}
