package engine.play_engine;

import engine.AbstractGameController;
import engine.IPlayControl;
import sprites.Sprite;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

/**
 * Controls the model for a game being played. Allows the view to modify and retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public class PlayControllerTemp extends AbstractGameController implements IPlayControl {

    private ElementManager elementManager;

    public PlayControllerTemp(){
        super();
        elementManager = new ElementManager();
    }

    @Override
    public void loadGameState(String saveName, int level) throws FileNotFoundException {
        elementManager.setCurrentElements(loadGameStateElements(saveName, level));
        loadGameStateSettings(saveName, level);
    }

    @Override
    public void pause() {
        // TODO
    }

    @Override
    public void resume() {
        // TODO
    }

    @Override
    public boolean isLost() {
        // TODO
        return false;
    }

    @Override
    public boolean isWon() {
        // TODO
        return false;
    }

    // TODO - IDs instead of returning sprite ?
    @Override
    public Sprite placeElement(String elementName, double x, double y){
        return elementManager.placeElement(elementName, x, y);

    }

    // TODO - IDs instead of returning sprite ?
    @Override
    public Collection<Sprite> getLevelSprites(int level) throws IllegalArgumentException {
        if (!getLevelSpritesMap().containsKey(level)) {
            throw new IllegalArgumentException();
        }
        return getLevelSpritesMap().get(level);

    }

    @Override
    public Map<String, String> getStatus() {
        return getLevelStatuses().get(getCurrentLevel());
    }
}
