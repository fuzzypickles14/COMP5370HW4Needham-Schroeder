import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * Actor.java
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
public abstract class Actor implements Runnable {
    protected static final String LOCAL_HOST = "localhost";
    protected static final String MESSAGE_DELIMITER = "<<---->>";
    private SecretKey myKey;
    private String name;
    private int port;
    private ServerSocket socket;
    protected Key sessionKey;

    public Actor(String name, ServerSocket socket, SecretKey key, int port) {
        this.name = name;
        this.socket = socket;
        this.myKey = key;
        this.port = port;
    }


    protected void send(String message, Socket socket) {
        try {
            DataOutputStream sendToServer = new DataOutputStream(socket.getOutputStream());
            printLine(String.format("Sending %s", message));
            sendToServer.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendWithEncryption(String message, Key key, Socket socket) {
        try {
            DataOutputStream sendToServer = new DataOutputStream(socket.getOutputStream());
            printLine(String.format("Sending %s", message));
            sendToServer.writeBytes(encryptMessage(message, key) + "\r");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected String receive(Socket socket) {
        String retVal = "";
        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            retVal = inFromServer.readLine();
            if (Objects.equals(retVal, "")) {
                retVal = inFromServer.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        printLine(String.format("Received %s", retVal));
        return retVal;
    }

    protected String receiveWithDecryption(Socket socket, Key key) {
        String message = receive(socket);
        return decryptMessage(message, key);
    }

    protected String receiveAll(Socket socket) {
        StringBuilder retVal = new StringBuilder();
        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            do {
                retVal.append(inFromServer.readLine());
            }
            while (!retVal.toString().contains("{[END]}") || inFromServer.ready());
        } catch (IOException e) {
            e.printStackTrace();
        }
        printLine(String.format("Received %s", retVal));
        return retVal.toString().replace("{[END]}", "");

    }
    protected synchronized void printLine(String message) {
        System.out.println(String.format("%s %s", getName(), message));
    }

    protected String buildMessage(String... messages) {
        return String.join(MESSAGE_DELIMITER, messages);
    }

    protected String buildMessageToSend(String... messages) {
        return String.join(MESSAGE_DELIMITER, messages) + "\n";
    }

    protected String decryptMessage(String message) {
        String retVal = "";
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getMyKey());
            byte[] decryptedMessage = cipher.doFinal(new BASE64Decoder().decodeBuffer(message));
            retVal = new String(decryptedMessage);
            printLine(String.format("Decrypted message: %s", retVal));
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    protected String decryptMessage(String message, Key key) {
        String retVal = "";
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedMessage = cipher.doFinal(new BASE64Decoder().decodeBuffer(message));
            retVal = new String(decryptedMessage);
            printLine(String.format("Decrypted message: %s", retVal));
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    protected String encryptMessage(String message, Key key) {
        Cipher cipher = null;
        String retVal = "";
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());
            retVal =  new BASE64Encoder().encode(encryptedMessage);
            printLine(String.format("Encrypted message: %s", retVal));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return retVal;
    }


    protected Key buildSessionKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }


    //region Getters and Setters

    protected String getName() {
        return this.name + ':';
    }

    protected SecretKey getMyKey() {
        return this.myKey;
    }

    protected void setMyKey(SecretKey myKey) {
        this.myKey = myKey;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSocket(ServerSocket socket) {
        this.socket = socket;
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    //endregion
}
