import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

class main {
    public static void main(String[] args) {

        /* HashMap declaration */
        HashMap<String, String> hmap = new HashMap<String, String>();

        try {
            //Bus Line
            String password = "123456";

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            System.out.println(sb.toString());

            // IP + Port
            String port_s= "4321";
            String ip_i = InetAddress.getLocalHost().toString();
            String anna = ip_i.concat(port_s);

            MessageDigest mdip = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytesip = md.digest(anna.getBytes(StandardCharsets.UTF_8));

            StringBuilder sbip = new StringBuilder();
            for (byte i : hashInBytesip) {
                sbip.append(String.format("%02x", i));
            }
            System.out.println(sbip.toString());

            // Check if bigger or smaller than IP + Port
            if(password.compareTo(anna) > 0){
                System.out.println("Pass has greater value..");
                hmap.put(password, "bigger");
            }else{
                System.out.print("Pass has lower value..");
                hmap.put(password, "smaller");
            }

            // Display content using Iterator
            Set set = hmap.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                System.out.print("The busLine(key) is: "+ mentry.getKey() + "& is: ");
                System.out.println(mentry.getValue());
            }

            Publisher anya = new Publisher();
            anya.push();

        }catch (Exception e){
            System.out.println("nop");
        }



    }
}
