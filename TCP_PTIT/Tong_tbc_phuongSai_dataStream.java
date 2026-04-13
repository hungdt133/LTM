
package TCP_PTIT;

//Một chương trình server cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5 giây). Yêu cầu là xây dựng chương trình client tương tác với server bằng các byte stream (DataInputStream/DataOutputStream) để trao đổi thông tin theo trình tự sau:
//a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi ở định dạng "studentCode;qCode"
//Ví dụ: "B10DCCN000;A1B2C3D4".
//b. Nhận lần lượt từ server các số nguyên gồm:  - số nguyên n kích thước của mảng  - các số nguyên a1, a2, ... an là các giá trị trong mảng
//Ví dụ: n = 5, và các giá trị 5, 9, 3, 6, 8
//c. Tính tổng, trung bình cộng, và phương sai của mảng. Gửi kết quả lần lượt lên server dưới dạng số nguyên và float. Ví dụ, gửi lên lần lượt: 31, 6.2, 4.5599995.  Biết:  Phương sai = [(x1 - Trung bình)^2 + (x2 - Trung bình)^2 + ... + (xn - Trung bình)^2] / n
//d. Đóng kết nối và kết thúc chương trình.

import java.io.*;
import java.net.*;

public class Tong_tbc_phuongSai_dataStream{
    public static void main(String[] args) {
        String serverHost = "36.50.135.242"; // đổi nếu server khác máy
        int port = 2207;

        try (
            Socket socket = new Socket(serverHost, port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ) {
            // a. Gửi mã sinh viên + mã câu hỏi
            String message = "B22DCDT133;tNn6440U";
            dos.writeUTF(message);
            dos.flush();

            // b. Nhận dữ liệu
            int n = dis.readInt();
            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                arr[i] = dis.readInt();
            }

            // c. Tính toán
            int sum = 0;
            for (int x : arr) {
                sum += x;
            }

            float avg = (float) sum / n;

            float variance = 0;
            for (int x : arr) {
                variance += Math.pow(x - avg, 2);
            }
            variance = variance / n;

            // d. Gửi kết quả
            dos.writeInt(sum);
            dos.writeFloat(avg);
            dos.writeFloat(variance);
            dos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}