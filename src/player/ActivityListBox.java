package player;

import java.util.ArrayList;
import java.util.List;
import networking.AbstractClient;

public class ActivityListBox extends MultiplayerListBox{
	public static final String BOT_NAME = "bot";
	
//	private Map<String, Integer> activityLog;
	private List<String> activityLog;
	
	public ActivityListBox() {
		super();
//		activityLog = new TreeMap<String, Integer>();
		activityLog = new ArrayList<String>();
	}
	
	public void setNames(AbstractClient multi) {
		activityLog.clear();
		/*
		for(String lobby : multi.getGameRooms()) {
			multi.joinGameRoom(lobby, "bot");
//			activityLog.put(lobby, multi.getPlayerNames().size());
			activityLog.add(lobby + ": " + (multi.getPlayerNames().size() - 1));
			System.out.println(lobby + ": " + multi.getPlayerNames().size());
			multi.exitGameRoom();
		}
		setNames(activityLog);
		*/
	}
}
