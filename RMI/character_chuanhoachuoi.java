package RMI;

import java.rmi.registry.*;

public class character_chuanhoachuoi{

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

        String data =
                sv.requestCharacter(
                        "B22DCDT133",
                        "f1R9fi1x"
                );

        System.out.println(data);

        // =========================
        // Chuẩn hóa chuỗi
        // =========================

        String result =
                data.toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitCharacter(
                "B22DCDT133",
                "f1R9fi1x",
                result
        );
    }
}
