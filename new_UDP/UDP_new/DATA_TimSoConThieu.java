package UDP_new;
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng
//2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng “;studentCode;qCode”
//Ví dụ: “;B15DCCN001;371EA16D”
//b. Nhận thông điệp là một chuỗi từ server theo định dạng “requestId; n; A1,A2,...An”, với  - requestId là chuỗi ngẫu nhiên duy nhất  - n là một số ngẫu nhiên nhỏ hơn
//100.  -            A1, A2 ... Am với m <= n là các giá trị nguyên liên tiếp, nhỏ hơn hoặc bằng n và không trùng nhau
//Ví dụ: requestId;10;2,3,5,6,9
//c. Tìm kiếm các giá trị còn thiếu và gửi lên server theo định dạng “requestId;B1,B2,...,Bm”
//Ví dụ: requestId;1,4,7,8,10
//d. Đóng socket và kết thúc chương trình.
import java.net.*;
public class DATA_TimSoConThieu {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        InetAddress sA = InetAddress.getByName("36.50.135.242");
        int sP = 2207;
        //a.
        String code = ";B22DCDT133;kOjbaK5P";
        DatagramPacket dpGui = new DatagramPacket(code.getBytes(), code.length(), sA, sP);
        socket.send(dpGui);
        //b.
        byte []buffer = new byte[1024];
        DatagramPacket dpNhan = new DatagramPacket(buffer, buffer.length);
        socket.receive(dpNhan);
        //
        String s = new String(dpNhan.getData());
        System.out.println(s);
        String []sTmp = s.trim().split(";");
        String rI = sTmp[0]; int n = Integer.parseInt(sTmp[1]);
        String num = sTmp[2];
        //Xử lý mảng - Đếm
        int []cnt = new int[1000005];
        String []tmp = num.trim().split(",");
        for(int i = 0;i<tmp.length;i++) cnt[Integer.parseInt(tmp[i])]++;
        //Thiếu
        String res = rI + ";";
        for(int i = 1;i<=n;i++){
          if(cnt[i]==0) res+=String.format("%d,", i);  
        }
        res = res.substring(0, res.length() - 1);
        System.out.println(res);
        //Gửi
        DatagramPacket dpGui1 = new DatagramPacket(res.getBytes(), res.length(), sA, sP);
        socket.send(dpGui1);
    }
}
