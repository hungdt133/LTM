//Nội dung
//Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu xây dựng chương trình client thực hiện kết nối tới server trên sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
//a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode"
//Ví dụ: "B16DCCN999;FF49DC02
//b. Nhận dữ liệu từ server là một chuỗi các giá trị số nguyên được phân tách nhau bởi ký tự ","
//Ex: 1,3,9,19,33,20
//c. Thực hiện tìm giá trị khoảng cách nhỏ nhất của các phần tử nằm trong chuỗi và hai giá trị lớn nhất tạo nên khoảng cách đó. Gửi lên server chuỗi gồm "khoảng cách nhỏ nhất, số thứ nhất, số thứ hai".
//Ex: 1,19,20
//d. Đóng kết nối và kết thúc

package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCP_Byte_Stream_khoang_cach_nho_nhat {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2206;

        try {
            // 1. Kết nối
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // 2. Gửi dữ liệu
            String message = "B22DCDT133;X2y3bbFb";
            out.write((message + "\n").getBytes()); 
            out.flush();

            // 3. Nhận dữ liệu (byte stream)
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);

            String data = new String(buffer, 0, bytesRead).trim();
            System.out.println("Received: " + data);

            // 4. Xử lý
            String[] parts = data.split(",");
            int[] arr = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                arr[i] = Integer.parseInt(parts[i]);
            }

            // 🔥 tối ưu O(n log n)
            Arrays.sort(arr);

            int minDiff = Integer.MAX_VALUE;
            int num1 = 0, num2 = 0;

            for (int i = 0; i < arr.length - 1; i++) {
                int diff = arr[i + 1] - arr[i];
                if (diff < minDiff) {
                    minDiff = diff;
                    num1 = arr[i];
                    num2 = arr[i + 1];
                }
            }

            // 5. Gửi kết quả
            String result = minDiff + "," + num1 + "," + num2;
            out.write((result + "\n").getBytes()); // 🔥 thêm \n
            out.flush();

            System.out.println("Sent: " + result);

            // 6. Đóng
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

