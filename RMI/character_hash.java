package RMI;

import java.rmi.registry.*;
import java.util.*;

public class character_hash {

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
        // Nhận dữ liệu
        // =========================

        String s =
                sv.requestCharacter(
                        "B22DCDT133",
                        "lg4lx04q"
                );

        System.out.println(s);

        // =========================
        // Chuẩn hóa
        // =========================

        s = s.toLowerCase();

        s = s.replaceAll(
                "[^a-z0-9 ]",
                " "
        );

        s = s.replaceAll(
                "\\s+",
                " "
        ).trim();

        System.out.println(s);

        // =========================
        // Tách từ
        // =========================

        String[] arr =
                s.split(" ");

        int words =
                arr.length;

        // =========================
        // Tìm từ dài nhất
        // Nếu bằng nhau lấy từ nhỏ hơn
        // =========================

        String longest = "";

        for(String x : arr){

            if(
                    x.length() > longest.length()
            ){
                longest = x;
            }
            else if(
                    x.length() == longest.length()
                    &&
                    x.compareTo(longest) < 0
            ){
                longest = x;
            }
        }

        // =========================
        // Tính hash
        // =========================

        int hash = 0;

        for(char c : s.toCharArray()){

            hash =
                    (hash + c)
                    % 10000;
        }

        // =========================
        // Kết quả
        // =========================

        String result =
                "words="
                + words
                + ";longest="
                + longest
                + ";hash="
                + hash;

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitCharacter(
                "B22DCDT133",
                "lg4lx04q",
                result
        );
    }
}
