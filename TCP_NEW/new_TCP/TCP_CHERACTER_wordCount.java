//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208, sử dụng BufferedReader và BufferedWriter để trao đổi chuỗi ký tự.
//
//Yêu cầu
//a. Gửi một dòng chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" bằng BufferedWriter, sau đó kết thúc dòng. Ví dụ: "B15DCCN999;BAA62945".
//
//b. Nhận từ server một câu tiếng Anh ngẫu nhiên.
//
//c. Chuẩn hóa chuỗi bằng cách chuyển về chữ thường, loại bỏ ký tự không thuộc [a-z0-9 ], gom nhiều khoảng trắng thành một khoảng trắng, sau đó đếm tần suất xuất hiện của từng từ.
//
//d. Gửi kết quả theo thứ tự từ tăng dần theo từ điển, định dạng word=count|word=count. Ví dụ: account=2|payment=1.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là Payment Account Account ticket REFUND REFUND customer Payment ticket customer ticket. thì dữ liệu nộp lại là account=2|customer=2|payment=2|refund=2|ticket=3.
package new_TCP;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class TCP_CHERACTER_wordCount {

    public static void main(String[] args) {

        String host = "36.50.135.242"; // thay IP server
        int port = 2208;

        String studentCode = "B22DCDT133";
        String qCode = "NIg1O0hS";

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

            // b. Nhận chuỗi
            String data = br.readLine();

            System.out.println("Received:");
            System.out.println(data);

            // c. Chuẩn hóa chuỗi
            data = data.toLowerCase();

            // Loại bỏ ký tự không hợp lệ
            data = data.replaceAll("[^a-z0-9 ]", " ");

            // Gom nhiều khoảng trắng
            data = data.replaceAll("\\s+", " ").trim();

            // Đếm tần suất
            String[] words = data.split(" ");

            TreeMap<String, Integer> map = new TreeMap<>();

            for (String word : words) {

                if (!word.isEmpty()) {
                    map.put(word, map.getOrDefault(word, 0) + 1);
                }
            }

            // d. Tạo kết quả
            StringBuilder result = new StringBuilder();

            boolean first = true;

            for (Map.Entry<String, Integer> entry : map.entrySet()) {

                if (!first) {
                    result.append("|");
                }

                result.append(entry.getKey())
                      .append("=")
                      .append(entry.getValue());

                first = false;
            }

            // Gửi kết quả
            bw.write(result.toString());
            bw.newLine();
            bw.flush();

            System.out.println("Sent:");
            System.out.println(result);

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}