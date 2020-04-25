package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MazeConnection extends Observable {
    public static boolean stop= false;
    String ip;
    int port;
    Socket client;
    public static BlockingQueue<String> toSend;
    String solution;
    public MazeConnection(String ip, int port) {
        stop = false;
        this.ip = ip;
        this.port = port;
        toSend = new LinkedBlockingDeque();
    }

    public void run() {
        try {
            client = new Socket(ip, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            updateStatus(1);//Connected
            while (!stop) {
                try {
                    String s = toSend.take();
                    out.println(s);
                    out.flush();
                    System.out.println(s);
                    solution = in.readLine();
                    System.out.println("Got solution:");
                    System.out.println(solution);
                    if(solution == null) {
                        updateStatus(0);//Connection failed
                        break;
                    }
                    updateStatus(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus(0);//Connection failed
        }
    }
    public void setStop() throws IOException {
        client.close();
        stop= true;
    }
    void updateStatus(int status)
    {
        setChanged();
        notifyObservers(status);
    }
    public void solve(String s) throws InterruptedException {
        toSend.put(s);
    }
    public String getSolution()
    {
        return solution;
    }
}
