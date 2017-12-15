package networking;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		MultiPlayerServer multiPlayerServer = new MultiPlayerServer();
		CollaborativeAuthoringServer collabServer = new CollaborativeAuthoringServer();
		multiPlayerServer.startServer();
		collabServer.startServer();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
