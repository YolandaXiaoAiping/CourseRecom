package CourseRecom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class NewCourseAdapter {
	public static final String TABLE_NAME = "new_course";
	
	private String dept_code;
	private int course_number = -1;
	private static String selectCourse = "SELECT * FROM new_course";
	private String deleteCourse = "DELETE FROM new_course "
									+ "WHERE dept_code =?"
									+ "and course_number =?";
	private String addCourse = "INSERT INTO new_course VALUES(?,?)";
	
	private String courseN = "SELECT COUNT(*) as count FROM new_course";
	
	public static List<NewCourseAdapter> getAllNewCourses(Connection conn)throws SQLException{
		List<NewCourseAdapter> nc = new ArrayList<NewCourseAdapter>();
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(selectCourse);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				NewCourseAdapter n = new NewCourseAdapter();
				n.setDeptCode(res.getString("dept_code"));
				n.setCourseNumber(res.getInt("course_number"));
				nc.add(n);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return nc;
	}
	
	public void deleteCourse(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(deleteCourse);
			stmt.setString(1, this.dept_code);
			stmt.setInt(2, this.course_number);
			stmt.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void addCourse(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(addCourse);
			stmt.setString(1, this.dept_code);
			stmt.setInt(2, this.course_number);
			stmt.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public int countCourse(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		int count = 0;
		try{
			stmt = conn.prepareStatement(courseN);
			count = stmt.executeQuery().getInt("count");
		}catch(SQLException e){
			SQLError.show(e);
		}
		return count;
	}
	
	public void setDeptCode(String dept_code){
		this.dept_code = dept_code;
	}
	
	public void setCourseNumber(int course_number){
		this.course_number = course_number;
	}
	
	public String getDeptCode(){
		return this.dept_code;
	}
	
	public int getCourseNumber(){
		return this.course_number;
	}
	
	
}
