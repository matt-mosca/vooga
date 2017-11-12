package sprites;

// TODO - Just a basic outline for reference from Behavior
// Schwen please feel free to add / change / remove as necessary
// Could be sub-classed by -> MortalSprite -> MovingSprite etc
public abstract class Sprite {

	private String name;
	private double xCoord;
	private double yCoord;
	// Flag to facilitate clean-up of 'dead' elements - only active elements
	// displayed by front end
	private boolean isActive;

	public Sprite(String name) {
		this.name = name;
	}

	/**
	 * Update self for given number of cycles based on per-sprite logic
	 * 
	 * @param cycles
	 *            number of cycles of updates to perform
	 */
	public abstract void update(int cycles);

	public String getName() {
		return name;
	}

	public double getX() {
		return xCoord;
	}

	public double getY() {
		return yCoord;
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
