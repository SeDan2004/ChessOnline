package ChessOnline.Controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ChessOnline.Classes.Caesar;
import ChessOnline.Classes.Person;
import ChessOnline.DAO.PersonDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/", produces="text/plain;charset=UTF-8")
public class AuthAndRegController {
	
	@Autowired
	private PersonDAO personDao;
	
	@Autowired
	private Caesar caesar;
	
	private HttpSession session;
	
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
    	session = request.getSession();
    	Map<String, String> cookies = List.of(request.getCookies())
            .stream()
        	.collect(Collectors.toMap(c -> c.getName(), c -> c.getValue()));
                
        if (cookies.containsKey("login") && cookies.containsKey("password")) {
            try {
            	String login;
            	String pass;
            	Person person;
            	
            	login = cookies.get("login");
            	pass = cookies.get("password");
            	
            	login = caesar.action(login, "decrypt");
                pass = caesar.action(pass, "decrypt");
                person = personDao.checkUserInDb(login, pass);
                
                session.setAttribute("person", person);
                
                return "redirect:/lobby";
            } catch (Exception ex) {
                return "index";
            }
        } else {
        	return "index";
        }
    }
    
    @GetMapping("/lobby")
    public String lobby(Model model, HttpServletRequest request) {
    	session = request.getSession();
    	Person person = (Person) session.getAttribute("person");
    	
    	model.addAttribute("person", person);
    	
    	return "lobby";
    }
	
    @PostMapping("/reg")
    @ResponseBody
    public String reg(@ModelAttribute("person") Person person,
                      HttpServletRequest request) {
        
    	try {
        	session = request.getSession();
        	
            personDao.createUser(person);
            session.setAttribute("person", person);
            
            return "";
        } catch (Exception ex) {
            return "error: " + ex.getMessage();
        }
    }
    
    @PostMapping("/auth")
    @ResponseBody
    public String auth(@RequestParam("login") String login,
            @RequestParam("password") String pass,
            @RequestParam("rememberMe") boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response) {
    	
        try {
        	session = request.getSession();
            Person person = personDao.checkUserInDb(login, pass);
            
            session.setAttribute("person", person);
            
            if (rememberMe) {
                Cookie loginCookie;
                Cookie passCookie;
            	
                login = caesar.action(login, "encrypt");
                pass = caesar.action(pass, "encrypt");
                
                loginCookie = new Cookie("login", login);
                passCookie = new Cookie("password", pass);
                
                loginCookie.setMaxAge(3600 * 24 * 7);
                passCookie.setMaxAge(3600 * 24 * 7);
                
                response.addCookie(loginCookie);
                response.addCookie(passCookie);
            }
            
            return "";
        } catch(Exception ex) {
            return "error: " + ex.getMessage();
        }
    }
}