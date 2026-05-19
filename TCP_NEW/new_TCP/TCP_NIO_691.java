//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211, sử dụng SocketChannel và ByteBuffer để trao đổi dữ liệu theo frame.
//
//Yêu cầu
//a. Gửi frame đầu tiên gồm 04 byte độ dài big-endian và payload UTF-8 là chuỗi studentCode;qCode. Ví dụ payload: B21DCCN001;9F8E7D6C.
//
//b. Nhận đúng 03 frame liên tiếp, nối payload theo thứ tự để được danh sách bản ghi invoiceId:quantity:unitPrice phân tách bằng |. Ví dụ: I400:2:120.50|I401:5:90.00.
//
//c. Tính tổng tiền tất cả bản ghi và đếm số dòng có quantity * unitPrice >= 500.
//
//d. Gửi một frame kết quả theo định dạng total=<tổng>;large=<n>, tổng làm tròn 02 chữ số thập phân. Ví dụ: total=691.00;large=1.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package new_TCP;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TCP_NIO_691{

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
            String qCode = "u7DVS4WQ";

            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận đúng 3 frame
            // =========================================
            StringBuilder allPayload =
                    new StringBuilder();

            for (int i = 1; i <= 3; i++) {

                String payload =
                        receiveFrame(channel);

                System.out.println(
                        "\nFrame " + i + ": " + payload
                );

                allPayload.append(payload);
            }

            // =========================================
            // Ghép dữ liệu
            // =========================================
            String data = allPayload.toString();

            System.out.println("\nDữ liệu hoàn chỉnh:");
            System.out.println(data);

            // =========================================
            // c. Xử lý invoice
            // format:
            // invoiceId:quantity:unitPrice
            // =========================================
            String[] records = data.split("\\|");

            double total = 0;

            int largeCount = 0;

            for (String record : records) {

                record = record.trim();

                if (record.isEmpty()) {
                    continue;
                }

                String[] parts =
                        record.split(":");

                if (parts.length != 3) {
                    continue;
                }

                String invoiceId = parts[0];

                int quantity =
                        Integer.parseInt(parts[1]);

                double unitPrice =
                        Double.parseDouble(parts[2]);

                double amount =
                        quantity * unitPrice;

                total += amount;

                if (amount >= 500) {
                    largeCount++;
                }

                System.out.println(
                        "\nInvoice: " + invoiceId
                        + ", quantity=" + quantity
                        + ", unitPrice=" + unitPrice
                        + ", amount=" + amount
                );
            }

            // =========================================
            // d. Gửi kết quả
            // =========================================
            String result = String.format(
                    "total=%.2f;large=%d",
                    total,
                    largeCount
            );

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

        // 4 byte length big-endian
        buffer.putInt(payload.length);

        // payload
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