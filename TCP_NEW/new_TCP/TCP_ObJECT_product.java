//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209, sử dụng ObjectInputStream và ObjectOutputStream để trao đổi đối tượng Java.
//
//Yêu cầu
//a. Gửi một đối tượng String chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" bằng ObjectOutputStream. Ví dụ: "B15DCCN999;5AD2B818".
//
//b. Nhận từ server một đối tượng TCP.Product gồm id, name, price và discount.
//
//c. Tính lại price theo công thức round2(price * (100 - discount) / 100). Giữ nguyên id, name và discount.
//
//d. Gửi lại đối tượng TCP.Product đã được cập nhật price lên server.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là id=9817;name=product-74SD;price=786.64;discount=32 thì dữ liệu nộp lại là id=9817;name=product-74SD;price=534.92;discount=32.
package new_TCP;

import TCP.Product;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCP_ObJECT_product{

    public static void main(String[] args) {

        String host = "36.50.135.242"; // thay IP server
        int port = 2209;

        String studentCode = "B22DCDT133";
        String qCode = "I3k5FmlG";

        try (
                Socket socket = new Socket(host, port);

                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
        ) {

            // a. Gửi mã sinh viên + mã câu hỏi
            String request = studentCode + ";" + qCode;

            oos.writeObject(request);
            oos.flush();

            // b. Nhận Product
            Product product = (Product) ois.readObject();

            System.out.println("Received:");
            System.out.println(product.getName());

            // c. Tính lại price
            double newPrice = product.getPrice()
                    * (100 - product.getDiscount()) / 100.0;

            // Làm tròn 2 chữ số
            newPrice = Math.round(newPrice * 100.0) / 100.0;

            product.setPrice(newPrice);

            // d. Gửi lại object
            oos.writeObject(product);
            oos.flush();

            System.out.println("Updated price: " + newPrice);

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
}