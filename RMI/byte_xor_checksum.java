package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class byte_xor_checksum {

    public static void main(String[] args) throws Exception{

        Registry registry =
                LocateRegistry.getRegistry(
                        "36.50.135.242",
                        1099
                );

        ByteService service =
                (ByteService) registry.lookup(
                        "RMIByteService"
                );

        String studentCode =
                "B22DCDT133";

        String qCode =
                "HTeXh21F";

        // =========================
        // Nhận dữ liệu
        // =========================

        byte[] data =
                service.requestData(
                        studentCode,
                        qCode
                );

        // =========================
        // Tính XOR
        // =========================

        int xor = 0;

        for(byte b : data){

            xor ^= (b & 0xff);
        }

        String result =
                String.valueOf(xor);

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        service.submitData(
                studentCode,
                qCode,
                result.getBytes("UTF-8")
        );
    }
}
