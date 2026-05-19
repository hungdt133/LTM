//Thông tin sản phẩm vì một lý do nào đó đã bị sửa đổi thành không đúng, cụ thể:
//a) Tên sản phẩm bị đổi ngược từ đầu tiên và từ cuối cùng, ví dụ: “lenovo thinkpad T520” bị chuyển thành “T520 thinkpad lenovo”
//b) Số lượng sản phẩm cũng bị đảo ngược giá trị, ví dụ từ 9981 thành 1899    Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectInputStream / ObjectOutputStream) để gửi/nhận và sửa các thông tin bị sai của sản phẩm. Chi tiết dưới đây:
//a) Đối tượng trao đổi là thể hiện của lớp Laptop được mô tả như sau
//• Tên đầy đủ của lớp: TCP.Laptop
//• Các thuộc tính: id int, code String, name String, quantity int
//• Hàm khởi tạo đầy đủ các thuộc tính được liệt kê ở trên
//• Trường dữ liệu: private static final long serialVersionUID = 20150711L
//b. Tương tác với server theo kịch bản
//1) Gửi đối tượng là chuỗi chứa mã sinh viên và mã câu hỏi với định dạng "studentCode;qCode"
//Ví dụ: "B15DCCN999;5AD2B818"
//2) Nhận một đối tượng là thể hiện của lớp Laptop từ server
//3) Sửa các thông tin sai của sản phẩm về tên và số lượng.  Gửi đối tượng vừa được sửa sai lên server
//4) Đóng socket và kết thúc chương trình.
package new_TCP;
import TCP.Laptop;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCP_OBJECT_LAPTOP {

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // =====================================
            // Kết nối server
            // =====================================
            String host = "36.50.135.242";
            int port = 2209;

            socket = new Socket(host, port);

            socket.setSoTimeout(5000);

            System.out.println("Đã kết nối server.");

            // =====================================
            // Tạo Object Stream
            // =====================================
            ObjectOutputStream oos =
                    new ObjectOutputStream(
                            socket.getOutputStream()
                    );

            ObjectInputStream ois =
                    new ObjectInputStream(
                            socket.getInputStream()
                    );

            // =====================================
            // 1. Gửi studentCode;qCode
            // =====================================
            String studentCode = "B22DCDT133";
            String qCode = "FQJvsGJy";

            String request =
                    studentCode + ";" + qCode;

            oos.writeObject(request);
            oos.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =====================================
            // 2. Nhận Laptop
            // =====================================
            Laptop laptop =
                    (Laptop) ois.readObject();

            System.out.println(
                    "\nLaptop nhận được:"
            );

            System.out.println(laptop);

            // =====================================
// Chỉ đổi từ đầu và từ cuối
// =====================================
String[] words =
        laptop.getName()
                .trim()
                .split("\\s+");

if (words.length >= 2) {

    String temp = words[0];

    words[0] = words[words.length - 1];

    words[words.length - 1] = temp;
}

String fixedName =
        String.join(" ", words);

laptop.setName(fixedName);
            // =====================================
            // 3b. Sửa quantity
            // Ví dụ:
            // 1899 -> 9981
            // =====================================
            String quantityStr =
                    String.valueOf(
                            laptop.getQuantity()
                    );

            String reversedQuantity =
                    new StringBuilder(quantityStr)
                            .reverse()
                            .toString();

            int fixedQuantity =
                    Integer.parseInt(
                            reversedQuantity
                    );

            laptop.setQuantity(fixedQuantity);

            System.out.println(
                    "\nLaptop sau khi sửa:"
            );

            System.out.println(laptop);

            // =====================================
            // Gửi lại Laptop
            // =====================================
            oos.writeObject(laptop);
            oos.flush();

            System.out.println(
                    "\nĐã gửi Laptop cập nhật."
            );

        } catch (Exception e) {

            System.out.println(
                    "\nCó lỗi xảy ra:"
            );

            e.printStackTrace();

        } finally {

            // =====================================
            // 4. Đóng kết nối
            // =====================================
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
        }
    }
}