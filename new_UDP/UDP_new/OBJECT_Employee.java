//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209 theo cơ chế hai pha.
//
//Yêu cầu
//a. Gửi datagram đầu tiên chứa chuỗi theo định dạng ;studentCode;qCode. Ví dụ: ;B15DCCN001;EE29C059.
//
//b. Nhận phản hồi từ server theo định dạng requestId;data. Ví dụ: yuAdfGfj;id=yuAdfGfj;name=ngUYEN vAn khlp;salary=11516.9;hireDate=2024-02-15.
//
//c. Từ đối tượng UDP.Employee nhận được sau 08 byte requestId, chuẩn hóa tên theo dạng viết hoa chữ cái đầu mỗi từ và tăng lương thêm 8%, làm tròn 02 chữ số thập phân.
//
//d. Gửi datagram nộp kết quả theo định dạng requestId;answer. Ví dụ: yuAdfGfj;id=yuAdfGfj;name=Nguyen Van Khlp;salary=12438.25;hireDate=2024-02-15.
//
//e. Đóng kết nối hoặc kết thúc client sau khi nộp kết quả.
package UDP_new;

import UDP.Employee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class OBJECT_Employee {

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
            String qCode = "T4g1v6pi";

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

            System.out.println(
                    "\nRequestId:"
            );

            System.out.println(requestId);

            // =====================================
            // Deserialize Employee
            // =====================================
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(
                            packetData,
                            8,
                            packetLength - 8
                    );

            ObjectInputStream ois =
                    new ObjectInputStream(bais);

            Employee employee =
                    (Employee) ois.readObject();

            System.out.println(
                    "\nEmployee nhận được:"
            );

            System.out.println(employee);

            // =====================================
            // c. Chuẩn hóa tên
            // =====================================
            String name =
                    employee.getName()
                            .toLowerCase()
                            .trim()
                            .replaceAll("\\s+", " ");

            String[] words =
                    name.split(" ");

            StringBuilder normalizedName =
                    new StringBuilder();

            for (String word : words) {

                normalizedName.append(
                        Character.toUpperCase(
                                word.charAt(0)
                        )
                );

                if (word.length() > 1) {

                    normalizedName.append(
                            word.substring(1)
                    );
                }

                normalizedName.append(" ");
            }

            employee.setName(
                    normalizedName.toString().trim()
            );

            // =====================================
            // Tăng lương 8%
            // =====================================
            double newSalary =
                    employee.getSalary() * 1.08;

            newSalary =
                    Math.round(newSalary * 100.0)
                    / 100.0;

            employee.setSalary(newSalary);

            // =====================================
            // d. Serialize object gửi lại
            // =====================================
            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            // ghi requestId trước
            baos.write(requestId.getBytes());

            ObjectOutputStream oos =
                    new ObjectOutputStream(baos);

            oos.writeObject(employee);

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
                    "\nĐã gửi Employee đã chuẩn hóa."
            );

        } catch (Exception e) {

            System.out.println(
                    "\nCó lỗi xảy ra:"
            );

            e.printStackTrace();

        } finally {

            // =====================================
            // e. Đóng socket
            // =====================================
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