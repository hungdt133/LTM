package RMI;

import java.rmi.registry.*;
import java.util.*;

public class data_average_p95{

    public static void main(String[] args) throws Exception{

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
                        "nh3drAcw"
                );

        System.out.println(data);

        String[] arr =
                data.split(",");

        ArrayList<Double> a =
                new ArrayList<>();

        double sum = 0;

        for(String x : arr){

            double value =
                    Double.parseDouble(x);

            a.add(value);

            sum += value;
        }

        // =========================
        // average
        // =========================

        double average =
                sum / a.size();

        average =
                Math.round(average * 100.0)
                / 100.0;

        // =========================
        // p95
        // =========================

        Collections.sort(a);

        int idx =
                (int)Math.ceil(
                        a.size() * 0.95
                ) - 1;

        double p95 =
                a.get(idx);

        p95 =
                Math.round(p95 * 100.0)
                / 100.0;

        // =========================
        // aboveAvg
        // =========================

        int aboveAvg = 0;

        for(double x : a){

            if(x > average){
                aboveAvg++;
            }
        }

        // =========================
        // Kết quả
        // =========================

        String result =
                String.format(
                        "%.2f;%.2f;%d",
                        average,
                        p95,
                        aboveAvg
                );

        System.out.println(result);

        // =========================
        // Gửi kết quả
        // =========================

        sv.submitData(
                "B22DCDT133",
                "nh3drAcw",
                result
        );
    }
}
