//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B15DCCN001;EE29C059.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data. Ví dụ: kYop10jO;id=kYop10jO;name=SKU-d6kck;basePrice=298.72;taxRate=14.0;discountRate=5.0;finalPrice=0.0.
//
//c. Từ đối tượng UDP.PricedProduct nhận được sau 08 byte requestId, tính finalPrice = round2(basePrice * (1 + taxRate/100) * (1 - discountRate/100)).
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;answer. Ví dụ: kYop10jO;id=kYop10jO;name=SKU-d6kck;basePrice=298.72;taxRate=14.0;discountRate=5.0;finalPrice=323.51.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package UDP_new;

import UDP.PricedProduct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class OBJECT_PricedProduct {

    public static void main(String[] args) {

        DatagramSocket socket = null;

        try {

            // =====================================
            // Kết nối server
            // =====================================
            String host = "36.50.135.242";
            int port = 2209;

            socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(host);

            // =====================================
            // a. Gửi request
            // =====================================
            String studentCode = "B22DCDT133";
            String qCode = "rd8Fo0ce";

            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] sendData =
                    request.getBytes();

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            port
                    );

            socket.send(sendPacket);

            System.out.println("Đã gửi:");
            System.out.println(request);

            // =====================================
            // b. Nhận dữ liệu
            // =====================================
            byte[] receiveData =
                    new byte[65535];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveData,
                            receiveData.length
                    );

            socket.receive(receivePacket);

            byte[] packetData =
                    receivePacket.getData();

            int packetLength =
                    receivePacket.getLength();

            // =====================================
            // 08 byte đầu là requestId
            // =====================================
            String requestId =
                    new String(
                            packetData,
                            0,
                            8
                    );

            System.out.println("\nRequestId:");
            System.out.println(requestId);

            // =====================================
            // Deserialize object
            // =====================================
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(
                            packetData,
                            8,
                            packetLength - 8
                    );

            ObjectInputStream ois =
                    new ObjectInputStream(bais);

            PricedProduct product =
                    (PricedProduct) ois.readObject();

            System.out.println("\nObject nhận được:");
            System.out.println(product);

            // =====================================
            // c. Tính finalPrice
            // =====================================
            double finalPrice =
                    product.getBasePrice()
                    * (1 + product.getTaxRate() / 100.0)
                    * (1 - product.getDiscountRate() / 100.0);

            finalPrice =
                    Math.round(finalPrice * 100.0)
                    / 100.0;

            product.setFinalPrice(finalPrice);

            // =====================================
            // d. Serialize gửi lại
            // =====================================
            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            // ghi requestId trước
            baos.write(requestId.getBytes());

            ObjectOutputStream oos =
                    new ObjectOutputStream(baos);

            oos.writeObject(product);

            oos.flush();

            byte[] resultData =
                    baos.toByteArray();

            DatagramPacket resultPacket =
                    new DatagramPacket(
                            resultData,
                            resultData.length,
                            serverAddress,
                            port
                    );

            socket.send(resultPacket);

            System.out.println(
                    "\nĐã gửi kết quả."
            );

        } catch (Exception e) {

            System.out.println(
                    "\nCó lỗi xảy ra:"
            );

            e.printStackTrace();

        } finally {

            if (socket != null
                    && !socket.isClosed()) {

                socket.close();

                System.out.println(
                        "\nĐã đóng socket."
                );
            }
        }
    }
}