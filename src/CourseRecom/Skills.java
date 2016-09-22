package CourseRecom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CourseRecom.SQLError;

public class Skills {
	private static final String TABLE_NAME = "Skills";
	
	private String getSkill = "select skill FROM skills WHERE skill_id=?";
	private String getAll_skill="SELECT * FROM skills";
	private String addNewSkill = "INSERT INTO skills VALUES(?,?)";
	private String countSkill = "SELECT COUNT(*) as count FROM skills";

	private int skill_id;
	private String skill;
	
	public void getAllSkills(Connection conn,Map<String,Integer> skill_map)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(getAll_skill);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				skill_map.put(res.getString("skill"),0);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
	}
	
	/****************function for final info collection*************************/
	public List<String> getSkillList(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		List<String> sl = new ArrayList<String>();
		try{
			stmt = conn.prepareStatement(getAll_skill);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				sl.add(rs.getString("skill"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return sl;
	}
	
	public void insertNewSkill(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try{
			stmt = conn.prepareStatement(countSkill);
			int skillN = stmt.executeQuery().getInt("count") + 1;
			stmt1 = conn.prepareStatement(addNewSkill);
			stmt1.setInt(1, skillN);
			stmt1.setString(2, this.skill);
			stmt1.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	/****************function for final info collection*************************/
	
	public String getSkill(Connection conn, int skill_id)throws SQLException{
		String skill ="";
		PreparedStatement stmt = null;
		
		try{
			stmt = conn.prepareStatement(getSkill);
			stmt.setInt(1, skill_id);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				skill=res.getString("skill");
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return skill;
	}
	public int getSkill_id(){
		return this.skill_id;
	}
	public String getSkill(){
		return this.skill;
	}
	
	public void setSkill_id(int skill_id){
		this.skill_id = skill_id;
	}
	public void setSkill(String skill){
		this.skill = skill;
	}
}
