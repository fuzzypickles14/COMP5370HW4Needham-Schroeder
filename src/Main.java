import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        //init threads
        int alicePort = 50001;
        int bobPort = 50002;
        int caPort = 50003;
        try {
            Alice alice = new Alice("Alice", new Socket("Alice", alicePort), "asdf", alicePort);
            Bob bob = new Bob("Bob", new Socket("Bob", bobPort), "asdf", bobPort);
            CentralAuthority centralAuthority = new CentralAuthority("CentralAuthority", new Socket("CentralAuthority", caPort), "asdf", caPort);
        //connect threads

        //alice send to cathy Alice || Bob || r1

        // cathy send to alice { Alice || Bob || r1 || ks || { Alice || ks } kBC } kAC

        // alice to bob { Alice || ks } kBC

        // bob to alice { r2 } ks

        //alice to bob { r2 - 1 } ks











        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
