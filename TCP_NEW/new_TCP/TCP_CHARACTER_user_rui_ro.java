//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208, sử dụng BufferedReader và BufferedWriter để trao đổi chuỗi ký tự.
//
//Yêu cầu
//a. Gửi một dòng chứa mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode bằng BufferedWriter, sau đó kết thúc dòng. Ví dụ: B21DCCN001;BAA62945.
//
//b. Nhận từ server một chuỗi log giao dịch, các dòng nối bằng ||. Mỗi dòng có dạng user action=... risk=... amount=.... Ví dụ: U300 action=PAY risk=85 amount=7200.50.
//
//c. Một dòng rủi ro cao nếu risk >= 70 hoặc amount >= 5000. Giữ thứ tự các user rủi ro cao theo dữ liệu ban đầu và tính tổng amount của các dòng này.
//
//d. Gửi kết quả theo định dạng count=<n>;ids=<id1,id2>;amount=<tổng>, tổng làm tròn 02 chữ số thập phân. Ví dụ: count=2;ids=U300,U302;amount=10500.75.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package new_TCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCP_CHARACTER_user_rui_ro {

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String serverHost = "36.50.135.242";
            int serverPort = 2208;

            socket = new Socket(serverHost, serverPort);

            System.out.println("Đã kết nối server.");

            // =========================================
            // Tạo BufferedReader / BufferedWriter
            // =========================================
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "XLrJFu1Z";

            String request = studentCode + ";" + qCode;

            writer.write(request);
            writer.newLine();
            writer.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận chuỗi log từ server
            // =========================================
            String response = reader.readLine();

            System.out.println("\nDữ liệu nhận được:");
            System.out.println(response);

            // =========================================
            // Tách các dòng log
            // =========================================
            String[] logs = response.split("\\|\\|");

            List<String> highRiskUsers = new ArrayList<>();

            double totalAmount = 0;

            // =========================================
            // c. Kiểm tra rủi ro cao
            // risk >= 70 hoặc amount >= 5000
            // =========================================
            for (String log : logs) {

                log = log.trim();

                if (log.isEmpty()) {
                    continue;
                }

                System.out.println("\nĐang xử lý:");
                System.out.println(log);

                // Ví dụ:
                // U300 action=PAY risk=85 amount=7200.50

                String[] parts = log.split("\\s+");

                String userId = parts[0];

                int risk = 0;
                double amount = 0;

                for (String part : parts) {

                    if (part.startsWith("risk=")) {

                        risk = Integer.parseInt(
                                part.substring(5)
                        );

                    } else if (part.startsWith("amount=")) {

                        amount = Double.parseDouble(
                                part.substring(7)
                        );
                    }
                }

                // Kiểm tra high risk
                if (risk >= 70 || amount >= 5000) {

                    highRiskUsers.add(userId);

                    totalAmount += amount;

                    System.out.println("=> High Risk");
                } else {
                    System.out.println("=> Normal");
                }
            }

            // =========================================
            // Tạo danh sách ids
            // =========================================
            String ids = String.join(",", highRiskUsers);

            // =========================================
            // d. Gửi kết quả
            // =========================================
            String result = String.format(
                    "count=%d;ids=%s;amount=%.2f",
                    highRiskUsers.size(),
                    ids,
                    totalAmount
            );

            System.out.println("\nKết quả gửi:");
            System.out.println(result);

            writer.write(result);
            writer.newLine();
            writer.flush();

            System.out.println("\nĐã gửi kết quả thành công.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            // =========================================
            // e. Đóng kết nối
            // =========================================
            try {

                if (socket != null && !socket.isClosed()) {

                    socket.close();

                    System.out.println("\nĐã đóng kết nối.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}