package new_TCP;
import java.io.*;
import java.net.*;
import java.util.*;
public class PhepToanCoBan {
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("36.50.135.242", 2207);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String code = "B22DCDT133;zKxiivsV";
        out.writeUTF(code);
        out.flush();
        int a = in.readInt(), b = in.readInt();
        int tong = a + b, tich = a * b;
         out.writeInt(tong); out.writeInt(tich);
        in.close();
        out.close();
        socket.close();
    }
}
