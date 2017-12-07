package sprites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the upgrading of game elements, including storage of upgrade properties.
 *
 * @author Ben Schwennesen
 */
public class SpriteUpgrader {

    private Map<Sprite, Integer> currentSpriteLevels = new HashMap<>();
    private Map<Sprite, String> spriteTemplateAssociation = new HashMap<>();
    private Map<String, List<Map<String, String>>> spriteUpgradesByTemplate = new HashMap<>();
    private SpriteFactory spriteFactory;

    /**
     * Create a sprite upgrader instance to handle sprite upgrades and upgrade definitions.
     *
     * @param spriteFactory the sprite factory used to create (upgraded) sprites
     */
    public SpriteUpgrader(SpriteFactory spriteFactory) {
        this.spriteFactory = spriteFactory;
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
        Map<String, String> betweenDefinedUpgradesProperties = templateUpgrades.get(templateUpgrades.size() - 1);
        for (int i = templateUpgrades.size(); i < upgradeLevel; i++) {
            templateUpgrades.add(betweenDefinedUpgradesProperties);
        }
        templateUpgrades.add(upgradeProperties);
    }

    /**
     * Register a newly created sprite which is potentially eligible for upgrading.
     *
     * @param templateName the base template of a sprite
     * @param sprite the sprite built using the template
     */
    public void registerNewSprite(String templateName, Sprite sprite) {
        currentSpriteLevels.put(sprite, 0);
        spriteTemplateAssociation.put(sprite, templateName);
    }


    /**
     * Upgrade a particular sprite.
     *
     * @param sprite the sprite to upgrade
     * @return the sprite in its upgraded state
     * @throws IllegalArgumentException if the sprite cannot be upgraded
     */
    public Sprite upgradeSprite(Sprite sprite) throws IllegalArgumentException {
        if (!spriteTemplateAssociation.containsKey(sprite) || !currentSpriteLevels.containsKey(sprite)) {
            throw new IllegalArgumentException();
        }
        String templateName = spriteTemplateAssociation.get(sprite);
        int newUpgradeLevel = currentSpriteLevels.get(sprite);
        if (!canUpgrade(templateName, newUpgradeLevel)) {
            throw new IllegalArgumentException();
        }
        Map<String, String> upgradeProperties = spriteUpgradesByTemplate.get(templateName).get(newUpgradeLevel);
        return spriteFactory.generateSprite(upgradeProperties, new HashMap<>());
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
