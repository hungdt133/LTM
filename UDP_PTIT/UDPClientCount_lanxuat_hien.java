//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng
//2208. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
//a. Gửi một thông điệp chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode"
//Ví dụ: ";B15DCCN001;9F8C2D3A".
//b. Nhận một thông điệp từ server theo định dạng "requestId;data", với:      requestId là chuỗi ngẫu nhiên duy nhất.      data là một chuỗi ký tự liên tiếp cần xử lý
//Ví dụ: "requestId;aaabbbccdaa
//c. Xử lý chuỗi bằng cách duyệt lần lượt và đếm số lần xuất hiện của từng ký tự. Gửi kết quả về server theo định dạng: "requestId;processedData"
//Ví dụ: Với chuỗi "aaazbbbccdaaz", kết quả sẽ là: "requestId;5a2z3b2c1d
//d. Đóng socket và kết thúc chương trình
package UDP_PTIT;

import java.net.*;
import java.io.*;
import java.util.*;

public class UDPClientCount_lanxuat_hien {
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            InetAddress serverAddress = InetAddress.getByName("ptit.store");
            int port = 2208;

            socket = new DatagramSocket();

            // a. Gửi request
            String message = ";B22DCDT133;oUIZnmqh";
            DatagramPacket sendPacket = new DatagramPacket(
                    message.getBytes(),
                    message.length(),
                    serverAddress,
                    port
            );
            socket.send(sendPacket);

            // b. Nhận dữ liệu
            byte[] receiveData = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + response);

            String[] parts = response.split(";");
            String requestId = parts[0];
            String data = parts[1];

            // c. Xử lý
            LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();

            for (char c : data.toCharArray()) {
                map.put(c, map.getOrDefault(c, 0) + 1);
            }

            StringBuilder result = new StringBuilder();
            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                result.append(entry.getValue()).append(entry.getKey());
            }

            String output = requestId + ";" + result.toString();

            // gửi lại server
            DatagramPacket resultPacket = new DatagramPacket(
                    output.getBytes(),
                    output.length(),
                    serverAddress,
                    port
            );

            socket.send(resultPacket);

            System.out.println("Sent: " + output);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}