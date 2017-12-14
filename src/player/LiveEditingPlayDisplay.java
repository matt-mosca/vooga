package player;

import engine.PlayModelController;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Launches the player without going through the save file chooser.
 *
 * @author Ben Schwennesen
 */
public class LiveEditingPlayDisplay extends AbstractPlayDisplay {

    public LiveEditingPlayDisplay(int width, int height, Stage stage,
                                  PlayModelController myController) {
        super(width, height, stage, myController);
    }

    public void launchGame(String gameName) {
        try {
            getClientMessageUtils().initializeLoadedLevel(getMyController().loadOriginalGameState(gameName, 1));
            initializeLevelSprites();
        } catch (IOException e) {
            Alert.AlertType alertType = Alert.AlertType.ERROR;
            String message = e.getMessage();
            launchAlertAndWait(message, alertType);
        }
    }
}
