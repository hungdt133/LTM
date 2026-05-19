package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class DATA_segment_csv {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "B22DCDT133";
        String qCode = "neqoMgIw";

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

            byte[] receiveBuffer =
                    new byte[65535];

            DatagramPacket responsePacket =
                    new DatagramPacket(
                            receiveBuffer,
                            receiveBuffer.length
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

            // requestId;data
            String[] parts =
                    response.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // =====================================
            // c. FIND LONGEST STRICTLY INCREASING
            // =====================================

            String[] arr = data.split(",");

            int n = arr.length;

            int[] nums = new int[n];

            for (int i = 0; i < n; i++) {

                nums[i] =
                        Integer.parseInt(arr[i].trim());
            }

            int bestStart = 0;
            int bestLen = 1;

            int currentStart = 0;
            int currentLen = 1;

            for (int i = 1; i < n; i++) {

                if (nums[i] > nums[i - 1]) {

                    currentLen++;

                } else {

                    if (currentLen > bestLen) {

                        bestLen = currentLen;
                        bestStart = currentStart;
                    }

                    currentStart = i;
                    currentLen = 1;
                }
            }

            // check last segment
            if (currentLen > bestLen) {

                bestLen = currentLen;
                bestStart = currentStart;
            }

            // build segment + sum
            StringBuilder segment =
                    new StringBuilder();

            long sum = 0;

            for (int i = bestStart;
                 i < bestStart + bestLen;
                 i++) {

                if (i > bestStart) {
                    segment.append(",");
                }

                segment.append(nums[i]);

                sum += nums[i];
            }

            // =====================================
            // d. SEND ANSWER
            // =====================================

            String answer =
                    requestId
                    + ";segment=" + segment
                    + ";length=" + bestLen
                    + ";sum=" + sum;

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
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B21DCCN001;EE29C059.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data, trong đó data là dãy số nguyên phân tách bằng dấu phẩy. Ví dụ: Ef56Gh78;4,9,12,3,8.
//
//c. Tìm đoạn con liên tiếp tăng nghiêm ngặt dài nhất. Nếu có nhiều đoạn cùng độ dài, chọn đoạn xuất hiện trước.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;segment=<csv>;length=<n>;sum=<tổng>. Ví dụ: Ef56Gh78;segment=4,9,12;length=3;sum=25.
//
//e. Đóng socket hoặc kết thúc client sau khi nộp kết quả.