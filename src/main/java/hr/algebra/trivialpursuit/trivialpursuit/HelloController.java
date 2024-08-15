package hr.algebra.trivialpursuit.trivialpursuit;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {

    /*
    * NE = North East
    * SE = South East
    * SW = South West
    * NW = North West
    */
    @FXML
    private Button Startbtn;
    @FXML
    private Button Question1btn;
    @FXML
    private Button Halfwaybtn;
    @FXML
    private Button Question2btn;
    @FXML
    private Button NEbtn1;
    @FXML
    private Button NEbtn2;
    @FXML
    private Button NEbtn3;
    @FXML
    private Button SEbtn1;
    @FXML
    private Button SEbtn2;
    @FXML
    private Button SEbtn3;
    @FXML
    private Button SWbtn1;
    @FXML
    private Button SWbtn2;
    @FXML
    private Button SWbtn3;
    @FXML
    private Button NWbtn1;
    @FXML
    private Button NWbtn2;
    @FXML
    private Button NWbtn3;
    @FXML
    private Button Rollbtn;


    public void buttonPressed(Event event){
        Button buttonPressed = (Button) event.getSource();
        buttonPressed.setText(Letter.A.name());
    }

    public void RollbuttonPressed(Event event){
        int min = 1;
        int max = 6;
        double random = Math.random()*(max - min + 1) + min;
        Button RollbuttonPressed = (Button) event.getSource();
        RollbuttonPressed.setText("Roll: " + (int)Math.round(random));
    }
}