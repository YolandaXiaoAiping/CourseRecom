package CourseRecom;

import java.sql.*;

import CourseRecom.SQLError;

public class Students {
	private int permission,age;
	private String username,gender,native_country;
	private String check = "select * from students where username =?";
	private String insertNew = "insert into students values(?,NULL,?,?,?)";
	
	PreparedStatement stmt = null;
	
	public boolean checkUserid(Connection conn)throws SQLException{
		ResultSet rs = null;
		boolean res = false;
		try{
			stmt = conn.prepareStatement(check);
			stmt.setString(1, this.username);
			rs = stmt.executeQuery();
			if(!rs.next()) res = false;
			else res = true;
		} catch (SQLException e){
			SQLError.show(e);
		} finally{
			stmt.close();
		}
		return res;
	}
	
	//insert new user info into the database
	public void insertNewUser(Connection conn)throws SQLException{
		try{
			stmt = conn.prepareStatement(insertNew);
			stmt.setString(1,this.username);
			stmt.setInt(2,this.age);
			stmt.setString(3,this.gender);
			stmt.setString(4,this.native_country);
			stmt.execute();
		}catch (SQLException e){
			SQLError.show(e);
		}
	}
	
	public int getPermission(){
		return this.permission;
	}
	public int getAge(){
		return this.age;
	}
	public String getUsername(){
		return this.username;
	}
	public String getGender(){
		return this.gender;
	}
	public String getNative_country(){
		return this.native_country;
	}
	
	public void setPermission(int permission){
		this.permission = permission;
	}
	public void setAge(int age){
		this.age = age;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setGender(String gender){
		this.gender = gender;
	}
	public void setNative_country(String native_country){
		this.native_country = native_country;
	}
}
