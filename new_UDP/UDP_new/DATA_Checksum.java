package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class DATA_Checksum {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "B22DCDT133";
        String qCode = "hQxg4lUX";

        try {

            DatagramSocket socket =
                    new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(host);

            // =====================================
            // a. Gửi request
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
            // b. Nhận response
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
            // c. Tính checksum
            // =====================================

            String[] arr = data.split(",");

            long checksum = 0;

            for (int i = 0; i < arr.length; i++) {

                int value =
                        Integer.parseInt(arr[i].trim());

                checksum += (long) (i + 1) * value;
            }

            checksum %= 100000;

            // =====================================
            // d. Gửi kết quả
            // =====================================

            String answer =
                    requestId + ";" + checksum;

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
            // e. Close
            // =====================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B15DCCN001;EE29C059.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data. Ví dụ: NjMPqAdf;264,340,630,733,395,954,431,631,650,412.
//
//c. Tính checksum theo công thức sum((i + 1) * value[i]) mod 100000, với i bắt đầu từ 0.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;answer. Ví dụ: NjMPqAdf;31500.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.