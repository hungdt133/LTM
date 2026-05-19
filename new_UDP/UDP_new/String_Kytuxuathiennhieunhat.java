package UDP_new;
//Một chương trình server cho phép kết nối qua giao thức UDP tại cổng
//2208. Yêu cầu là xây dựng một chương trình client tương tác với server kịch bản dưới đây:
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”
//Ví dụ: “;B15DCCN001;EE29C059”
//b. Nhận thông điệp từ server theo định dạng “requestId; data”   - requestId là một chuỗi ngẫu nhiên duy nhất  - data là chuỗi dữ liệu đầu vào cần xử lý
//Ex: “requestId;Qnc8d5x78aldSGWWmaAAjyg3”
//c. Tìm kiếm ký tự xuất hiện nhiều nhất trong chuỗi và gửi lên server theo định dạng “requestId;ký tự xuất hiện nhiều nhất: các vị trí xuất hiện ký tự đó”   ví dụ: “requestId;8:4,9,”
//d. Đóng socket và kết thúc chương trình
import java.net.*;
import java.util.*;

public class String_Kytuxuathiennhieunhat {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
            int port = 2208;

            socket = new DatagramSocket();

            // a. Gửi message
            String message = ";B22DCDT133;cldNRE6f";
            DatagramPacket sendPacket = new DatagramPacket(
                    message.getBytes(),
                    message.length(),
                    serverAddress,
                    port
            );
            socket.send(sendPacket);

            // b. Nhận dữ liệu
            byte[] buffer = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + response);

            // Tách requestId và data
            String[] parts = response.split(";", 2);
            String requestId = parts[0];
            String data = parts[1]; // ❗ GIỮ NGUYÊN, KHÔNG xóa space

            // Map lưu vị trí
            Map<Character, List<Integer>> map = new HashMap<>();

            for (int i = 0; i < data.length(); i++) {
                char c = data.charAt(i);

                if (c == ' ') continue; // ❗ bỏ qua space nhưng KHÔNG xóa

                map.putIfAbsent(c, new ArrayList<>());
                map.get(c).add(i + 1); // vị trí tính theo chuỗi gốc
            }

            // tìm ký tự xuất hiện nhiều nhất (ưu tiên xuất hiện sớm)
            char maxChar = 0;
            int maxCount = 0;

            for (int i = 0; i < data.length(); i++) {
                char c = data.charAt(i);

                if (c == ' ') continue;

                int count = map.get(c).size();

                if (count > maxCount) {
                    maxCount = count;
                    maxChar = c;
                }
            }

            // lấy vị trí
            List<Integer> positions = map.get(maxChar);

            // tạo kết quả
            StringBuilder result = new StringBuilder();
            result.append(requestId).append(";").append(maxChar).append(":");

            for (int pos : positions) {
                result.append(pos).append(",");
            }

            // gửi lại server
            DatagramPacket resultPacket = new DatagramPacket(
                    result.toString().getBytes(),
                    result.length(),
                    serverAddress,
                    port
            );

            socket.send(resultPacket);

            System.out.println("Sent: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}