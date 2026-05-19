//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2207, sử dụng DataInputStream và DataOutputStream để trao đổi dữ liệu.
//
//Yêu cầu
//a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi bằng phương thức writeUTF theo định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;1D25ED92".
//
//b. Nhận từ server một số nguyên n, sau đó nhận tiếp n số nguyên.
//
//c. Tìm giá trị nhỏ nhất, giá trị lớn nhất và tổng của toàn bộ dãy số.
//
//d. Gửi kết quả bằng writeUTF theo định dạng min;max;sum. Ví dụ: 3;20;87.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là 496,342,197,73,-52,124,130,228,238,-147,-329,411 thì dữ liệu nộp lại là -329;496;1711.
package new_TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCP_DATA_Min_MAX_sum {

    public static void main(String[] args) {

        String host = "36.50.135.242"; // thay IP server
        int port = 2207;

        String studentCode = "B22DCDT133";
        String qCode = "LMJiqaOe";

        try (
                Socket socket = new Socket(host, port);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ) {

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            dos.writeUTF(request);
            dos.flush();

            // b. Nhận n
            int n = dis.readInt();

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            long sum = 0;

            // Nhận n số nguyên
            for (int i = 0; i < n; i++) {
                int x = dis.readInt();

                if (x < min) {
                    min = x;
                }

                if (x > max) {
                    max = x;
                }

                sum += x;
            }

            // c + d. Gửi kết quả
            String result = min + ";" + max + ";" + sum;

            dos.writeUTF(result);
            dos.flush();

            System.out.println("Sent: " + result);

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}