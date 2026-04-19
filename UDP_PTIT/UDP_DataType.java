
package UDP_PTIT;

import java.net.*;
import java.io.*;
import java.util.*;
public class UDP_DataType {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName("ptit.store");
            int port = 2207;

            socket = new DatagramSocket();

            // a. Gửi request
            String message = ";B22DCDT133;qqb7b2vp";
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length, serverAddress, port);
            socket.send(sendPacket);

            // b. Nhận phản hồi
            byte[] receiveData = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + response);

            String[] parts = response.split(";",2);
            String requestId = parts[0];
            String data = parts[1];
            //
            String[] arr = data.split(",");
            Arrays.sort(arr,(a,b) -> {
                int va = Integer.parseInt(a.split(":")[1]);
                int vb = Integer.parseInt(b.split(":")[1]);
                return Integer.compare(va,vb);
            });
            List<String> resultList = new ArrayList<>();
            for(String s : arr){
                resultList.add(s.split(":")[0]);
            }
            String sorted = String.join(",",resultList);
            String result = requestId + ";" + sorted;
            // gửi lại server
            byte[] resultData = result.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(
                    resultData, resultData.length, serverAddress, port);

            socket.send(resultPacket);

            System.out.println("Sent: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}