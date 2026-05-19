//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209, sử dụng ObjectInputStream và ObjectOutputStream để trao đổi đối tượng Java.
//
//Yêu cầu
//a. Gửi một đối tượng String chứa mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode bằng ObjectOutputStream. Ví dụ: B21DCCN001;5AD2B818.
//
//b. Nhận từ server một đối tượng TCP.Product gồm id, name, price và discount. Ví dụ: id=2001;price=1000.00;discount=10.
//
//c. Tính lại price theo công thức round2(price * (100 - discount) / 100 * 1.08 + fee), trong đó fee = 15 nếu giá gốc từ 800 trở lên, ngược lại fee = 0.
//
//d. Gửi lại đối tượng TCP.Product đã cập nhật price, giữ nguyên id, name và discount. Ví dụ giá nộp lại: 987.00.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package new_TCP;

import TCP.Product;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCP_OBJECT_Gia987{

    public static void main(String[] args) throws IOException {

        Socket socket = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String serverHost = "36.50.135.242";
            int serverPort = 2209;

            socket = new Socket(serverHost, serverPort);

            System.out.println("Đã kết nối server.");

            // =========================================
            // Tạo Object Stream
            // =========================================
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream ois =
                    new ObjectInputStream(socket.getInputStream());

            // =========================================
            // Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "dCbdKdDT";

            String request = studentCode + ";" + qCode;

            oos.writeObject(request);
            oos.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // Nhận Product1 từ server
            // =========================================
            Product product =
                    (Product) ois.readObject();

            System.out.println("\nProduct nhận được:");
            System.out.println(product);

            // =========================================
            // Tính price mới
            // =========================================
            double oldPrice = product.getPrice();

            double discount = product.getDiscount();

            double fee = 0;

            if (oldPrice >= 800) {
                fee = 15;
            }

            double newPrice =
                    oldPrice
                    * (100 - discount)
                    / 100
                    * 1.08
                    + fee;

            // Làm tròn 2 chữ số
            newPrice =
                    Math.round(newPrice * 100.0) / 100.0;

            // Cập nhật price
            product.setPrice(newPrice);

            System.out.println("\nPrice mới:");
            System.out.println(newPrice);

            // =========================================
            // Gửi lại object
            // =========================================
            oos.writeObject(product);
            oos.flush();

            System.out.println("\nĐã gửi Product cập nhật.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            try {

                if (socket != null
                        && !socket.isClosed()) {

                    socket.close();

                    System.out.println(
                            "\nĐã đóng kết nối."
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } socket.close();
    }  
 
}