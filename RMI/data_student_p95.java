package RMI;

import java.rmi.registry.*;
import java.util.*;

public class data_student_p95 {

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

        String data =
                (String) sv.requestData(
                        "B22DCAT134",
                        "FSoIf2vb"
                );

        System.out.println(data);

        String[] arr = data.split(",");

        ArrayList<Double> a =
                new ArrayList<>();

        for(String x : arr){

            a.add(
                    Double.parseDouble(
                            x.trim()
                    )
            );
        }

        // =========================
        // average
        // =========================

        double sum = 0;

        for(double x : a){
            sum += x;
        }

        double avg = sum / a.size();

        // =========================
        // stddev
        // =========================

        double variance = 0;

        for(double x : a){

            variance +=
                    Math.pow(x - avg, 2);
        }

        variance /= a.size();

        double stddev =
                Math.sqrt(variance);

        // =========================
        // p95
        // =========================

        Collections.sort(a);

        int idx =
                (int)Math.ceil(
                        a.size() * 0.95
                ) - 1;

        double p95 = a.get(idx);

        // =========================
        // round 2
        // =========================

        avg =
                Math.round(avg * 100.0)
                / 100.0;

        stddev =
                Math.round(stddev * 100.0)
                / 100.0;

        p95 =
                Math.round(p95 * 100.0)
                / 100.0;

        String result =
                "average="
                + String.format("%.2f", avg)
                + ";stddev="
                + String.format("%.2f", stddev)
                + ";p95="
                + String.format("%.2f", p95);

        System.out.println(result);

        sv.submitData(
                "B22DCAT134",
                "FSoIf2vb",
                result
        );
    }
}
