package UDP_new;
//Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208 . Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản dưới đây:
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ";studentCode;qCode"
//Ví dụ: ";B15DCCN001;06D6800D
//b. Nhận thông điệp là một chuỗi từ server theo định dạng "requestId;strInput"
//• requestId là chuỗi ngẫu nhiên duy nhất
//• strInput là chuỗi thông điệp cần xử lý
//c. Thực hiện loại bỏ ký tự đặc biệt, số, ký tự trùng và giữ nguyên thứ tự xuất hiện của chúng. Gửi thông điệp lên server theo định dạng "requestId;strOutput", trong đó strOutput là chuỗi đã được xử lý ở trên
//d. Đóng socket và kết thúc chương trình.
import java.net.*;
public class String_LoaiBoKytu {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        InetAddress sA = InetAddress.getByName("ptit.store");
        int sP = 2208;
        //a.
        String code = ";B22DCDT133;274y8ZFp";
        DatagramPacket dpGui = new DatagramPacket(code.getBytes(), code.length(), sA, sP);
        socket.send(dpGui);
        //b.
        byte []buffer = new byte[1024];
        DatagramPacket dpNhan = new DatagramPacket(buffer, buffer.length);
        socket.receive(dpNhan);
        //
        String s1 = new String(dpNhan.getData());
        System.out.println(s1);
        String []sTmp = s1.trim().split(";");
        String rI = sTmp[0]; String s = sTmp[1];
        //Đếm
        int []cnt = new int[10005];
        for(char x: s.toCharArray()){ 
            if(Character.isAlphabetic(x)) cnt[x]++;
        }
        String res = "";
        for(char x: s.toCharArray()){ 
            if(cnt[x] > 0){
                res+=x;
                cnt[x] = 0;
            }
        }
        res = rI + ";" + res;
        System.out.println(res);
        //Gửi
        DatagramPacket dpGui1 = new DatagramPacket(res.getBytes(), res.length(), sA, sP);
        socket.send(dpGui1);
    }
}
