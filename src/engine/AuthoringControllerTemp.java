package engine;

import engine.authoring_engine.AuthoringConstants;
import packaging.Packager;
import sprites.Sprite;
import sprites.SpriteFactory;
import util.SerializationUtils;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the model for a game being authored. Allows the view to modify and retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public class AuthoringControllerTemp extends AbstractGameController implements AuthorController {

    private Packager packager;

    public AuthoringControllerTemp() {
        super();
        packager = new Packager();
    }

    @Override
    public void exportGame() {
        getSpriteFactory().exportSpriteTemplates();
        packager.generateJar(getGameName());
    }

    @Override
    public Sprite createElement(String elementName, Map<String, Object> properties) {
        return getSpriteFactory().generateSprite(elementName, properties);
    }

    @Override
    public void createNewLevel(int level) {
        setLevel(level);
        if (!getLevelStatuses().containsKey(level)) {
            getLevelStatuses().put(level, new HashMap<>());
        }
    }

    @Override
    public void loadGameState(String saveName, int level) throws FileNotFoundException {
        loadGameStateElements(saveName, level);
        loadGameState(saveName, level);
    }
}
