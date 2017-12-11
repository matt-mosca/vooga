package player;

import display.factory.ButtonFactory;
import display.splashScreen.ScreenDisplay;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class MultiplayerLobby extends ScreenDisplay {
	private static final String BACKGROUND_IMAGE = "grass_large.png";
//	private static final String BACKGROUND_IMAGE = "space_background.jpeg";
	
	private BorderPane multiplayerLayout;
	private Label topScreenLabel;
	private Label lobbyPlayersLabel;
	private VBox buttonBox;
	private VBox playersBox;
	private VBox centerBox;
	private VBox rightBox;
	private VBox bottomBox;
	private ButtonFactory buttonFactory;
	private Button createGameLobby;
	private Button joinGameLobby;
	private Button startGame;
	private Button returnHome;

	public MultiplayerLobby(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		multiplayerLayout = new BorderPane();
		topScreenLabel = new Label();
		lobbyPlayersLabel = new Label();
		buttonFactory = new ButtonFactory();
		buttonBox = new VBox();
		playersBox = new VBox();
		centerBox = new VBox();
		rightBox = new VBox();
		bottomBox = new VBox();
		multiplayerLayout.setCenter(centerBox);
		multiplayerLayout.setRight(rightBox);
		multiplayerLayout.setBottom(bottomBox);
		setMultiplayerBackground(width, height);
		rootAdd(multiplayerLayout);
		createButtons();
		setUpButtonBox();
		setUpPlayersBox();
		initializeMultiplayerHomeScreen();
		styleLayout();
	}
	
	private void styleLayout() {
		rootStyleAndClear("player/resources/multiplayer.css");
	}
	
	private void setMultiplayerBackground(int width, int height) {
		String backgroundName = BACKGROUND_IMAGE;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(backgroundName));
		ImageView splashBackground = new ImageView(image);
		splashBackground.setFitWidth(width);
		splashBackground.setFitHeight(height);
		rootAdd(splashBackground);
	}
	
	private void initializeMultiplayerHomeScreen() {
		setTopLabelForMultiplayerHomeScreen();
		addButtonBox();
	}
	
	private void changeToLobby() {
		removeButtonBox();
		setTopLabelForLobby();
		addPlayersBox();
		addStartGame();
		addReturn();
	}
	
	private void returnToMultiplayerHome() {
		removePlayersBox();
		removeStartGame();
		removeReturn();
		initializeMultiplayerHomeScreen();
	}
	
	private void createLobby() {
		changeToLobby();
		promptForLobbyName();
	}
	
	private void joinLobby() {
		changeToLobby();
		promptForPlayerName();
	}
	
	//Call this method when user wants to create new lobby- to name the lobby
	//This method should pull up prompt box
	private void promptForLobbyName() {
		promptForPlayerName();
	}
	
	//Call this method when user wants to create or join lobby- to create name for other players to see
	//This method should pull up prompt box
	private void promptForPlayerName() {
		
	}
	
	//This method or an external method would be called when "START GAME" is pressed (serves as action for button)
	private void startGame() {
		
	}
	
	private void setTopLabelForLobby() {
		topScreenLabel.setText("VOOGA Lobby");
	}
	
	private void setTopLabelForMultiplayerHomeScreen() {
		topScreenLabel.setText("Multiplayer");
	}
	
	private void createButtons() {
		createGameLobby = buttonFactory.buildDefaultTextButton("Create Game Lobby", e -> createLobby());
		joinGameLobby = buttonFactory.buildDefaultTextButton("Join Game Lobby", e -> joinLobby());
		startGame = buttonFactory.buildDefaultTextButton("START GAME", e -> startGame());
		returnHome = buttonFactory.buildDefaultTextButton("Return to Multiplayer Home", e -> returnToMultiplayerHome());
	}
	
	private void setUpButtonBox() {
		buttonBox.getChildren().add(createGameLobby);
		buttonBox.getChildren().add(joinGameLobby);
	}
	
	private void setUpPlayersBox() {
		lobbyPlayersLabel.setText("Players");
		playersBox.getChildren().add(lobbyPlayersLabel);
	}
	
	private void addButtonBox() {
		centerBox.getChildren().add(buttonBox);
	}
	
	private void removeButtonBox() {
		centerBox.getChildren().remove(buttonBox);
	}

	private void addPlayersBox() {
		rightBox.getChildren().add(playersBox);
	}
	
	private void removePlayersBox() {
		rightBox.getChildren().remove(playersBox);
	}
	
	private void addStartGame() {
		bottomBox.getChildren().add(startGame);
	}
	
	private void removeStartGame() {
		bottomBox.getChildren().remove(startGame);
	}
	
	private void addReturn() {
		rightBox.getChildren().add(returnHome);
	}
	
	private void removeReturn() {
		rightBox.getChildren().remove(returnHome);
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listItemClicked(ImageView object) {
		// TODO Auto-generated method stub
		
	}

}
