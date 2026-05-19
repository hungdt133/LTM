//Nội dung
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng
//2209. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản sau:    Đối tượng trao đổi là thể hiện của lớp UDP.Book được mô tả:        Tên đầy đủ lớp: UDP.Book      Các thuộc tính: id (String), title (String), author (String), isbn (String), publishDate (String)      Hàm khởi tạo:          public Book(String id, String title, String author, String isbn, String publishDate)      Trường dữ liệu: private static final long serialVersionUID = 20251107L    Thực hiện:
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode"
//Ví dụ: ";B23DCCN005;eQkvAeId
//b. Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Book từ server. Trong đó, các thuộc tính id, title, author, isbn, và publishDate đã được thiết lập sẵn.
//c. Thực hiện:          Chuẩn hóa title: viết hoa chữ cái đầu của mỗi từ.          Chuẩn hóa author theo định dạng "HỌ, Tên".          Chuẩn hóa mã ISBN theo định dạng "978-3-16-148410-0"          Chuyển đổi publishDate từ yyyy-mm-dd sang mm/yyyy.
//d. Gửi lại đối tượng đã được chuẩn hóa về server với cấu trúc: 08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Book đã được sửa đổi.    Đóng socket và kết thúc chương trình.
package UDP_new;

import UDP.Book;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class OBJECT_Book {

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
            String qCode = "jh1Tm6Hw";

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
            // Deserialize Book
            // =====================================
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(
                            packetData,
                            8,
                            packetLength - 8
                    );

            ObjectInputStream ois =
                    new ObjectInputStream(bais);

            Book book =
                    (Book) ois.readObject();

            System.out.println(
                    "\nBook nhận được:"
            );

            System.out.println(book);

            // =====================================
            // c. Chuẩn hóa title
            // =====================================
            String title =
                    book.getTitle()
                            .toLowerCase()
                            .trim()
                            .replaceAll("\\s+", " ");

            String[] titleWords =
                    title.split(" ");

            StringBuilder normalizedTitle =
                    new StringBuilder();

            for (String word : titleWords) {

                normalizedTitle.append(
                        Character.toUpperCase(
                                word.charAt(0)
                        )
                );

                if (word.length() > 1) {

                    normalizedTitle.append(
                            word.substring(1)
                    );
                }

                normalizedTitle.append(" ");
            }

            book.setTitle(
                    normalizedTitle.toString().trim()
            );

                            // =====================================
                // Chuẩn hóa author
                // HỌ, Tên
                // HỌ là từ ĐẦU TIÊN
                // =====================================
                String author =
                        book.getAuthor()
                                .toLowerCase()
                                .trim()
                                .replaceAll("\\s+", " ");

                String[] authorWords =
                        author.split(" ");

                // từ đầu tiên là HỌ
                String firstName =
                        authorWords[0].toUpperCase();

                StringBuilder remain =
                        new StringBuilder();

                for (int i = 1;
                     i < authorWords.length;
                     i++) {

                    String w = authorWords[i];

                    remain.append(
                            Character.toUpperCase(
                                    w.charAt(0)
                            )
                    );

                    if (w.length() > 1) {

                        remain.append(
                                w.substring(1)
                        );
                    }

                    if (i < authorWords.length - 1) {
                        remain.append(" ");
                    }
                }

                String normalizedAuthor =
                        firstName + ", " + remain;

                book.setAuthor(normalizedAuthor);
            // =====================================
            // Chuẩn hóa ISBN
            // =====================================
            String isbn =
                    book.getIsbn()
                            .replaceAll("[^0-9X]", "");

            if (isbn.length() == 13) {

                isbn =
                        isbn.substring(0, 3)
                        + "-"
                        + isbn.substring(3, 4)
                        + "-"
                        + isbn.substring(4, 6)
                        + "-"
                        + isbn.substring(6, 12)
                        + "-"
                        + isbn.substring(12);
            }

            book.setIsbn(isbn);

            // =====================================
            // yyyy-mm-dd -> mm/yyyy
            // =====================================
            String[] dateParts =
                    book.getPublishDate()
                            .split("-");

            String publishDate =
                    dateParts[1]
                    + "/"
                    + dateParts[0];

            book.setPublishDate(publishDate);

            // =====================================
            // d. Serialize object gửi lại
            // =====================================
            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            baos.write(requestId.getBytes());

            ObjectOutputStream oos =
                    new ObjectOutputStream(baos);

            oos.writeObject(book);

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
                    "\nĐã gửi Book đã chuẩn hóa."
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