//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208, sử dụng BufferedReader và BufferedWriter để trao đổi chuỗi ký tự.
//
//Yêu cầu
//a. Gửi một dòng chứa mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode bằng BufferedWriter, sau đó kết thúc dòng. Ví dụ: B21DCCN001;BAA62945.
//
//b. Nhận từ server một câu tiếng Anh. Ví dụ: payment ticket payment ticket refund..
//
//c. Chuẩn hóa câu về chữ thường, loại bỏ ký tự không thuộc [a-z0-9 ], sau đó đếm tần suất các cụm hai từ liên tiếp.
//
//d. Gửi tối đa 03 cụm có tần suất cao nhất theo định dạng word_word=count|word_word=count; nếu bằng tần suất thì sắp xếp tăng dần theo từ điển. Ví dụ: payment_ticket=2|ticket_payment=1|ticket_refund=1.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package new_TCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TCP_CHARACTER_CumHaiTu{

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String serverHost = "36.50.135.242";
            int serverPort = 2208;

            socket = new Socket(serverHost, serverPort);

            System.out.println("Đã kết nối server.");

            // =========================================
            // Tạo BufferedReader / BufferedWriter
            // =========================================
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "qnY4Pr9j";

            String request = studentCode + ";" + qCode;

            writer.write(request);
            writer.newLine();
            writer.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận câu tiếng Anh từ server
            // =========================================
            String sentence = reader.readLine();

            System.out.println("\nDữ liệu nhận được:");
            System.out.println(sentence);

            // =========================================
            // c. Chuẩn hóa chuỗi
            // - chuyển lowercase
            // - loại bỏ ký tự ngoài [a-z0-9 ]
            // =========================================
            sentence = sentence.toLowerCase();

            sentence = sentence.replaceAll("[^a-z0-9 ]", " ");

            sentence = sentence.replaceAll("\\s+", " ").trim();

            System.out.println("\nSau khi chuẩn hóa:");
            System.out.println(sentence);

            // =========================================
            // Tách từ
            // =========================================
            String[] words = sentence.split(" ");

            // Đếm bigram
            Map<String, Integer> bigramCount = new HashMap<>();

            for (int i = 0; i < words.length - 1; i++) {

                String bigram =
                        words[i] + "_" + words[i + 1];

                bigramCount.put(
                        bigram,
                        bigramCount.getOrDefault(bigram, 0) + 1
                );
            }

            // =========================================
            // Sắp xếp:
            // - frequency giảm dần
            // - từ điển tăng dần nếu bằng nhau
            // =========================================
            List<Map.Entry<String, Integer>> list =
                    new ArrayList<>(bigramCount.entrySet());

            Collections.sort(list, (a, b) -> {

                // frequency giảm dần
                if (!a.getValue().equals(b.getValue())) {

                    return b.getValue() - a.getValue();
                }

                // từ điển tăng dần
                return a.getKey().compareTo(b.getKey());
            });

            // =========================================
            // d. Lấy tối đa 3 bigram
            // =========================================
            StringBuilder result = new StringBuilder();

            int limit = Math.min(3, list.size());

            for (int i = 0; i < limit; i++) {

                Map.Entry<String, Integer> entry = list.get(i);

                result.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());

                if (i < limit - 1) {
                    result.append("|");
                }
            }

            // =========================================
            // Gửi kết quả
            // =========================================
            String finalResult = result.toString();

            System.out.println("\nKết quả gửi:");
            System.out.println(finalResult);

            writer.write(finalResult);
            writer.newLine();
            writer.flush();

            System.out.println("\nĐã gửi kết quả thành công.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            // =========================================
            // e. Đóng kết nối
            // =========================================
            try {

                if (socket != null && !socket.isClosed()) {

                    socket.close();

                    System.out.println("\nĐã đóng kết nối.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}