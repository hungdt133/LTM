package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class byte_bytekdauxuathiennhieunhat {

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
                "gyvMKSga";

        // =========================
        // Nhận dữ liệu
        // =========================

        byte[] data =
                service.requestData(
                        studentCode,
                        qCode
                );

        // =========================
        // Đếm tần suất
        // =========================

        int[] cnt = new int[256];

        for(byte b : data){

            int value = b & 0xff;

            cnt[value]++;
        }

        int value = 0;

        int maxFreq = 0;

        for(byte b : data){

            int x = b & 0xff;

            if(cnt[x] > maxFreq){

                maxFreq = cnt[x];

                value = x;
            }
        }

        String result =
                value + ":" + maxFreq;

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
