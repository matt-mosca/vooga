package engine.play_engine;

import engine.AbstractGameController;
import engine.IPlayControl;
import engine.play_engine.ElementManager;
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

    @Override
    public Sprite placeElement(String elementName, double x, double y){
        Sprite sprite = getSpriteFactory().generateSprite(elementName);
        sprite.setX(x);
        sprite.setY(y);
        return sprite;
    }


    @Override
    public Map<String, String> getStatus() {
        return getLevelStatuses().get(getCurrentLevel());
    }
}
