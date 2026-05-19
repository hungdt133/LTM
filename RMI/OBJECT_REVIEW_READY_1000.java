package RMI;

import RMI.ObjectService;
import RMI.OrderTotal;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OBJECT_REVIEW_READY_1000 {
    public static void main(String[] args) {
        String studentCode = "B22DCDT133";
        String qCode = "XcXLou7K";

        try {
            // Kết nối tới RMI Registry Server
            Registry registry = LocateRegistry.getRegistry("36.50.135.242", 1099);

            // Lookup service
            ObjectService service =
                    (ObjectService) registry.lookup("RMIObjectService");

            // Gọi requestObject để nhận dữ liệu
            OrderTotal order = (OrderTotal) service.requestObject(studentCode, qCode);

            // Tính total
            double total = order.getItemsSubtotal()
                    * (1 - order.getDiscountRate() / 100.0)
                    + order.getShippingFee();

            // Làm tròn 2 chữ số thập phân
            total = Math.round(total * 100.0) / 100.0;

            // Cập nhật object
            order.setTotal(total);

            // Thiết lập status
            if (total >= 1000) {
                order.setStatus("REVIEW");
            } else {
                order.setStatus("READY");
            }

            // Gửi lại object
            service.submitObject(studentCode, qCode, order);

            System.out.println("Submitted successfully!");
            System.out.println(order);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}