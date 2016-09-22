package CourseRecom;

import java.sql.Connection;
import java.sql.*;
import java.util.Map;

import javax.swing.JOptionPane;

public class FindClosest {
	private static final String TABLE_NAME = "FindClosest";
	
	Connection conn;
	
	String username;
	
	//to get the student evaluation from other class
	Map<String,Integer> skill_map;
	Map<String,Integer> topic_map;
	
	//get all skill_skudent pairs
	private String dropView = "DROP VIEW IF EXISTS student_skill";
	
	private String student_skill = "CREATE VIEW student_skill AS"
			+ " SELECT skill,username FROM skills,students WHERE username<> '"+username+"'";
	//get student-skillname pairs
	private String dropView2 = "DROP VIEW IF EXISTS skill_value";
	
	private String getSkill_Evalu = "CREATE VIEW skill_value as"
			+ " SELECT skill,username,rank_before from skills,skill_rankings"
			+ " WHERE skills.skill_id=skill_rankings.skill_id";
	//get all student skill and rank pairs
	private String dropView3 = "DROP VIEW IF EXISTS student_skill_val";

	private String student_skill_val = "CREATE VIEW student_skill_val AS"
			+ " SELECT student_skill.skill,student_skill.username,avg(rank_before) as rank_before from student_skill LEFT OUTER JOIN skill_value"
			+ " ON student_skill.skill = skill_value.skill AND student_skill.username = skill_value.username "
			+ " GROUP BY student_skill.skill,student_skill.username ORDER BY student_skill.username";
	
	private String dropTable2 = "DROP TABLE IF EXISTS distance_val";
	
	private String getAllrank = "SELECT * FROM student_skill_val";
	
	/**********************************************************Topics************************************************************/
	//get all student-topic pairs
	private String dropView4 ="DROP VIEW IF EXISTS student_topic";
	private String student_topic = "CREATE VIEW student_topic AS"
			+ " SELECT topic,username FROM topics,topic_interests WHERE username<> '"+username+"'";
	//get all student topicname pairs
	private String dropView6 = "DROP VIEW IF EXISTS topic_value";
	private String getTopic_Evalu = "CREATE VIEW topic_value as"
			+ " SELECT topic,username,interest_before from topics,topic_interests"
			+ " WHERE topics.topic_id=topic_interests.topic_id";
	//get all student skill and rank pairs
	private String dropView7 = "DROP VIEW IF EXISTS student_topic_val";
	private String student_topic_val = "CREATE VIEW student_topic_val AS"
			+ " SELECT student_topic.topic,student_topic.username,avg(interest_before) as interest_before FROM student_topic LEFT OUTER JOIN topic_value"
			+ " ON student_topic.topic =topic_value.topic AND student_topic.username = topic_value.username"
			+ " GROUP BY student_topic.topic,student_topic.username";
	
	private String dropTable = "DROP TABLE IF EXISTS distance_val1";
	
	private String getAllInterest = "SELECT * FROM student_topic_val";
	
