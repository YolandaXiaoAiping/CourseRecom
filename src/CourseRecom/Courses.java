package CourseRecom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Courses {
	private static final String TABLE_NAME = "course";

	private String getCourse_id = "SELECT course_id FROM courses WHERE dept_code = ?";
	private static String getAllCourses = "SELECT DISTINCT t1.dept_code, t1.course_number FROM courses t1, course_editions t2, enrollments t3 "
			+ "WHERE t3.username =? and t3.edition_id = t2.edition_id and t2.course_id = t1.course_id";
	
	/*****************used for final info collection*******************/
	private String CourseExistence = "SELECT course_id FROM courses "
										+ "WHERE dept_code = ? AND course_number = ?";
	//if the course exists in the database, we don't need to add it again,
	//else we have to add it to the end of the table
	private String insertNew = "INSERT INTO courses VALUES(?,?,?,NULL)";
	private String NewCourseId = "SELECT COUNT(*) AS number FROM courses";
	/*****************used for final info collection*******************/
	
	private int course_id,course_number;
	private String dept_code,course_name;
	
	public List<Integer> getCourse_id(Connection con, String dept_code)throws SQLException{
		List<Integer> c_id = new ArrayList<>();
		PreparedStatement stmt = null;
		
		try{
			stmt = con.prepareStatement(getCourse_id);
			stmt.setString(1, dept_code);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				c_id.add(res.getInt("course_id"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return c_id;
	}
	
	public static List<Courses> getUserCourses(Connection conn, String username)throws SQLException{
		List<Courses> courses = new ArrayList <Courses>();
		PreparedStatement stmt = null;
		
		try{
			stmt = conn.prepareStatement(getAllCourses);
			stmt.setString(1, username);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				Courses c = new Courses();
				c.setDept_code(res.getString("dept_code"));
				c.setCourse_number(res.getInt("course_number"));
				courses.add(c);
			}
		}catch (SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return courses;
	}
	
	public boolean checkExistence(Connection conn)throws SQLException{
		boolean exist = false;
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(CourseExistence);
			stmt.setString(1, this.dept_code);
			stmt.setInt(2, this.course_number);
			ResultSet res = stmt.executeQuery();
			if(res.next()){
				exist = true;
				this.course_id = res.getInt("course_id");
			}
			else exist = false;
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return exist;
	}
	
	public void addNewCourse(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		int id;
		try{
			stmt = conn.prepareStatement(NewCourseId);
			ResultSet rs = stmt.executeQuery();
			id = rs.getInt("number") + 1;
			this.course_id = id;
			stmt1 = conn.prepareStatement(insertNew);
			stmt1.setInt(1, this.course_id);
			stmt1.setString(2, this.dept_code);
			stmt1.setInt(3, this.course_number);
			stmt1.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
			if(stmt1 != null) stmt1.close();
		}
	}
	
	public int getCourse_id(){
		return this.course_id;
	}
	public int getCourse_number(){
		return this.course_number;
	}
	public String getDept_code(){
		return this.dept_code;
	}
	public String getCourse_name(){
		return this.course_name;
	}
	
	public void setCourse_id(int course_id){
		this.course_id = course_id;
	}
	public void setCourse_number(int course_number){
		this.course_number = course_number;
	}
	public void setDept_code(String dept_code){
		this.dept_code = dept_code;
	}
	public void setCourse_name(String course_name){
		this.course_name = course_name;
	}
}
