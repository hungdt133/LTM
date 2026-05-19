//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2206, sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin.
//
//Yêu cầu
//a. Gửi chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode qua OutputStream. Ví dụ: B21DCCN001;A1B2C3D4.
//
//b. Nhận từ server một chuỗi số byte không dấu phân tách bằng dấu phẩy. Ví dụ: 12,45,255,8,19,20.
//
//c. Tính primeSum là tổng các giá trị ở vị trí nguyên tố theo chỉ số bắt đầu từ 1, lấy modulo 65536; đồng thời tính xor của toàn bộ giá trị.
//
//d. Gửi kết quả theo định dạng primeSum=<n>;xor=<m>. Ví dụ: primeSum=319;xor=217.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.



package new_TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCP_BYTE_PrimeSum_XOR {

    // =====================================
    // Kiểm tra số nguyên tố
    // =====================================
    public static boolean isPrime(int n) {

        if (n < 2) {
            return false;
        }

        for (int i = 2; i * i <= n; i++) {

            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2206;

        String studentCode = "B22DCDT133";
        String qCode = "uyjKuFaN";

        try {

            Socket socket = new Socket(host, port);

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // =====================================
            // a. Gửi request
            // =====================================

            String request =
                     studentCode + ";" + qCode + "\n";

            os.write(
                    request.getBytes(StandardCharsets.UTF_8)
            );

            os.flush();

            // =====================================
            // b. Nhận dữ liệu
            // =====================================

            byte[] buffer = new byte[8192];

            int len = is.read(buffer);

            if (len == -1) {

                System.out.println("No data received");
                socket.close();
                return;
            }

            String data =
                    new String(buffer, 0, len);

            System.out.println("Received:");
            System.out.println(data);

            // =====================================
            // c. Tính primeSum + xor
            // =====================================

            String[] arr = data.trim().split(",");

            int primeSum = 0;
            int xor = 0;

            for (int i = 0; i < arr.length; i++) {

                int value =
                        Integer.parseInt(arr[i].trim());

                // XOR toàn bộ
                xor ^= value;

                // index bắt đầu từ 1
                int position = i + 1;

                if (isPrime(position)) {
                    primeSum += value;
                }
            }

            // modulo 65536
            primeSum %= 65536;

            // =====================================
            // d. Gửi kết quả
            // =====================================

            String result =
                    "primeSum=" + primeSum
                    + ";xor=" + xor;

            os.write(
                    result.getBytes(StandardCharsets.UTF_8)
            );

            os.flush();

            System.out.println("Sent:");
            System.out.println(result);

            // =====================================
            // e. Đóng kết nối
            // =====================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}