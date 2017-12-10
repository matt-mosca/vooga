package engine.game_elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the upgrading of game elements, including storage of upgrade properties.
 *
 * @author Ben Schwennesen
 */
public class GameElementUpgrader {

    private Map<GameElement, Integer> currentSpriteLevels = new HashMap<>();
    private Map<GameElement, String> spriteTemplateAssociation = new HashMap<>();
    private Map<String, List<Map<String, String>>> spriteUpgradesByTemplate = new HashMap<>();
    private GameElementFactory gameElementFactory;

    /**
     * Create a sprite upgrader instance to handle sprite upgrades and upgrade definitions.
     *
     * @param gameElementFactory the sprite factory used to create (upgraded) sprites
     */
    public GameElementUpgrader(GameElementFactory gameElementFactory) {
        this.gameElementFactory = gameElementFactory;
    }

    /**
     * Define a new upgrade level for a particular.
     *
     * @param spriteTemplateName the name of the sprite template
     * @param upgradeLevel
     * @param upgradeProperties  a map of properties for sprites using this template
     */
    public void defineUpgrade(String spriteTemplateName, int upgradeLevel, Map<String, String> upgradeProperties) {
        List<Map<String, String>> templateUpgrades =
                spriteUpgradesByTemplate.getOrDefault(spriteTemplateName, new ArrayList<>());
        if (templateUpgrades.size() > 0) {
            Map<String, String> betweenDefinedUpgradesProperties = templateUpgrades.get(templateUpgrades.size() - 1);
            for (int i = templateUpgrades.size(); i < upgradeLevel; i++) {
                templateUpgrades.add(betweenDefinedUpgradesProperties);
            }
        }
        templateUpgrades.add(upgradeProperties);
        // put shouldn't be necessary but let's do it for clarity's sake
        spriteUpgradesByTemplate.put(spriteTemplateName, templateUpgrades);
    }

    /**
     * Register a newly created gameElement which is potentially eligible for upgrading.
     *
     * @param templateName the base template of a gameElement
     * @param gameElement the gameElement built using the template
     */
    public void registerNewSprite(String templateName, GameElement gameElement) {
        currentSpriteLevels.put(gameElement, 0);
        spriteTemplateAssociation.put(gameElement, templateName);
    }


    /**
     * Upgrade a particular gameElement.
     *
     * @param gameElement the gameElement to upgrade
     * @return the gameElement in its upgraded state
     * @throws IllegalArgumentException if the gameElement cannot be upgraded
     */
    public GameElement upgradeSprite(GameElement gameElement) throws IllegalArgumentException {
        if (!spriteTemplateAssociation.containsKey(gameElement) || !currentSpriteLevels.containsKey(gameElement)) {
            throw new IllegalArgumentException();
        }
        String templateName = spriteTemplateAssociation.get(gameElement);
        int newUpgradeLevel = currentSpriteLevels.get(gameElement);
        if (!canUpgrade(templateName, newUpgradeLevel)) {
            throw new IllegalArgumentException();
        }
        Map<String, String> upgradeProperties = spriteUpgradesByTemplate.get(templateName).get(newUpgradeLevel);
        return gameElementFactory.generateSprite(upgradeProperties, new HashMap<>());
    }

    private boolean canUpgrade(String templateName, int currentUpgradeLevel) {
        return spriteUpgradesByTemplate.containsKey(templateName) &&
                spriteUpgradesByTemplate.get(templateName).size() > currentUpgradeLevel;
    }

    /**
     * Load the sprite upgrade templates from an authored game
     *
     * @param spriteUpgradesByTemplate upgrade templates, loaded from memory, associated with their base templates
     */
    public void loadSpriteUpgrades(Map<String, List<Map<String, String>>> spriteUpgradesByTemplate) {
        this.spriteUpgradesByTemplate = spriteUpgradesByTemplate;
    }

    /**
     * Retrieve the upgrade templates.
     *
     * @return the sprite upgrade templates for the current game.
     */
    public Map<String, List<Map<String, String>>> getSpriteUpgradesForEachTemplate() {
        return spriteUpgradesByTemplate;
    }
}
