import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        //init threads
        int alicePort = 51501;
        int bobPort = 57621;
        int caPort = 51794;
        String aliceKey = "cdLraSpCJ1ng7eg9PaZmpD3T2nMAao2ZMwxU7Jk+/AU=";

        String bobKey = "WFhNuvmc4GUxQ+z1xUyPjJXEDJ5wgZgSq3LzDETes8I=";

        try {
            Alice alice = new Alice("Alice", new ServerSocket(alicePort), "asdf", alicePort);
            Bob bob = new Bob("Bob", new ServerSocket(bobPort), "asdf", bobPort);
            CentralAuthority centralAuthority = new CentralAuthority("CentralAuthority", new ServerSocket(caPort), "asdf", caPort);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
