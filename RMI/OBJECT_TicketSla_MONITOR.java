package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OBJECT_TicketSla_MONITOR{

    public static void main(String[] args) {

        String studentCode = "B22DCDT133";
        String qCode = "h3JQynMn";

        try {
            // Kết nối tới RMI Registry
            Registry registry =
                    LocateRegistry.getRegistry("36.50.135.242", 1099);

            // Lookup service
            ObjectService service =
                    (ObjectService) registry.lookup("RMIObjectService");

            // Nhận object từ server
            TicketSla ticket =
                    (TicketSla) service.requestObject(studentCode, qCode);

            String priority = ticket.getPriority();
            int openedHoursAgo = ticket.getOpenedHoursAgo();

            boolean breached = false;

            // Kiểm tra điều kiện breached
            if (priority.equals("CRITICAL") && openedHoursAgo > 4) {
                breached = true;
            } else if (priority.equals("HIGH") && openedHoursAgo > 12) {
                breached = true;
            } else if (openedHoursAgo > 48) {
                breached = true;
            }

            ticket.setBreached(breached);

            // Thiết lập action
            if (breached) {
                ticket.setAction("ESCALATE");
            } else {
                ticket.setAction("MONITOR");
            }

            // Gửi lại object
            service.submitObject(studentCode, qCode, ticket);

            System.out.println("Submit successful!");
            System.out.println(ticket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}