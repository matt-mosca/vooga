package engine.behavior.collision;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Encapsulates game elements' collision fields and behavior. Responsible for checking for and handling collisions.
 *
 * @author Ben Schwennesen
 */
public class CollisionHandler {

    // TODO - default from prop getter
    private final String DEFAULT_IMAGE_PATH = "/images/tortoise.png";

    private CollisionVisitor collisionVisitor;
    private CollisionVisitable collisionVisitable;
    private ImageView graphicalRepresentation;

    public CollisionHandler(CollisionVisitor collisionVisitor, CollisionVisitable collisionVisitable) {
        this.collisionVisitor = collisionVisitor;
        this.collisionVisitable = collisionVisitable;
        graphicalRepresentation = new ImageView(new Image(DEFAULT_IMAGE_PATH));
    }

    public boolean collidesWith(CollisionHandler other) {
        return other.graphicalRepresentation.getBoundsInLocal()
                .intersects(this.graphicalRepresentation.getBoundsInLocal());
    }

    public void processCollision(CollisionHandler other) {
        other.collisionVisitable.accept(collisionVisitor);
    }

    public boolean isBlocked() {
        return collisionVisitor.isBlocked();
    }

    public void unBlock() {
        collisionVisitor.unBlock();
    }

    public boolean isAlive() {
        return collisionVisitor.isAlive();
    }

    /**
     * Get the unique identifier corresponding to player owning this sprite.
     *
     * @return identifier of player controlling this sprite
     */
    public int getPlayerId() {
        return collisionVisitor.getPlayerId();
    }

    public void setGraphicalRepresentation(ImageView graphicalRepresentation) {
        this.graphicalRepresentation = graphicalRepresentation;
    }
}
