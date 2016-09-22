package CourseRecom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import CourseRecom.SQLError;

import java.util.ArrayList;
import java.util.List;

public class Department {
	private static final String TABLE_NAME = "course";

	private String getDept = "SELECT * FROM departments";
	
	private String dept_code,dept_name;
	
	//get all department
	public List<Department> getAllDept(Connection con)throws SQLException{
		List<Department> dept = new ArrayList<Department>();
		PreparedStatement stmt = null;
		
		try{
			stmt = con.prepareStatement(getDept);
			ResultSet res = stmt.executeQuery();
			
			while(res.next()){
				Department department = new Department();
				department.setCode(res.getString("dept_code"));
				department.setName(res.getString("dept_name"));
				dept.add(department);
			}
		}catch(SQLException e){
	    	SQLError.show(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
		}
		return dept;
	}
	public String getCode(){
		return this.dept_code;
	}
	public String getName(){
		return this.dept_name;
	}
	
	public void setCode(String dept_code){
		this.dept_code = dept_code;
	}
	public void setName(String dept_name){
		this.dept_name = dept_name;
	}
}
