package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OBJECT_REVIEW_1500 {

    public static void main(String[] args) {

        String studentCode = "B22DCDT133";
        String qCode = "qteqqlIl";

        try {
            // Kết nối Registry Server
            Registry registry =
                    LocateRegistry.getRegistry("36.50.135.242", 1099);

            // Lookup service
            ObjectService service =
                    (ObjectService) registry.lookup("RMIObjectService");

            // Nhận object từ server
            OrderTotal order =
                    (OrderTotal) service.requestObject(studentCode, qCode);

            // Tính total
            double total =
                    order.getItemsSubtotal()
                    * (1 - order.getDiscountRate() / 100.0)
                    + order.getShippingFee();

            // Làm tròn 2 chữ số
            total = Math.round(total * 100.0) / 100.0;

            order.setTotal(total);

            // Thiết lập status
            if (total >= 1500) {
                order.setStatus("VIP_REVIEW");
            } else if (total >= 800) {
                order.setStatus("REVIEW");
            } else {
                order.setStatus("READY");
            }

            // Gửi lại object
            service.submitObject(studentCode, qCode, order);

            System.out.println("Submit successful!");
            System.out.println(order);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}