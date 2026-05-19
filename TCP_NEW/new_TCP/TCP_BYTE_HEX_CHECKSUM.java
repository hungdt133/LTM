
package new_TCP;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCP_BYTE_HEX_CHECKSUM {

    public static void main(String[] args) {

        Socket socket = null;

        try {

            // ==============================
            // Kết nối server TCP
            // ==============================
            String serverHost = "36.50.135.242";
            int serverPort = 2206;

            socket = new Socket(serverHost, serverPort);

            System.out.println("Đã kết nối server.");

            // ==============================
            // Tạo InputStream / OutputStream
            // ==============================
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // ==============================
            // a. Gửi studentCode;qCode
            // ==============================
            String studentCode = "B22DCDT133";
            String qCode = "4KsvWMZw";

            String request = studentCode + ";" + qCode;

            outputStream.write(request.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            System.out.println("Đã gửi:");
            System.out.println(request);

            // ==============================
            // b. Nhận dữ liệu từ server
            // ==============================
            byte[] buffer = new byte[4096];

            int bytesRead = inputStream.read(buffer);

            String response = new String(
                    buffer,
                    0,
                    bytesRead,
                    StandardCharsets.UTF_8
            ).trim();

            System.out.println("\nDữ liệu nhận được:");
            System.out.println(response);

            // ==============================
            // Xử lý các packet
            // ==============================
            String[] packets = response.split("\\|");

            int validPackets = 0;
            int totalPayloadBytes = 0;

            for (String packet : packets) {

                packet = packet.trim();

                if (packet.isEmpty()) {
                    continue;
                }

                System.out.println("\nĐang kiểm tra packet: " + packet);

                // Tách các byte hex
                String[] hexBytes = packet.split("-");

                // Phải có payload + checksum
                if (hexBytes.length < 2) {
                    continue;
                }

                int sum = 0;

                // ==============================
                // Tính tổng payload
                // ==============================
                for (int i = 0; i < hexBytes.length - 1; i++) {

                    String hex = hexBytes[i].trim();

                    int value = Integer.parseInt(hex, 16);

                    sum += value;
                }

                // modulo 256
                int calculatedChecksum = sum % 256;

                // ==============================
                // Lấy checksum cuối packet
                // ==============================
                String checksumHex =
                        hexBytes[hexBytes.length - 1].trim();

                int packetChecksum =
                        Integer.parseInt(checksumHex, 16);

                // ==============================
                // Kiểm tra hợp lệ
                // ==============================
                System.out.println("Checksum tính được: "
                        + calculatedChecksum);

                System.out.println("Checksum packet: "
                        + packetChecksum);

                if (calculatedChecksum == packetChecksum) {

                    validPackets++;

                    // số byte payload
                    totalPayloadBytes += (hexBytes.length - 1);

                    System.out.println("=> Packet hợp lệ");
                } else {
                    System.out.println("=> Packet không hợp lệ");
                }
            }

            // ==============================
            // d. Gửi kết quả
            // ==============================
            String result =
                    "valid=" + validPackets
                    + ";payloadBytes="
                    + totalPayloadBytes;

            System.out.println("\nKết quả gửi về server:");
            System.out.println(result);

            outputStream.write(
                    result.getBytes(StandardCharsets.UTF_8)
            );

            outputStream.flush();

            System.out.println("\nĐã gửi kết quả thành công.");

        } catch (Exception e) {

            System.out.println("\nCó lỗi xảy ra:");
            e.printStackTrace();

        } finally {

            // ==============================
            // e. Đóng kết nối
            // ==============================
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