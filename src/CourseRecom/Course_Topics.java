package CourseRecom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course_Topics {
	private static final String TABLE_NAME = "course_topics";
	
	private String getCourse_topic = "SELECT DISTINCT topic_id FROM course_topics WHERE course_id IN (SELECT course_id FROM courses WHERE dept_code=?)";
	private String insertNew = "INSERT INTO course_topics VALUES(?, ?)";
	private String findTopicid = "SELECT topic_id FROM topics WHERE topic = ?";
	
	private int topic_id;
	private int course_id;
	private String topic;
	
	public List<Integer> getCourse_topic(Connection con,String dept_code)throws SQLException{
		List<Integer> c_topic = new ArrayList<>();
		PreparedStatement stmt = null;
		
		try{
			stmt = con.prepareStatement(getCourse_topic);
			stmt.setString(1, dept_code);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				c_topic.add(res.getInt("topic_id"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return c_topic;
	}
	
	public List<Integer> getCourse_skill(Connection con,int course_id)throws SQLException{
		List<Integer> c_topic = new ArrayList<>();
		PreparedStatement stmt = null;
		
		try{
			stmt = con.prepareStatement(getCourse_topic);
			stmt.setInt(1, course_id);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				c_topic.add(res.getInt("topic_id"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return c_topic;	
	}
	
	public void insertNewCTopic(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try{
			stmt = conn.prepareStatement(findTopicid);
			stmt.setString(1, this.topic);
			int tid = stmt.executeQuery().getInt("topic_id");
			stmt1 = conn.prepareStatement(insertNew);
			stmt1.setInt(1, tid);
			stmt1.setInt(2, this.course_id);
			stmt1.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	public void setTopic(String topic){
		this.topic = topic;
	}
	
	public void setTopicId(int topic_id){
		this.topic_id = topic_id;
	}
	
	public void setCourseId(int course_id){
		this.course_id = course_id;
	}
	
	public int getTopicId(){
		return this.topic_id;
	}
	
	public int getCourseId(){
		return this.course_id;
	}
	
	public String getTopic(){
		return this.topic;
	}
	
}
