package CourseRecom;

import java.sql.*;
import java.util.*;

public class CourseRecomAdapter {
private static final String PROGRAM_NAME = "courserecomadapter";
	
	//Connection connection;
	//String username;
	private String dept_code, letter_grade;
	private int course_number;
	private double avg_ranking, avg_skill, avg_interest;
	
	
	private static String HighestRanking = "SELECT t2.dept_code as dept_code, t2.course_number as course_number, "
										+ "avg(t1.course_ranking) as avg_ranking "
										+ "FROM tf_final t1, courses t2 "
										+ "WHERE t1.course_id = t2.course_id "
										+ "GROUP BY dept_code, course_number "
										+ "ORDER BY 3 DESC "
										+ "LIMIT 5";
	
	private static String SkillImprovement = "SELECT t2.dept_code as dept_code, t2.course_number as course_number,"
										+ "avg(t1.skill_improvement) as avg_skill "
										+ "FROM tf_skill t1, courses t2 "
										+ "WHERE t1.course_id = t2.course_id "
										+ "GROUP BY dept_code, course_number "
										+ "ORDER BY 3 DESC "
										+ "LIMIT 5";
	
	private static String InterestIncreasing = "SELECT t2.dept_code as dept_code, t2.course_number as course_number,"
										+ "avg(t1.interest_increasing) as avg_interest "
										+ "FROM tf_interest t1, courses t2 "
										+ "WHERE t1.course_id = t2.course_id "
										+ "GROUP BY dept_code, course_number "
										+ "ORDER BY 3 DESC "
										+ "LIMIT 5";
	
	private static String BestScore = "SELECT t3.dept_code as dept_code, t3.course_number as course_number, avg(t2.max_grade) as grade "
										+ "FROM tf_final t1, letter_grades t2, courses t3 "
										+ "WHERE t1.letter_grade = t2.letter_grade AND t3.course_id = t1.course_id "
										+ "GROUP BY t1.course_id "
										+ "ORDER BY 3 DESC "
										+ "LIMIT 5";
	
	public static List<CourseRecomAdapter> getHighestRanking(Connection conn)throws SQLException{
		List<CourseRecomAdapter> hr = new ArrayList<CourseRecomAdapter>();
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(HighestRanking);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				CourseRecomAdapter cra = new CourseRecomAdapter();
				cra.setDeptCode(res.getString("dept_code"));
				cra.setCourseN(res.getInt("course_number"));
				cra.setAvgRanking(res.getDouble("avg_ranking"));
				hr.add(cra);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return hr;
	}
	
	public static List<CourseRecomAdapter> getAverageSkill(Connection conn)throws SQLException{
		List<CourseRecomAdapter> hr = new ArrayList<CourseRecomAdapter>();
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(SkillImprovement);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				CourseRecomAdapter cra = new CourseRecomAdapter();
				cra.setDeptCode(res.getString("dept_code"));
				cra.setCourseN(res.getInt("course_number"));
				cra.setAvgSkill(res.getDouble("avg_skill"));
				hr.add(cra);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return hr;
	}
	
	public static List<CourseRecomAdapter> getAverageInterest(Connection conn)throws SQLException{
		List<CourseRecomAdapter> hr = new ArrayList<CourseRecomAdapter>();
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(InterestIncreasing);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				CourseRecomAdapter cra = new CourseRecomAdapter();
				cra.setDeptCode(res.getString("dept_code"));
				cra.setCourseN(res.getInt("course_number"));
				cra.setAvgInterest(res.getDouble("avg_interest"));
				hr.add(cra);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return hr;
	}
	
	public static List<CourseRecomAdapter> getLetterGrade(Connection conn)throws SQLException{
		List<CourseRecomAdapter> hr = new ArrayList<CourseRecomAdapter>();
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement(BestScore);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				CourseRecomAdapter cra = new CourseRecomAdapter();
				cra.setDeptCode(res.getString("dept_code"));
				cra.setCourseN(res.getInt("course_number"));
				cra.setLetterGrade(res.getDouble("grade"));
				hr.add(cra);
			}
		}catch(SQLException e){
			SQLError.show(e);
		}finally{
			if(stmt != null) stmt.close();
		}
		return hr;
	}
	
	public void setDeptCode(String dept_code){
		this.dept_code = dept_code;
	}
	
	public void setCourseN(int course_number){
		this.course_number = course_number;
	}
	
	public void setAvgRanking(double avg_ranking){
		this.avg_ranking = avg_ranking;
	}
	
	public void setAvgSkill(double avg_skill){
		this.avg_skill = avg_skill;
	}
	
	public void setAvgInterest(double avg_interest){
		this.avg_interest = avg_interest;
	}
	
	public void setLetterGrade(double letter_grade){
		if(letter_grade >= 90)	this.letter_grade = "A+";
		else if(letter_grade >= 85) this.letter_grade = "A";
		else if(letter_grade >= 80) this.letter_grade = "A-";
		else if(letter_grade >= 77) this.letter_grade = "B+";
		else if(letter_grade >= 73) this.letter_grade = "B";
		else if(letter_grade >= 70) this.letter_grade = "B-";
		else if(letter_grade >= 67) this.letter_grade = "C+";
		else if(letter_grade >= 63) this.letter_grade = "C";
		else if(letter_grade >= 60) this.letter_grade = "C-";
		else if(letter_grade >= 57) this.letter_grade = "D+";
		else if(letter_grade >= 53) this.letter_grade = "D";
		else if(letter_grade >= 50) this.letter_grade = "D-";
		else this.letter_grade = "F";
	}
	
	/* letter grades transformation
	letter_grade  min_grade   max_grade   gpv
	------------  ----------  ----------  ----------
	A+            90          100         4.0
	A             85          89          4.0
	A-            80          84          3.7
	B+            77          79          3.3
	B             73          76          3.0
	B-            70          72          2.7
	C+            67          69          2.3
	C             63          66          2.0
	C-            60          62          1.7
	D+            57          59          1.3
	D-            53          56          1.0
	D             50          52          0.7
	F             0           49          0.0
	*/
	
	public String getDeptCode(){
		return this.dept_code;
	}
	
	public int getCourseN(){
		return this.course_number;
	}
	
	public double getAvgRanking(){
		return this.avg_ranking;
	}
	
	public double getAvgSkill(){
		return this.avg_skill;
	}
	
	public double getAvgInterest(){
		return this.avg_interest;
	}
	
	public String getLetterGrade(){
		return this.letter_grade;
	}
}
