package hr.algebra.trivialpursuit.trivialpursuit;

import java.io.Serializable;
import java.util.Arrays;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] buttonState;
    private String turnState;
    private int numberOfTurns;

    public GameState(String[] buttonState, String turnState, int numberOfTurns) {
        this.buttonState = buttonState;
        this.turnState = turnState;
        this.numberOfTurns = numberOfTurns;
    }

    public String[] getButtonState() {
        return buttonState;
    }

    public String getTurnState() {
        return turnState;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setButtonState(String[] buttonState) {
        this.buttonState = buttonState;
    }

    public void setTurnState(String turnState) {
        this.turnState = turnState;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public void incrementNumberOfTurns() {
        this.numberOfTurns++;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "buttonState=" + Arrays.toString(buttonState) +
                ", turnState='" + turnState + '\'' +
                ", numberOfTurns=" + numberOfTurns +
                '}';
    }
}
