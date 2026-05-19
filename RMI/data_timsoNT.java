package RMI;

import java.rmi.registry.*;
import java.util.*;

public class data_timsoNT {

    public static boolean nt(int n){

        if(n < 2){
            return false;
        }

        for(int i = 2;i * i <= n;i++){
            if(n % i == 0){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception{

        Registry registry = LocateRegistry.getRegistry("36.50.135.242",1099);

        DataService service = (DataService) registry.lookup("RMIDataService");

        String studentCode = "B22DCDT133";
        String qCode = "DKdLstlR";

        // request
        int n = (Integer) service.requestData(studentCode,qCode);

        System.out.println(n);

        // xu ly
        ArrayList<Integer> ans = new ArrayList<>();

        for(int i = 2;i <= n;i++){

            if(nt(i)){
                ans.add(i);
            }
        }

        // submit
        service.submitData(studentCode,qCode,ans);

        System.out.println("Done");
    }
}
