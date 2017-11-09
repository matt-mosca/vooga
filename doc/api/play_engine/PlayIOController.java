
public interface PlayIOController {

	// Save state of currently played game - assumes only 1 game in play for a given
	// engine at a time?
	public void saveGameState();

	// Load game state for previously saved game, returning root element from which
	// all other elements can be accessed
	public TowerDefenseElement loadGameState(String savedGameName);

}
