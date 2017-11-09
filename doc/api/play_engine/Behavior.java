package play_engine;

public interface Behavior {

	/**
	 * Run the game loop for the given number of cycles
	 * @param cycles the number of cycles
	 */
	public void update(int cycles);
	
	/**
	 * Get lives left
	 * 
	 * @return number of lives left
	 */
	public int getLives();
	
	/**
	 * Retrieve the amount of each resource left
	 * 
	 * @return map of resource name to amount left
	 */
	public Map<String, Integer> getResources();
	
	/**
	 * Query whether the game is currently in play
	 * 
	 * @return true if in play, false if over / paused
	 */
	public boolean isInPlay();
	
	/**
	 * Query whether game has been won
	 * 
	 * @return true if won, false otherwise
	 */
	public boolean isWon();
	
	/**
	 * Query whether game has been lost
	 * 
	 * @return true if lost, false otherwise
	 */
	public boolean isLost();
	
	/**
	 * Pause the game
	 */
	public void pause();
	
	/**
	 * Resume the game
	 */
	public void resume();
}
