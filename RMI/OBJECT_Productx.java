package RMI;

import java.rmi.registry.*;

public class OBJECT_Productx {

    public static void main(String[] args) throws Exception {

        // =========================
        // CONNECT RMI
        // =========================
        Registry rg =
                LocateRegistry.getRegistry("36.50.135.242", 1099);

        ObjectService sv =
                (ObjectService) rg.lookup("RMIObjectService");

        String studentCode = "B22DCDT133";
        String qAlias = "1fyCUzkG";

        // =========================
        // REQUEST OBJECT
        // =========================
        ProductX p =
                (ProductX) sv.requestObject(studentCode, qAlias);

        System.out.println("Before: " + p.getDiscountCode());

        // =========================
        // TÍNH TỔNG CHỮ SỐ
        // =========================
        String code = p.getDiscountCode();

        int sum = 0;

        for (int i = 0; i < code.length(); i++) {

            char c = code.charAt(i);

            if (Character.isDigit(c)) {
                sum += c - '0';
            }
        }

        // =========================
        // UPDATE OBJECT
        // =========================
        p.setDiscount(sum);

        System.out.println("Discount = " + sum);

        // =========================
        // SUBMIT OBJECT
        // =========================
        sv.submitObject(studentCode, qAlias, p);

        System.out.println("Done");
    }
}