package CourseRecom;

public class Letter_Grades {
	private static final String TABLE_NAME = "letter_grades";
	
	private String letter_grade;
	private int min_grade, max_grade;
	private double gpv;
	
	public void setLetterGrade(String letter_grade){
		this.letter_grade = letter_grade;
	}
	
	public void setMinGrade(int min_grade){
		this.min_grade = min_grade;
	}
	
	public void setMaxGrade(int max_grade){
		this.max_grade = max_grade;
	}
	
	public void setGpv(double gpv){
		this.gpv = gpv;
	}
	
	public String setLetterGrade(){
		return this.letter_grade;
	}
	
	public int setMinGrade(){
		return this.min_grade;
	}
	
	public int setMaxGrade(){
		return this.max_grade;
	}
	
	public double setGpv(){
		return this.gpv;
	}

}
