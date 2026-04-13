//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2206 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s).  Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng các luồng byte (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:
//a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode"
//Ví dụ: "B16DCCN999;2B3A6510
//b. Nhận dữ liệu từ server là một số nguyên n nhỏ hơn
//400
//Ví dụ: 7
//c. Thực hiện các bước sau đây để sinh ra chuỗi từ số nguyên n ban đầu và gửi lên server.         
    //Bắt đầu với số nguyên nn:             
    //Nếu n là số chẵn, chia nn cho 2 để tạo ra số tiếp theo trong dãy.             
    //Nếu n là số lẻ và khác 1, thực hiện phép toán n=3*n+1 để tạo ra số tiếp theo.        
    //Lặp lại quá trình trên cho đến khi n=1, tại đó dừng thuật toán.  
    //Kết quả là một dãy số liên tiếp, bắt đầu từ n ban đầu, kết thúc tại 1 và độ dài của chuỗi theo format "chuỗi kết quả; độ dài"
//Ví dụ: kết quả với n = 7 thì dãy: 7 22 11 34 17 52 26 13 40 20 10 5 16 8 4 2 1; 17
//d. Đóng kết nối và kết thúc chương trình.
package TCP;

import java.io.*;
import java.net.*;

public class TCP_Byte_Stream_3n {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2206;

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // 1. Gửi mã SV + mã đề
            String message = "B22DCDT133;V9wK4Zgm";
            out.write((message + "\n").getBytes());
            out.flush();

            // 2. Nhận n
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            String data = new String(buffer, 0, bytesRead).trim();

            int n = Integer.parseInt(data);
            System.out.println("Received n = " + n);

            // 3. Sinh dãy Collatz
            StringBuilder sequence = new StringBuilder();
            int count = 0;

            while (true) {
                sequence.append(n).append(" ");
                count++;

                if (n == 1) break;

                if (n % 2 == 0) {
                    n = n / 2;
                } else {
                    n = 3 * n + 1;
                }
            }

            // bỏ dấu cách cuối
            String resultSeq = sequence.toString().trim();

            // format kết quả
            String result = resultSeq + "; " + count;

            // 4. Gửi lại server
            out.write((result + "\n").getBytes());
            out.flush();

            System.out.println("Sent: " + result);

            // 5. Đóng
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}