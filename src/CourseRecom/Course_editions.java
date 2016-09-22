package CourseRecom;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course_editions {
	private int edition_id,course_id,year,total_student;
	private String semester,time_day;

	private static String getInfo = "SELECT t1.edition_id as edition, t1.course_id as course_id, year, semester, total_students, time_day "
										+ "FROM course_editions t1, courses t2 "
										+ "WHERE t1.course_id = t2.course_id "
										+ "AND t2.dept_code = ? AND t2.course_number = ?";
	
	private String insertInfo = "INSERT INTO course_editions VALUES(?,?,?,?,?,?)";
	
	private String countEdition = "SELECT COUNT(*) as count FROM course_editions";
	
	
	
	public static List<Course_editions> getAllInfo(Connection conn, String dept_code, int course_number)throws SQLException{
		PreparedStatement stmt = null;
		List<Course_editions> ce = new ArrayList<Course_editions>();
		try{
			stmt = conn.prepareStatement(getInfo);
			stmt.setString(1, dept_code);
			stmt.setInt(2, course_number);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Course_editions c = new Course_editions();
				c.setYear(rs.getInt("year"));
				c.setSemester(rs.getString("semester"));
				c.setTotal_student(rs.getInt("total_students"));
				c.setTime_day(rs.getString("time_day"));
				c.setEdition_id(rs.getInt("edition"));
				ce.add(c);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
		return ce;
	}
	
	public void insertNewEdition(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try{
			stmt = conn.prepareStatement(countEdition);
			int e_number = stmt.executeQuery().getInt("count") + 1;
			stmt1 = conn.prepareStatement(insertInfo);
			stmt1.setInt(1, e_number);
			stmt1.setInt(2, this.course_id);
			stmt1.setString(3, this.semester);
			stmt1.setInt(4, this.year);
			stmt1.setInt(5, this.total_student);
			stmt1.setString(6, this.time_day);
			stmt1.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public int getEdition_id(){
		return this.edition_id;
	}
	public int getCourse_id(){
		return this.course_id;
	}
	public int getYear(){
		return this.year;
	}
	public int getTotal_student(){
		return this.total_student;
	}
	public String getSemester(){
		return this.semester;
	}
	public String getTime_day(){
		return this.time_day;
	}
	
	public void setEdition_id(int edition_id){
		this.edition_id = edition_id;
	}
	public void setCourse_id(int course_id){
		this.course_id = course_id;
	}
	public void setYear(int year){
		this.year = year;
	}
	public void setTotal_student(int total_student){
		this.total_student = total_student;
	}
	public void setSemester(String semester){
		this.semester = semester;
	}
	public void setTime_day(String time_day){
		this.time_day = time_day;
	}
	}