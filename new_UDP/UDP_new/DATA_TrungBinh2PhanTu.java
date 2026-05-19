package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DATA_TrungBinh2PhanTu {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "B22DCDT133";
        String qCode = "0PYwtdY5";

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

            byte[] sendData =
                    request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            port
                    );

            socket.send(sendPacket);

            // =====================================
            // b. Nhận response
            // =====================================

            byte[] receiveData =
                    new byte[65535];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveData,
                            receiveData.length
                    );

            socket.receive(receivePacket);

            String response =
                    new String(
                            receivePacket.getData(),
                            0,
                            receivePacket.getLength(),
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
            // c. Sort + median
            // =====================================

            String[] arr = data.split(",");

            int n = arr.length;

            int[] numbers = new int[n];

            for (int i = 0; i < n; i++) {

                numbers[i] =
                        Integer.parseInt(arr[i].trim());
            }

            Arrays.sort(numbers);

            double median;

            if (n % 2 == 1) {

                // lẻ
                median = numbers[n / 2];

            } else {

                // chẵn
                median =
                        (numbers[n / 2 - 1]
                        + numbers[n / 2]) / 2.0;

                median =
                        Math.round(median * 100.0)
                        / 100.0;
            }

            // =====================================
            // d. Gửi kết quả
            // =====================================

            String answer;

            // nếu là số nguyên thì bỏ .0
            if (median == (int) median) {

                answer =
                        requestId + ";"
                        + (int) median;

            } else {

                answer =
                        requestId + ";"
                        + String.format("%.2f", median);
            }

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
//b. Nhận phản hồi từ server theo định dạng requestId;data. Ví dụ: SjkZFpBk;268,125,584,33,685,530,661,168,589,515,142,619,286,24,241.
//
//c. Sắp xếp dãy số tăng dần và tìm giá trị trung vị. Nếu số lượng phần tử chẵn, lấy trung bình hai phần tử giữa và làm tròn 02 chữ số thập phân.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;answer. Ví dụ: SjkZFpBk;286.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.