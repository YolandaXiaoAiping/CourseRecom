package CourseRecom;

import java.sql.*;
import java.util.*;
import java.util.List;

public class Enrollments {
	private static final String TABLE_NAME = "enrollments";
	
	private int edition_id, course_ranking, instr_ranking;
	private String username, letter_grade;
	
	/******************for final info collection********************/
	private static String CourseExperience = "SELECT letter_grade, course_ranking, instr_ranking "
										+ "FROM enrollments "
										+ "WHERE username = ? AND edition_id = ?";
	
	//private String insertNew = "INSERT INTO enrollments VALUES(?,?,?,?,?)";
	private String deleteNew = "DELETE FROM enrollments WHERE edition_id = ? AND username = ?";
	private String resetNew = "UPDATE enrollments SET letter_grade = NULL, instr_ranking = NULL, "
									+ "course_ranking = NULL WHERE edition_id = ? "
									+ "AND username = ?";
	private String insertNew = "INSERT INTO enrollments VALUES(?,?,NULL,NULL,NULL)";
	private String updateNew = "UPDATE enrollments SET letter_grade = ?, instr_ranking = ?, "
									+ "course_ranking = ? WHERE edition_id = ? "
									+ "AND username = ?";
	private String checkExist = "SELECT * FROM enrollments WHERE edition_id = ? AND username = ?";
	/******************for final info collection********************/
	
	
	public static List<Enrollments> getCourseExperience(Connection conn, int edition_id, String username)throws SQLException{
		PreparedStatement stmt = null;
		List<Enrollments> e = new ArrayList<Enrollments>();
		try{
			stmt = conn.prepareStatement(CourseExperience);
			stmt.setString(1, username);
			stmt.setInt(2, edition_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Enrollments el = new Enrollments();
				el.setLetterGrade(rs.getString("letter_grade"));
				el.setInstrRanking(rs.getInt("instr_ranking"));
				el.setCourseRanking(rs.getInt("course_ranking"));
				e.add(el);
			}
		}catch(SQLException ex){
			SQLError.show(ex);
		}
		return e;
	}
	
	public void insertNewEdition(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(insertNew);
			stmt.setInt(1, this.edition_id);
			stmt.setString(2, this.username);
			stmt.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void updateNewEdition(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(updateNew);
			stmt.setInt(4, this.edition_id);
			stmt.setString(5, username);
			stmt.setString(1, this.letter_grade);
			stmt.setInt(2, this.course_ranking);
			stmt.setInt(3, this.instr_ranking);
			stmt.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void deleteNewEdition(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(resetNew);
			stmt.setInt(1, this.edition_id);
			stmt.setString(2, this.username);
			stmt.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public boolean checkEdition(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		boolean res = false;
		try{
			stmt = conn.prepareStatement(checkExist);
			stmt.setInt(1, this.edition_id);
			stmt.setString(2, this.username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) res = true;
		}catch(SQLException e){
			SQLError.show(e);
		}
		return res;
	}
	
	
	public void setUserName(String username){
		this.username = username;
	}
	
	public void setEditionId(int edition_id){
		this.edition_id = edition_id;
	}
	
	public void setLetterGrade(String letter_grade){
		this.letter_grade = letter_grade;
	}
	
	public void setCourseRanking(int course_ranking){
		this.course_ranking = course_ranking;
	}
	
	public void setInstrRanking(int instr_ranking){
		this.instr_ranking = instr_ranking;
	}
	
	public String getUserName(){
		return this.username;
	}
	
	public int getEditionId(){
		return this.edition_id;
	}
	
	public String getLetterGrade(){
		return this.letter_grade;
	}
	
	public int getCourseRanking(){
		return this.course_ranking;
	}
	
	public int getInstrRanking(){
		return this.instr_ranking;
	}
	
}
