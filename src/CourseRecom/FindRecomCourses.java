package CourseRecom;
import java.sql.*;

public class FindRecomCourses {
	private final static String PROGRAM_NAME = "find_recom_courses";
	
	Connection connection;
	String username;

	private String dropview = "DROP VIEW IF EXISTS topfifteen";
	
	private String topfifteen = "CREATE VIEW topfifteen AS "
									+ "SELECT username FROM closest";
	
	private String dropview1 = "DROP VIEW IF EXISTS attended";
	
	private String dropview2 = "DROP VIEW IF EXISTS oldpossible";
	
	private String OldPossible = "CREATE VIEW oldpossible AS "
										+ "SELECT username, dept_code, course_number FROM topfifteen, attended";
	
	private String dropview3 = "DROP VIEW IF EXISTS newpossible";
	
	private String NewPossible = "CREATE VIEW newpossible AS "
										+ "SELECT username, dept_code, course_number "
										+ "FROM topfifteen, new_course";
	
	private String dropview4 = "DROP VIEW IF EXISTS tfcourses";
	
	private String TFCourses = "CREATE VIEW tfcourses AS "
										+ "SELECT DISTINCT t3.username, t1.dept_code, t1.course_number "
										+ "FROM courses t1, course_editions t2, enrollments t3, topfifteen t4 "
										+ "WHERE t3.username = t4.username AND t3.edition_id = t2.edition_id "
										+ "AND t2.course_id = t1.course_id";
	
	private String dropview5 = "DROP VIEW IF EXISTS tfc_except_old";
	
	private String TFCExceptOld = "CREATE VIEW tfc_except_old AS "
										+ "SELECT * FROM tfcourses "
										+ "EXCEPT "
										+ "SELECT * FROM oldpossible";
	
	private String dropview6 = "DROP VIEW IF EXISTS tfc_except_new";
	
	private String TFCExceptNew = "CREATE VIEW tfc_except_new AS "
										+ "SELECT * FROM tfc_except_old "
										+ "EXCEPT "
										+ "SELECT * FROM newpossible";
	
	private String dropview7 = "DROP VIEW IF EXISTS tf_courseid";
	
	private String TFCourseId = "CREATE VIEW tf_courseid AS "
										+ "SELECT DISTINCT username, t1.course_id "
										+ "FROM tfc_except_new t2, courses t1 "
										+ "WHERE t1.dept_code = t2.dept_code AND t1.course_number = t2.course_number";
	
	private String dropview8 = "DROP VIEW IF EXISTS tf_edition";
	
	private String TF_Edition = "CREATE VIEW tf_edition AS "
									+ "SELECT t1.username, t2.edition_id, t2.letter_grade, t2.course_ranking "
									+ "FROM topfifteen t1, enrollments t2 "
									+ "WHERE t1.username = t2.username";
			
	private String dropview9 = "DROP VIEW IF EXISTS tf_ce";
	
	private String TF_CE = "CREATE VIEW tf_ce AS "
								+ "SELECT t1.username, t1.edition_id, t2.course_id, t1.letter_grade, t1.course_ranking "
								+ "FROM tf_edition t1, course_editions t2 "
								+ "WHERE t1.edition_id = t2.edition_id";
	
	private String dropview10 = "DROP VIEW IF EXISTS tf_final";
	
	private String TF_FINAL = "CREATE VIEW tf_final AS "
								+ "SELECT t1.username, t1.edition_id, t1.course_id, t1.letter_grade, t1.course_ranking "
								+ "FROM tf_ce t1, tf_courseid t2 "
								+ "WHERE t1.username = t2.username AND t1.course_id = t2.course_id";
	
	private String dropview11 = "DROP VIEW IF EXISTS tf_skill";
	
	/*------------------------------------------------------------------
	 * This query is to test all skill rank value before sum
	CREATE VIEW tf_skill AS
	SELECT t1.username, t1.edition_id, t1.course_id, t2.skill_id,
	(t2.rank_after - t2.rank_before) as skill_improvement
	FROM tf_final t1, skill_rankings t2
	WHERE t1.username = t2.username AND t1.edition_id = t2.edition_id
	AND t1.course_id = t2.course_id;
	-------------------------------------------------------------------*/
	
	
	private String TF_SKILL = "CREATE VIEW tf_skill AS "
									+ "SELECT t1.username, t1.course_id, "
									+ "sum((t2.rank_after - t2.rank_before)) as skill_improvement "
									+ "FROM tf_final t1, skill_rankings t2 "
									+ "WHERE t1.username = t2.username AND t1.edition_id = t2.edition_id "
									+ "AND t1.course_id = t2.course_id "
									+ "GROUP BY t1.username, t1.course_id";
	
