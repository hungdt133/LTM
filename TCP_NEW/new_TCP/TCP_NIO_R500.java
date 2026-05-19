//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211, sử dụng SocketChannel và ByteBuffer để trao đổi dữ liệu theo frame.
//
//Yêu cầu
//a. Gửi frame đầu tiên gồm 04 byte độ dài big-endian và payload UTF-8 là chuỗi studentCode;qCode. Ví dụ payload: B21DCCN001;9F8E7D6C.
//
//b. Nhận đúng 02 frame liên tiếp, nối payload theo thứ tự để được danh sách bản ghi id,type,score phân tách bằng |. Ví dụ: R500,AUTH,90|R501,PAY,45.
//
//c. Phân loại id vào PASS nếu score >= 80, REVIEW nếu score từ 50 đến 79, và FAIL nếu score nhỏ hơn 50; giữ thứ tự xuất hiện.
//
//d. Gửi một frame kết quả theo định dạng PASS=id,id;REVIEW=id;FAIL=id. Ví dụ: PASS=R500;REVIEW=;FAIL=R501.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package new_TCP;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TCP_NIO_R500 {

    public static void main(String[] args) {

        SocketChannel channel = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String host = "36.50.135.242";
            int port = 2211;

            channel = SocketChannel.open();

            channel.connect(
                    new InetSocketAddress(host, port)
            );

            System.out.println("Đã kết nối server.");

            // =========================================
            // a. Gửi frame đầu tiên
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "KCLLMOPZ";

            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận đúng 2 frame
            // =========================================
            StringBuilder allPayload =
                    new StringBuilder();

            for (int i = 1; i <= 2; i++) {

                String payload =
                        receiveFrame(channel);

                System.out.println(
                        "\nFrame " + i + ": " + payload
                );

                allPayload.append(payload);
            }

            // =========================================
            // Ghép dữ liệu hoàn chỉnh
            // =========================================
            String data = allPayload.toString();

            System.out.println("\nDữ liệu hoàn chỉnh:");
            System.out.println(data);

            // =========================================
            // c. Phân loại score
            // =========================================
            String[] records = data.split("\\|");

            List<String> passList = new ArrayList<>();
            List<String> reviewList = new ArrayList<>();
            List<String> failList = new ArrayList<>();

            for (String record : records) {

                record = record.trim();

                if (record.isEmpty()) {
                    continue;
                }

                // format:
                // id,type,score
                String[] parts =
                        record.split(",");

                if (parts.length != 3) {
                    continue;
                }

                String id = parts[0];

                int score =
                        Integer.parseInt(parts[2]);

                // PASS
                if (score >= 80) {

                    passList.add(id);

                }
                // REVIEW
                else if (score >= 50) {

                    reviewList.add(id);

                }
                // FAIL
                else {

                    failList.add(id);
                }

                System.out.println(
                        "ID=" + id
                        + ", score=" + score
                );
            }

            // =========================================
            // Tạo chuỗi kết quả
            // =========================================
            String pass =
                    String.join(",", passList);

            String review =
                    String.join(",", reviewList);

            String fail =
                    String.join(",", failList);

            String result =
                    "PASS=" + pass
                    + ";REVIEW=" + review
                    + ";FAIL=" + fail;

            // =========================================
            // d. Gửi frame kết quả
            // =========================================
            System.out.println("\nKết quả gửi:");
            System.out.println(result);

            sendFrame(channel, result);

            System.out.println(
                    "\nĐã gửi kết quả thành công."
            );

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            // =========================================
            // e. Đóng kết nối
            // =========================================
            try {

                if (channel != null
                        && channel.isOpen()) {

                    channel.close();

                    System.out.println(
                            "\nĐã đóng kết nối."
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =========================================
    // Hàm gửi frame
    // =========================================
    public static void sendFrame(
            SocketChannel channel,
            String message
    ) throws Exception {

        byte[] payload =
                message.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer =
                ByteBuffer.allocate(4 + payload.length);

        // Ghi độ dài big-endian
        buffer.putInt(payload.length);

        // Ghi payload
        buffer.put(payload);

        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    // =========================================
    // Hàm nhận frame
    // =========================================
    public static String receiveFrame(
            SocketChannel channel
    ) throws Exception {

        // Đọc 4 byte length
        ByteBuffer lengthBuffer =
                ByteBuffer.allocate(4);

        while (lengthBuffer.hasRemaining()) {
            channel.read(lengthBuffer);
        }

        lengthBuffer.flip();

        int length = lengthBuffer.getInt();

        // Đọc payload
        ByteBuffer payloadBuffer =
                ByteBuffer.allocate(length);

        while (payloadBuffer.hasRemaining()) {
            channel.read(payloadBuffer);
        }

        payloadBuffer.flip();

        byte[] data = new byte[length];

        payloadBuffer.get(data);

        return new String(
                data,
                StandardCharsets.UTF_8
        );
    }
}