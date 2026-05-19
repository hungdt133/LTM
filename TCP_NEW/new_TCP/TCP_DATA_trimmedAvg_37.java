
package new_TCP;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class TCP_DATA_trimmedAvg_37 {

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // =========================================
            // Kết nối server
            // =========================================
            String serverHost = "36.50.135.242";
            int serverPort = 2207;

            socket = new Socket(serverHost, serverPort);

            System.out.println("Đã kết nối server.");

            // =========================================
            // Tạo DataInputStream / DataOutputStream
            // =========================================
            DataInputStream dis =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream dos =
                    new DataOutputStream(socket.getOutputStream());

            // =========================================
            // a. Gửi studentCode;qCode
            // =========================================
            String studentCode = "B22DCDT133";
            String qCode = "oMh2aVVc";

            String request = studentCode + ";" + qCode;

            dos.writeUTF(request);
            dos.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận số lượng phần tử
            // =========================================
            int n = dis.readInt();

            System.out.println("\nSố lượng giá trị: " + n);

            double[] values = new double[n];

            // =========================================
            // Nhận các giá trị double
            // =========================================
            double totalSum = 0;

            for (int i = 0; i < n; i++) {

                values[i] = dis.readDouble();

                totalSum += values[i];

                System.out.println(
                        "values[" + i + "] = " + values[i]
                );
            }

            // =========================================
            // c. Tính trimmed average
            // Loại 1 min và 1 max
            // =========================================
            double trimmedAvg = 0;

            if (n > 2) {

                double[] sorted = values.clone();

                Arrays.sort(sorted);

                double trimmedSum = 0;

                for (int i = 1; i < sorted.length - 1; i++) {
                    trimmedSum += sorted[i];
                }

                trimmedAvg = trimmedSum / (n - 2);
            }

            // =========================================
            // Tính average toàn bộ dãy
            // =========================================
            double average = totalSum / n;

            // =========================================
            // Tính standard deviation
            // stddev = sqrt(sum((x-avg)^2)/n)
            // =========================================
            double varianceSum = 0;

            for (double value : values) {

                varianceSum += Math.pow(value - average, 2);
            }

            double variance = varianceSum / n;

            double stddev = Math.sqrt(variance);

            // =========================================
            // Đếm outliers
            // value > average + stddev
            // =========================================
            int outliers = 0;

            double threshold = average + stddev;

            for (double value : values) {

                if (value > threshold) {
                    outliers++;
                }
            }

            // =========================================
            // d. Gửi kết quả
            // =========================================
            String result = String.format(
                    "trimmedAvg=%.2f;stddev=%.2f;outliers=%d",
                    trimmedAvg,
                    stddev,
                    outliers
            );

            System.out.println("\nKết quả gửi:");
            System.out.println(result);

            dos.writeUTF(result);
            dos.flush();

            System.out.println("\nĐã gửi kết quả thành công.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            // =========================================
            // e. Đóng kết nối
            // =========================================
            try {

                if (socket != null && !socket.isClosed()) {

                    socket.close();

                    System.out.println("\nĐã đóng kết nối.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}