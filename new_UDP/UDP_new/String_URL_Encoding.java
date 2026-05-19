package UDP_new;
import java.net.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class String_URL_Encoding {

    public static void main(String[] args) {

        String studentCode = "B22DCDT133";
        String qCode = "TGpp6Qyy";

        String host = "36.50.135.242";
        int port = 2208;

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);

            // =========================
            // 1. SEND REQUEST
            // =========================
            String request = ";" + studentCode + ";" + qCode;

            byte[] sendData = request.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(sendData, sendData.length, address, port));

            // =========================
            // 2. RECEIVE RESPONSE
            // =========================
            byte[] buffer = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            String response = new String(
                    receivePacket.getData(),
                    0,
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            );

            System.out.println("RAW RESPONSE: " + response);

            // =========================
            // 3. SPLIT requestId + queryString
            // =========================
            String requestId;
            String queryString;

            String[] parts = response.split(";", 2);

            requestId = parts[0];
            queryString = parts.length > 1 ? parts[1] : "";

            System.out.println("QUERY STRING: " + queryString);

            // =========================
            // 4. PARSE + DECODE + SORT
            // =========================
            Map<String, String> map = new TreeMap<>();

            for (String pair : queryString.split("&")) {
                if (!pair.contains("=")) continue;

                String[] kv = pair.split("=", 2);

                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);

                map.put(key, value);
            }

            // =========================
            // 5. BUILD RESULT
            // =========================
            StringBuilder result = new StringBuilder();
            result.append(requestId);

            for (Map.Entry<String, String> e : map.entrySet()) {
                result.append(";")
                      .append(e.getKey())
                      .append("=")
                      .append(e.getValue());
            }

            String finalResult = result.toString();

            System.out.println("FINAL RESULT: " + finalResult);

            // =========================
            // 6. SEND RESULT BACK
            // =========================
            byte[] sendResult = finalResult.getBytes(StandardCharsets.UTF_8);

            socket.send(new DatagramPacket(sendResult, sendResult.length, address, port));

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2208 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B21DCCN001;XbYdNZ3A.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data, trong đó data là query string. Ví dụ: Qr12St34;role=risk%20team&user=alice.
//
//c. Giải mã URL encoding cho key và value, sau đó sắp xếp key tăng dần theo thứ tự từ điển.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;key=value;key=value. Ví dụ: Qr12St34;role=risk team;user=alice.
//
//e. Đóng socket hoặc kết thúc client sau khi nộp kết quả.
