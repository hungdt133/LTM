//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211, sử dụng SocketChannel và ByteBuffer để trao đổi dữ liệu theo giao thức frame.
//
//Yêu cầu
//a. Gửi frame đầu tiên gồm 4 byte độ dài big-endian và payload UTF-8 là chuỗi theo định dạng "studentCode;qCode". Ví dụ payload: "B15DCCN999;9F8E7D6C".
//
//b. Nhận 02 frame liên tiếp, mỗi frame chứa một phần của danh sách bản ghi. Bản ghi có định dạng id,operation,status và được phân tách bởi ký tự |.
//
//c. Gom các id theo từng trạng thái OK, FAIL và RETRY, giữ nguyên thứ tự xuất hiện trong dữ liệu.
//
//d. Gửi một frame kết quả theo định dạng OK=id,id;FAIL=id;RETRY=id.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là 300,op0,OK|301,op1,FAIL|302,op2,RETRY thì dữ liệu nộp lại là OK=300;FAIL=301;RETRY=302.
package new_TCP;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TCP_NIO_Frame_Status {

  
    public static void sendFrame(
            SocketChannel channel,
            String message
    ) throws Exception {

        byte[] data =
                message.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer =
                ByteBuffer.allocate(4 + data.length);

        // 4 byte big-endian length
        buffer.putInt(data.length);

        // payload
        buffer.put(data);

        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    public static String receiveFrame(
            SocketChannel channel
    ) throws Exception {

        // đọc length
        ByteBuffer lenBuffer =
                ByteBuffer.allocate(4);

        while (lenBuffer.hasRemaining()) {
            channel.read(lenBuffer);
        }

        lenBuffer.flip();

        int length = lenBuffer.getInt();

        // đọc payload
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
        String qCode = "a43FKeye";

        try {

            SocketChannel channel =
                    SocketChannel.open();

            channel.connect(
                    new InetSocketAddress(host, port)
            );


            String request =
                    studentCode + ";" + qCode;

            sendFrame(channel, request);

            

            String frame1 = receiveFrame(channel);
            String frame2 = receiveFrame(channel);

            System.out.println("Frame1:");
            System.out.println(frame1);

            System.out.println("Frame2:");
            System.out.println(frame2);

            // nối dữ liệu
            String allData = frame1 + frame2;

           
            List<String> okList = new ArrayList<>();
            List<String> failList = new ArrayList<>();
            List<String> retryList = new ArrayList<>();

            String[] records = allData.split("\\|");

            for (String record : records) {

                String[] parts = record.split(",");

                if (parts.length < 3) {
                    continue;
                }

                String id = parts[0];
                String status = parts[2];

                switch (status) {

                    case "OK":
                        okList.add(id);
                        break;

                    case "FAIL":
                        failList.add(id);
                        break;

                    case "RETRY":
                        retryList.add(id);
                        break;
                }
            }

           
            String result =
                    "OK=" + String.join(",", okList)
                    + ";FAIL=" + String.join(",", failList)
                    + ";RETRY=" + String.join(",", retryList);

            sendFrame(channel, result);

            System.out.println("Sent:");
            System.out.println(result);

           
            channel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}