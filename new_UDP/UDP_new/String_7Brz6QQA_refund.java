
package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class String_7Brz6QQA_refund {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "B22DCDT133";
        String qCode = "LkMPdRrK";

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
            // c. NORMALIZE STRING
            // =====================================

            // lowercase
            data = data.toLowerCase();

            // remove punctuation
            data = data.replaceAll(
                    "[^a-z0-9\\s]",
                    " "
            );

            // multiple spaces -> single space
            data = data.replaceAll(
                    "\\s+",
                    " "
            ).trim();

            // replace space with -
            data = data.replace(" ", "-");

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
//b. Nhận phản hồi từ server theo định dạng requestId;data. Ví dụ: 7Brz6QQA;REFUND ticket Payment Payment customer Payment customer ticket REFUND customer shipping shipping Payment..
//c. Chuyển chuỗi về chữ thường, loại bỏ dấu câu, gom nhiều khoảng trắng thành một khoảng trắng và thay khoảng trắng bằng dấu gạch ngang.
//d. Gửi datagram nộp kết quả theo định dạng requestId;answer. Ví dụ: 7Brz6QQA;refund-ticket-payment-payment-customer-payment-customer-ticket-refund-customer-shipping-shipping-payment.
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.