	public FindClosest(Connection conn,Map<String,Integer> skill_map,Map<String,Integer> topic_map,String username)throws SQLException{
		
		this.conn = conn;
		this.skill_map = skill_map;
		this.topic_map = topic_map;
		this.username = username;
		
		//run the queries
		PreparedStatement stmt =null;
		PreparedStatement stmt2 =null;
		PreparedStatement stmt3 =null;
		PreparedStatement stmt4 =null;
		PreparedStatement stmt5 =null;
		PreparedStatement stmt6 =null;

		try{
			stmt = conn.prepareStatement(dropView);
			stmt.executeUpdate();
			stmt2 = conn.prepareStatement(student_skill);
			stmt2.executeUpdate();
			stmt3 = conn.prepareStatement(dropView2);
			stmt3.executeUpdate();
			stmt4 = conn.prepareStatement(getSkill_Evalu);
			stmt4.executeUpdate();
			stmt5 = conn.prepareStatement(dropView3);
			stmt5.executeUpdate();
			stmt6 = conn.prepareStatement(student_skill_val);
			stmt6.executeUpdate();
			

			}catch(SQLException e){
				SQLError.show(e);
			}finally{
				if(stmt != null){
					stmt.close();
				}
				if(stmt2 != null){
					stmt2.close();
				}if(stmt3 != null){
					stmt3.close();
				}if(stmt4 != null){
					stmt4.close();
				}if(stmt5 != null){
					stmt5.close();
				}if(stmt6 != null){
					stmt6.close();
				}
			}
		
		PreparedStatement stmta =null;
		PreparedStatement stmtb =null;
		PreparedStatement stmtc =null;
		PreparedStatement stmtd =null;
		PreparedStatement stmte =null;
		PreparedStatement stmtf =null;
		
		try{
			stmta = conn.prepareStatement(dropView4);
			stmta.executeUpdate();
			stmtb = conn.prepareStatement(student_topic);
			stmtb.executeUpdate();
			stmtc = conn.prepareStatement(dropView6);
			stmtc.executeUpdate();
			stmtd = conn.prepareStatement(getTopic_Evalu);
			stmtd.executeUpdate();
			stmte = conn.prepareStatement(dropView7);
			stmte.executeUpdate();
			stmtf = conn.prepareStatement(student_topic_val);
			stmtf.executeUpdate();
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmta != null){
				stmta.close();
			}
			if(stmtb != null){
				stmtb.close();
			}if(stmtc != null){
				stmtc.close();
			}if(stmtd != null){
				stmtd.close();
			}if(stmte != null){
				stmte.close();
			}if(stmtf != null){
				stmtf.close();
			}
		}

		
		//dynamically generate a new table to calculate the distance
		String table = "CREATE TABLE distance_val (username TEXT,\"";
		int count = 0;
		for(String key:skill_map.keySet()){
			if(skill_map.get(key)!=0){
				table+=key+"\" INTEGER,\"";
				count++;
			}
		}
		table = table.substring(0, table.length()-2);
		table+=")";
		
		/**************************** adding topics into the String*******************************/
		String table1 = "CREATE TABLE distance_val1 (username TEXT,\"";
		int count1 = 0;
		for(String key:topic_map.keySet()){
			if(topic_map.get(key) != 0){
				count1++;
				table1+=key+"\" INTEGER,\"";
			}
		}
		//System.out.print(table1+'\n');
		//System.out.print(table.charAt(table.length()-1)+'\n');
		table1 = table1.substring(0, table1.length()-2);
		//System.out.print(table1+'\n');
		table1+=")";
		//System.out.print(table1+'\n');

		PreparedStatement stmt9 =null;
		PreparedStatement stmt10 =null;

