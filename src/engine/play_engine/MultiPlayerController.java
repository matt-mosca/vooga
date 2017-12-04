package engine.play_engine;

import java.util.Set;

import engine.MultiPlayerModelController;

/**
 * Gateway of multi-player player clients to server back end Can handle multiple
 * game rooms at a time
 * 
 * @author radithya
 *
 */
public class MultiPlayerController extends PlayController implements MultiPlayerModelController {

	public MultiPlayerController() {
	}

	@Override
	public String createGameRoom(String gameName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean joinGameRoom(String gameRoomName, String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getGameRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getPlayerNames(String gameRoomName) {
		// TODO Auto-generated method stub
		return null;
	}

}
