package UDP_new;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class DATA_P90 {

    public static void main(String[] args) {

        DatagramSocket socket = null;

        try {

            // =====================================
            // Kết nối UDP server
            // =====================================
            String host = "36.50.135.242";
            int port = 2207;

            socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(host);

            // =====================================
            // a. Gửi datagram đầu tiên
            // ;studentCode;qCode
            // =====================================
            String studentCode = "B22DCDT133";
            String qCode = "eD9xRwIl";

            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] sendData =
                    request.getBytes();

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
            // requestId;10,20,30
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
                            receivePacket.getLength()
                    ).trim();

            System.out.println(
                    "\nDữ liệu nhận được:"
            );

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
            String[] numberStrings =
                    numberData.split(",");

            int n = numberStrings.length;

            int[] numbers =
                    new int[n];

            double sum = 0;

            for (int i = 0; i < n; i++) {

                numbers[i] =
                        Integer.parseInt(
                                numberStrings[i].trim()
                        );

                sum += numbers[i];
            }

            // =====================================
            // Tính average
            // =====================================
            double average =
                    sum / n;

            // =====================================
            // Đếm phần tử > average
            // =====================================
            int aboveAvg = 0;

            for (int value : numbers) {

                if (value > average) {
                    aboveAvg++;
                }
            }

            // =====================================
            // Sắp xếp tăng dần
            // =====================================
            Arrays.sort(numbers);

            // =====================================
            // Tính p90
            // ceil(n * 0.9) - 1
            // =====================================
            int index =
                    (int) Math.ceil(n * 0.9) - 1;

            int p90 =
                    numbers[index];

            // =====================================
            // d. Gửi kết quả
            // requestId;p90=x;aboveAvg=y
            // =====================================
            String result =
                    requestId
                    + ";p90="
                    + p90
                    + ";aboveAvg="
                    + aboveAvg;

            System.out.println(
                    "\nKết quả gửi:"
            );

            System.out.println(result);

            byte[] resultData =
                    result.getBytes();

            DatagramPacket resultPacket =
                    new DatagramPacket(
                            resultData,
                            resultData.length,
                            serverAddress,
                            port
                    );

            socket.send(resultPacket);

            System.out.println(
                    "\nĐã gửi kết quả."
            );

        } catch (Exception e) {

            System.out.println(
                    "\nCó lỗi xảy ra:"
            );

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
//b. Nhận phản hồi từ server theo định dạng requestId;data, trong đó data là dãy số nguyên phân tách bằng dấu phẩy. Ví dụ: Ab12Cd34;10,20,30,40,100.
//
//c. Sắp xếp dãy tăng dần và lấy phân vị 90 tại vị trí ceil(n * 0.9) - 1; đồng thời đếm số phần tử lớn hơn giá trị trung bình của dãy ban đầu.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;p90=<giá_trị>;aboveAvg=<n>. Ví dụ: Ab12Cd34;p90=100;aboveAvg=1.
//
//e. Đóng socket hoặc kết thúc client sau khi nộp kết quả.