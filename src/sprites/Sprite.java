package sprites;

import engine.behavior.collision.CollisionHandler;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.movement.MovementHandler;
import engine.behavior.movement.TrackingPoint;
import javafx.scene.image.ImageView;

/**
 * Represents game objects in the backend. Responsible for controlling the object's update behavior.
 *
 * TODO - documentation
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
	//private MovementStrategy movementStrategy;
	//private BlockingStrategy blockingStrategy;
	private MovementHandler movementHandler;
	//private CollisionVisitor collisionVisitor;
	//private CollisionVisitable collisionVisitable;
	private CollisionHandler collisionHandler;
	// Might need custom code in setProperties for handling loading of image and
	// subsequent construction of ImageView?
	//private ImageView spriteImageView;

	public Sprite(FiringStrategy firingStrategy, MovementHandler movementHandler, CollisionHandler collisionHandler) {
		this.firingStrategy = firingStrategy;
		this.movementHandler = movementHandler;
		this.collisionHandler = collisionHandler;
	}

	/**
	 * Move one cycle in direction of current velocity vector
	 */
	public void move() {
		if (collisionHandler.isBlocked()) {
			//movementStrategy.handleBlock();
			// TODO - handle block
			collisionHandler.unBlock();
		}
		movementHandler.move();
	}

	/**
	 * Attack in whatever way necessary Likely called by interaction_engine in
	 * event-handlers for keys / clicks
	 */
	public void attack() {
		firingStrategy.fire();
	}

	/**
	 * Check for a collision with another sprite.
	 *
	 * @param other the other sprite with which this sprite might be colliding
	 * @return true if the sprites collide, false otherwise
	 */
	public boolean collidesWith(Sprite other) {
		return this.collisionHandler.collidesWith(other.collisionHandler);
	}

	/**
	 * Apply the effects of a collision with another sprite to this sprite.
	 * 
	 * @param other the other sprite with which this sprite collided
	 */
	public void processCollision(Sprite other) {
		this.collisionHandler.processCollision(other.collisionHandler);
	}

	/**
	 * Check if this sprite has been destroyed during gameplay.
	 *
	 * @return true if the sprite has not been destroyed, false otherwise
	 */
	public boolean isAlive() {
		return collisionHandler.isAlive();
	}

	/**
	 * Auto-updating (NOT snapshot) position of this AbstractMovementStrategy for tracking
	 *
	 * @return auto-updating position that changes with movement
	 */
	public TrackingPoint getPositionForTracking() {
		return movementHandler.getPositionForTracking();
	}

	public double getX() {
		return movementHandler.getCurrentX();
	}

	public double getY() {
		return movementHandler.getCurrentY();
	}

	public void setGraphicalRepresentation(ImageView graphicalRepresentation) {
		collisionHandler.setGraphicalRepresentation(graphicalRepresentation);
	}

	public void setX(double newX) {
		movementHandler.setX(newX);
	}

	public void setY(double newY) {
		movementHandler.setY(newY);
	}
	
	/**
	 * Player id corresponding to player owning this sprite
	 * @return id of player controlling this sprite
	 */
	public int getPlayerId() {
		return collisionHandler.getPlayerId();
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
