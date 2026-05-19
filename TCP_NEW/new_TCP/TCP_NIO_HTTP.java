//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng SocketChannel và ByteBuffer để trao đổi thông tin theo giao thức frame: 4 byte độ dài (int32) + payload (UTF-8).
//Lưu ý: server & client đều phải đọc đủ dữ liệu bằng vòng lặp (readFully) do server luôn chia nhỏ dữ liệu khi gửi. Trình tự trao đổi như sau:
//a. Gửi mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode"
//Ví dụ: "B16DCCN999;fkdRJYuX
//b. Nhận dữ liệu từ server gồm đúng 3 frame liên tiếp. Payload của mỗi frame là một phần của cùng một HTTP request, client phải nối 3 payload theo đúng thứ tự để thu được chuỗi HTTP request hoàn chỉnh (các dòng phân tách bởi "\r\n" và kết thúc bằng "\r\n\r\n").
//c. Từ chuỗi HTTP request hoàn chỉnh, trích xuất và gửi lại lên server theo định dạng "METHOD;PATH;HOST" trong đó PATH luôn bao gồm query-string.
//d. Đóng kết nối và kết thúc chương trình.
package new_TCP;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TCP_NIO_HTTP {

    public static void main(String[] args) {

        SocketChannel channel = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String host = "36.50.135.242";
            int port = 2211;

            channel = SocketChannel.open();

            // timeout 5 giây
            channel.socket().setSoTimeout(5000);

            channel.connect(
                    new InetSocketAddress(host, port)
            );

            System.out.println("Đã kết nối server.");

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "tuAgtrsu";

            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận đúng 3 frame
            // =========================================
            StringBuilder httpRequest =
                    new StringBuilder();

            for (int i = 1; i <= 3; i++) {

                String payload =
                        receiveFrame(channel);

                System.out.println(
                        "\nFrame " + i + ":"
                );

                System.out.println(payload);

                httpRequest.append(payload);
            }

            // =========================================
            // HTTP request hoàn chỉnh
            // =========================================
            String fullRequest =
                    httpRequest.toString();

            System.out.println(
                    "\nHTTP Request hoàn chỉnh:"
            );

            System.out.println(fullRequest);

            // =========================================
            // c. Parse METHOD, PATH, HOST
            // =========================================

            String method = "";
            String path = "";
            String hostHeader = "";

            // Tách các dòng
            String[] lines =
                    fullRequest.split("\r\n");

            // =========================================
            // Dòng đầu:
            // GET /abc?a=1 HTTP/1.1
            // =========================================
            if (lines.length > 0) {

                String[] firstLine =
                        lines[0].split(" ");

                if (firstLine.length >= 2) {

                    method = firstLine[0];

                    path = firstLine[1];
                }
            }

            // =========================================
            // Tìm Host:
            // =========================================
            for (String line : lines) {

                if (line.toLowerCase()
                        .startsWith("host:")) {

                    hostHeader =
                            line.substring(5).trim();

                    break;
                }
            }

            // =========================================
            // Kết quả:
            // METHOD;PATH;HOST
            // =========================================
            String result =
                    method
                    + ";"
                    + path
                    + ";"
                    + hostHeader;

            System.out.println(
                    "\nKết quả gửi:"
            );

            System.out.println(result);

            // =========================================
            // Gửi kết quả
            // =========================================
            sendFrame(channel, result);

            System.out.println(
                    "\nĐã gửi kết quả thành công."
            );

        } catch (Exception e) {

            System.out.println(
                    "\nCó lỗi xảy ra:"
            );

            e.printStackTrace();

        } finally {

            // =========================================
            // d. Đóng kết nối
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
    // Gửi frame
    // [4 byte length][payload UTF-8]
    // =========================================
    public static void sendFrame(
            SocketChannel channel,
            String message
    ) throws Exception {

        byte[] data =
                message.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer =
                ByteBuffer.allocate(4 + data.length);

        // int32 big-endian
        buffer.putInt(data.length);

        // payload
        buffer.put(data);

        buffer.flip();

        while (buffer.hasRemaining()) {

            channel.write(buffer);
        }
    }

    // =========================================
    // Nhận frame (readFully)
    // =========================================
    public static String receiveFrame(
            SocketChannel channel
    ) throws Exception {

        // =========================
        // Đọc đủ 4 byte length
        // =========================
        ByteBuffer lengthBuffer =
                ByteBuffer.allocate(4);

        while (lengthBuffer.hasRemaining()) {

            int read =
                    channel.read(lengthBuffer);

            if (read == -1) {

                throw new Exception(
                        "Server closed connection"
                );
            }
        }

        lengthBuffer.flip();

        int length =
                lengthBuffer.getInt();

        // =========================
        // Đọc đủ payload
        // =========================
        ByteBuffer dataBuffer =
                ByteBuffer.allocate(length);

        while (dataBuffer.hasRemaining()) {

            int read =
                    channel.read(dataBuffer);

            if (read == -1) {

                throw new Exception(
                        "Server closed connection"
                );
            }
        }

        dataBuffer.flip();

        byte[] data =
                new byte[length];

        dataBuffer.get(data);

        return new String(
                data,
                StandardCharsets.UTF_8
        );
    }
}