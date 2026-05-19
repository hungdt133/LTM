//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208, sử dụng BufferedReader và BufferedWriter để trao đổi chuỗi ký tự.
//
//Yêu cầu
//a. Gửi một dòng chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" bằng BufferedWriter, sau đó kết thúc dòng. Ví dụ: "B15DCCN999;BAA62945".
//
//b. Nhận từ server một chuỗi log gồm nhiều dòng được nối bằng ký tự ||. Mỗi dòng có thể chứa email, số điện thoại Việt Nam và token.
//
//c. Thay mọi email bằng [EMAIL], mọi số điện thoại 10 chữ số bắt đầu bằng 0 bằng [PHONE], và mọi token dạng token=<giá_trị> bằng token=[TOKEN].
//
//d. Gửi chuỗi log đã được che dữ liệu nhạy cảm, giữ nguyên thứ tự các dòng và ký tự phân tách ||.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là INFO user=cuong email=cuong0@example.com phone=0684775637 token=ftrTU9XmBX action=shipping thì dữ liệu nộp lại là INFO user=cuong email=[EMAIL] phone=[PHONE] token=[TOKEN] action=shipping.
package new_TCP;
import java.io.*;
import java.net.Socket;

public class TCP_CHERACTER_Log {

    public static void main(String[] args) {

        String host = "36.50.135.242"; // thay IP server
        int port = 2208;

        String studentCode = "B22DCDT133";
        String qCode = "3nsPu0WL";

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

            // b. Nhận log
            String data = br.readLine();

            System.out.println("Received:");
            System.out.println(data);

            // c. Che email
            data = data.replaceAll(
                    "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
                    "[EMAIL]"
            );

            // Che số điện thoại VN 10 số bắt đầu bằng 0
            data = data.replaceAll(
                    "\\b0\\d{9}\\b",
                    "[PHONE]"
            );

            // Che token
            data = data.replaceAll(
                    "token=[^\\s|]+",
                    "token=[TOKEN]"
            );

            // d. Gửi kết quả
            bw.write(data);
            bw.newLine();
            bw.flush();

            System.out.println("Sent:");
            System.out.println(data);

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}