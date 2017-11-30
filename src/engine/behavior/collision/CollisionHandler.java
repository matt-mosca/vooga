package engine.behavior.collision;

import engine.behavior.ParameterName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Exclude;

/**
 * Encapsulates game elements' collision fields and behavior. Responsible for checking for and handling collisions.
 *
 * @author Ben Schwennesen
 */
public class CollisionHandler {

    // TODO - default from prop getter
    private final String DEFAULT_IMAGE_PATH = "https://users.cs.duke.edu/~rcd/images/rcd.jpg";

    private CollisionVisitor collisionVisitor;
    private CollisionVisitable collisionVisitable;

    private String imageUrl;
    private double imageHeight;
    private double imageWidth;
    @Exclude private ImageView graphicalRepresentation;

    public CollisionHandler(CollisionVisitor collisionVisitor, CollisionVisitable collisionVisitable,
                            @ParameterName("imageUrl") String imageUrl,
                            @ParameterName("imageHeight") double imageHeight,
                            @ParameterName("imageWidth") double imageWidth) {
        this.collisionVisitor = collisionVisitor;
        this.collisionVisitable = collisionVisitable;
        this.imageUrl = imageUrl;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        constructGraphicalRepresentation();
    }

    private void constructGraphicalRepresentation() {
        Image image;
        try {
            image = new Image(imageUrl);
            graphicalRepresentation = new ImageView(image);
            graphicalRepresentation.setFitHeight(imageHeight);
            graphicalRepresentation.setFitWidth(imageWidth);
        } catch (NullPointerException | IllegalArgumentException imageUrlNotValidException) {
            graphicalRepresentation = new ImageView();
            graphicalRepresentation.setVisible(false);
        }
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
    
    public ImageView getGraphicalRepresentation() {
    	constructGraphicalRepresentation();
        return graphicalRepresentation;
    }
}
