package ChessOnline.Classes;

import org.springframework.stereotype.Component;

@Component
public class Caesar {
	
    public String action(String value, String type) {
        int offset = 125;
    	String res = "";
           	
        if (type == "decrypt") {
            offset = -offset;
        }
        
        for (int i = 0; i < value.length(); i++) {
            int chrNum = value.charAt(i);
            char chrWithOffset = (char) (chrNum + offset);
            
            res += chrWithOffset;
        }
        
        return res;
    }
}