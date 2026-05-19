
package new_TCP;

import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class TCP_NIO_URL {

    public static void main(String[] args) {

        SocketChannel channel = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String host = "36.50.135.242";
            int port = 2211;

            channel = SocketChannel.open();

            // timeout 5s
            channel.socket().setSoTimeout(5000);

            channel.connect(
                    new InetSocketAddress(host, port)
            );

            System.out.println("Đã kết nối server.");

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "dNkWkQgG";

            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận đúng 2 frame
            // =========================================
            StringBuilder fullQuery =
                    new StringBuilder();

            for (int i = 1; i <= 2; i++) {

                String payload =
                        receiveFrame(channel);

                System.out.println(
                        "\nFrame " + i + ":"
                );

                System.out.println(payload);

                fullQuery.append(payload);
            }

            // =========================================
            // Query string hoàn chỉnh
            // =========================================
            String queryString =
                    fullQuery.toString();

            System.out.println(
                    "\nQuery String hoàn chỉnh:"
            );

            System.out.println(queryString);

            // =========================================
            // c. Parse + decode + sort key
            // =========================================
            Map<String, String> map =
                    new TreeMap<>();

            String[] pairs =
                    queryString.split("&");

            for (String pair : pairs) {

                if (pair.trim().isEmpty()) {
                    continue;
                }

                String[] kv =
                        pair.split("=", 2);

                String key =
                        URLDecoder.decode(
                                kv[0],
                                StandardCharsets.UTF_8
                        );

                String value = "";

                if (kv.length > 1) {

                    value =
                            URLDecoder.decode(
                                    kv[1],
                                    StandardCharsets.UTF_8
                            );
                }

                map.put(key, value);
            }

            // =========================================
            // Chuẩn hóa:
            // k1=v1;k2=v2;...
            // =========================================
            StringBuilder normalized =
                    new StringBuilder();

            boolean first = true;

            for (Map.Entry<String, String> entry
                    : map.entrySet()) {

                if (!first) {
                    normalized.append(";");
                }

                normalized.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());

                first = false;
            }

            String result =
                    normalized.toString();

            System.out.println(
                    "\nKết quả gửi:"
            );

            System.out.println(result);

            // =========================================
            // Gửi frame kết quả
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
    // Gửi frame:
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

        // big-endian int32
        buffer.putInt(data.length);

        // payload
        buffer.put(data);

        buffer.flip();

        while (buffer.hasRemaining()) {

            channel.write(buffer);
        }
    }

    // =========================================
    // Read fully 4 byte length + payload
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