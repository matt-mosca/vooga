package sprites;

// TODO - Just a basic outline for reference from Behavior
// Schwen please feel free to add / change / remove as necessary
// Could be sub-classed by -> MortalSprite -> MovingSprite etc
public abstract class Sprite {

	private String name;
	// ^ I don't see why we'd want to name them?

	private double xCoord;
	private double yCoord;

	private double xVelocity;
	private double yVelocity;
	// ^ these might be subclass members (likely abstract subclass) instead if we want multi-genre flexibility

	// Flag to facilitate clean-up of 'dead' elements - only active elements
	// displayed by front end
	private boolean isActive;

	public Sprite(String name) {
		this.name = name;
	}

	/**
	 * Update self for one cycle based on current state
	 */
	public abstract void update();

	/**
	 * Move one cycle in direction of current velocity vector
	 */
	public abstract void move();

	/**
	 * Attack in whatever way necessary Likely called by interaction_engine in
	 * event-handlers for keys / clicks
	 */
	public abstract void attack();

	public String getName() {
		return name;
	}

	public double getX() {
		return xCoord;
	}

	public double getY() {
		return yCoord;
	}
	
	public double getXVelocity() {
		return xVelocity;
	}
	
	public double getYVelocity() {
		return yVelocity;
	}

	public boolean isActive() {
		return isActive;
	}

	/**
	 * Will cause Sprite to be displayed by front end Can be used to differentiate
	 * between Sprites in game area and those not (dead, off-screen???, etc)
	 */
	public void setActive() {
		isActive = true;
	}

	protected void setX(double newX) {
		xCoord = newX;
	}

	protected void setY(double newY) {
		yCoord = newY;
	}

	/**
	 * When the Sprite dies - will facilitate removal of element from
	 * ElementManager's collection of active sprites
	 */
	public void deactivate() {
		isActive = false;
	}

}
