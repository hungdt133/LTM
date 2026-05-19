package RMI;

import java.rmi.registry.*;

public class Character_caesar{
    public static void main(String[] args) throws Exception{

        Registry registry = LocateRegistry.getRegistry("36.50.135.242",1099);

        CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

        String studentCode = "B22DCAT134";
        String qCode = "H4661dEu";

        // request
        String s = service.requestCharacter(studentCode,qCode);

        System.out.println(s);

        // giai ma Caesar
        int shift = s.length() % 7;

        String ans = "";

        for(char x : s.toCharArray()){

            if(x >= 'A' && x <= 'Z'){
                ans += (char)((x - 'A' - shift + 26) % 26 + 'A');
            }
            else if(x >= 'a' && x <= 'z'){
                ans += (char)((x - 'a' - shift + 26) % 26 + 'a');
            }
            else{
                ans += x;
            }
        }

        System.out.println(ans);

        // submit
        service.submitCharacter(studentCode,qCode,ans);

        System.out.println("Done");
    }
}
