package RMI;

import java.rmi.registry.*;

public class character_Vigenere {
    public static void main(String[] args) throws Exception{

        Registry registry = LocateRegistry.getRegistry("36.50.135.242",1099);

        CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

        String studentCode = "B22DCDT133";
        String qCode = "6IFs8HX5";

        // request
        String s = service.requestCharacter(studentCode,qCode);

        System.out.println(s);

        // tach du lieu
        String[] parts = s.split(";");

        String key = parts[0];
        String text = parts[1];

        // ma hoa Vigenere
        String ans = "";

        for(int i = 0;i < text.length();i++){

            char t = text.charAt(i);

            char k = key.charAt(i % key.length());

            int shift = Character.toUpperCase(k) - 'A';

            if(t >= 'A' && t <= 'Z'){
                ans += (char)((t - 'A' + shift) % 26 + 'A');
            }
            else if(t >= 'a' && t <= 'z'){
                ans += (char)((t - 'a' + shift) % 26 + 'a');
            }
            else{
                ans += t;
            }
        }

        System.out.println(ans);

        // submit
        service.submitCharacter(studentCode,qCode,ans);

        System.out.println("Done");
    }
}
