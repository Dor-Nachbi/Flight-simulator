package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MazeSolverConnectionWindow {
    ViewModel vm;
    Stage s;
    @FXML
    TextField varIP, varPort;

    public void onConnect() {
        vm.mazeServerConnect();
    }
    public void setViewModel(ViewModel vm,Stage stage) {
        this.vm = vm;
        this.s = stage;
        vm.mazeServerIP.bind(varIP.textProperty());
        vm.mazeServerPort.bind(varPort.textProperty());
    }

    public void close(){
        s.close();
    }
}
