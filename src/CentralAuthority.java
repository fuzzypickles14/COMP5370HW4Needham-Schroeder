import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * CentralAuthority.java
 * Created By: Andrew Toomey
 * Created On: 10/31/17 9:39 AM
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
public class CentralAuthority extends Actor {

    private HashMap<String, SecretKey> keys = new HashMap<>();
    public CentralAuthority(String name, ServerSocket socket, int port) {
        super(name, socket, null, port);
    }

    @Override
    public void run() {
        printLine("Started Thread");
        try {
            Socket connected = getSocket().accept();
            String message = receive(connected);

            String[] messages = message.split(MESSAGE_DELIMITER);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);

            String sessionKey = new BASE64Encoder().encode(keyGenerator.generateKey().getEncoded());

            String messageForBob = buildMessage(messages[0], sessionKey);

            String encryptedMessageForBob = encryptMessage(messageForBob, keys.get(messages[1]));


            String response = buildMessageToSend(messages[0],messages[1], messages[2], sessionKey, encryptedMessageForBob);

            String encryptedString = encryptMessage(response, keys.get(messages[0]));

            send(encryptedString + "{[END]}\r", connected);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addKey(String name, SecretKey key) {
        this.keys.put(name, key);
    }
}
