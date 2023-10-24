package ChessOnline.Classes;

import org.springframework.stereotype.Component;

@Component("Person")
public class Person {
	
	private int id;
    private String login;
    private String password;
    private int rating;
    
    {
    	rating = 0;
    }
    
    public int getId() {
        return id;
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
}