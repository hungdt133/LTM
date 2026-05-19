//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2206, sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin.
//
//Yêu cầu
//a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" qua OutputStream. Ví dụ: "B15DCCN999;1D25ED92".
//
//b. Nhận từ server một chuỗi các giá trị byte không dấu, mỗi giá trị nằm trong khoảng 0..255 và được phân tách bởi dấu phẩy. Ví dụ: 12,45,255,8.
//
//c. Tính tổng tất cả các giá trị byte nhận được theo modulo 256.
//
//d. Gửi kết quả checksum lên server dưới dạng một chuỗi số nguyên. Ví dụ: 64.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là 75,68,12,158,115,63,175,113,189,91,158,56,29,215,122 thì dữ liệu nộp lại là 103.
package new_TCP;
import java.io.*;
import java.net.Socket;

public class TCP_BYTE_CheckSum {
    public static void main(String[] args) {
        String serverHost = "36.50.135.242"; // thay bằng IP server
        int serverPort = 2206;

        String studentCode = "B22DCDT133";
        String qCode = "YaaFsMV4";

        try (
                Socket socket = new Socket(serverHost, serverPort);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        ) {

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            bw.write(request);
            bw.newLine();
            bw.flush();

            // b. Nhận chuỗi byte từ server
            String data = br.readLine();
            System.out.println("Data received: " + data);

            // c. Tính checksum modulo 256
            String[] arr = data.split(",");
            int sum = 0;

            for (String s : arr) {
                sum += Integer.parseInt(s.trim());
            }

            int checksum = sum % 256;

            // d. Gửi kết quả lên server
            bw.write(String.valueOf(checksum));
            bw.newLine();
            bw.flush();

            System.out.println("Checksum sent: " + checksum);

            // e. Đóng kết nối tự động bởi try-with-resources

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}