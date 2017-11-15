package sprites;

/**
 * Generates spite objects for displaying during authoring and gameplay.
 *
 * @author Ben Schwennesen
 */
public class SpriteFactory {

    // TODO - keep track of created sprites?

    public Sprite generateSprite(String spriteType) throws ReflectiveOperationException {
        Class spriteClass = Class.forName(spriteType);
        return (Sprite) spriteClass.newInstance();
    }
}
