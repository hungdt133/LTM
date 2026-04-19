package UDP_PTIT;

//Thông tin sản phẩm vì một lý do nào đó đã bị sửa đổi thành không đúng, cụ thể:
//a. Tên sản phẩm bị đổi ngược từ đầu tiên và từ cuối cùng, ví dụ: “lenovo thinkpad T520” bị chuyển thành “T520 thinkpad lenovo”
//b. Số lượng sản phẩm cũng bị đảo ngược giá trị, ví dụ từ 9981 thành 1899    Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng
//2209. Yêu cầu là xây dựng một chương trình client giao tiếp với server để gửi/nhận các sản phẩm theo mô tả dưới đây:
//a. Đối tượng trao đổi là thể hiện của lớp Product được mô tả như sau
//• Tên đầy đủ của lớp: UDP.Product
//• Các thuộc tính: id String, code String, name String, quantity int
//• Một hàm khởi tạo có đầy đủ các thuộc tính được liệt kê ở trên
//• Trường dữ liệu: private static final long serialVersionUID = 20161107
//b. Giao tiếp với server theo kịch bản
//•       Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”
//Ví dụ: “;B15DCCN001;EE29C059”
//• Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Product từ server. Trong đối tượng này, các thuộc tính id, name và quantity đã được thiết lập giá trị.
//• Sửa các thông tin sai của đối tượng về tên và số lượng như mô tả ở trên và gửi đối tượng vừa được sửa đổi lên server theo cấu trúc:  08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Product đã được sửa đổi.
//• Đóng socket và kết thúc chương trình.
import java.net.*;
import java.io.*;
import UDP.Product;

public class UDPClientProduct {
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            InetAddress serverAddress = InetAddress.getByName("ptit.store");
            int port = 2209;

            socket = new DatagramSocket();

            // a. Gửi request
            String message = ";B22DCDT133;bdjlLCJB";
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

            byte[] data = receivePacket.getData();

            // 👉 Tách requestId (8 bytes)
            String requestId = new String(data, 0, 8);

            // 👉 Deserialize object
            ByteArrayInputStream bais = new ByteArrayInputStream(data, 8, receivePacket.getLength() - 8);
            ObjectInputStream ois = new ObjectInputStream(bais);

            Product product = (Product) ois.readObject();

            // ======================
            // c. Xử lý dữ liệu
            // ======================

            // 🔹 Sửa name (đảo từ)
            String[] words = product.getName().trim().split("\\s+");

                if (words.length > 1) {
                    String temp = words[0];
                    words[0] = words[words.length - 1];
                    words[words.length - 1] = temp;
                }

                String newName = String.join(" ", words);
                product.setName(newName);

            // 🔹 Sửa quantity (đảo số)
            String reversedQty = new StringBuilder(
                    String.valueOf(product.getQuantity())
            ).reverse().toString();

            product.setQuantity(Integer.parseInt(reversedQty));

            // ======================
            // d. Serialize lại object
            // ======================
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(product);
            oos.flush();

            byte[] objBytes = baos.toByteArray();

            // 👉 Ghép requestId + object
            ByteArrayOutputStream finalStream = new ByteArrayOutputStream();
            finalStream.write(requestId.getBytes());
            finalStream.write(objBytes);

            byte[] sendData = finalStream.toByteArray();

            DatagramPacket resultPacket = new DatagramPacket(
                    sendData,
                    sendData.length,
                    serverAddress,
                    port
            );

            socket.send(resultPacket);

            System.out.println("Sent fixed product!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}