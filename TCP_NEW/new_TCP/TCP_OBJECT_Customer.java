package new_TCP;

import TCP.Customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCP_OBJECT_Customer{

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
            String qCode = "3wi5tCBe";

            String request =
                    studentCode + ";" + qCode;

            oos.writeObject(request);
            oos.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =====================================
            // 2. Nhận Customer
            // =====================================
            Customer customer =
                    (Customer) ois.readObject();

            System.out.println(
                    "\nCustomer nhận được:"
            );

            System.out.println(customer);

            // =====================================
            // 3a. Chuẩn hóa tên
            // vd:
            // nguyen van hai duong
            // -> DUONG, Nguyen Van Hai
            // =====================================
            String fullName =
                    customer.getName()
                            .trim()
                            .toLowerCase()
                            .replaceAll("\\s+", " ");

            String[] words =
                    fullName.split(" ");

            String lastName =
                    words[words.length - 1]
                            .toUpperCase();

            StringBuilder firstMiddle =
                    new StringBuilder();

            for (int i = 0;
                 i < words.length - 1;
                 i++) {

                String w = words[i];

                firstMiddle.append(
                        Character.toUpperCase(
                                w.charAt(0)
                        )
                );

                if (w.length() > 1) {

                    firstMiddle.append(
                            w.substring(1)
                    );
                }

                if (i < words.length - 2) {
                    firstMiddle.append(" ");
                }
            }

            String normalizedName =
                    lastName
                    + ", "
                    + firstMiddle;

            customer.setName(normalizedName);

            // =====================================
            // 3b. Đổi ngày sinh
            // mm-dd-yyyy -> dd/mm/yyyy
            // =====================================
            String dob =
                    customer.getDayOfBirth();

            String[] dateParts =
                    dob.split("-");

            String newDob =
                    dateParts[1]
                    + "/"
                    + dateParts[0]
                    + "/"
                    + dateParts[2];

            customer.setDayOfBirth(newDob);

            // =====================================
            // 3c. Tạo username
            // nguyen van hai duong
            // -> nvhduong
            // =====================================
            StringBuilder username =
                    new StringBuilder();

            for (int i = 0;
                 i < words.length - 1;
                 i++) {

                username.append(
                        words[i].charAt(0)
                );
            }

            username.append(
                    words[words.length - 1]
            );

            customer.setUserName(
                    username.toString()
            );

            System.out.println(
                    "\nCustomer sau xử lý:"
            );

            System.out.println(customer);

            // =====================================
            // Gửi lại Customer
            // =====================================
            oos.writeObject(customer);
            oos.flush();

            System.out.println(
                    "\nĐã gửi Customer cập nhật."
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