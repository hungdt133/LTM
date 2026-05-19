package RMI;

import java.rmi.registry.*;

public class character_error_warn_info {

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
                        "k2AxnY6g"
                );

        System.out.println(data);

        // =========================
        // Đếm log
        // =========================

        int error = 0;

        int info = 0;

        int warn = 0;

        String[] logs =
                data.split("\\|");

        for(String log : logs){

            log = log.trim();

            if(log.startsWith("ERROR")){
                error++;
            }
            else if(log.startsWith("INFO")){
                info++;
            }
            else if(log.startsWith("WARN")){
                warn++;
            }
        }

        String result =
                "ERROR="
                + error
                + ";INFO="
                + info
                + ";WARN="
                + warn;

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitCharacter(
                "B22DCDT133",
                "k2AxnY6g",
                result
        );
    }
}
