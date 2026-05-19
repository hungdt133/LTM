package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_LongestIncreasingSegment {

    public static void main(String[] args) {

        DatagramSocket socket = null;

        try {

            // =====================================
            // Kết nối server
            // =====================================
            String host = "36.50.135.242";
            int port = 2207;

            socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(host);

            // =====================================
            // a. Gửi request
            // =====================================
            String studentCode = "B22DCDT133";
            String qCode = "neqoMgIw";

            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] sendData =
                    request.getBytes("UTF-8");

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            port
                    );

            socket.send(sendPacket);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =====================================
            // b. Nhận dữ liệu
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
                            "UTF-8"
                    ).trim();

            System.out.println("\nDữ liệu nhận được:");
            System.out.println(response);

            // =====================================
            // Tách requestId và data
            // =====================================
            String[] responseParts =
                    response.split(";", 2);

            String requestId =
                    responseParts[0];

            String numberData =
                    responseParts[1];

            // =====================================
            // Parse dãy số
            // =====================================
            String[] arr =
                    numberData.split(",");

            int n = arr.length;

            int[] numbers =
                    new int[n];

            for (int i = 0; i < n; i++) {

                numbers[i] =
                        Integer.parseInt(
                                arr[i].trim()
                        );
            }

            // =====================================
            // Tìm đoạn tăng liên tiếp dài nhất
            // Nếu bằng nhau -> lấy đoạn xuất hiện trước
            // =====================================
            int bestStart = 0;
            int bestLength = 1;

            int currentStart = 0;
            int currentLength = 1;

            for (int i = 1; i < n; i++) {

                if (numbers[i] > numbers[i - 1]) {

                    currentLength++;

                } else {

                    if (currentLength > bestLength) {

                        bestLength =
                                currentLength;

                        bestStart =
                                currentStart;
                    }

                    currentStart = i;
                    currentLength = 1;
                }
            }

            // kiểm tra đoạn cuối
            if (currentLength > bestLength) {

                bestLength =
                        currentLength;

                bestStart =
                        currentStart;
            }

            // =====================================
            // Tạo segment và sum
            // =====================================
            StringBuilder segment =
                    new StringBuilder();

            int sum = 0;

            for (int i = bestStart;
                 i < bestStart + bestLength;
                 i++) {

                segment.append(numbers[i]);

                sum += numbers[i];

                if (i < bestStart + bestLength - 1) {

                    segment.append(",");
                }
            }

            // =====================================
            // d. Gửi kết quả
            // =====================================
            String result =
                    requestId
                    + ";segment="
                    + segment.toString()
                    + ";length="
                    + bestLength
                    + ";sum="
                    + sum;

            System.out.println("\nKết quả gửi:");
            System.out.println(result);

            byte[] resultData =
                    result.getBytes("UTF-8");

            DatagramPacket resultPacket =
                    new DatagramPacket(
                            resultData,
                            resultData.length,
                            serverAddress,
                            port
                    );

            socket.send(resultPacket);

            System.out.println("\nĐã gửi kết quả.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");

            e.printStackTrace();

        } finally {

            // =====================================
            // e. Đóng socket
            // =====================================
            if (socket != null
                    && !socket.isClosed()) {

                socket.close();

                System.out.println(
                        "\nĐã đóng socket."
                );
            }
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