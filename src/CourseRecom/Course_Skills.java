package CourseRecom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course_Skills {
	private static final String TABLE_NAME = "course_skills";
	
	private String getCourse_skill = "SELECT Distinct skill_id FROM course_skills where course_id IN (SELECT course_id FROM courses WHERE dept_code=?)";
	private String insertNew = "INSERT INTO course_skills VALUES(?, ?)";
	private String findSkillid = "SELECT skill_id FROM skills WHERE skill = ?";
	
	private int skill_id;
	private int course_id;
	private String skill;
	
	public List<Integer> getCourse_skill(Connection con,String dept_code)throws SQLException{
		List<Integer> c_skill = new ArrayList<>();
		PreparedStatement stmt = null;
		
		try{
			stmt = con.prepareStatement(getCourse_skill);
			stmt.setString(1, dept_code);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				c_skill.add(res.getInt("skill_id"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return c_skill;
	}
	
	public void insertNewCSkill(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try{
			stmt = conn.prepareStatement(findSkillid);
			stmt.setString(1, this.skill);
			int sid = stmt.executeQuery().getInt("skill_id");
			stmt1 = conn.prepareStatement(insertNew);
			stmt1.setInt(1, sid);
			stmt1.setInt(2, this.course_id);
			stmt1.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void setSkill(String skill){
		this.skill = skill;
	}
	
	public void setSkillId(int skill_id){
		this.skill_id = skill_id;
	}
	
	public void setCourseId(int course_id){
		this.course_id = course_id;
	}
	
	public int getSkillId(){
		return this.skill_id;
	}
	
	public int getCourseId(){
		return this.course_id;
	}
	
	public String getSkill(){
		return this.skill;
	}
	
}
