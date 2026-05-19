package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class String_rBhDz2JJ_INFO{

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "B22DCDT133";
        String qCode = "A3k6GSvL";

        try {

            DatagramSocket socket =
                    new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(host);

            // =====================================
            // a. SEND REQUEST
            // =====================================

            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] requestBytes =
                    request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket requestPacket =
                    new DatagramPacket(
                            requestBytes,
                            requestBytes.length,
                            serverAddress,
                            port
                    );

            socket.send(requestPacket);

            // =====================================
            // b. RECEIVE RESPONSE
            // =====================================

            byte[] receiveData =
                    new byte[65535];

            DatagramPacket responsePacket =
                    new DatagramPacket(
                            receiveData,
                            receiveData.length
                    );

            socket.receive(responsePacket);

            String response =
                    new String(
                            responsePacket.getData(),
                            0,
                            responsePacket.getLength(),
                            StandardCharsets.UTF_8
                    );

            System.out.println("Received:");
            System.out.println(response);

            // format:
            // requestId;data

            String[] parts =
                    response.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // =====================================
            // c. MASK DATA
            // =====================================

            // email
            data = data.replaceAll(
                    "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
                    "[EMAIL]"
            );

            // phone
            data = data.replaceAll(
                    "\\b0\\d{9}\\b",
                    "[PHONE]"
            );

            // token
            data = data.replaceAll(
                    "token=[^\\s|]+",
                    "token=[TOKEN]"
            );

            // =====================================
            // d. SEND ANSWER
            // =====================================

            String answer =
                    requestId + ";" + data;

            byte[] answerBytes =
                    answer.getBytes(StandardCharsets.UTF_8);

            DatagramPacket answerPacket =
                    new DatagramPacket(
                            answerBytes,
                            answerBytes.length,
                            serverAddress,
                            port
                    );

            socket.send(answerPacket);

            System.out.println("Sent:");
            System.out.println(answer);

            // =====================================
            // e. CLOSE
            // =====================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2208 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B15DCCN001;EE29C059.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data. Ví dụ: rBhDz2JJ;INFO user=minh email=minh0@example.com phone=0309453907 token=bDzj8Olxc9 action=refund.
//
//c. Che email, số điện thoại và token theo quy tắc [EMAIL], [PHONE], token=[TOKEN], giữ nguyên thứ tự các dòng log.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;answer. Ví dụ: rBhDz2JJ;INFO user=minh email=[EMAIL] phone=[PHONE] token=[TOKEN] action=refund.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.