	private String dropview12 = "DROP VIEW IF EXISTS tf_interest";
	
	/*------------------------------------------------------------------
	 * This query is to test all interest rank value before sum
	CREATE VIEW tf_interest AS
	SELECT t1.username, t1.edition_id, t1.course_id,
	t2.topic_id, (t2.interest_after - t2.interest_before) as interest_increasing
	FROM tf_final t1, topic_interests t2
	WHERE t1.username = t2.username AND t1.edition_id = t2.edition_id
	AND t1.course_id = t2.course_id;
	-------------------------------------------------------------------*/
	
	private String TF_INTEREST = "CREATE VIEW tf_interest AS "
									+ "SELECT t1.username, t1.course_id, "
									+ "sum((t2.interest_after - t2.interest_before)) as interest_increasing "
									+ "FROM tf_final t1, topic_interests t2 "
									+ "WHERE t1.username = t2.username AND t1.edition_id = t2.edition_id "
									+ "AND t1.course_id = t2.course_id "
									+ "GROUP BY t1.username, t1.course_id";
	
	public FindRecomCourses(Connection conn, String username)throws SQLException{
		this.connection = conn;
		this.username = username;
		
		String findOldAttendedCourse = "CREATE VIEW attended AS "
				+ "SELECT DISTINCT t1.dept_code, t1.course_number FROM courses t1, course_editions t2, enrollments t3 "
				+ "WHERE t3.username = '" + username + "' and t3.edition_id = t2.edition_id and t2.course_id = t1.course_id";
		
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement stmt8 = null;
		PreparedStatement stmt9 = null;
		PreparedStatement stmt10 = null;
		PreparedStatement stmt11 = null;
		PreparedStatement stmt12 = null;
		PreparedStatement stmt13 = null;
		PreparedStatement stmt14 = null;
		PreparedStatement stmt15 = null;
		PreparedStatement stmt16 = null;
		PreparedStatement stmt17 = null;
		PreparedStatement stmt18 = null;
		PreparedStatement stmt19 = null;
		PreparedStatement stmt20 = null;
		PreparedStatement stmt21 = null;
		PreparedStatement stmt22 = null;
		PreparedStatement stmt23 = null;
		PreparedStatement stmt24 = null;
		PreparedStatement stmt25 = null;
		PreparedStatement stmt26 = null;
		
		try{
			stmt1 = conn.prepareStatement(dropview);
			stmt1.execute();
			stmt2 = conn.prepareStatement(topfifteen);
			stmt2.execute();
			stmt3 = conn.prepareStatement(dropview1);
			stmt3.execute();
			stmt4 = conn.prepareStatement(findOldAttendedCourse);
			stmt4.execute();
			stmt5 = conn.prepareStatement(dropview2);
			stmt5.execute();
			stmt6 = conn.prepareStatement(OldPossible);
			stmt6.execute();
			stmt7 = conn.prepareStatement(dropview3);
			stmt7.execute();
			stmt8 = conn.prepareStatement(NewPossible);
			stmt8.execute();
			stmt9 = conn.prepareStatement(dropview4);
			stmt9.execute();
			stmt10 = conn.prepareStatement(TFCourses);
			stmt10.execute();
			stmt11 = conn.prepareStatement(dropview5);
			stmt11.execute();
			stmt12 = conn.prepareStatement(TFCExceptOld);
			stmt12.execute();
			stmt13 = conn.prepareStatement(dropview6);
			stmt13.execute();
			stmt14 = conn.prepareStatement(TFCExceptNew);
			stmt14.execute();
			stmt15 = conn.prepareStatement(dropview7);
			stmt15.execute();
			stmt16 = conn.prepareStatement(TFCourseId);
			stmt16.execute();
			stmt17 = conn.prepareStatement(dropview8);
			stmt17.execute();
			stmt18 = conn.prepareStatement(TF_Edition);
			stmt18.execute();
			stmt19 = conn.prepareStatement(dropview9);
			stmt19.execute();
			stmt20 = conn.prepareStatement(TF_CE);
			stmt20.execute();
			stmt21 = conn.prepareStatement(dropview10);
			stmt21.execute();
			stmt22 = conn.prepareStatement(TF_FINAL);
			stmt22.execute();
			stmt23 = conn.prepareStatement(dropview11);
			stmt23.execute();
			stmt24 = conn.prepareStatement(TF_SKILL);
			stmt24.execute();
			stmt25 = conn.prepareStatement(dropview12);
			stmt25.execute();
			stmt26 = conn.prepareStatement(TF_INTEREST);
			stmt26.execute();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	
}
