package CourseRecom;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import CourseRecom.DBConnection;

public class MainClass {
	public static final String PROGRAM_NAME = "Main Function";

	public static void main(String[] args)throws IOException,SQLException{
		
		if (args.length == 0){
    		System.out.println("Usage: "+PROGRAM_NAME+" <name of properties file>");
    		System.exit(1);
    	}
		
		Properties props = new Properties();
    	FileInputStream in = new FileInputStream(args[0]);
    	props.load(in);
    	in.close();
    	java.sql.Connection conn = DBConnection.getConnection (props);	

		conn.createStatement().execute("PRAGMA foreign_keys = ON");

		if (conn == null) System.exit(1);
		
		Login_Interface app = new Login_Interface(conn);
    	app.setSize(300, 500);
    	app.setLocationRelativeTo(null);
        app.setVisible(true);
		
		
	}
}
