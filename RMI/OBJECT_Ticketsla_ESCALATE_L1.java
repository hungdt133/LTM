package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OBJECT_Ticketsla_ESCALATE_L1{

    public static void main(String[] args) {

        String studentCode = "B22DCDT133";
        String qCode = "CVitJiZk";

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

            // Kiểm tra null
            if (ticket == null) {
                System.out.println("No data returned from server!");
                return;
            }

            System.out.println("Received: " + ticket);

            String priority = ticket.getPriority();
            int openedHoursAgo = ticket.getOpenedHoursAgo();

            boolean breached = false;

            // Kiểm tra SLA
            if ("CRITICAL".equals(priority) && openedHoursAgo > 2) {
                breached = true;

            } else if ("HIGH".equals(priority) && openedHoursAgo > 8) {
                breached = true;

            } else if ("MEDIUM".equals(priority) && openedHoursAgo > 24) {
                breached = true;

            } else if ("LOW".equals(priority) && openedHoursAgo > 72) {
                breached = true;
            }

            // Set breached
            ticket.setBreached(breached);

            // Set action
            if (!breached) {

                ticket.setAction("MONITOR");

            } else {

                if ("CRITICAL".equals(priority)
                        || openedHoursAgo > 96) {

                    ticket.setAction("ESCALATE_L2");

                } else {

                    ticket.setAction("ESCALATE_L1");
                }
            }

            // Submit object
         service.submitObject(studentCode, qCode, ticket);

            System.out.println("Submit successful!");
            System.out.println("Updated object: " + ticket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}