//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209, sử dụng ObjectInputStream và ObjectOutputStream để trao đổi đối tượng Java.
//
//Yêu cầu
//a. Gửi một đối tượng String chứa mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode bằng ObjectOutputStream. Ví dụ: B21DCCN001;5AD2B818.
//
//b. Nhận từ server một đối tượng TCP.Student. Ví dụ: id=2002;code=sv-ab3-24;gpa=3.45.
//
//c. Chuẩn hóa code bằng cách chuyển sang chữ hoa và loại bỏ mọi ký tự không thuộc [A-Z0-9]; sau đó thiết lập gpaLetter: A nếu GPA >= 3.7, B+ nếu >= 3.4, B nếu >= 3.0, C+ nếu >= 2.5, C nếu >= 2.0, ngược lại F.
//
//d. Gửi lại đối tượng TCP.Student đã cập nhật. Ví dụ: code=SVAB324;gpaLetter=B+.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package new_TCP;
import TCP.Student;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCP_OBJECT_SVAB234 {

    public static void main(String[] args) {

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
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "gyotYcS0";

            String request = studentCode + ";" + qCode;

            oos.writeObject(request);
            oos.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận Student
            // =========================================
            Student student =
                    (Student) ois.readObject();

            System.out.println("\nStudent nhận được:");
            System.out.println(student);

            // =========================================
            // c. Chuẩn hóa code
            // - uppercase
            // - bỏ ký tự ngoài [A-Z0-9]
            // =========================================
            String normalizedCode =
                    student.getCode()
                            .toUpperCase()
                            .replaceAll("[^A-Z0-9]", "");

            student.setCode(normalizedCode);

            // =========================================
            // Xác định GPA Letter
            // =========================================
            double gpa = student.getGpa();

            String gpaLetter;

            if (gpa >= 3.7) {
                gpaLetter = "A";
            } else if (gpa >= 3.4) {
                gpaLetter = "B+";
            } else if (gpa >= 3.0) {
                gpaLetter = "B";
            } else if (gpa >= 2.5) {
                gpaLetter = "C+";
            } else if (gpa >= 2.0) {
                gpaLetter = "C";
            } else {
                gpaLetter = "F";
            }

            student.setGpaLetter(gpaLetter);

            System.out.println("\nStudent sau cập nhật:");
            System.out.println(student);

            // =========================================
            // d. Gửi lại Student
            // =========================================
            oos.writeObject(student);
            oos.flush();

            System.out.println("\nĐã gửi Student cập nhật.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            // =========================================
            // e. Đóng kết nối
            // =========================================
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