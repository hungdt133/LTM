package new_TCP;
import java.io.*;
import java.net.*;
import java.util.*;
public class timtudai {
    public static void main(String[] args) throws IOException{
        //
        Socket socket = new Socket("ptit.store", 2208);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //
        String code = "B22DCDT133;Opa3bQtr";
        bw.write(code); 
        bw.newLine(); 
        bw.flush();
        //
        String s = br.readLine();
        System.out.println(s);
        String []a = s.trim().split("\\s+");
        //
        String ans = a[0];int res = a[0].length(); int pos = 1;
        for(int i = 1;i<a.length;i++){
            if(a[i].length() > res){
                ans = a[i];
                res = a[i].length();
                pos = s.indexOf(ans);
            }
        }
        System.out.println(ans);
        System.out.println(pos);
        bw.write(ans); bw.newLine(); bw.flush();
        bw.write(pos + ""); bw.newLine(); bw.flush();
        socket.close();
    }
    
}
