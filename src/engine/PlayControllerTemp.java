package engine;

import engine.IPlayControl;
import engine.play_engine.ElementManager;
import engine.play_engine.PlayController;
import sprites.Sprite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayControllerTemp extends AbstractGameController implements IPlayControl {

    private ElementManager elementManager;

    public PlayControllerTemp(){
        super();
        elementManager = new ElementManager();
    }

    @Override
    public void saveGameState(String saveName) {
    }

    @Override
    public void loadGameState(int level) {

    }

    @Override
    public Sprite placeElement(String elementName, double x, double y) {
        return null;
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
    public Map<String, String> getStatus() {
        return null;
    }

    @Override
    public Collection<Sprite> getLevelSprites(String gameName, int level) {
        return null;
    }
}
