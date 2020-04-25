package application;

import java.io.IOException;

public interface SimulatorModel {
    void connect(String ip, int port);
    void mazeServerConnect(String ip, int port);
    void runServer(int port, int timeOut);
    String getServerStatus();
    void disconnect() throws IOException, InterruptedException;
    void startAutoPilot(String[] script);
    void stopAutoPilot();
    double[] getSlidersValue();
    void updateSliders();
    void setThrottle(double value);
    void setRudder(double value);
    void updateJoystickValues(double aileron, double elevator);
    void solveProblem(String problem);
    String getSolution();
    void updateAirplanePosition();
    String getAirplanePosition();
}
