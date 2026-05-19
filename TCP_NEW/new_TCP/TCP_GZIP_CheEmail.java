
package new_TCP;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TCP_GZIP_CheEmail {

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
            // Tạo GZIP Output
            // =========================================
            GZIPOutputStream gzipOut =
                    new GZIPOutputStream(
                            socket.getOutputStream()
                    );

            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(gzipOut)
                    );

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "C189DaLV";

            String request =
                    studentCode + ";" + qCode;

            writer.write(request);
            writer.newLine();

            writer.flush();

            // Đẩy dữ liệu GZIP đi
            gzipOut.finish();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // Tạo GZIP Input
            // =========================================
            GZIPInputStream gzipIn =
                    new GZIPInputStream(
                            socket.getInputStream()
                    );

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(gzipIn)
                    );

            // =========================================
            // b. Nhận log đã nén
            // =========================================
            String logData = reader.readLine();

            System.out.println("\nLog nhận được:");
            System.out.println(logData);

            // =========================================
            // c. Che dữ liệu nhạy cảm
            // =========================================

            // Che email
            logData = logData.replaceAll(
                    "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+",
                    "[EMAIL]"
            );

            // Che phone
            logData = logData.replaceAll(
                    "\\b\\d{10,11}\\b",
                    "[PHONE]"
            );

            // Che token
            logData = logData.replaceAll(
                    "token=[^\\s|]+",
                    "token=[TOKEN]"
            );

            System.out.println("\nLog sau xử lý:");
            System.out.println(logData);

            // =========================================
            // d. Gửi log đã xử lý
            // =========================================

            GZIPOutputStream gzipOut2 =
                    new GZIPOutputStream(
                            socket.getOutputStream()
                    );

            BufferedWriter writer2 =
                    new BufferedWriter(
                            new OutputStreamWriter(gzipOut2)
                    );

            writer2.write(logData);
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