package hr.algebra.trivialpursuit.trivialpursuit.model;

import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private Map<String, Button> buttons;

    public ButtonManager() {
        buttons = new HashMap<>();
    }

    public void addButton(String id, Button button) {
        buttons.put(id, button);
    }

    public void setButtonText(String id, String button) {
        Button b = buttons.get(id);
        if (b != null) {
            b.setText(button);
        } else {
            System.out.println("Unknown button id: " + id);
        }
    }
}
