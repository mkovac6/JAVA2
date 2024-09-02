package hr.algebra.trivialpursuit.trivialpursuit.view;

import hr.algebra.trivialpursuit.trivialpursuit.GameState;
import hr.algebra.trivialpursuit.trivialpursuit.repository.QuestionRepository;
import hr.algebra.trivialpursuit.trivialpursuit.utils.DocumentationGen;
import hr.algebra.trivialpursuit.trivialpursuit.utils.NetworkUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
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
    private TextArea chatArea;
    @FXML
    private TextField inputField;

    private static Letter turn;

    private static Integer numberofTurns = 0;

    private Socket socket;
    private ObjectOutputStream output;
    private NetworkUtils networkUtils;
    private ButtonManager buttonManager;

    public void initialize() {
        try {
            socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            networkUtils = NetworkUtils.getInstance();
            buttonManager = new ButtonManager();
            initializeButtons();
            listenForMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        turn = Letter.A;
        numberofTurns = 0;
    }

    private void initializeButtons() {
        buttonManager.addButton("Startbtn", Startbtn);
        buttonManager.addButton("Question1btn", Question1btn);
        buttonManager.addButton("Halfwaybtn", Halfwaybtn);
        buttonManager.addButton("Question2btn", Question2btn);
        buttonManager.addButton("NEbtn1", NEbtn1);
        buttonManager.addButton("NEbtn2", NEbtn2);
        buttonManager.addButton("NEbtn3", NEbtn3);
        buttonManager.addButton("SEbtn1", SEbtn1);
        buttonManager.addButton("SEbtn2", SEbtn2);
        buttonManager.addButton("SEbtn3", SEbtn3);
        buttonManager.addButton("SWbtn1", SWbtn1);
        buttonManager.addButton("SWbtn2", SWbtn2);
        buttonManager.addButton("SWbtn3", SWbtn3);
        buttonManager.addButton("NWbtn1", NWbtn1);
        buttonManager.addButton("NWbtn2", NWbtn2);
        buttonManager.addButton("NWbtn3", NWbtn3);
        buttonManager.addButton("SWbtn1", SWbtn2);
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
        networkUtils.sendGameState(getGameState());
    }

    public void buttonPressed(Event event) {
        Button buttonPressed = (Button) event.getSource();
        buttonPressed.setText(turn.name());
        numberofTurns++;
        turn = turn == Letter.A ? Letter.B : Letter.A;

        networkUtils.sendGameState(getGameState());

    }

    public void questionButtonPressed(Event event) {
        Button buttonPressed = (Button) event.getSource();

        buttonPressed.setText(turn.name());
        turn = turn == Letter.A ? Letter.B : Letter.A;
        numberofTurns++;

        Task<String> questionTask = new Task<>() {
            @Override
            protected String call() {
                QuestionRepository repository = new QuestionRepository();
                return repository.getRandomQuestion();
            }

            @Override
            protected void succeeded() {
                String question = getValue();
                showQuestionDialog(question);
            }

            @Override
            protected void failed() {
                Throwable ex = getException();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Could not retrieve question");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                });
            }
        };
        networkUtils.sendGameState(getGameState());
        new Thread(questionTask).start();
    }

    private void showQuestionDialog(String question) {
        TextInputDialog dialog = new TextInputDialog("Answer");
        dialog.setTitle("QUESTION");
        dialog.setHeaderText("Random question!");
        dialog.setContentText("Question: " + question);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(playerAnswer -> {
            QuestionRepository repository = new QuestionRepository();
            if (repository.isAnswerCorrect(question, playerAnswer)) {
                correct();
                sendMessageToChat("Player " + turn.name() + " answered a question correctly!");
            } else {
                incorrect();
                sendMessageToChat("Player " + turn.name() + " answered a question incorrectly!");
            }
        });
    }

    private void sendMessageToChat(String chatMessage) {
        try {
            output.writeObject(chatMessage);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
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


        buttonPressed.setText(turn.name());
        turn = turn == Letter.A ? Letter.B : Letter.A;
        numberofTurns++;

        Task<String> questionTask = new Task<>() {
            @Override
            protected String call() {
                QuestionRepository repository = new QuestionRepository();
                return repository.getRandomQuestion();
            }

            @Override
            protected void succeeded() {
                String question = getValue();
                showWinnerQuestionDialog(question);
            }

            @Override
            protected void failed() {
                Throwable ex = getException();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Could not retrieve question");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                });
            }
        };
        networkUtils.sendGameState(getGameState());
        new Thread(questionTask).start();

    }

    private void showWinnerQuestionDialog(String question) {
        TextInputDialog dialog = new TextInputDialog("Answer");
        dialog.setTitle("QUESTION");
        dialog.setHeaderText("FINAL question!");
        dialog.setContentText("Question: " + question);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(playerAnswer -> {
            QuestionRepository repository = new QuestionRepository();
            boolean isCorrect = repository.isAnswerCorrect(question, playerAnswer);

            if (isCorrect) {
                winner();
                sendMessageToChat("Player " + turn.name() + " has answered the final question correctly and won the game!" + "\n" + "Starting new game...");
            } else {
                incorrect();
                sendMessageToChat("Player " + turn.name() + " answered the final question incorrectly!");
            }
        });
        networkUtils.sendGameState(getGameState());
    }

    public void winner() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game over");
        alert.setHeaderText("Congratulations, you won!");
        alert.setContentText("Would you like to start a new game?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            newGame();
        }
    }

    @FXML
    public void RollbuttonPressed(Event event) {
        int min = 1;
        int max = 5;
        double random = Math.random() * (max - min + 1) + min;
        Button rollButton = (Button) event.getSource();
        int rolledValue = (int) Math.round(random);
        rollButton.setText("Roll: " + rolledValue);

        String message = "Player rolled: " + rolledValue;
        networkUtils.sendGameState(getGameState());
        networkUtils.sendMessage(message);
        networkUtils.sendGameState(getGameState());
    }


    @FXML
    public void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            try {
                output.writeObject(message);
                output.flush();
                inputField.clear();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void listenForMessages() {
        new Thread(() -> {
            try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                Object obj;
                while ((obj = inputStream.readObject()) != null) {
                    if (obj instanceof String) {
                        appendMessage(obj.toString());
                    } else if (obj instanceof GameState gameState) {
                        loadGameState(gameState);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void appendMessage(String message) {
        javafx.application.Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public void saveGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Save");
        alert.setHeaderText("Are you sure you want to save the game?");
        alert.setContentText("This will overwrite the current game state.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Save canceled!");
            return;
        }

        Task<Void> saveGameTask = new Task<>() {
            @Override
            protected Void call() {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.dat"))) {
                    GameState gameState = getGameState();
                    out.writeObject(gameState);

                    Platform.runLater(() -> {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Save Successful");
                        successAlert.setHeaderText("Game state saved successfully.");
                        successAlert.showAndWait();
                    });

                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText("Could not save game state");
                        errorAlert.setContentText(e.getMessage());
                        errorAlert.showAndWait();
                    });
                }
                return null;
            }
        };
        new Thread(saveGameTask).start();
    }

    public GameState getGameState() {
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

        return new GameState(buttonState, turn.name(), numberofTurns);
    }


    public void loadGame() {
        if (loadCanceled()) return;

        Task<Void> loadGameTask = new Task<>() {
            @Override
            protected Void call() {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.dat"))) {
                    GameState gameState = (GameState) in.readObject();

                    loadGameState(gameState);

                } catch (IOException | ClassNotFoundException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Could not load game state");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    });
                }
                return null;
            }
        };
        networkUtils.sendGameState(getGameState());
        new Thread(loadGameTask).start();
    }

    private void loadGameState(GameState gameState) {
        Platform.runLater(() -> {
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
        });
    }


    private boolean loadCanceled() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm load");
        alert.setHeaderText("Are you sure you want to load this game?");
        alert.setContentText("This will overwrite the current game state.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Load canceled!");
            return true;
        }
        return false;
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

    public void saveGameXML1() {
        Alert alert = createConfirmationDialog();

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            Platform.runLater(() -> {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Save Successful");
                successAlert.setHeaderText("Game Configuration Saved");
                successAlert.setContentText("The game configuration has been successfully saved.");
                successAlert.showAndWait();
            });
            Task<Void> saveTask = createSaveTask();

            new Thread(saveTask).start();
        }
    }

    private Alert createConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Save");
        alert.setHeaderText("Save Game Configuration");
        alert.setContentText("Are you sure you want to save the game configuration?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        return alert;
    }

    private Task<Void> createSaveTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                try {
                    Document doc = createXMLDocument();
                    saveXMLDocument(doc);
                } catch (Exception e) {
                    updateMessage("Error: " + e.getMessage());
                    showErrorDialog(e.getMessage());
                }
                return null;
            }
        };
    }

    private Document createXMLDocument() throws ParserConfigurationException {
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

        return doc;
    }

    private void saveXMLDocument(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult resultStream = new StreamResult(new File("gameConfiguration.xml"));
        transformer.transform(source, resultStream);
    }

    private void showErrorDialog(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Saving Configuration");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    /*public void sameGameXML() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Save");
        alert.setHeaderText("Save Game Configuration");
        alert.setContentText("Are you sure you want to save the game configuration?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            Task<Void> saveTask = new Task<>() {
                @Override
                protected Void call() {
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
                        StreamResult resultStream = new StreamResult(new File("gameConfiguration.xml"));
                        transformer.transform(source, resultStream);

                    } catch (ParserConfigurationException | TransformerException e) {
                        updateMessage("Error: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    Platform.runLater(() -> {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Save Successful");
                        successAlert.setHeaderText("Game Configuration Saved");
                        successAlert.setContentText("The game configuration has been successfully saved.");
                        successAlert.showAndWait();
                    });
                }

                @Override
                protected void failed() {
                    Throwable ex = getException();
                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText("Could not save game configuration");
                        errorAlert.setContentText(ex.getMessage());
                        errorAlert.showAndWait();
                    });
                }
            };
            new Thread(saveTask).start();
        } else {
            System.out.println("Save action cancelled.");
        }
    }*/


    private void extractedButtonState(Document doc, Element buttonsElement) {
        // Array of buttons and their corresponding IDs
        Button[] buttons = {Startbtn, Question1btn, Halfwaybtn, Question2btn,
                NEbtn1, NEbtn2, NEbtn3, SEbtn1, SEbtn2, SEbtn3,
                SWbtn1, SWbtn2, SWbtn3, NWbtn1, NWbtn2, NWbtn3};

        String[] buttonIds = {"Startbtn", "Question1btn", "Halfwaybtn", "Question2btn",
                "NEbtn1", "NEbtn2", "NEbtn3", "SEbtn1", "SEbtn2", "SEbtn3",
                "SWbtn1", "SWbtn2", "SWbtn3", "NWbtn1", "NWbtn2", "NWbtn3"};

        for (int i = 0; i < buttons.length; i++) {
            addButtonStateToXML(doc, buttonsElement, buttons[i], buttonIds[i]);
        }
    }


    private void addButtonStateToXML(Document doc, Element parent, Button button, String id) {

        Element buttonElement = doc.createElement("Button");
        buttonElement.setAttribute("id", id);
        buttonElement.appendChild(doc.createTextNode(button.getText()));
        parent.appendChild(buttonElement);

    }

    public void loadGameFromXML() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Load");
        alert.setHeaderText("Are you sure you want to load the game?");
        alert.setContentText("This will overwrite the current game state.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("Load Game operation canceled.");
            return;
        }

        System.out.println("Load Game button pressed");

        Task<Void> loadGameTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    File xmlFile = new File("gameConfiguration.xml");
                    if (!xmlFile.exists()) {
                        System.out.println("XML file not found: " + xmlFile.getAbsolutePath());
                        return null;
                    }

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(xmlFile);
                    doc.getDocumentElement().normalize();

                    System.out.println("XML Content:");
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult resultStream = new StreamResult(new StringWriter());
                    transformer.transform(source, resultStream);
                    System.out.println(resultStream.getWriter().toString());

                    NodeList turnList = doc.getElementsByTagName("Turn");
                    if (turnList.getLength() > 0) {
                        String turnValue = turnList.item(0).getTextContent().trim();
                        turn = Letter.valueOf(turnValue);
                        System.out.println("Turn loaded: " + turn);
                    }

                    NodeList numberOfTurnsList = doc.getElementsByTagName("NumberOfTurns");
                    if (numberOfTurnsList.getLength() > 0) {
                        numberofTurns = Integer.parseInt(numberOfTurnsList.item(0).getTextContent().trim());
                    }

                    NodeList buttonList = doc.getElementsByTagName("Button");
                    System.out.println("Number of buttons found: " + buttonList.getLength()); // Debugging output

                    if (buttonList.getLength() == 0) {
                        System.out.println("No buttons found in the XML.");
                        return null;
                    }

                    Platform.runLater(() -> {
                        for (int i = 0; i < buttonList.getLength(); i++) {
                            Node node = buttonList.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element buttonElement = (Element) node;
                                String id = buttonElement.getAttribute("id").trim(); // Trim whitespace
                                String text = buttonElement.getTextContent().trim(); // Trim whitespace

                                System.out.println("Processing button with ID: " + id); // Debugging output
                                System.out.println("Button text: '" + text + "'"); // Debugging output for text

                                buttonManager.setButtonText(id, text);
                            }
                        }
                    });
                } catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        networkUtils.sendGameState(getGameState());
        new Thread(loadGameTask).start();
    }
}