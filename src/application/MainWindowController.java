package application;

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController implements Observer {
    ViewModel vm;
    Stage s1,s2;
    @FXML
    Slider throttleS, rudderS;
    @FXML
    Label statusLabel;
    @FXML
    TextArea autoPilot;
    @FXML
    RadioButton autoPilotRadioButton, manualRadioButton;
    @FXML
    Circle joystickBorderCircle, joystickControlCircle;
    @FXML
    Canvas MapDisplayer;
    Joystick myJoystick;
    MapDisplay map;
    boolean mazeServerConnected;
    StringProperty mazeSolution;
    @FXML
    public void setViewModel(ViewModel vm) {
        this.vm = vm;
        vm.throttle.bindBidirectional(throttleS.valueProperty());
        vm.rudder.bindBidirectional(rudderS.valueProperty());
        statusLabel.textProperty().bind(vm.status);
        vm.autoPilotScript.bind(autoPilot.textProperty());
        myJoystick = new Joystick(joystickBorderCircle, joystickControlCircle, this);
        vm.aileron.bind(myJoystick.varXProperty());
        vm.elevator.bind(myJoystick.varYProperty());
        mazeSolution = new SimpleStringProperty();
        mazeSolution.bind(vm.mazeSolution);
        mazeServerConnected = false;
    }

    public void calculatePath() throws IOException {
        FXMLLoader fxl = new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("MazeSolverConnection.fxml").openStream());
        MazeSolverConnectionWindow cw = fxl.getController();
        s2 = new Stage();
        s2.setScene(new Scene(root));
        s2.setTitle("Maze Server Connection");
        s2.setResizable(false);
        cw.setViewModel(vm, s2);
        s2.show();
    }
    public void manual(){
        vm.stopAutoPilot();
        vm.updateSliders();
        setSlidersDisable(false);
        myJoystick.setEnabled(true);
    }
    public void onThrottleDragged(){
        vm.setThrottle();
    }
    public void onRudderDragged(){
        vm.setRudder();
    }

    public void autoPilot() throws IOException {
        if(autoPilot.getText().equals("")) {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open script file");
            fc.setInitialDirectory(new File("./"));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File chosen = fc.showOpenDialog(null);
            if (chosen != null) {
                BufferedReader reader = new BufferedReader(new FileReader(new File(chosen.getPath())));
                String line;
                while ((line = reader.readLine()) != null) {
                    autoPilot.appendText(line + "\n");
                }
            }
        }
        myJoystick.setEnabled(false);
        setSlidersDisable(true);
        vm.startAutoPilot();
    }
    private void setSlidersDisable(boolean val)
    {
        throttleS.setDisable(val);
        rudderS.setDisable(val);
    }
    public void loadCSVFile(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Open map file");
        fc.setInitialDirectory(new File("./"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File chosen = fc.showOpenDialog(null);
        if(chosen !=null) {
            String csvFile = chosen.getPath();
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String[] params;
                double latitude, longitude, scale;
                ArrayList<String> mapArray = new ArrayList<>();
                params = br.readLine().split(",");
                latitude = Double.parseDouble(params[1]);
                longitude = Double.parseDouble(params[0]);
                params = br.readLine().split(",");
                scale = Double.parseDouble(params[0]);//152X247
                System.out.println(latitude + "#" + longitude);
                System.out.println(scale);
                while ((line = br.readLine()) != null) {
                    mapArray.add(line);
                }
                map = new MapDisplay(MapDisplayer, mapArray);
                map.airplaneLatLongProperty().bindBidirectional(vm.airplaneLatLong);
                vm.mazeStartPosition.bind(map.startStringProperty());
                vm.mazeEndPosition.bind(map.destStringProperty());
                vm.mazeMap.bind(map.mapStringProperty());
                map.mazeSolutionProperty().bind(vm.mazeSolution);
                map.setAirplaneLatLong(latitude + "," + longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void mapClicked(MouseEvent event) throws IOException {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
            map.setMode(map.getMode() % 2 + 1);
        else if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
            System.out.println(map.getDest(event.getX(), event.getY()));
            String[] location = map.getDest(event.getX(), event.getY()).split(",");
            map.drawDest(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
            if(mazeServerConnected)
                vm.solveProblem();
        }
    }
    public void connectWindow() throws IOException {
        FXMLLoader fxl = new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("Connection.fxml").openStream());
        ConnectionWindow cw = fxl.getController();
        s1 = new Stage();
        s1.setScene(new Scene(root));
        s1.setTitle("Connection");
        s1.setResizable(false);
        cw.setViewModel(vm, s1);
        s1.show();

    }
    public void update(Observable o, Object arg) {
        if(o==vm) {
            switch (arg.toString()) {
                case "ConnectedToSimulator": //Connected to simulator successfully

                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setHeaderText(null);
                        a.setTitle("Simulator connection");
                        a.setContentText("Connected successfully!!");
                        a.showAndWait();
                        s1.close();
                        manualRadioButton.setDisable(false);
                        autoPilotRadioButton.setDisable(false);
                    });
                    break;
                case "ConnectedToMazeServer":
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setHeaderText(null);
                        a.setTitle("Maze server connection");
                        a.setContentText("Connected successfully!!");
                        a.showAndWait();
                        mazeServerConnected = true;
                        if (map != null && map.isDestExist())
                            vm.solveProblem();
                        s2.close();
                    });
                    break;
                case "MazeServerConnectionFailed":
                    Platform.runLater(() -> mazeServerConnected = false);
                    break;
                case "GotMazeSolution":
                    map.drawPath();
                    break;
                case "AirplanePositionChanged":
                    if (map != null)
                        map.updateAirplanePosition();
                    break;
            }
        }
        if(o==myJoystick)
            vm.updateJoystickValues();
    }
}
