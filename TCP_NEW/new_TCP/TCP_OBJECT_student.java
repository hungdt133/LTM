//
//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209, sử dụng ObjectInputStream và ObjectOutputStream để trao đổi đối tượng Java.
//
//Yêu cầu
//a. Gửi một đối tượng String chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" bằng ObjectOutputStream. Ví dụ: "B15DCCN999;5AD2B818".
//
//b. Nhận từ server một đối tượng TCP.Student.
//
//c. Chuyển trường code sang chữ hoa và thiết lập gpaLetter theo quy tắc: A nếu GPA >= 3.6, B nếu GPA >= 3.2, C nếu GPA >= 2.5, D nếu GPA >= 2.0, ngược lại F.
//
//d. Gửi lại đối tượng TCP.Student đã cập nhật lên server.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là id=2897;code=sv0uone;gpa=1.19;gpaLetter= thì dữ liệu nộp lại là id=2897;code=SV0UONE;gpa=1.19;gpaLetter=F.
package new_TCP;

import TCP.Student;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCP_OBJECT_student {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "B22DCDT133";
        String qCode = "6eunKKHf";

        try (
                Socket socket = new Socket(host, port);

                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
        ) {

            // a. Gửi request
            String request = studentCode + ";" + qCode;

            oos.writeObject(request);
            oos.flush();

            // b. Nhận Student
            Student student = (Student) ois.readObject();

            // c. Xử lý dữ liệu

            // code -> uppercase
            student.setCode(student.getCode().toUpperCase());

            // gpaLetter
            double gpa = student.getGpa();

            String grade;

            if (gpa >= 3.6) {
                grade = "A";
            } else if (gpa >= 3.2) {
                grade = "B";
            } else if (gpa >= 2.5) {
                grade = "C";
            } else if (gpa >= 2.0) {
                grade = "D";
            } else {
                grade = "F";
            }

            student.setGpaLetter(grade);

            // d. Gửi lại object
            oos.writeObject(student);
            oos.flush();

            System.out.println("Done!");

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}