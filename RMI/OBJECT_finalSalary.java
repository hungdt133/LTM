package RMI;

import java.rmi.registry.*;
import java.io.Serializable;

public class OBJECT_finalSalary  {

    // =========================
    // SUM OF DIGITS
    // =========================
    public static int sumDigits(int n) {
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    // =========================
    // COUNT DIVISORS
    // =========================
    public static int countDivisors(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) count++;
        }
        return count;
    }

    public static void main(String[] args) throws Exception {

        // =========================
        // CONNECT RMI
        // =========================
        Registry rg =
                LocateRegistry.getRegistry("36.50.135.242", 1099);

        ObjectService sv =
                (ObjectService) rg.lookup("RMIObjectService");

        String studentCode = "B22DCDT133";
        String qCode = "DNMtrERe";

        // =========================
        // REQUEST OBJECT
        // =========================
        Employee e =
                (Employee) sv.requestObject(studentCode, qCode);

        System.out.println("Before salary = " + e.getFinalSalary());

        int years = e.getExperienceYears();

        // =========================
        // CALCULATE FACTOR
        // =========================
        int digitSum = sumDigits(years);
        int divisorCount = countDivisors(years);

        double factor =
                (years + digitSum + divisorCount) / 100.0;

        // =========================
        // FINAL SALARY
        // =========================
        double finalSalary =
                e.getBaseSalary() * (1 + factor);

        e.setFinalSalary(finalSalary);

        System.out.println("Final salary = " + finalSalary);

        // =========================
        // SUBMIT OBJECT
        // =========================
        sv.submitObject(studentCode, qCode, e);

        System.out.println("Done");
    }
}