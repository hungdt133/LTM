
package new_TCP;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCP_DATA_category_BOOK {

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // =========================================
            // Kết nối tới server
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
            String qCode = "Q85FXkdZ";

            String request = studentCode + ";" + qCode;

            dos.writeUTF(request);
            dos.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =========================================
            // b. Nhận số lượng bản ghi
            // =========================================
            int n = dis.readInt();

            System.out.println("\nSố bản ghi nhận được: " + n);

            // Lưu doanh thu theo category
            Map<String, Double> revenueMap = new HashMap<>();

            double totalRevenue = 0;

            // =========================================
            // Nhận từng bản ghi
            // =========================================
            for (int i = 0; i < n; i++) {

                String category = dis.readUTF();

                double amount = dis.readDouble();

                int quantity = dis.readInt();

                double revenue = amount * quantity;

                totalRevenue += revenue;

                // Cộng doanh thu theo category
                revenueMap.put(
                        category,
                        revenueMap.getOrDefault(category, 0.0)
                                + revenue
                );

                System.out.println(
                        "Bản ghi " + (i + 1)
                                + ": "
                                + category
                                + ", amount="
                                + amount
                                + ", quantity="
                                + quantity
                                + ", revenue="
                                + revenue
                );
            }

            // =========================================
            // c. Tìm category doanh thu lớn nhất
            // Nếu bằng nhau -> chọn từ điển nhỏ hơn
            // =========================================
            String bestCategory = "";

            double maxRevenue = -1;

            for (Map.Entry<String, Double> entry
                    : revenueMap.entrySet()) {

                String category = entry.getKey();

                double revenue = entry.getValue();

                if (revenue > maxRevenue) {

                    maxRevenue = revenue;
                    bestCategory = category;

                } else if (revenue == maxRevenue) {

                    // So sánh từ điển
                    if (category.compareTo(bestCategory) < 0) {
                        bestCategory = category;
                    }
                }
            }

            // =========================================
            // d. Gửi kết quả
            // =========================================
            String result = String.format(
                    "category=%s;total=%.2f;records=%d",
                    bestCategory,
                    totalRevenue,
                    n
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