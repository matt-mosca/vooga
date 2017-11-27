package sprites;

import java.awt.geom.Point2D;
import engine.behavior.blocking.BlockingStrategy;
import engine.behavior.collision.CollisionVisitable;
import engine.behavior.collision.CollisionVisitor;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.movement.MovementStrategy;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

/**
 * Represents  game objects in the backend. Responsible for controlling the object's update behavior.
 *
 * @author Ben Schwennesen
 */
public class Sprite {

	private enum Team {
		NEUTRAL,
		COMPUTER,
		HUMAN
	}
		
	// These fields should be set through setProperties
	private FiringStrategy firingStrategy;
	private MovementStrategy movementStrategy;
	private BlockingStrategy blockingStrategy;
	private CollisionVisitor collisionVisitor;
	private CollisionVisitable collisionVisitable;
	// Might need custom code in setProperties for handling loading of image and
	// subsequent construction of ImageView?
	private ImageView spriteImageView;

	public Sprite(FiringStrategy firingStrategy, MovementStrategy movementStrategy, CollisionVisitor collisionVisitor,
                  CollisionVisitable collisionVisitable, String imageUrl) {
		this.firingStrategy = firingStrategy;
		this.movementStrategy = movementStrategy;
		this.collisionVisitor = collisionVisitor;
		this.collisionVisitable = collisionVisitable;
	}

	/**
	 * Move one cycle in direction of current velocity vector
	 */
	public void move() {
		if (collisionVisitor.isBlocked()) {
			//movementStrategy.handleBlock();
			// TODO - handle block
			collisionVisitor.unBlock();
		}
		movementStrategy.move();
	}

	/**
	 * Attack in whatever way necessary Likely called by interaction_engine in
	 * event-handlers for keys / clicks
	 */
	public void attack() {
		firingStrategy.fire();
	}

	/**
	 * Handle the effects ON THIS SPRITE only of collision with another Sprite
	 * 
	 * @param other
	 *            the other Sprite which this Sprite collided with
	 */
	public void processCollision(Sprite other) {
		other.collisionVisitable.accept(collisionVisitor);
	}

	public boolean isAlive() {
		return collisionVisitor.isAlive();
	}

	/**
	 * For front end to retrieve, rescale and display
	 * 
	 * @return
	 */
	public ImageView getRepresentation() {
		return spriteImageView;
	}

	/**
	 * Scaling-aware boundaries of sprite's representation
	 * 
	 * @return
	 */
	public Bounds getBounds() {
		return spriteImageView.getBoundsInParent();
	}

	public double getX() {
		return movementStrategy.getX();
	}

	public double getY() {
		return movementStrategy.getY();
	}

	/**
	 * The current (x, y) position as a Point2D.Double
	 * 
	 * @return current position
	 */
	public Point2D.Double getCurrentPosition() {
		return movementStrategy.getCurrentPosition();
	}

	/**
	 * Auto-updating (NOT snapshot) position of this AbstractMovementStrategy for tracking
	 * 
	 * @return auto-updating position that changes with movement
	 */
	public Point2D.Double getPositionForTracking() {
		return movementStrategy.getPositionForTracking();
	}

	public void setX(double newXCoord) {
		movementStrategy.setX(newXCoord);
	}

	public void setY(double newYCoord) {
		movementStrategy.setY(newYCoord);
	}
	
	/**
	 * Player id corresponding to player owning this sprite
	 * @return id of player controlling this sprite
	 */
	public int getPlayerId() {
		return collisionVisitor.getPlayerId();
	}
	
	// TODO (extension) - for multi-player extension, modify to take in a playerId parameter 
	public boolean isEnemy() {
		return getPlayerId() == Team.COMPUTER.ordinal();
	}
	
	// TODO (extension) - for multi-player extension, modify to take in a playerId parameter
	public boolean isAlly() {
		return getPlayerId() == Team.HUMAN.ordinal();
	}
}
