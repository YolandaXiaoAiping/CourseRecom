package CourseRecom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Topic_Interests {
	private static final String TABLE_NAME = "topic_interests";

	private String username, topic;
	private int edition_id, course_id, topic_id, interest_before, interest_after;
	
	private String topicInfo = "SELECT t1.topic_id as topic_id, topic FROM course_topics t1, topics t2 "
			+ "WHERE t1.course_id = ? AND t1.topic_id = t2.topic_id";
	
	private String topicInterest = "SELECT interest_before, interest_after "
			+ "FROM topic_interests "
			+ "WHERE topic_id = ? AND course_id = ? AND edition_id = ? "
			+ "AND username = ?";
	
	private String check = "SELECT * FROM topic_interests "
			+ "WHERE topic_id = ? AND edition_id = ? "
			+ "AND course_id = ? AND username = ?";
	
	private String insertRank = "INSERT INTO topic_interests VALUES(?,?,?,?,?,?)";
	
	private String updateRank = "UPDATE topic_interests SET interest_before = ?, interest_after = ? "
			+ "WHERE course_id = ? AND edition_id = ? AND topic_id = ? "
			+ "AND username = ?";
	
	private String delete = "DELETE FROM topic_interests "
			+ "WHERE topic_id = ? AND edition_id = ? "
			+ "AND course_id = ? AND username = ?";
	
	public List<Topic_Interests> getTopicInfo(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		List<Topic_Interests> ti = new ArrayList<Topic_Interests>();
		try{
			stmt = conn.prepareStatement(topicInfo);
			stmt.setInt(1, this.course_id);
			//System.out.print(this.course_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Topic_Interests t = new Topic_Interests();
				t.setTopicId(rs.getInt("topic_id"));
				t.setTopicName(rs.getString("topic"));
				t.setCourseId(this.course_id);
				t.setUserName(this.username);
				t.setEditionId(this.edition_id);
				ti.add(t);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
		return ti;
	}
	
	public List<Topic_Interests> getTopicInterest(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		List<Topic_Interests> sr = new ArrayList<Topic_Interests>();
		try{
			stmt = conn.prepareStatement(topicInterest);
			stmt.setInt(1, this.topic_id);
			//System.out.print("topic: " + this.topic_id + "\n");
			stmt.setInt(2, this.course_id);
			//System.out.print("course_id: " + this.course_id);
			stmt.setInt(3, this.edition_id);
			//System.out.print("e_id: " + this.edition_id);
			stmt.setString(4, this.username);
			//System.out.print("user: " + this.username);
			ResultSet res = stmt.executeQuery();
			while(res.next()){
				Topic_Interests s = new Topic_Interests();
				s.setInterestBefore(res.getInt("interest_before"));
				//System.out.print(res.getInt("rank_before") + "\n");
				s.setInterestAfter(res.getInt("interest_after"));
				//System.out.print(res.getInt("rank_after"));
				sr.add(s);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
		return sr;
	}
	
	public void updateTopicRank(Connection conn, int indicator)throws SQLException{
		PreparedStatement stmt = null;
		try{
			if(indicator == 0){
				stmt = conn.prepareStatement(insertRank);
				stmt.setInt(1, this.course_id);
				stmt.setInt(2, this.edition_id);
				stmt.setString(3, this.username);
				stmt.setInt(4, this.topic_id);
				stmt.setInt(5, this.interest_before);
				stmt.setInt(6, this.interest_after);
				stmt.execute();
			}else{
				stmt = conn.prepareStatement(updateRank);
				stmt.setInt(1, this.interest_before);
				stmt.setInt(2, this.interest_after);
				stmt.setInt(3, this.course_id);
				stmt.setInt(4, this.edition_id);
				stmt.setInt(5, this.topic_id);
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
	
	public void deleteTopicRank(Connection conn)throws SQLException{
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(delete);
			stmt.setInt(3, this.course_id);
			stmt.setInt(2, this.edition_id);
			stmt.setString(4, this.username);
			stmt.setInt(1, this.topic_id);
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
			stmt.setInt(1, this.topic_id);
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
	
	public void setTopicName(String topic){
		this.topic = topic;
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
	
	public void setTopicId(int topic_id){
		this.topic_id = topic_id;
	}
	
	public void setInterestBefore(int interest_before){
		this.interest_before = interest_before;
	}
	
	public void setInterestAfter(int interest_after){
		this.interest_after = interest_after;
	}
	
	public String getTopicName(){
		return this.topic;
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
	
	public int getTopicId(){
		return this.topic_id;
	}
	
	public int getInterestBefore(){
		return this.interest_before;
	}
	
	public int getInterestAfter(){
		return this.interest_after;
	}
}
