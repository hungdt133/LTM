//Mật mã caesar, còn gọi là mật mã dịch chuyển, để giải mã thì mỗi ký tự nhận được sẽ được thay thế bằng một ký tự cách nó một đoạn s
//Ví dụ: với s = 3 thì ký tự “A” sẽ được thay thế bằng ký tự “D”.  Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng
//2207. Yêu cầu xây dựng chương trình client trao đổi thông tin với server theo kịch bản mô tả dưới đây:
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode"
//Ví dụ: ";B15DCCN001;825EE3A7
//b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;strEncode;s".
//• requestId là chuỗi ngẫu nhiên duy nhất
//• strEncode là chuỗi thông điệp bị mã hóa
//• s là số nguyên chứa giá trị độ dịch của mã
//c. Giải mã tìm thông điệp ban đầu và gửi lên server theo định dạng “requestId;strDecode”
//d. Đóng socket và kết thúc chương trình.
package UDP_PTIT;

import java.net.*;
import java.io.*;

public class UDP_DataType_Caesar {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName("ptit.store");
            int port = 2207;

            socket = new DatagramSocket();

            // a. Gửi request
            String message = ";B22DCDT133;4hwDxm83";
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length, serverAddress, port);
            socket.send(sendPacket);

            // b. Nhận phản hồi
            byte[] receiveData = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + response);

            String[] parts = response.split(";");
            String requestId = parts[0];
            String strEncode = parts[1];
            int s = Integer.parseInt(parts[2]);

            // c. Giải mã Caesar
            StringBuilder decoded = new StringBuilder();

                    for (char c : strEncode.toCharArray()) {
             if (c >= 'A' && c <= 'Z') {
                 char d = (char)((c - 'A' + s) % 26 + 'A');
                 decoded.append(d);
             } else if (c >= 'a' && c <= 'z') {
                 char d = (char)((c - 'a' + s) % 26 + 'a');
                 decoded.append(d);
             } else {
                 decoded.append(c);
             }
         }   

            String result = requestId + ";" + decoded.toString();

            // gửi lại server
            byte[] resultData = result.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(
                    resultData, resultData.length, serverAddress, port);

            socket.send(resultPacket);

            System.out.println("Sent: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}