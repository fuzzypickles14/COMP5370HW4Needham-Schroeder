import sun.misc.BASE64Decoder;

import javax.crypto.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Bob.java
 * Created By: Andrew Toomey
 * Created On: 10/31/17 9:38 AM
 * MIT License
 * <p>
 * Copyright (c) 2017 Andrew Toomey
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Bob extends Actor {

    public Bob(String name, ServerSocket socket, SecretKey key, int port) {
        super(name, socket, key, port);
    }

    @Override
    public void run() {
        printLine("Started Thread");
        Socket sendTo;
        try {
            sendTo = getSocket().accept();
            DataOutputStream sendToServer = new DataOutputStream(sendTo.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendTo.getInputStream()));
            Random random = new Random();

            StringBuilder newMessage = new StringBuilder();
            do {
                newMessage.append(inFromServer.readLine() + "\n");
            }
            while (inFromServer.ready());

            printLine("Received: " + newMessage);


            String decryptedString = decryptMessage(newMessage.toString());
            printLine("Decrypted message: " + decryptedString);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void send(String message) {

    }

    @Override
    protected void receive() {

    }
}
