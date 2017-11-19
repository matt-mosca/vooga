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

public class AuthoringControllerTemp extends AbstractGameController implements AuthorController {

    private Packager packager;

    // this should be from a properties file? or handled in some better way?
    private final String DEFAULT_GAME_NAME = "untitled";
    private String gameName;
    private String gameDescription;

    public AuthoringControllerTemp() {
        super();
        packager = new Packager();
        gameName = DEFAULT_GAME_NAME;
        gameDescription = "";
    }

    @Override
    public void exportGame() {
        packager.generateJar(gameName);
    }

    @Override
    public Sprite createElement(String elementName, String serializedProperties) {
        return null;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    @Override
    public void createNewLevel(int level) {
        setLevel(level);
        if (!getLevelStatuses().containsKey(level)) {
            getLevelStatuses().put(level, new HashMap<>());
        }
    }

    @Override
    public void saveGameState(String saveName) {
        // Serialize separately for every level
        Map<Integer, String> serializedLevelsData = new HashMap<>();
        for (int level = 0; level < getLevelStatuses().size(); level++) {
            serializedLevelsData.put(level, getIoController().getLevelSerialization(level, gameDescription,
                    getLevelStatuses().getOrDefault(level, new HashMap<>())));
        }
        // Serialize map of level to per-level serialized data
        getIoController().saveGameStateForMultipleLevels(saveName, serializedLevelsData, true);
    }

    @Override
    public void loadGameState(String saveName, int level) throws FileNotFoundException {
        getLevelStatuses().put(level, getIoController().loadGameStateSettings(saveName, level, true));
    }
}
