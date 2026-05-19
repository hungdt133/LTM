//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng byte (BufferedWriter/BufferedReader) theo kịch bản sau:
//a. Gửi một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode"
//Ví dụ: "B15DCCN999;BAA62945
//b. Nhận một chuỗi ngẫu nhiên từ server
//Ví dụ: dgUOo ch2k22ldsOo
//c. Liệt kê các ký tự (là chữ hoặc số) xuất hiện nhiều hơn một lần trong chuỗi và số lần xuất hiện của chúng và gửi lên server
//Ví dụ: d:2,O:2,o:2,2:3,
//d. Đóng kết nối và kết thúc chương trình.
package new_TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCP_CHARACTER_liet_ke_ky_tu_xuat_hien_nhiue_hon_1_lan {

    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2208;

        try {
            // 1. Kết nối
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            // 2. Gửi mã SV + mã đề
            String message = "B22DCDT133;OLJbEagF";
            out.write(message);
            out.newLine(); // 
            out.flush();

            // 3. Nhận chuỗi từ server
            String data = in.readLine();
            System.out.println("Received: " + data);

            // 4. Đếm ký tự
            Map<Character, Integer> map = new LinkedHashMap<>();

            for (char c : data.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    map.put(c, map.getOrDefault(c, 0) + 1);
                }
            }

            // 5. Tạo kết quả
            StringBuilder result = new StringBuilder();

            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                if (entry.getValue() > 1) {
                    result.append(entry.getKey())
                          .append(":")
                          .append(entry.getValue())
                          .append(",");
                }
            }

            // 6. Gửi lại server
            out.write(result.toString());
            out.newLine(); 
            out.flush();

            System.out.println("Sent: " + result);

            // 7. Đóng
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}