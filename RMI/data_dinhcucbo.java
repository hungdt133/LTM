package RMI;

import java.rmi.registry.*;
import java.util.*;

public class data_dinhcucbo {

    public static void main(String[] args) throws Exception {

        // =========================
        // Kết nối RMI
        // =========================
        Registry rg = LocateRegistry.getRegistry("36.50.135.242", 1099);

        DataService sv = (DataService) rg.lookup("RMIDataService");

        String studentCode = "B22DCDT133";
        String qCode = "xW6eEjWq";

        // =========================
        // Nhận dữ liệu
        // =========================
        int[] a = (int[]) sv.requestData(studentCode, qCode);

        List<Integer> res = new ArrayList<>();

        int n = a.length;

        // =========================
        // Tìm đỉnh cục bộ
        // =========================

        for (int i = 0; i < n; i++) {

            // phần tử đầu
            if (i == 0) {
                if (n > 1 && a[i] > a[i + 1]) {
                    res.add(i + 1);
                }
            }

            // phần tử cuối
            else if (i == n - 1) {
                if (a[i] > a[i - 1]) {
                    res.add(i + 1);
                }
            }

            // phần tử giữa
            else {
                if (a[i] > a[i - 1] && a[i] > a[i + 1]) {
                    res.add(i + 1);
                }
            }
        }

        // =========================
        // In kết quả
        // =========================
        System.out.println(res);

        // =========================
        // Gửi kết quả
        // =========================
        sv.submitData(studentCode, qCode, res);
    }
}
