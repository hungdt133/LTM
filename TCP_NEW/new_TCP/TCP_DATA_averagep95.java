//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2207, sử dụng DataInputStream và DataOutputStream để trao đổi dữ liệu số thực.
//
//Yêu cầu
//a. Gửi một chuỗi chứa mã sinh viên và mã câu hỏi bằng phương thức writeUTF theo định dạng "studentCode;qCode". Ví dụ: "B15DCCN999;1D25ED92".
//
//b. Nhận từ server một số nguyên n, sau đó nhận tiếp n giá trị double.
//
//c. Tính giá trị trung bình, giá trị p95 tại vị trí ceil(n * 0.95) - 1 sau khi sắp xếp tăng dần, và số phần tử lớn hơn giá trị trung bình.
//
//d. Gửi kết quả bằng writeUTF theo định dạng average;p95;aboveAvg, trong đó các giá trị số thực được làm tròn 02 chữ số thập phân. Ví dụ: 18.45;30.12;4.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là 90.0,47.66,81.95,93.56,9.72,77.04,13.72,88.62,5.34,85.55 thì dữ liệu nộp lại là 59.32;93.56;6.
package new_TCP;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class TCP_DATA_averagep95{

    public static void main(String[] args) {

        String host = "36.50.135.242"; // thay IP server
        int port = 2207;

        String studentCode = "B22DCDT133";
        String qCode = "hvaFoyGD";

        try (
                Socket socket = new Socket(host, port);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ) {

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;

            dos.writeUTF(request);
            dos.flush();

            // b. Nhận n
            int n = dis.readInt();

            double[] arr = new double[n];

            double sum = 0;

            for (int i = 0; i < n; i++) {
                arr[i] = dis.readDouble();
                sum += arr[i];
            }

            // c. Tính average
            double average = sum / n;

            // Tính p95
            Arrays.sort(arr);

            int index = (int) Math.ceil(n * 0.95) - 1;

            double p95 = arr[index];

            // Đếm số phần tử > average
            int aboveAvg = 0;

            for (double x : arr) {
                if (x > average) {
                    aboveAvg++;
                }
            }

            // d. Format kết quả làm tròn 2 chữ số
            String result = String.format("%.2f;%.2f;%d",
                    average,
                    p95,
                    aboveAvg);

            dos.writeUTF(result);
            dos.flush();

            System.out.println("Sent: " + result);

            // e. Đóng kết nối tự động

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}