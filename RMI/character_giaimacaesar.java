package RMI;

import java.rmi.registry.*;

public class character_giaimacaesar {

    public static void main(String[] args) throws Exception{

        Registry rg =
                LocateRegistry.getRegistry(
                        "36.50.135.242",
                        1099
                );

        CharacterService sv =
                (CharacterService) rg.lookup(
                        "RMICharacterService"
                );

        // =========================
        // Nhận chuỗi
        // =========================

        String s =
                sv.requestCharacter(
                        "B22DCDT133",
                        "74M3dh3u"
                );

        System.out.println(s);

        // =========================
        // Giải mã Caesar
        // =========================

        int shift =
                s.length() % 7;

        String result = "";

        for(char c : s.toCharArray()){

            if(c >= 'a' && c <= 'z'){

                result +=
                        (char)(
                                (c - 'a' - shift + 26)
                                % 26
                                + 'a'
                        );
            }
            else if(c >= 'A' && c <= 'Z'){

                result +=
                        (char)(
                                (c - 'A' - shift + 26)
                                % 26
                                + 'A'
                        );
            }
            else{

                result += c;
            }
        }

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitCharacter(
                "B22DCAT134",
                "H4661dEu",
                result
        );
    }
}
