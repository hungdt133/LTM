
package UDP_new;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDP_CheLog_ThongKe_Client {

    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            // Địa chỉ server
            InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
            int serverPort = 2208;

            // Tạo socket UDP
            socket = new DatagramSocket();

            // =========================
            // PHA 1: Gửi studentCode và qCode
            // =========================
            String studentCode = "B22DCDT133";
            String qCode = "VCqIgJY6";

            String firstMessage = ";" + studentCode + ";" + qCode;

            byte[] sendData = firstMessage.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData,
                    sendData.length,
                    serverAddress,
                    serverPort
            );

            socket.send(sendPacket);

            // =========================
            // Nhận phản hồi từ server
            // =========================
            byte[] receiveData = new byte[65535];

            DatagramPacket receivePacket = new DatagramPacket(
                    receiveData,
                    receiveData.length
            );

            socket.receive(receivePacket);

            String response = new String(
                    receivePacket.getData(),
                    0,
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            );

            System.out.println("Phản hồi từ server:");
            System.out.println(response);

            // =========================
            // Tách requestId và data
            // =========================
            String[] parts = response.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // Các dòng log ngăn cách bởi ||
            String[] logs = data.split("\\|\\|");

            int errorCount = 0;
            int infoCount = 0;
            int warnCount = 0;

            StringBuilder maskedLogs = new StringBuilder();

            for (int i = 0; i < logs.length; i++) {

                String log = logs[i];

                // Đếm loại log
                if (log.startsWith("ERROR")) {
                    errorCount++;
                } else if (log.startsWith("INFO")) {
                    infoCount++;
                } else if (log.startsWith("WARN")) {
                    warnCount++;
                }

                // Che email
                log = log.replaceAll(
                        "email=[^\\s]+",
                        "email=[EMAIL]"
                );

                // Che phone
                log = log.replaceAll(
                        "phone=[^\\s]+",
                        "phone=[PHONE]"
                );

                // Che token
                log = log.replaceAll(
                        "token=[^\\s]+",
                        "token=[TOKEN]"
                );

                maskedLogs.append(log);

                if (i < logs.length - 1) {
                    maskedLogs.append("||");
                }
            }

            // =========================
            // Tạo kết quả gửi lại server
            // =========================
            String result =
                    requestId + ";" +
                    maskedLogs +
                    "##ERROR=" + errorCount +
                    ";INFO=" + infoCount +
                    ";WARN=" + warnCount;

            System.out.println("\nDữ liệu gửi lại:");
            System.out.println(result);

            // =========================
            // Gửi kết quả về server
            // =========================
            byte[] resultData = result.getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket = new DatagramPacket(
                    resultData,
                    resultData.length,
                    serverAddress,
                    serverPort
            );

            socket.send(resultPacket);

            System.out.println("\nĐã gửi kết quả thành công.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2208 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B21DCCN001;XbYdNZ3A.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data, trong đó data gồm nhiều dòng log nối bằng ||. Ví dụ: Wx12Yz34;ERROR user=a email=a@example.com phone=0123456789 token=abc.
//
//c. Che email, số điện thoại và token; đồng thời đếm số dòng bắt đầu bằng ERROR, INFO, WARN.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;maskedLog##ERROR=n;INFO=n;WARN=n. Ví dụ: Wx12Yz34;ERROR user=a email=[EMAIL] phone=[PHONE] token=[TOKEN]##ERROR=1;INFO=0;WARN=0.
//
//e. Đóng socket hoặc kết thúc client sau khi nộp kết quả.