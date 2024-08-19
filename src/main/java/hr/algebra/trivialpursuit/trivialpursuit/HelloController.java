package hr.algebra.trivialpursuit.trivialpursuit;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

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

    private static Letter turn;

    private static Integer numberofTurns;

    public void initialize() {
        turn = Letter.A;
        numberofTurns = 0;
    }

    public void newGame(){
        numberofTurns = 0;
        turn = Letter.A;
    }

    public void buttonPressed(Event event) {
        Button buttonPressed = (Button) event.getSource();
        if (buttonPressed.getText().isBlank() || buttonPressed.getText().contains("START")
                || buttonPressed.getText().contains("QUESTION")
                || buttonPressed.getText().contains("HALFWAY")) {
            buttonPressed.setText(turn.name());
            numberofTurns++;
            checkWinner(turn);
            turn = turn == Letter.A ? Letter.B : Letter.A;
        }
    }

    public void questionbuttonPressed(Event event) {
        Button buttonPressed = (Button) event.getSource();
        if (buttonPressed.getText().isBlank() || buttonPressed.getText().contains("START")
                || buttonPressed.getText().contains("QUESTION")
                || buttonPressed.getText().contains("HALFWAY")) {
            buttonPressed.setText(turn.name());
            checkWinner(turn);
            turn = turn == Letter.A ? Letter.B : Letter.A;
            numberofTurns++;
            TextInputDialog dialog = new TextInputDialog("Answer");
            dialog.setTitle("QUESTION");
            dialog.setHeaderText("GEOGRAPHY QUESTION");
            dialog.setContentText("Question example: ");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> System.out.println("Your name: " + s));
        }
    }

    public void checkWinner(Letter letter) {

    }

    public void RollbuttonPressed(Event event) {
        int min = 1;
        int max = 6;
        double random = Math.random() * (max - min + 1) + min;
        Button RollbuttonPressed = (Button) event.getSource();
        RollbuttonPressed.setText("Roll: " + (int) Math.round(random));
    }
}