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
package TCP;

import java.io.*;
import java.net.*;

public class TCP_Object_Client_Laptop_Doi_Dau_Dit {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // gửi
            out.writeObject("B22DCDT133;FQJvsGJy");
            out.flush();

            // nhận
            Laptop laptop = (Laptop) in.readObject();

            // sửa name
            String[] words = laptop.getName().trim().split("\\s+");

            // chỉ swap đầu và cuối
            if (words.length > 1) {
                String temp = words[0];
                words[0] = words[words.length - 1];
                words[words.length - 1] = temp;
            }

            // ghép lại
            String correctName = String.join(" ", words);
            laptop.setName(correctName);

            // sửa quantity
            String q = new StringBuilder(String.valueOf(laptop.getQuantity()))
                    .reverse().toString();
            laptop.setQuantity(Integer.parseInt(q));

            // gửi lại
            out.writeObject(laptop);
            out.flush();

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}