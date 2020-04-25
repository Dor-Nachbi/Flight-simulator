package test;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

public class DataReaderServer extends Observable {
    public static boolean stop;
    public static boolean connected;
    int port,timeOut;
    ServerSocket dataReaderServer;

    public DataReaderServer(int port, int timeOut) {
        this.port = port;
        this.timeOut = timeOut;
    }

    public void runServer() {
        try {
            stop = false;
            dataReaderServer = new ServerSocket(port);
            Socket sendingDataClient = dataReaderServer.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(sendingDataClient.getInputStream()));
            updateStatus(1);
            String line= null;
            while (!stop && (line=in.readLine()) != null) {

                String[] values = line.split(",");
                //System.out.println(line);
                CommandHandler.symbolTable.put("/instrumentation/airspeed-indicator/indicated-speed-kt", Double.parseDouble(values[0]));
                CommandHandler.symbolTable.put("/instrumentation/altimeter/indicated-altitude-ft", Double.parseDouble(values[1]));
                CommandHandler.symbolTable.put("/instrumentation/altimeter/pressure-alt-ft", Double.parseDouble(values[2]));
                CommandHandler.symbolTable.put("/instrumentation/attitude-indicator/indicated-pitch-deg", Double.parseDouble(values[3]));
                CommandHandler.symbolTable.put("/instrumentation/attitude-indicator/indicated-roll-deg", Double.parseDouble(values[4]));
                CommandHandler.symbolTable.put("/instrumentation/attitude-indicator/internal-pitch-deg", Double.parseDouble(values[5]));
                CommandHandler.symbolTable.put("/instrumentation/attitude-indicator/internal-roll-deg", Double.parseDouble(values[6]));
                CommandHandler.symbolTable.put("/instrumentation/encoder/indicated-altitude-ft", Double.parseDouble(values[7]));
                CommandHandler.symbolTable.put("/instrumentation/encoder/pressure-alt-ft", Double.parseDouble(values[8]));
                CommandHandler.symbolTable.put("/instrumentation/gps/indicated-altitude-ft", Double.parseDouble(values[9]));
                CommandHandler.symbolTable.put("/instrumentation/gps/indicated-ground-speed-kt", Double.parseDouble(values[10]));
                CommandHandler.symbolTable.put("/instrumentation/gps/indicated-vertical-speed", Double.parseDouble(values[11]));
                CommandHandler.symbolTable.put("/instrumentation/heading-indicator/indicated-heading-deg", Double.parseDouble(values[12]));
                CommandHandler.symbolTable.put("/instrumentation/magnetic-compass/indicated-heading-deg", Double.parseDouble(values[13]));
                CommandHandler.symbolTable.put("/instrumentation/slip-skid-ball/indicated-slip-skid", Double.parseDouble(values[14]));
                CommandHandler.symbolTable.put("/instrumentation/turn-indicator/indicated-turn-rate", Double.parseDouble(values[15]));
                CommandHandler.symbolTable.put("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", Double.parseDouble(values[16]));
                CommandHandler.symbolTable.put("/controls/flight/aileron", Double.parseDouble(values[17]));
                CommandHandler.symbolTable.put("/controls/flight/elevator", Double.parseDouble(values[18]));
                CommandHandler.symbolTable.put("/controls/flight/rudder", Double.parseDouble(values[19]));
                CommandHandler.symbolTable.put("/controls/flight/flaps", Double.parseDouble(values[20]));
                CommandHandler.symbolTable.put("/controls/engines/current-engine/throttle", Double.parseDouble(values[21]));
                CommandHandler.symbolTable.put("/engines/engine/rpm", Double.parseDouble(values[22]));
                CommandHandler.symbolTable.put("/position/latitude-deg", Double.parseDouble(values[23]));
                CommandHandler.symbolTable.put("/position/longitude-deg", Double.parseDouble(values[24]));
                //System.out.println("Server: symbolTable Updated.");
                connected=true;
                try {
                    Thread.sleep(timeOut/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dataReaderServer.close();
            sendingDataClient.close();
            in.close();
            updateStatus(0);

        } catch (IOException e) {}
    }
    public void setStop() throws IOException {
        dataReaderServer.close();
        stop = true;
    }
    void updateStatus(int status)
    {
        setChanged();
        notifyObservers(status);
    }
}