		try{
			stmt9 = conn.prepareStatement(dropTable2);
			stmt9.executeUpdate();
			stmt10 = conn.prepareStatement(table);
			stmt10.executeUpdate();
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt9 != null){
				stmt9.close();
			}
			if(stmt10 != null){
				stmt10.close();
			}
		}
		/**********************val1 for skills****************************/
		PreparedStatement stmtg =null;
		PreparedStatement stmth =null;
		
		try{
			stmtg = conn.prepareStatement(dropTable);
			stmtg.executeUpdate();
			stmth = conn.prepareStatement(table1);
			stmth.executeUpdate();
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmtg != null){
				stmtg.close();
			}
			if(stmth != null){
				stmth.close();
			}
		}

		//insert all corresponding values into the table
		PreparedStatement stmt11 =null;
		PreparedStatement stmt12 =null;
		PreparedStatement stmt13 =null;
		PreparedStatement stmt14 =null;
		PreparedStatement stmt15 =null;


		try{
			stmt11 = conn.prepareStatement(getAllrank);
			ResultSet res = stmt11.executeQuery();
			
			while(res.next()){
				//if the user has ranked the skill
				if(skill_map.get(res.getString("skill")) != 0){
					//check if the name is already in the table
					String username1 = res.getString("username");
					String check = "SELECT * FROM distance_val WHERE username='"+username1+"'";
					stmt12 = conn.prepareStatement(check);
					ResultSet res2 = stmt12.executeQuery();
					if(res2.next()){
						String updateValue = "UPDATE distance_val SET \""+res.getString("skill")+"\"="+Integer.toString(res.getInt("rank_before"))+""
								+ " WHERE username= '"+res.getString("username")+"'";
						//System.out.print(updateValue+'\n');
						stmt13 = conn.prepareStatement(updateValue);
						stmt13.executeUpdate();
					}else{
						//if the name is not in the table, we need to insert the username first
						String insertName = "INSERT INTO distance_val VALUES('"+res.getString("username")+"',";
						for(int i = 0; i<count; i++){
							insertName+="NULL,";
						}
						insertName = insertName.substring(0, insertName.length()-1);
						insertName+=")";
						//System.out.print(insertName+'\n');
						String updateValue = "UPDATE distance_val SET \""+res.getString("skill")+"\"="+Integer.toString(res.getInt("rank_before"))+""
								+ " WHERE username='"+res.getString("username")+"'";
						stmt14 = conn.prepareStatement(insertName);
						stmt14.executeUpdate();
						stmt15 = conn.prepareStatement(updateValue);
						stmt15.executeUpdate();
					}
				}
			}
			
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt11 != null){
				stmt11.close();
			}
			if(stmt12 != null){
				stmt12.close();
			}if(stmt13 != null){
				stmt13.close();
			}if(stmt14 != null){
				stmt14.close();
			}if(stmt15 != null){
				stmt15.close();
			}
		}
		
		/*************************insert for topics********************************************/
		
		PreparedStatement stmti = null;
		PreparedStatement stmtj = null;
		PreparedStatement stmtk = null;
		PreparedStatement stmtl = null;
		PreparedStatement stmtm = null;


		try{
			stmti = conn.prepareStatement(getAllInterest);
			ResultSet res = stmti.executeQuery();
			while(res.next()){
				//if the user has ranked the skill
				if(topic_map.get(res.getString("topic")) != 0&&!res.getString("username").equals("w'")){
					//check if the name is already in the table
					String username1 = res.getString("username");
					String check = "SELECT * FROM distance_val1 WHERE username='"+username1+"'";
					stmtj = conn.prepareStatement(check);
					ResultSet res2 = stmtj.executeQuery();
					if(res2.next()){
						String updateValue = "UPDATE distance_val1 SET \""+res.getString("topic")+"\"="+Integer.toString(res.getInt("interest_before"))
								+ " WHERE username= '"+res.getString("username")+"'";
						//System.out.print(updateValue+'\n');
						stmtk = conn.prepareStatement(updateValue);
						stmtk.executeUpdate();
					}else{
						//if the name is not in the table, we need to insert the username first
						String insertName = "INSERT INTO distance_val1 VALUES('"+res.getString("username")+"',";
						for(int i = 0; i<count1; i++){
							insertName+="NULL,";
						}
						insertName = insertName.substring(0, insertName.length()-1);
						insertName+=")";
						//System.out.print(insertName+'\n');
						String updateValue = "UPDATE distance_val1 SET \""+res.getString("topic")+"\"="+Integer.toString(res.getInt("interest_before"))
								+ " WHERE username='"+res.getString("username")+"'";
						stmtl = conn.prepareStatement(insertName);
						stmtl.executeUpdate();
						stmtm = conn.prepareStatement(updateValue);
						stmtm.executeUpdate();
						
					}
				}
			}
			
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmti != null){
				stmti.close();
			}
			if(stmtj != null){
				stmtj.close();
			}if(stmtk != null){
				stmtk.close();
			}if(stmtl != null){
				stmtl.close();
			}if(stmtm != null){
				stmtm.close();
			}
		}
		
		String dropView5 = "DROP VIEW IF EXISTS closest";
		// we need to adjust the value a little bit
		PreparedStatement stmt16 =null;
		PreparedStatement stmt17 =null;
		PreparedStatement stmt18 =null;

		for(String key:skill_map.keySet()){
			if(skill_map.get(key)!=0){
				String update = "UPDATE distance_val SET \""+key+"\"="+Integer.toString(25)+" WHERE \""+ key+"\"=0";
				String update2 = "UPDATE distance_val SET \""+key+"\"=\""+key+"\"-"+Integer.toString(skill_map.get(key))+" WHERE \""+ key+"\"<25";
				String update3 = "UPDATE distance_val SET \""+key+"\"=\""+key+"\"*\""+key+"\" WHERE \""+ key+"\"<25";
				try{
					stmt16 = conn.prepareStatement(update);
					stmt16.executeUpdate();
					stmt17 = conn.prepareStatement(update2);
					stmt17.executeUpdate();
					stmt18 = conn.prepareStatement(update3);
					stmt18.executeUpdate();
					
				}catch(SQLException e){
					SQLError.show(e);
				}finally{
					if(stmt16 != null){
						stmt16.close();
					}
					if(stmt17 != null){
						stmt17.close();
					}if(stmt18 != null){
						stmt18.close();
					}
				}
			}
		}
		/*************************for topics******************************/
		
		PreparedStatement stmto =null;
		PreparedStatement stmtp =null;
		PreparedStatement stmtq =null;
		
		for(String key:topic_map.keySet()){
			if(topic_map.get(key)!=0){
				String update = "UPDATE distance_val1 SET \""+key+"\"="+Integer.toString(25)+" WHERE \""+ key+"\"=0";
				String update2 = "UPDATE distance_val1 SET \""+key+"\"=\""+key+"\"-"+Integer.toString(topic_map.get(key))+" WHERE \""+ key+"\"<25";
				String update3 = "UPDATE distance_val1 SET \""+key+"\"=\""+key+"\"*\""+key+"\" WHERE \""+ key+"\"<25";
				try{
					stmto = conn.prepareStatement(update);
					stmto.executeUpdate();
					stmtp = conn.prepareStatement(update2);
					stmtp.executeUpdate();
					stmtq = conn.prepareStatement(update3);
					stmtq.executeUpdate();
					
				}catch(SQLException e){
					SQLError.show(e);
				}finally{
					if(stmto != null){
						stmto.close();
					}
					if(stmtp != null){
						stmtp.close();
					}if(stmtq != null){
						stmtq.close();
					}
				}
			}
		}
		
		//calculate two table's sum to avoid problems
			String dropView7 = "DROP VIEW IF EXISTS val_sum ";
			String createSum1 = "CREATE VIEW val_sum AS"
					+ " SELECT username,(\"";
			for(String key:skill_map.keySet()){
				if(skill_map.get(key)!=0){
					createSum1+=key+"\"+\"";
				}
			}
			createSum1 =createSum1.substring(0,createSum1.length()-2);
			createSum1+=") AS skill_sum FROM distance_val";
			
			String dropView8 = "DROP VIEW IF EXISTS val_sum1";
			String createSum2 = "CREATE VIEW val_sum1 AS"
					+ " SELECT username,(\"";
			for(String key:topic_map.keySet()){
				if(topic_map.get(key)!=0){
					createSum2+=key+"\"+\"";
				}
			}
			createSum2 = createSum2.substring(0,createSum2.length()-2);
			createSum2+=") AS topic_sum FROM distance_val1";
			
			String dropView10 = "DROP VIEW IF EXISTS maxDis";
			String createmax = "CREATE VIEW maxDis AS"
					+ " SELECT val_sum.username FROM val_sum,val_sum1"
					+ " WHERE skill_sum="+Integer.toString(count*25)+" AND topic_sum ="+Integer.toString(count1*25)+" AND val_sum.username=val_sum1.username";
					
			PreparedStatement stmtaa = null;
			PreparedStatement stmtab = null;
			PreparedStatement stmtac = null;
			PreparedStatement stmtad = null;
			PreparedStatement stmtae = null;
			PreparedStatement stmtaf = null;


			try{
				stmtaa = conn.prepareStatement(dropView7);
				stmtaa.executeUpdate();
				stmtab = conn.prepareStatement(createSum1);
				stmtab.executeUpdate();
				stmtac = conn.prepareStatement(dropView8);
				stmtac.executeUpdate();
				stmtad = conn.prepareStatement(createSum2);
				stmtad.executeUpdate();
				stmtae = conn.prepareStatement(dropView10);
				stmtae.executeUpdate();
				stmtaf = conn.prepareStatement(createmax);
				stmtaf.executeUpdate();
			}catch(SQLException e){
				SQLError.show(e);
			}finally{
				if(stmtaa!=null){
					stmtaa.close();
				}
				if(stmtab!=null){
					stmtab.close();
				}
				if(stmtac!=null){
					stmtac.close();
				}
				if(stmtad!=null){
					stmtad.close();
				}
				if(stmtae!=null){
					stmtae.close();
				}
				if(stmtaf!=null){
					stmtaf.close();
				}
			}
		//the last step, we need to gather other information difference
			//get the user information first
			String getUser = "SELECT * FROM students WHERE username='"+username+"'";
			PreparedStatement statement = null;
			int age = 0;
			String gender = "";
			String country = "";
			try{
				statement = conn.prepareStatement(getUser);
				ResultSet rs = statement.executeQuery();
				 if(rs.next()){
					 age = rs.getInt("age");
					 gender = rs.getString("gender");
						if(rs.wasNull()){
							gender = "";
						}
					 country = rs.getString("native_country");
						if(rs.wasNull()){
							country ="";
						}
				 }
			}catch(SQLException e){
				SQLError.show(e);
			}
			statement.close();	
			
			//first we need to calculate the average age from the database in case we will use it
			String getAverage_age = "SELECT avg(age) AS age FROM students";
			int avg_age = 0;
			try{
				statement = conn.prepareStatement(getAverage_age);
				ResultSet result1 = statement.executeQuery();
				if(result1.next()){
					avg_age = result1.getInt("age");
				}
			}catch(SQLException e){
				SQLError.show(e);
			}
			
			statement.close();
			
			String countUser = "SELECT count(username) AS countUser FROM students";
			String getPortion_female = "Select count(username) AS fNum FROM students WHERE gender='f'";
			String getPortion_male = "Select count(username) AS mNum FROM students WHERE gender='m'";
			
			int userNum = 0;
			try{
				statement = conn.prepareStatement(countUser);
				ResultSet result = statement.executeQuery();
				if(result.next()){
					userNum = result.getInt("countUser");
				}
			}catch(SQLException e){
				SQLError.show(e);
			}
			statement.close();
			
			int fNum = 0;
			try{
				statement = conn.prepareStatement(getPortion_female);
				ResultSet result = statement.executeQuery();
				if(result.next()){
					fNum = result.getInt("fNum");
				}
			}catch(SQLException e){
				SQLError.show(e);
			}		
			statement.close();
			
			int mNum = 0;
			try{
				statement = conn.prepareStatement(getPortion_male);
				ResultSet result = statement.executeQuery();
				if(result.next()){
					mNum = result.getInt("mNum");
				}
			}catch(SQLException e){
				SQLError.show(e);
			}				
			statement.close();
			
			double percentF = (double)fNum/(double)userNum;
			double percentM = (double)mNum/(double)userNum;
			
			String dropView9 = "DROP VIEW IF EXISTS student_info";
			String data = "CREATE VIEW student_info AS"
					+ " SELECT username,";
			if(age!=0){
				data+="(CASE WHEN age IS NOT NULL THEN (age-"+Integer.toString(age)+")*(age-"+Integer.toString(age)+")"
						+ " ELSE ("+Integer.toString(avg_age)+"-"+Integer.toString(age)+")*("+Integer.toString(avg_age)+"-"+Integer.toString(age)+") END)+";
			}
			if(!gender.equals("")){
				if(gender.equals("f")){
					data+="(CASE WHEN gender IS NULL THEN "+Double.toString(1.0-percentF)
							+ " WHEN gender='"+gender+"' THEN 0"
							+ " WHEN gender<>'"+gender+"' THEN 1"
							+ " END)+";
				}else{
					data+="(CASE WHEN gender IS NULL THEN "+Double.toString(1.0-percentM)
					+ " WHEN gender='"+gender+"' THEN 0"
					+ " WHEN gender<>'"+gender+"' THEN 1"
					+ " END)+";
				}
			}
			if(!country.equals("")){
				data+="(CASE WHEN native_country IS NULL THEN 1"
						+ " WHEN native_country='"+country+"' THEN 0"
								+ " WHEN native_country<>'"+country+"' THEN 1"
										+ " END)";
			}
			
			if(data.charAt(data.length()-1)=='+'){
				data = data.substring(0,data.length()-1);
			}
			
			data+=" AS student_distance FROM students WHERE username<>'"+username+"'";
			//System.out.print(data);
			
			PreparedStatement ps =null;
			PreparedStatement ps1 =null;
			 try{
				 ps = conn.prepareStatement(dropView9);
				 ps.executeUpdate();
				 ps1 = conn.prepareStatement(data);
				 ps1.executeUpdate();
			 }catch(SQLException e){
					SQLError.show(e);
			 }
			 
			 ps.close();
			 ps1.close();

		//then we need to get the closest 15 person 
		PreparedStatement stmt19 =null;
		PreparedStatement stmt20 =null;
		String sum = "CREATE VIEW closest AS "
				+ " SELECT val_sum.username,(skill_sum+topic_sum+student_distance) AS sum"
				+ " FROM val_sum,val_sum1,student_info"
				+ " WHERE val_sum.username=val_sum1.username AND val_sum.username = student_info.username AND val_sum.username NOT IN ("
				+ "SELECT username FROM maxDis) ORDER BY sum ASC LIMIT 15";	
		try{
			stmt19 = conn.prepareStatement(dropView5);
			stmt19.executeUpdate();
			stmt20 = conn.prepareStatement(sum);
			stmt20.executeUpdate();
			
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt19 != null){
				stmt19.close();
			}
			if(stmt20 != null){
				stmt20.close();
			}
		}
		
		//we need to check if the 15 person has the same distance
		String check_distance = "SELECT sum,count(sum) AS count FROM closest GROUP BY sum";
		PreparedStatement ps2 = null;
		try{
			ps2 = conn.prepareStatement(check_distance);
			ResultSet res = ps2.executeQuery();
			if(res.next()){
				if(res.getInt("count")==15){
					/****************************we can not recommend course******************************************/
    				JOptionPane.showMessageDialog(null, "Sorry the program failed to distinguish between similar and non-similar users and no recommendations will be issues in this case.","Distance Problem",JOptionPane.ERROR_MESSAGE);
				}else{
					/*********************recommend courses************************/
				}
			}
		}catch(SQLException e){
			SQLError.show(e);
		}
		//test
		PreparedStatement stmt21 =null;

		String test = "SELECT * FROM closest ";
		try{
			stmt21 = conn.prepareStatement(test);
			ResultSet res = stmt21.executeQuery();
			while(res.next()){
				//System.out.print("finish!!!!!");
				System.out.print(res.getString("username")+'\n');
			}
			
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt21 != null){
				stmt21.close();
			}
		}
		
		/****************************now we need to get all those 15 persons' courses***********************************/
		
		
	}
	


}
