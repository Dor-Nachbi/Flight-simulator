package application;

import javafx.application.Platform;
import test.*;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MySimulatorModel extends Observable implements SimulatorModel, Observer {
    Connect simulatorConnection;
    MazeConnection mazeConnection;
    DataReaderServer dataReaderServer;
    Thread clientThread;
    Thread serverThread;
    Thread mazeThread;
    Thread autoPilotThread;
    Thread airplanePositionThread;
    String serverStatus = "";
    String airplaneLatLong="";
    double throttle, rudder;
    public void connect(String ip, int port) {
        simulatorConnection = new Connect(ip, port);
        simulatorConnection.addObserver(this);
        clientThread = new Thread(new Runnable() {
            public void run() {
                simulatorConnection.run();
            }
        });
        clientThread.start();
    }
    public void mazeServerConnect(String ip, int port) {
        mazeConnection = new MazeConnection(ip, port);
        mazeConnection.addObserver(this);
        mazeThread = new Thread(new Runnable() {
            public void run() {
                mazeConnection.run();
            }
        });
        mazeThread.start();
    }
    public void solveProblem(String problem)
    {
        try {
            mazeConnection.solve(problem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getSolution(){
        return mazeConnection.getSolution();
    }
    public String getAirplanePosition()
    {
        return airplaneLatLong;
    }
    public void updateAirplanePosition() {
        airplanePositionThread = new Thread(new Runnable() {
            public void run() {
                while(serverStatus.equals("Simulator connected")) {
                    double latitude = CommandHandler.symbolTable.get("/position/latitude-deg");
                    double longitude = CommandHandler.symbolTable.get("/position/longitude-deg");
                    airplaneLatLong = latitude + "," + longitude;
                    setChanged();
                    notifyObservers("AirplanePositionChanged");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        airplanePositionThread.start();
    }

    public void runServer(int port, int timeOut) {
        dataReaderServer = new DataReaderServer(port, timeOut);
        dataReaderServer.addObserver(this);
        serverThread = new Thread(new Runnable() {
            public void run() {
                dataReaderServer.runServer();
            }
        });
        serverThread.start();
    }

    public void disconnect() throws IOException, InterruptedException {
        CommandHandler.commandsQueue.put("bye");
        dataReaderServer.setStop();
        try {
            simulatorConnection.setStop();
        } catch (Exception e) {
        }
        CommandHandler.setRun(false);
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void startAutoPilot(String[] script) {
        String[] commands = MyInterpreter.interpret(script);
        CommandHandler handle = new CommandHandler();
        handle.setRun(true);
        autoPilotThread = new Thread(new Runnable() {
            public void run() {
                handle.parser(commands);
            }
        });
        autoPilotThread.start();
    }

    public void stopAutoPilot() {
        CommandHandler.setRun(false);
    }
    public void updateSliders(){
        throttle = CommandHandler.symbolTable.get("/controls/engines/current-engine/throttle");
        rudder = CommandHandler.symbolTable.get("/controls/flight/rudder");
        setChanged();
        notifyObservers("SlidersUpdated");
    }
    public double[] getSlidersValue() {
        return new double[]{throttle, rudder};
    }
    public void setThrottle(double value)
    {
        try {
            CommandHandler.commandsQueue.put("set /controls/engines/current-engine/throttle " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setRudder(double value)
    {
        try {
            CommandHandler.commandsQueue.put("set /controls/flight/rudder " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updateJoystickValues(double aileron, double elevator){
        try {
            CommandHandler.commandsQueue.put("set /controls/flight/aileron " + aileron);
            CommandHandler.commandsQueue.put("set /controls/flight/elevator " + elevator);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        if (o == simulatorConnection && Integer.parseInt(arg.toString()) == 1) {
            notifyObservers("ConnectedToSimulator");
        }
        else if (o == dataReaderServer) {
            if (Integer.parseInt(arg.toString()) == 0) {
                Platform.runLater(() -> serverStatus = "Sever closed");
                notifyObservers("ServerStatusUpdated");
            }
            else if (Integer.parseInt(arg.toString()) == 1) {
                Platform.runLater(() -> serverStatus = "Simulator connected");
                notifyObservers("ServerStatusUpdated");////Simulator connected
            }
        }
        else if(o== mazeConnection){
            if (Integer.parseInt(arg.toString()) == 0) {
                notifyObservers("MazeServerConnectionFailed");
            }
            else if (Integer.parseInt(arg.toString()) == 1) {
                notifyObservers("ConnectedToMazeServer"); //Connected to maze server
            }
            else if (Integer.parseInt(arg.toString()) == 2) {
                notifyObservers("GotMazeSolution"); //Got solution
            }
        }
    }
}