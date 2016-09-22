package CourseRecom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Skill_Rankings {
	private static final String TABLE_NAME = "skill_rankings";
	
	private String username, skill;
	private int edition_id, course_id, skill_id, rank_before, rank_after;
	
	private String skillInfo = "SELECT t1.skill_id as skill_id, skill FROM course_skills t1, skills t2 "
									+ "WHERE t1.course_id = ? AND t1.skill_id = t2.skill_id";
	
	private String skillRank = "SELECT rank_before, rank_after "
									+ "FROM skill_rankings "
									+ "WHERE skill_id = ? AND course_id = ? AND edition_id = ? "
									+ "AND username = ?";
	
	private String insertRank = "INSERT INTO skill_rankings VALUES(?,?,?,?,?,?)";
	
	private String updateRank = "UPDATE skill_rankings SET rank_before = ?, rank_after = ? "
									+ "WHERE course_id = ? AND edition_id = ? AND skill_id = ? "
									+ "AND username = ?";
	
	private String check = "SELECT * FROM skill_rankings "
									+ "WHERE skill_id = ? AND edition_id = ? "
									+ "AND course_id = ? AND username = ?";
	
	private String delete = "DELETE FROM skill_rankings "
									+ "WHERE skill_id = ? AND edition_id = ? "
									+ "AND course_id = ? AND username = ?";
	
	public List<Skill_Rankings> getSkillInfo(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		List<Skill_Rankings> sr = new ArrayList<Skill_Rankings>();
		try{
			stmt = conn.prepareStatement(skillInfo);
			stmt.setInt(1, this.course_id);
			//System.out.print(this.course_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Skill_Rankings s = new Skill_Rankings();
				s.setSkillId(rs.getInt("skill_id"));
				s.setSkillName(rs.getString("skill"));
				s.setCourseId(this.course_id);
				s.setUserName(this.username);
				s.setEditionId(this.edition_id);
				sr.add(s);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
		return sr;
	}
	
	public List<Skill_Rankings> getSkillRank(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		List<Skill_Rankings> sr = new ArrayList<Skill_Rankings>();
		try{
			stmt = conn.prepareStatement(skillRank);
			stmt.setInt(1, this.skill_id);
			//System.out.print("skill: " + this.skill_id + "\n");
			stmt.setInt(2, this.course_id);
			//System.out.print("course_id: " + this.course_id);
			stmt.setInt(3, this.edition_id);
			//System.out.print("e_id: " + this.edition_id);
			stmt.setString(4, this.username);
			//System.out.print("user: " + this.username);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				Skill_Rankings s = new Skill_Rankings();
				s.setRankBefore(res.getInt("rank_before"));
				//System.out.print(res.getInt("rank_before") + "\n");
				s.setRankAfter(res.getInt("rank_after"));
				//System.out.print(res.getInt("rank_after"));
				sr.add(s);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
		return sr;
	}
	
	public void updateSkillRank(Connection conn, int indicator)throws SQLException{
		PreparedStatement stmt = null;
		try{
			if(indicator == 0){
				stmt = conn.prepareStatement(insertRank);
				stmt.setInt(1, this.course_id);
				stmt.setInt(2, this.edition_id);
				stmt.setString(3, this.username);
				stmt.setInt(4, this.skill_id);
				stmt.setInt(5, this.rank_before);
				stmt.setInt(6, this.rank_after);
				stmt.execute();
			}else{
				stmt = conn.prepareStatement(updateRank);
				stmt.setInt(1, this.rank_before);
				stmt.setInt(2, this.rank_after);
				stmt.setInt(3, this.course_id);
				stmt.setInt(4, this.edition_id);
				stmt.setInt(5, this.skill_id);
				stmt.setString(6, this.username);
				//System.out.print("When update but not insert:\n");
				//System.out.print(this.rank_before + "\n");
				//System.out.print(this.rank_after + "\n");
				stmt.execute();
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void deleteSkillRank(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(delete);
			stmt.setInt(3, this.course_id);
			stmt.setInt(2, this.edition_id);
			stmt.setString(4, this.username);
			stmt.setInt(1, this.skill_id);
			stmt.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public boolean checkExist(Connection conn)throws SQLException{
		boolean res = false;
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(check);
			stmt.setInt(1, this.skill_id);
			stmt.setInt(2, this.edition_id);
			stmt.setInt(3, this.course_id);
			stmt.setString(4, this.username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) res = true;
		}catch(SQLException e){
			SQLError.show(e);
		}
		return res;
	}
	
	public void setSkillName(String skill){
		this.skill = skill;
	}
	
	public void setEditionId(int edition_id){
		this.edition_id = edition_id;
	}
	
	public void setUserName(String username){
		this.username = username;
	}
	
	public void setCourseId(int course_id){
		this.course_id = course_id;
	}
	
	public void setSkillId(int skill_id){
		this.skill_id = skill_id;
	}
	
	public void setRankBefore(int rank_before){
		this.rank_before = rank_before;
	}
	
	public void setRankAfter(int rank_after){
		this.rank_after = rank_after;
	}
	
	public String getSkillName(){
		return this.skill;
	}

	public String getUserName(){
		return this.username;
	}
	
	public int getEditionId(){
		return this.edition_id;
	}
	
	public int getCourseId(){
		return this.course_id;
	}
	
	public int getSkillId(){
		return this.skill_id;
	}
	
	public int getRankBefore(){
		return this.rank_before;
	}
	
	public int getRankAfter(){
		return this.rank_after;
	}
	
}
