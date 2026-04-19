//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng
//2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode"
//Ví dụ: ";B21DCCN795;ylrhZ6UM".
//b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;n;k;z1,z2,...,zn", trong đó:      requestId là chuỗi ngẫu nhiên duy nhất.      n là số phần tử của mảng.      k là kích thước cửa sổ trượt (k < n).      z1 đến zn là n phần tử là số nguyên của mảng.
//c. Thực hiện tìm giá trị lớn nhất trong mỗi cửa sổ trượt với kích thước k trên mảng số nguyên nhận được, và gửi thông điệp lên server theo định dạng "requestId;max1,max2,...,maxm", trong đó max1 đến maxm là các giá trị lớn nhất tương ứng trong mỗi cửa sổ
//Ví dụ: "requestId;5;3;1,5,2,3,4"  Kết quả: "requestId;5,5,4
//d. Đóng socket và kết thúc chương trình.
package UDP_PTIT;
import java.net.*;
import java.io.*;
import java.util.*;

public class UDP_Tim_Max_Tren_Mang {
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            InetAddress serverAddress = InetAddress.getByName("ptit.store");
            int port = 2207;

            socket = new DatagramSocket();

            // a. Gửi request
            String message = ";B22DCDT133;j2h7aI2T";
            DatagramPacket sendPacket = new DatagramPacket(
                    message.getBytes(),
                    message.length(),
                    serverAddress,
                    port
            );
            socket.send(sendPacket);

            // b. Nhận dữ liệu
            byte[] receiveData = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + response);

            String[] parts = response.split(";");
            String requestId = parts[0];
            int n = Integer.parseInt(parts[1]);
            int k = Integer.parseInt(parts[2]);

            String[] arrStr = parts[3].split(",");
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = Integer.parseInt(arrStr[i]);
            }

            // c. Sliding window max
            List<Integer> result = new ArrayList<>();
            Deque<Integer> deque = new ArrayDeque<>();

            for (int i = 0; i < n; i++) {
                // loại phần tử ngoài cửa sổ
                while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                    deque.pollFirst();
                }

                // loại phần tử nhỏ hơn
                while (!deque.isEmpty() && arr[deque.peekLast()] <= arr[i]) {
                    deque.pollLast();
                }

                deque.addLast(i);

                // bắt đầu có window hợp lệ
                if (i >= k - 1) {
                    result.add(arr[deque.peekFirst()]);
                }
            }

            // format output
            StringBuilder output = new StringBuilder(requestId + ";");
            for (int i = 0; i < result.size(); i++) {
                output.append(result.get(i));
                if (i < result.size() - 1) output.append(",");
            }

            // gửi lại server
            DatagramPacket resultPacket = new DatagramPacket(
                    output.toString().getBytes(),
                    output.length(),
                    serverAddress,
                    port
            );
            socket.send(resultPacket);

            System.out.println("Sent: " + output);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}