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

public class TCP_NIO_909{

    // =========================================
    // Gửi 1 frame
    // =========================================
    public static void sendFrame(SocketChannel channel, String message)
            throws Exception {

        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer =
                ByteBuffer.allocate(4 + data.length);

        // 4 byte length big-endian
        buffer.putInt(data.length);

        // payload
        buffer.put(data);

        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    // =========================================
    // Nhận 1 frame
    // =========================================
    public static String receiveFrame(SocketChannel channel)
            throws Exception {

        // Đọc 4 byte length
        ByteBuffer lenBuffer = ByteBuffer.allocate(4);

        while (lenBuffer.hasRemaining()) {
            channel.read(lenBuffer);
        }

        lenBuffer.flip();

        int length = lenBuffer.getInt();

        // Đọc payload
        ByteBuffer dataBuffer =
                ByteBuffer.allocate(length);

        while (dataBuffer.hasRemaining()) {
            channel.read(dataBuffer);
        }

        dataBuffer.flip();

        byte[] data = new byte[length];

        dataBuffer.get(data);

        return new String(data, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2211;

        String studentCode = "B22DCDT133";
        String qCode = "6uTXoH2y";

        try {

            SocketChannel channel = SocketChannel.open();

            channel.connect(
                    new InetSocketAddress(host, port)
            );

            // =====================================
            // a. Gửi frame studentCode;qCode
            // =====================================

            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            // =====================================
            // b. Nhận đúng 3 frame
            // =====================================

            String frame1 = receiveFrame(channel);
            String frame2 = receiveFrame(channel);
            String frame3 = receiveFrame(channel);

            System.out.println("Frame1: " + frame1);
            System.out.println("Frame2: " + frame2);
            System.out.println("Frame3: " + frame3);

            // =====================================
            // c. Nối chuỗi + checksum
            // =====================================

            String all = frame1 + frame2 + frame3;

            int len = all.length();

            int checksum = 0;

            for (char c : all.toCharArray()) {
                checksum = (checksum + c) % 100000;
            }

            // =====================================
            // d. Gửi kết quả
            // =====================================

            String result =
                    "len=" + len
                            + ";checksum=" + checksum;

            sendFrame(channel, result);

            System.out.println("Sent: " + result);

            // =====================================
            // e. Đóng
            // =====================================

            channel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}