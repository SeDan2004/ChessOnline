package ChessOnline.Classes;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.stereotype.Component;

@Component
public class Database {
    
	public Connection conn;
	
    private String URL;
    private String LOGIN;
    private String PASS;
    
    public Database() {
        try {
        	URL = "jdbc:postgresql://localhost:5432/chess_online";
        	LOGIN = "postgres";
        	PASS = "root";
        	
        	conn = getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private Connection getConnection() throws Exception {
    	Class.forName("org.postgresql.Driver")
    	     .getDeclaredConstructor()
    	     .newInstance();
    	
    	return DriverManager.getConnection(URL, LOGIN, PASS);
    }
}