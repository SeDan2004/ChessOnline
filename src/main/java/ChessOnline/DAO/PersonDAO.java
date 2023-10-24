package ChessOnline.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ChessOnline.Classes.Database;
import ChessOnline.Classes.Person;

@Component
public class PersonDAO {
    
	@Autowired
    private Database db;
		
	public boolean checkUser(String login, String password) throws Exception {
		String sql;
		Connection conn;
	    PreparedStatement statement;
        ResultSet resSet;
	    
	    sql = "SELECT * FROM \"Users\" WHERE login = ? AND password = ?";
	    conn = db.conn;
	    statement = conn.prepareStatement(sql);
	    
	    statement.setString(1, login);
	    statement.setString(2, password);
	    
	    resSet = statement.executeQuery();
	    
	    return resSet.next();
	}
	
	public void createUser(Person person) throws Exception {
	    String login = person.getLogin();
	    String password = person.getPassword();
	    boolean isUserExist = checkUser(login, password);
	    
	    if (!isUserExist) {
	        Connection conn;
	        PreparedStatement statement;
	        String sql;
	        
	        sql = "INSERT INTO \"Users\" (login, password) VALUES (?, ?)";
	        conn = db.conn;
	        statement = conn.prepareStatement(sql);
	        
	        statement.setString(1, login);
	        statement.setString(2, password);
	        
	        statement.execute();
	    } else {
	        throw new UserExists("Пользователь существует!");
	    }
	}
	
	public Person checkUserInDb(String login, String password) throws Exception {
	    boolean isUserExists = checkUser(login, password);
	    
	    if (isUserExists) {
	        String sql;
	        Person person;
	        Connection conn;
	        PreparedStatement statement;
	        ResultSet resSet;
	        
	        sql = "SELECT id, rating FROM \"Users\" "
	        	  + "WHERE login = ? AND password = ?";
	        
	        person = new Person();
	        conn = db.conn;
	        statement = conn.prepareStatement(sql);
	        
	        statement.setString(1, login);
	        statement.setString(2, password);
	        
	        resSet = statement.executeQuery();
	        
	        while (resSet.next()) {
	            person.setId(resSet.getInt("id"));
	            person.setRating(resSet.getInt("rating"));
	        }
	        
	        person.setLogin(login);
	        person.setPassword(password);
	        
	        return person;
	    } else {
	        throw new UserNotExists("Пользователь не существует!");
	    }
	}
}

class UserExists extends Exception {
    UserExists(String msg) {
    	super(msg);
    }
}

class UserNotExists extends Exception {
	UserNotExists(String msg) {
	    super(msg);
	}
}