package ui;

import dm.entities.News;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Optional;

/**
 * Controller for the windows where
 * the posts are displayed.
 */
public class ResultController {

    /**
     * Used to log events in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(ResultController.class);
    }

    /**
     * List of the News that will be displayed in
     * the table.
     */
    private static ObservableList<News> newsList;

    /**
     * Table for displaying News.
     */
    @FXML private TableView<News> results;

    /**
     * Column for post order numbers.
     */
    @FXML private TableColumn<News, Integer> position;

    /**
     * Column for publish dates.
     */
    @FXML private TableColumn<News, String> date;

    /**
     * Column where original posts are displayed.
     */
    @FXML private TableColumn<News, String> original;

    /**
     * Column where inverted posts are displayed.
     */
    @FXML private TableColumn<News, String> opposite;

    /**
     * Used for closing the application.
     */
    private static Stage stage;

    /**
     * This method initializes the Stage of the Result
     * Controller.
     */
    public void initialize() {

        stage.setOnCloseRequest(event -> {
            event.consume();
            exit();
        });

        position.setCellValueFactory(
                new PropertyValueFactory<>("position"));

        date.setCellValueFactory(p -> {
            News newsConfig = p.getValue();

            Calendar cal = Calendar.getInstance();
            cal.setTime(newsConfig.getDate());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            return new SimpleStringProperty(day + "." + month + "." + year);
        });

        original.setCellValueFactory(new PropertyValueFactory<>("original"));
        opposite.setCellValueFactory(new PropertyValueFactory<>("opposite"));

        position.prefWidthProperty().bind(results.widthProperty().multiply(0.05));
        date.prefWidthProperty().bind(results.widthProperty().multiply(0.10));
        original.prefWidthProperty().bind(results.widthProperty().multiply(0.425));
        opposite.prefWidthProperty().bind(results.widthProperty().multiply(0.425));

        if (newsList != null) {
            results.setItems(newsList);
        }
    }

    /**
     * Sets News that will be displayed.
     * @param news - News that will be displayed
     */
    public static void setNews(final ObservableList<News> news) {
        newsList = news;
    }

    /**
     * Sets the stage for this window.
     * @param newStage - new stage to be set
     */
    public static void setStage(final Stage newStage) {
        stage = newStage;
    }

    /**
     * This method is called when the application
     * termination is requested. It asks the user
     * if he/she is sure about the termination.
     */
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setContentText("Are you sure you want to exit"
                + "the application?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            LOGGER.info("Closing the application.");
            stage.close();
        }
    }

}
