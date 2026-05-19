package RMI;

import java.rmi.registry.*;
import java.util.*;

public class data_phantulonthuK {
    public static void main(String[] args) throws Exception{

        Registry registry = LocateRegistry.getRegistry("36.50.135.242",1099);

        DataService service = (DataService) registry.lookup("RMIDataService");

        String studentCode = "B22DCDT133";
        String qCode = "y5HPK9Vz";

        // request
        String s = (String) service.requestData(studentCode,qCode);

        System.out.println(s);

        // xu ly
        String[] parts = s.split(";");

String[] arr = parts[0].split(",");

int k = Integer.parseInt(parts[1].trim());

ArrayList<Integer> a = new ArrayList<>();

for(String x : arr){
    a.add(Integer.parseInt(x.trim()));
}

Collections.sort(a,Collections.reverseOrder());

int ans = a.get(k - 1);


        // submit
        service.submitData(studentCode,qCode,ans);

        System.out.println("Done");
    }
}
