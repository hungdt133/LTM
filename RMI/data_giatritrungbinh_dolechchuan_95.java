package RMI;

import java.rmi.registry.*;
import java.util.*;
import java.text.DecimalFormat;

public class data_giatritrungbinh_dolechchuan_95 {

    public static void main(String[] args) throws Exception {

        // =========================
        // RMI CONNECT
        // =========================
        Registry rg = LocateRegistry.getRegistry("36.50.135.242", 1099);

        DataService sv = (DataService) rg.lookup("RMIDataService");

        String studentCode = "B22DCDT133";
        String qCode = "JgeOP73l";

        // =========================
        // REQUEST DATA
        // =========================
        String data = (String) sv.requestData(studentCode, qCode);

        String[] parts = data.split(",");

        int n = parts.length;
        double[] a = new double[n];

        double sum = 0;

        for (int i = 0; i < n; i++) {
            a[i] = Double.parseDouble(parts[i]);
            sum += a[i];
        }

        // =========================
        // AVERAGE
        // =========================
        double avg = sum / n;

        // =========================
        // STD DEV
        // =========================
        double variance = 0;

        for (double x : a) {
            variance += (x - avg) * (x - avg);
        }

        variance /= n;
        double stddev = Math.sqrt(variance);

        // =========================
        // P95
        // =========================
        Arrays.sort(a);

        int idx = (int) Math.ceil(n * 0.95) - 1;
        if (idx < 0) idx = 0;
        if (idx >= n) idx = n - 1;

        double p95 = a[idx];

        // =========================
        // FORMAT 2 DECIMAL
        // =========================
        DecimalFormat df = new DecimalFormat("0.00");

        String result =
                "average=" + df.format(avg) +
                ";stddev=" + df.format(stddev) +
                ";p95=" + df.format(p95);

        System.out.println(result);

        // =========================
        // SUBMIT
        // =========================
        sv.submitData(studentCode, qCode, result);
    }
}