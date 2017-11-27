package sprites;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import engine.behavior.collision.CollisionVisitable;
import engine.behavior.collision.CollisionVisitor;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.movement.MovementStrategy;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

/**
 * Represents displayed game objects in the backend. Responsible for controlling
 * the object's update behavior.
 *
 * TODO - we need to change the way properties are set, unless we want the
 * frontend to generate the strategies objects making this happen will require
 * defining a set of properties we're okay with each sprite having e.g. fire
 * rate, image path/url, destructible flag, etc. the creation of the strategy
 * objects will then be handled in the factory
 *
 * @author Ben Schwennesen
 */
public class Sprite {
	
	// Player id for any 'neutral' sprites such as obstacles, trees, etc.
	public static final int NEUTRAL_ID = 0;
	// Player id for AI-controlled sprites (enemies in single player)
	public static final int AI_ID = 1;
	// TODO - if implementing multi-player, deprecate this
	public static final int HUMAN_ID = 2;
		
	// These fields should be set through setProperties
	private FiringStrategy firingStrategy;
	private MovementStrategy movementStrategy;
	private CollisionVisitor collisionVisitor;
	private CollisionVisitable collisionVisitable;
	// Might need custom code in setProperties for handling loading of image and
	// subsequent construction of ImageView?
	private ImageView spriteImageView;

	public Sprite(Map<String, ?> properties) {
		setProperties(properties, false);
	}

	public Sprite(FiringStrategy firingStrategy, MovementStrategy movementStrategy, CollisionVisitor collisionVisitor,
			CollisionVisitable collisionVisitable) {
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
	 * Set the properties of this sprite.
	 *
	 * @param properties
	 *            - maps instance variables of this sprite to properties, as strings
	 */
	public void setProperties(Map<String, ?> properties, boolean updating) {
		List<Field> fields = getFieldsForAllMembers();
		for (Field field : fields) {
			field.setAccessible(true);
			if (properties.containsKey(field.getName())) {
				setField(properties, field);
			} else if (!updating) {
				// TODO - throw custom exception? set to a default value?
			}
		}
	}

	private List<Field> getFieldsForAllMembers() {
		List<Field> fields = new ArrayList<>(Arrays.asList(this.getClass().getDeclaredFields()));
		/*
		 * fields.addAll(Arrays.asList(firingStrategy.getClass().getDeclaredFields()));
		 * fields.addAll(Arrays.asList(movementStrategy.getClass().getDeclaredFields()))
		 * ;
		 * fields.addAll(Arrays.asList(collisionVisitor.getClass().getDeclaredFields()))
		 * ;
		 * fields.addAll(Arrays.asList(collisionVisitable.getClass().getDeclaredFields()
		 * ));
		 */
		return fields;
	}

	private void setField(Map<String, ?> properties, Field field) {
		try {
			field.set(this, properties.get(field.getName()));
		} catch (IllegalAccessException e) {
			// TODO - because of setAccessible above this won't happen (?), so remove print
			// statement
			System.out.println("Sprite reflection exception: this should never happen");
		}
	}

	/**
	 * Get all the field names of this sprite instance, for creating a property map.
	 *
	 * @return a list of all field names (regardless of accessibility) in this
	 *         instance's inheritance hierarchy
	 */
	public List<String> getFieldNames() {
		return getFieldsForAllMembers().stream().map(Field::getName).collect(Collectors.toList());
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
	 * Auto-updating (NOT snapshot) position of this MovementStrategy for tracking
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
		return getPlayerId() == AI_ID;
	}
	
	// TODO (extension) - for multi-player extension, modify to take in a playerId parameter
	public boolean isAlly() {
		return getPlayerId() == HUMAN_ID;
	}

}
