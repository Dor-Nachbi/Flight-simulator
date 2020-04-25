package application;

import java.util.*;

import javafx.application.Platform;
import javafx.beans.property.*;


public class ViewModel extends Observable implements Observer{
    SimulatorModel m;
    public DoubleProperty throttle, rudder, aileron, elevator;
    public StringProperty ip, port, status, autoPilotScript, mazeServerIP,mazeServerPort;
    public StringProperty airplaneLatLong, mazeStartPosition, mazeEndPosition, mazeMap, mazeSolution;
    public ViewModel(SimulatorModel m) {
        this.m=m;
        throttle=new SimpleDoubleProperty();
        rudder=new SimpleDoubleProperty();
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        ip = new SimpleStringProperty();
        port = new SimpleStringProperty();
        status =  new SimpleStringProperty();
        autoPilotScript = new SimpleStringProperty();
        mazeServerIP = new SimpleStringProperty();
        mazeServerPort = new SimpleStringProperty();
        airplaneLatLong = new SimpleStringProperty();
        mazeStartPosition = new SimpleStringProperty();
        mazeEndPosition = new SimpleStringProperty();
        mazeMap = new SimpleStringProperty();
        mazeSolution = new SimpleStringProperty();
        status.setValue("Status: Waiting for simulator connection...");
        m.runServer(5400, 100);
    }
    public void connect() {
        m.connect(ip.get(), Integer.parseInt(port.get()));
    }
    public void mazeServerConnect(){
        m.mazeServerConnect(mazeServerIP.get(),Integer.parseInt(mazeServerPort.get()));
    }
    public void startAutoPilot(){
        String[] script = autoPilotScript.get().split("\n");
        m.startAutoPilot(script);
    }
    public void solveProblem(){
        StringBuilder problem = new StringBuilder();
        problem.append(mazeMap.get());
        problem.append("end\n");
        problem.append(mazeStartPosition.get() + "\n");
        problem.append(mazeEndPosition.get());
        m.solveProblem(problem.toString());
    }
    public void stopAutoPilot() {
        m.stopAutoPilot();
    }
    public void updateSliders(){
        m.updateSliders();
    }
    public void setThrottle()
    {
        m.setThrottle(throttle.get());
    }
    public void setRudder()
    {
        m.setRudder(rudder.get());
    }
    public void updateJoystickValues() {
        m.updateJoystickValues(aileron.get(), elevator.get());
    }
    public void update(Observable o, Object arg) {
        setChanged();
        if(o==m){
            switch (arg.toString())
            {
                case "ConnectedToSimulator": //Connected to simulator successfully
                    m.updateAirplanePosition();
                    notifyObservers(arg);
                    break;
                case "ServerStatusUpdated":
                    Platform.runLater(() -> status.set("Status: " + m.getServerStatus()));
                    break;
                case "SlidersUpdated":
                    throttle.set(m.getSlidersValue()[0]);
                    rudder.set(m.getSlidersValue()[1]);
                    break;
                case "ConnectedToMazeServer":
                    notifyObservers(arg);
                    break;
                case "MazeServerConnectionFailed":
                    notifyObservers(arg);
                    break;
                case "GotMazeSolution":
                    mazeSolution.set(m.getSolution());
                    notifyObservers(arg);
                    break;
                case "AirplanePositionChanged":
                    airplaneLatLong.set(m.getAirplanePosition());
                    notifyObservers(arg);
                    break;
            }
        }
    }
}
