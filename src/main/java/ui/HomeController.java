package ui;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.google.GoogleService;
import service.Service;

/**
 * Controller for the home window.
 */
public class HomeController {

    /**
     * Used to log events in this class.
     */
    private static final Logger LOGGER;

    /**
     * Font size.
     */
    private static final int FONT_SIZE = 15;

    static {
        LOGGER = LogManager.getLogger(HomeController.class);
    }

    /**
     * Account that we will connect to.
     */
    private String account;

    /**
     * This window.
     */
    private static Stage stage;

    /**
     * TextField where we enter the desired
     * person.
     */
    @FXML private TextField targetField;

    /**
     * Label that displays error messages.
     */
    @FXML private Label messageLabel;

    /**
     * Additional Label for error messages.
     */
    @FXML private Label messageLabel2;

    /**
     * Container for all the elements on this
     * window.
     */
    @FXML private VBox vbox;

    /**
     * Pane for this window.
     */
    @FXML private Pane pane;

    /**
     * Indicator that is displayed.
     */
    @FXML private ProgressIndicator indicator;

    /**
    * This method initializes the Stage of the Home
    * Controller.
    */
    public void initialize() {

        LOGGER.info("Entering the application.");

        stage.setOnCloseRequest(event -> exit());

        messageLabel.setVisible(false);
        messageLabel2.setVisible(false);

        targetField.textProperty().addListener((event -> {
            messageLabel.setVisible(false);
            messageLabel2.setVisible(false);
        }));

        targetField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                startSearching();
            }
        });

        indicator.setVisible(false);

        LOGGER.info("Starting view successfully set.");
    }

    /**
    * This method is called when the 'Start searching' button
    * is clicked. It finds the Twitter account of the specified
    * person and triggers the status fetching.
    */
    public void startSearching() {
        LOGGER.info("Starting to search for Twitter account.");

        account = GoogleService.findTwitterAccountFor(targetField.getText());

        if (account == null) {
            messageLabel.setVisible(true);
            messageLabel2.setVisible(true);

            LOGGER.error("The Twitter account for "
                    + targetField.getText() + " cannot be found.");
        } else {
            LOGGER.info("Connected to Twitter account @" + account);

            setLoadingView();

            Task loadingTask = initializeTask();

            indicator.progressProperty().bind(loadingTask.progressProperty());
            indicator.setVisible(true);
            new Thread(loadingTask).start();
        }
    }

    /**
    * Sets up the thread that fetches the statuses and
    * displays the progress indicator.
    *
    * @return a set up fetching thread, ready to start
    */
    private Task initializeTask() {

        Task loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ResultController.setNews(Service.setNewsForUser(account));

                updateProgress(1, 1);
                return null;
            }
        };

        loadingTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(final WorkerStateEvent t) {
                try {
                    FXMLLoader resultLoader
                            = new FXMLLoader(getClass()
                            .getResource("/result.fxml"));

                    Stage resultStage = new Stage();
                    resultStage.setMaximized(true);

                    ResultController.setStage(resultStage);
                    resultStage.setScene(new Scene(resultLoader.load()));

                    resultStage.show();
                    exit();
                } catch (IOException exception) {
                    LOGGER.error("result.fxml can not be loaded.");
                    exit();
                }
            }
        });

        return loadingTask;
    }

    /**
    * Displays the process indicator instead of start screen.
    */
    private void setLoadingView() {
        pane.getChildren().clear();
        vbox.getChildren().clear();

        pane.getChildren().add(vbox);

        Text text = new Text("Loading statuses");
        text.setFont(Font.font("System", FONT_SIZE));

        vbox.getChildren().add(text);
        vbox.getChildren().add(indicator);

        LOGGER.info("Loading view is ready and shown.");
    }

    /**
    * This method is called in order to terminate the current
     * window.
    */
    public void exit() {
        LOGGER.info("Closing the home window.");
        stage.close();
    }

    /**
     * Sets the stage for this window.
     * @param newStage - new stage to be set
     */
    public static void setStage(final Stage newStage) {
        stage = newStage;
    }

}
