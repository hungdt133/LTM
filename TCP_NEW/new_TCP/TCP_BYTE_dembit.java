//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2206, sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin.
//
//Yêu cầu
//a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" qua OutputStream. Ví dụ: "B15DCCN999;1D25ED92".
//
//b. Nhận từ server một chuỗi gồm nhiều đoạn nhị phân, các đoạn được phân tách bởi ký tự |. Ví dụ: 1010|111|0001.
//
//c. Đếm tổng số bit 1 và tổng số bit 0 trong toàn bộ chuỗi dữ liệu nhận được.
//
//d. Gửi kết quả theo định dạng ones=<số_lượng_bit_1>;zeros=<số_lượng_bit_0>. Ví dụ: ones=6;zeros=5.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là 000111000|101101|0110010|101101100|011110101|0110010 thì dữ liệu nộp lại là ones=24;zeros=23.
package new_TCP;

import java.io.*;
import java.net.Socket;

public class TCP_BYTE_dembit {

    public static void main(String[] args) {

        String host = "36.50.135.242"; // thay bằng IP server
        int port = 2206;

        String studentCode = "B22DCDT133";
        String qCode = "zny1jBFr";

        try (
                Socket socket = new Socket(host, port);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
        ) {

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            bw.write(request);
            bw.newLine();
            bw.flush();

            // b. Nhận chuỗi nhị phân
            String data = br.readLine();

            System.out.println("Received: " + data);

            // c. Đếm bit 1 và bit 0
            int ones = 0;
            int zeros = 0;

            for (char c : data.toCharArray()) {
                if (c == '1') {
                    ones++;
                } else if (c == '0') {
                    zeros++;
                }
            }

            // d. Gửi kết quả
            String result = "ones=" + ones + ";zeros=" + zeros;

            bw.write(result);
            bw.newLine();
            bw.flush();

            System.out.println("Sent: " + result);

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}