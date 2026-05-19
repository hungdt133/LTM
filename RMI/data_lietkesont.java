package RMI;

import java.rmi.registry.*;
import java.util.*;

public class data_lietkesont {

    public static int check(int n){

        if(n < 2){
            return 0;
        }

        for(int i = 2; i * i <= n; i++){

            if(n % i == 0){
                return 0;
            }
        }

        return 1;
    }

    public static void main(String[] args) throws Exception {

        // =========================
        // Kết nối RMI
        // =========================

        Registry rg =
                LocateRegistry.getRegistry(
                        "36.50.135.242",
                        1099
                );

        DataService sv =
                (DataService) rg.lookup(
                        "RMIDataService"
                );

        // =========================
        // Nhận dữ liệu
        // =========================

        String data =
                (String) sv.requestData(
                        "B22DCDT133",
                        "vlHtAD5l"
                );

        System.out.println(data);

        String[] parts =
                data.split(";");

        int start =
                Integer.parseInt(parts[0]);

        int end =
                Integer.parseInt(parts[1]);

        // =========================
        // Tìm số nguyên tố
        // =========================

        List<Integer> res =
                new ArrayList<>();

        for(int i = start; i <= end; i++){

            if(check(i) == 1){
                res.add(i);
            }
        }

        System.out.println(res);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitData(
                "B22DCDT133",
                "vlHtAD5l",
                res
        );
    }
}
