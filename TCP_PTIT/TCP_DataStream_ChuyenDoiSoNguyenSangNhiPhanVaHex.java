//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2207 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu sinh viên xây dựng chương trình client để tương tác với server, sử dụng các luồng data (DataInputStream và DataOutputStream) để trao đổi thông tin theo thứ tự sau:
//a. Gửi mã sinh viên và mã câu hỏi: Chuỗi gồm mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode"
//Ví dụ: "B15DCCN999;D68C93F7".
//b. Nhận một số nguyên hệ thập phân từ server
//Ví dụ::
//15226.
//c. Chuyển đổi số nguyên nhận được sang hệ nhị phân và thập lục phân, ghép thành chuỗi và gửi lên server
//Ví dụ: 15226 sẽ thành "11101101111010;3B7A
//d. Đóng kết nối: Kết thúc chương trình sau khi gửi kết quả chuyển đổi.
package TCP;

import java.io.*;
import java.net.*;
public class TCP_DataStream_ChuyenDoiSoNguyenSangNhiPhanVaHex {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // 1. Gửi mã SV + mã đề
            String message = "B22DCDT133;hwruJ5rc";
            out.writeUTF(message);
            out.flush();

            // 2. Nhận số nguyên
            int n;
            try {
                n = in.readInt(); // thường là kiểu này
            } catch (Exception e) {
                // fallback nếu server gửi dạng chuỗi
                String data = in.readUTF();
                n = Integer.parseInt(data.trim());
            }

            System.out.println("Received: " + n);

            // 3. Chuyển đổi
            String binary = Integer.toBinaryString(n);
            String hex = Integer.toHexString(n).toUpperCase();

            String result = binary + ";" + hex;

            // 4. Gửi lại
            out.writeUTF(result);
            out.flush();

            System.out.println("Sent: " + result);

            // 5. Đóng
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}