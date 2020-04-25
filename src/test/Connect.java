package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class Connect extends Observable {
    public static boolean stop= false;
    String ip;
    int port;
    Socket client;
    public Connect(String ip, int port) {
        stop = false;
        this.ip = ip;
        this.port = port;
    }

    public void run()
    {
        try {
            client = new Socket(ip, port);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            updateStatus(1);//Connected
            while (!stop) {
                try {
                    String command = CommandHandler.commandsQueue.take();
                    out.println(command);
                    //System.out.println("Client: " +command);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.flush();
            }
            client.close();
            out.close();
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
}
