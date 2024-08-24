package hr.algebra.trivialpursuit.trivialpursuit;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.io.*;

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
            } else {
                incorrect();
            }

        }
    }

    public void correct() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Answer feedback!");
        alert.setHeaderText(null);
        alert.setContentText("Your answer is CORRECT!!!");

        alert.showAndWait();
    }

    public void incorrect() {
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
            } else {
                incorrect();
            }
        }
    }

    public void winner() {
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

    public void saveGame() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.dat"))) {

            String[] buttonState = {
                    Startbtn.getText(),
                    Question1btn.getText(),
                    Halfwaybtn.getText(),
                    Question2btn.getText(),
                    NEbtn1.getText(),
                    NEbtn2.getText(),
                    NEbtn3.getText(),
                    SEbtn1.getText(),
                    SEbtn2.getText(),
                    SEbtn3.getText(),
                    SWbtn1.getText(),
                    SWbtn2.getText(),
                    SWbtn3.getText(),
                    NWbtn1.getText(),
                    NWbtn2.getText(),
                    NWbtn3.getText()
            };

            GameState gameState = new GameState(buttonState, turn.name(), numberofTurns);
            out.writeObject(gameState);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save game state");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    public void loadGame() {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.dat"))) {
            GameState gameState = (GameState) in.readObject();

            String[] buttonState = gameState.getButtonState();
            Startbtn.setText(buttonState[0]);
            Question1btn.setText(buttonState[1]);
            Halfwaybtn.setText(buttonState[2]);
            Question2btn.setText(buttonState[3]);
            NEbtn1.setText(buttonState[4]);
            NEbtn2.setText(buttonState[5]);
            NEbtn3.setText(buttonState[6]);
            SEbtn1.setText(buttonState[7]);
            SEbtn2.setText(buttonState[8]);
            SEbtn3.setText(buttonState[9]);
            SWbtn1.setText(buttonState[10]);
            SWbtn2.setText(buttonState[11]);
            SWbtn3.setText(buttonState[12]);
            NWbtn1.setText(buttonState[13]);
            NWbtn2.setText(buttonState[14]);
            NWbtn3.setText(buttonState[15]);
            turn = Letter.valueOf(gameState.getTurnState());
            numberofTurns = gameState.getNumberOfTurns();

        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load game state");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}