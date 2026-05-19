//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211, sử dụng SocketChannel và ByteBuffer để trao đổi dữ liệu theo giao thức frame.
//
//Yêu cầu
//a. Gửi frame đầu tiên gồm 4 byte độ dài big-endian và payload UTF-8 là chuỗi theo định dạng "studentCode;qCode". Ví dụ payload: "B15DCCN999;9F8E7D6C".
//
//b. Sau khi gửi frame chứa studentCode;qCode, nhận đúng 03 frame liên tiếp. Mỗi frame gồm 4 byte độ dài big-endian và payload UTF-8.
//
//c. Nối payload của 03 frame theo đúng thứ tự, tính độ dài chuỗi và tổng mã ký tự theo modulo 100000.
//
//d. Gửi một frame kết quả theo định dạng len=<độ_dài>;checksum=<checksum>.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là frame1=abc;frame2=def;frame3=ghi thì dữ liệu nộp lại là len=9;checksum=909.
package new_TCP;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TCP_NIO_Module1000 {

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
            // a. Gửi frame studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "FFzPzIrO";

            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận đúng 3 frame
            // =========================================
            StringBuilder allData =
                    new StringBuilder();

            for (int i = 1; i <= 3; i++) {

                String payload =
                        receiveFrame(channel);

                System.out.println(
                        "\nFrame " + i + ": " + payload
                );

                allData.append(payload);
            }

            // =========================================
            // c. Tính len và checksum
            // =========================================
            String finalData = allData.toString();

            int len = finalData.length();

            int checksum = 0;

            for (char c : finalData.toCharArray()) {

                checksum =
                        (checksum + (int) c) % 100000;
            }

            // =========================================
            // d. Gửi kết quả
            // =========================================
            String result =
                    "len=" + len
                    + ";checksum="
                    + checksum;

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
    // Frame:
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

        // Ghi độ dài big-endian
        buffer.putInt(data.length);

        // Ghi payload
        buffer.put(data);

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

        // Đọc 4 byte độ dài
        ByteBuffer lengthBuffer =
                ByteBuffer.allocate(4);

        while (lengthBuffer.hasRemaining()) {
            channel.read(lengthBuffer);
        }

        lengthBuffer.flip();

        int length = lengthBuffer.getInt();

        // Đọc payload
        ByteBuffer dataBuffer =
                ByteBuffer.allocate(length);

        while (dataBuffer.hasRemaining()) {
            channel.read(dataBuffer);
        }

        dataBuffer.flip();

        byte[] data = new byte[length];

        dataBuffer.get(data);

        return new String(
                data,
                StandardCharsets.UTF_8
        );
    }
}