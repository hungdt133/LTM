package RMI;

import java.rmi.registry.*;

public class byte_checksum_mod100000 {

    public static void main(String[] args) throws Exception{

        Registry rg =
                LocateRegistry.getRegistry(
                        "36.50.135.242",
                        1099
                );

        ByteService sv =
                (ByteService) rg.lookup(
                        "RMIByteService"
                );

        // =========================
        // Nhận dữ liệu
        // =========================

        byte[] data =
                sv.requestData(
                        "B22DCDT133",
                        "EEtD0BuE"
                );

        // =========================
        // Tính checksum
        // =========================

        int checksum = 0;

        int evenBytes = 0;

        for(byte b : data){

            int value = b & 0xff;

            checksum =
                    (checksum * 31 + value)
                    % 100000;

            if(value % 2 == 0){
                evenBytes++;
            }
        }

        String result =
                "checksum="
                + checksum
                + ";evenBytes="
                + evenBytes;

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitData(
                "B22DCDT133",
                "EEtD0BuE",
                result.getBytes("UTF-8")
        );
    }
}
