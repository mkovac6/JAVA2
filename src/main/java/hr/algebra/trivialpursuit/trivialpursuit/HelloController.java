package hr.algebra.trivialpursuit.trivialpursuit;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLOutput;

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

    private static Integer numberofTurns = 0;

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

    @FXML
    public void showDocumentation() {
        String documentation = DocumentationGen.generateDocumentation(HelloController.class);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Documentation");
        alert.setHeaderText("Generated documentation of HelloController class.");
        alert.setContentText(documentation);

        TextArea textArea = new TextArea(documentation);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);

        alert.showAndWait();
    }

    public void sameGameXML() {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("GameConfiguration");
            doc.appendChild(root);

            Element turnElement = doc.createElement("Turn");
            turnElement.appendChild(doc.createTextNode(turn.name()));
            root.appendChild(turnElement);

            Element numberOfTurnsElement = doc.createElement("NumberOfTurns");
            numberOfTurnsElement.appendChild(doc.createTextNode(Integer.toString(numberofTurns)));
            root.appendChild(numberOfTurnsElement);

            Element buttonsElement = doc.createElement("Buttons");
            root.appendChild(buttonsElement);
            extractedButtonState(doc, buttonsElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("gameConfiguration.xml"));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractedButtonState(Document doc, Element buttonsElement) {
        addButtonStateToXML(doc, buttonsElement, Startbtn, "Startbtn");
        addButtonStateToXML(doc, buttonsElement, Question1btn, "Question1btn");
        addButtonStateToXML(doc, buttonsElement, Halfwaybtn, "Halfwaybtn");
        addButtonStateToXML(doc, buttonsElement, Question2btn, "Question2btn");
        addButtonStateToXML(doc, buttonsElement, NEbtn1, "NEbtn1");
        addButtonStateToXML(doc, buttonsElement, NEbtn2, "NEbtn2");
        addButtonStateToXML(doc, buttonsElement, NEbtn3, "NEbtn3");
        addButtonStateToXML(doc, buttonsElement, SEbtn1, "SEbtn1");
        addButtonStateToXML(doc, buttonsElement, SEbtn2, "SEbtn2");
        addButtonStateToXML(doc, buttonsElement, SEbtn3, "SEbtn3");
        addButtonStateToXML(doc, buttonsElement, SWbtn1, "SWbtn1");
        addButtonStateToXML(doc, buttonsElement, SWbtn2, "SWbtn2");
        addButtonStateToXML(doc, buttonsElement, SWbtn3, "SWbtn3");
        addButtonStateToXML(doc, buttonsElement, NWbtn1, "NWbtn1");
        addButtonStateToXML(doc, buttonsElement, NWbtn2, "NWbtn2");
        addButtonStateToXML(doc, buttonsElement, NWbtn3, "NWbtn3");
    }

    private void addButtonStateToXML(Document doc, Element parent, Button button, String id) {

        Element buttonElement = doc.createElement("Button");
        buttonElement.setAttribute("id", id);
        buttonElement.appendChild(doc.createTextNode(button.getText()));
        parent.appendChild(buttonElement);

    }

    public void loadGameFromXML() {
        System.out.println("Load Game button pressed");
        try {
            File xmlFile = new File("gameConfiguration.xml");
            if (!xmlFile.exists()) {
                System.out.println("XML file not found: " + xmlFile.getAbsolutePath());
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Print the entire XML content for debugging
            System.out.println("XML Content:");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);
            System.out.println(result.getWriter().toString());

            // Load turn
            NodeList turnList = doc.getElementsByTagName("Turn");
            if (turnList.getLength() > 0) {
                String turnValue = turnList.item(0).getTextContent().trim();
                turn = Letter.valueOf(turnValue);
                System.out.println("Turn loaded: " + turn);
            }

            // Load number of turns
            NodeList numberOfTurnsList = doc.getElementsByTagName("NumberOfTurns");
            if (numberOfTurnsList.getLength() > 0) {
                numberofTurns = Integer.parseInt(numberOfTurnsList.item(0).getTextContent().trim());
            }

            // Load button texts
            NodeList buttonList = doc.getElementsByTagName("Button");
            System.out.println("Number of buttons found: " + buttonList.getLength()); // Debugging output

            if (buttonList.getLength() == 0) {
                System.out.println("No buttons found in the XML.");
                return; // Exit if no buttons are found
            }

            for (int i = 0; i < buttonList.getLength(); i++) {
                Node node = buttonList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element buttonElement = (Element) node;
                    String id = buttonElement.getAttribute("id").trim(); // Trim whitespace
                    String text = buttonElement.getTextContent().trim(); // Trim whitespace

                    // Check if ID and text are being retrieved correctly
                    System.out.println("Processing button with ID: " + id); // Debugging output
                    System.out.println("Button text: '" + text + "'"); // Debugging output for text

                    switch (id) {
                        case "Startbtn":
                            Startbtn.setText(text);
                            break;
                        case "Question1btn":
                            Question1btn.setText(text);
                            break;
                        case "Halfwaybtn":
                            Halfwaybtn.setText(text);
                            break;
                        case "Question2btn":
                            Question2btn.setText(text);
                            break;
                        case "NEbtn1":
                            NEbtn1.setText(text);
                            break;
                        case "NEbtn2":
                            NEbtn2.setText(text);
                            break;
                        case "NEbtn3":
                            NEbtn3.setText(text);
                            break;
                        case "SEbtn1":
                            SEbtn1.setText(text);
                            break;
                        case "SEbtn2":
                            SEbtn2.setText(text);
                            break;
                        case "SEbtn3":
                            SEbtn3.setText(text);
                            break;
                        case "SWbtn1":
                            SWbtn1.setText(text);
                            break;
                        case "SWbtn2":
                            SWbtn2.setText(text);
                            break;
                        case "SWbtn3":
                            SWbtn3.setText(text);
                            break;
                        case "NWbtn1":
                            NWbtn1.setText(text);
                            break;
                        case "NWbtn2":
                            NWbtn2.setText(text);
                            break;
                        case "NWbtn3":
                            NWbtn3.setText(text);
                            break;
                        default:
                            System.out.println("Unknown button ID: " + id);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

}