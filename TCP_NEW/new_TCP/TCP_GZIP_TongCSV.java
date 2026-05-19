//Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2210, sử dụng GZIPInputStream và GZIPOutputStream để trao đổi dữ liệu đã nén.
//
//Yêu cầu
//a. Gửi chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng "studentCode;qCode" qua GZIPOutputStream, kết thúc bằng ký tự xuống dòng. Ví dụ: "B15DCCN999;A1B2C3D4".
//
//b. Nhận từ server một dòng CSV các số nguyên sau khi giải nén.
//
//c. Tính tổng tất cả các số nguyên trong CSV.
//
//d. Gửi kết quả tổng lên server dưới dạng một dòng dữ liệu được nén GZIP.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
//
//Ví dụ: nếu dữ liệu nhận được là 858,141,217,223,436,927,227,745,827,111,994,902,627,561 thì dữ liệu nộp lại là 7796.
package new_TCP;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TCP_GZIP_TongCSV{

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String host = "36.50.135.242";
            int port = 2210;

            socket = new Socket(host, port);

            System.out.println("Đã kết nối server.");

            // =========================================
            // Tạo GZIP Stream
            // =========================================
            GZIPOutputStream gzipOut =
                    new GZIPOutputStream(
                            socket.getOutputStream()
                    );

            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(gzipOut)
                    );

            GZIPInputStream gzipIn =
                    new GZIPInputStream(
                            socket.getInputStream()
                    );

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(gzipIn)
                    );

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "EZEliwMf";

            String request =
                    studentCode + ";" + qCode;

            writer.write(request);
            writer.newLine();

            // flush để đẩy dữ liệu nén đi
            writer.flush();

            // finish gzip packet
            gzipOut.finish();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận dòng CSV
            // =========================================
            String csv = reader.readLine();

            System.out.println("\nCSV nhận được:");
            System.out.println(csv);

            // =========================================
            // c. Tính tổng
            // =========================================
            String[] numbers = csv.split(",");

            long sum = 0;

            for (String number : numbers) {

                number = number.trim();

                if (!number.isEmpty()) {

                    sum += Integer.parseInt(number);
                }
            }

            System.out.println("\nTổng:");
            System.out.println(sum);

            // =========================================
            // d. Gửi kết quả tổng
            // =========================================

            // Tạo stream gzip mới để gửi lại
            GZIPOutputStream gzipOut2 =
                    new GZIPOutputStream(
                            socket.getOutputStream()
                    );

            BufferedWriter writer2 =
                    new BufferedWriter(
                            new OutputStreamWriter(gzipOut2)
                    );

            writer2.write(String.valueOf(sum));
            writer2.newLine();

            writer2.flush();

            gzipOut2.finish();

            System.out.println(
                    "\nĐã gửi kết quả thành công."
            );

        } catch (Exception e) {

            System.out.println(
                    "\nCó lỗi xảy ra:"
            );

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