package RMI;

import java.rmi.registry.*;
import java.util.*;

public class byte_3giatritansuatcaonhat {

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
                        "DBCskxCL"
                );

        // =========================
        // Đếm tần suất
        // =========================

        int[] cnt =
                new int[256];

        for(byte b : data){

            int value = b & 0xff;

            cnt[value]++;
        }

        ArrayList<int[]> list =
                new ArrayList<>();

        for(int i = 0; i < 256; i++){

            if(cnt[i] > 0){

                list.add(
                        new int[]{
                                i,
                                cnt[i]
                        }
                );
            }
        }

        Collections.sort(
                list,
                (a, b) -> {

                    if(a[1] != b[1]){
                        return b[1] - a[1];
                    }

                    return a[0] - b[0];
                }
        );

        // =========================
        // Lấy tối đa 3 giá trị
        // =========================

        String result = "";

        int limit =
                Math.min(3, list.size());

        for(int i = 0; i < limit; i++){

            int value =
                    list.get(i)[0];

            int freq =
                    list.get(i)[1];

            result +=
                    value
                    + "="
                    + freq
                    + "|";
        }

        result =
                result.substring(
                        0,
                        result.length() - 1
                );

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitData(
                "B22DCDT133",
                "DBCskxCL",
                result.getBytes("UTF-8")
        );
    }
}
