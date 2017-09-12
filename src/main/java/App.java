import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ui.HomeController;

/**
 * Class that runs the whole application.
 */
public class App extends Application {

    /**
     * Used for logging events that happen in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(App.class);
    }

    /**
     * Window width.
     */
    private static final int WIDTH = 800;

    /**
     * Window height.
     */
    private static final int HEIGHT = 600;

    /**
     * Starts the UI.
     *
     * @param primaryStage
     *            - Stage that will be run first
     * @throws IOException
     *             if there is a problem with loading .fxml file
     */
    @Override
    public void start(final Stage primaryStage) throws IOException {
        try {
            HomeController.setStage(primaryStage);

            Parent root = FXMLLoader.load(getClass()
                    .getResource("/home.fxml"));
            primaryStage.setTitle("Twitter Stream");
            primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
            primaryStage.setMinWidth(WIDTH);
            primaryStage.setMinHeight(HEIGHT);
            primaryStage.show();
            root.requestFocus();

        } catch (IOException exception) {
            exception.printStackTrace();
            LOGGER.error("home.fxml can not be loaded.");
            throw exception;

        } catch (Exception exception) {
            throw exception;
        }
    }

    /**
     * Starts the whole application.
     * @param args - needed only for Maven compilation
     */
    public static void main(final String[] args) {
        try {
            BasicConfigurator.configure();
            launch();
        } catch (Exception exception) {
            LOGGER.error("Something went wrong.");
            return;
        }
    }

}
