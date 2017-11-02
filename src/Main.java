import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static int ALICE_PORT = 51501;
    public static int BOB_PORT = 57622;
    public static int CA_PORT = 51795;
    public static void main(String[] args) {
        //init threads

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);

            SecretKey aliceKey = keyGenerator.generateKey();
            SecretKey bobKey = keyGenerator.generateKey();

            Alice alice = new Alice("Alice", new ServerSocket(ALICE_PORT), aliceKey, ALICE_PORT);
            Bob bob = new Bob("Bob", new ServerSocket(BOB_PORT), bobKey, BOB_PORT);
            CentralAuthority centralAuthority = new CentralAuthority("Central Authority", new ServerSocket(CA_PORT), CA_PORT);

            centralAuthority.addKey("Alice", aliceKey);
            centralAuthority.addKey("Bob", bobKey);



            Thread aliceThread = new Thread(alice);
            Thread bobThread = new Thread(bob);
            Thread caThread = new Thread(centralAuthority);

            aliceThread.start();
            bobThread.start();
            caThread.start();









        //connect threads

        //alice send to cathy Alice || Bob || r1

        // cathy send to alice { Alice || Bob || r1 || ks || { Alice || ks } kBC } kAC

        // alice to bob { Alice || ks } kBC

        // bob to alice { r2 } ks

        //alice to bob { r2 - 1 } ks

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
