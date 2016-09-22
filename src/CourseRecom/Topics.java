package CourseRecom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Topics {
	private static final String TABLE_NAME = "Topics";
	
	private String getTopic = "SELECT topic FROM topics WHERE topic_id=?";
	private String getAllTopic = "SELECT topic FROM topics";
	private String addNewTopic = "INSERT INTO topics VALUES(?,?)";
	private String countTopic = "SELECT COUNT(*) as count FROM topics";

	private int topic_id;
	private String topic;
	
	public void getAllTopic(Connection conn,Map<String,Integer> map)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(getAllTopic);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				map.put(res.getString("topic"), 0);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	/****************function for final info collection*************************/
	public List<String> getTopicList(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		List<String> tl = new ArrayList<String>();
		try{
			stmt = conn.prepareStatement(getAllTopic);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				tl.add(rs.getString("topic"));
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return tl;
	}
	
	public void insertNewTopic(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try{
			stmt = conn.prepareStatement(countTopic);
			int topicN = stmt.executeQuery().getInt("count") + 1;
			stmt1 = conn.prepareStatement(addNewTopic);
			stmt1.setInt(1, topicN);
			stmt1.setString(2, this.topic);
			stmt1.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	/****************function for final info collection*************************/
	
	public String getTopic(Connection con, int topic_id) throws SQLException{
		String topic = "";
		PreparedStatement stmt = null;
		
		try{
			stmt = con.prepareStatement(getTopic);
			stmt.setInt(1, topic_id);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				topic=res.getString("topic");
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return topic;
	}
	
	public int getTopic_id(){
		return this.topic_id;
	}
	public String getTopic(){
		return this.topic;
	}
	
	public void setTopic_id(int topic_id){
		this.topic_id = topic_id;
	}
	public void setTopic(String topic){
		this.topic = topic;
	}
}
