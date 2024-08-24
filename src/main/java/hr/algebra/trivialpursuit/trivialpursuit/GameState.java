package hr.algebra.trivialpursuit.trivialpursuit;

import java.io.Serializable;

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
}
