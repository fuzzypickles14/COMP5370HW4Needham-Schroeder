import javax.crypto.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Alice.java
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
public class Alice extends Actor {
    public Alice(String name, ServerSocket socket, SecretKey key, int port) {
        super(name, socket, key, port);
    }

    @Override
    public void run() {
        printLine("Started Thread");
        Socket sendTo;
        try {
            sendTo = new Socket(LOCAL_HOST, Main.CA_PORT);
            Random random = new Random();

            String message = buildMessageToSend("Alice", "Bob", String.valueOf(random.nextInt()));

            send(message, sendTo);

            String receivedMessage = receiveAll(sendTo);


            String decryptedString = decryptMessage(receivedMessage);

            sendTo.close();
            String[] decryptedFields = decryptedString.split(Actor.MESSAGE_DELIMITER);

            sessionKey = buildSessionKey(decryptedFields[3]);


            sendTo = new Socket(LOCAL_HOST, Main.BOB_PORT);

            send(decryptedFields[4] + '\r', sendTo);
            String encryptedNonce = receive(sendTo);


            String nonce = decryptMessage(encryptedNonce, sessionKey);

            String newNonce = String.valueOf(Integer.parseInt(nonce) - 1);

            send(encryptMessage(newNonce,  sessionKey) + "\r", sendTo);

            for (int i = 1; i < 4; i ++) {
                sendWithEncryption(String.format("This is message %d to Bob.", i), sessionKey, sendTo);
                receiveWithDecryption(sendTo, sessionKey);
            }
            sendTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
