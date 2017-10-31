import java.net.Socket;

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
    private String myKey;
    private String name;
    private int port;
    protected Socket socket;

    protected abstract void connect();
    protected abstract void disconnect();
    protected abstract void send(String message);
    protected abstract void receive();




    protected boolean isConnected() {
        return socket.isConnected() && !socket.isClosed();
    }



    //region Getters and Setters

    protected String getName() {
        return this.name + ':';
    }

    protected String getMyKey() {
        return this.myKey;
    }

    protected void setMyKey(String myKey) {
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

    //endregion
}