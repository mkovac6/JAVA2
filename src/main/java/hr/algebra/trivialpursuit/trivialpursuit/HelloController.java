package hr.algebra.trivialpursuit.trivialpursuit;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

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

    private static Letter turn;

    private static Integer numberofTurns;

    public void initialize() {
        turn = Letter.A;
        numberofTurns = 0;
    }

    @FXML
    public void newGame() {
        Startbtn.setText("Start");
        Question1btn.setText("QUESTION");
        Halfwaybtn.setText("HALFWAY");
        Question2btn.setText("QUESTION");
        NEbtn1.setText("");
        NEbtn2.setText("");
        NEbtn3.setText("");
        SEbtn1.setText("");
        SEbtn2.setText("");
        SEbtn3.setText("");
        SWbtn1.setText("");
        SWbtn2.setText("");
        SWbtn3.setText("");
        NWbtn1.setText("");
        NWbtn2.setText("");
        NWbtn3.setText("");
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
            turn = turn == Letter.A ? Letter.B : Letter.A;
        }
    }

    public void questionbuttonPressed(Event event) {
        Button buttonPressed = (Button) event.getSource();
        if (buttonPressed.getText().isBlank() || buttonPressed.getText().contains("START")
                || buttonPressed.getText().contains("QUESTION")
                || buttonPressed.getText().contains("HALFWAY")) {
            buttonPressed.setText(turn.name());
            turn = turn == Letter.A ? Letter.B : Letter.A;
            numberofTurns++;

            QuestionRepository repository = new QuestionRepository();
            String question = repository.getRandomQuestion();
            TextInputDialog dialog = new TextInputDialog("Answer");
            dialog.setTitle("QUESTION");
            dialog.setHeaderText("Random question!");
            dialog.setContentText("Question: " + question);

            dialog.showAndWait();
            String playerAnswer = dialog.getEditor().getText();

            if (repository.isAnswerCorrect(question, playerAnswer)) {
                correct();
            }
            else {
                incorrect();
            }

        }
    }

    public void correct(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Answer feedback!");
        alert.setHeaderText(null);
        alert.setContentText("Your answer is CORRECT!!!");

        alert.showAndWait();
    }

    public void incorrect(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Answer feedback!");
        alert.setHeaderText(null);
        alert.setContentText("Your answer is NOT CORRECT!!!");

        alert.showAndWait();
    }

    public void winnerButtonPressed(Event event) {
        Button buttonPressed = (Button) event.getSource();
        if (buttonPressed.getText().isBlank() || buttonPressed.getText().contains("START")
                || buttonPressed.getText().contains("QUESTION")
                || buttonPressed.getText().contains("HALFWAY")) {
            buttonPressed.setText(turn.name());
            turn = turn == Letter.A ? Letter.B : Letter.A;
            numberofTurns++;

            QuestionRepository repository = new QuestionRepository();
            String question = repository.getRandomQuestion();
            TextInputDialog dialog = new TextInputDialog("Answer");
            dialog.setTitle("QUESTION");
            dialog.setHeaderText("FINAL question!");
            dialog.setContentText("Question: " + question);

            dialog.showAndWait();
            String playerAnswer = dialog.getEditor().getText();

            if (repository.isAnswerCorrect(question, playerAnswer)) {
                winner();
            }
            else {
                incorrect();
            }

        }
    }

    public void winner(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ENDGAME");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations, you won!");

        alert.showAndWait();
    }

    public void RollbuttonPressed(Event event) {
        int min = 1;
        int max = 6;
        double random = Math.random() * (max - min + 1) + min;
        Button RollbuttonPressed = (Button) event.getSource();
        RollbuttonPressed.setText("Roll: " + (int) Math.round(random));
    }
}