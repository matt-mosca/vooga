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
import networking.MultiPlayerClient;
import util.io.SerializationUtils;

public class MultiplayerLobby extends ScreenDisplay {
//	private static final String BACKGROUND_IMAGE = "grass_large.png";
	private static final String BACKGROUND_IMAGE = "space_background.jpeg";
	
	private BorderPane multiplayerLayout;
	private Label topScreenLabel;
	private Label lobbyPlayersLabel;
	private Label lobbiesLabel;
	private VBox buttonBox;
	private VBox playersBox;
	private VBox lobbiesListBox;
	private ButtonFactory buttonFactory;
	private Button createGameLobby;
	private Button joinGameLobby;
	private Button startGame;
	private Button returnHome;
	private Button backHome;
	private Button returnToLobbies;
	private Button joinSelectedLobby;
	private MultiplayerListBox lobbies;
	private MultiplayerListBox players;
	private MultiPlayerClient multiClient;

	//Add a way to select a lobby in the list and then press "Join Selected Lobby" to join it
	//To do this, make an EventHandler- upon clicking, reset an instance variable
	//Clicking Join Selected Lobby button will then join the lobby specified by that variable
	
	public MultiplayerLobby(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
//		multiplayerLayout = new BorderPane();
		topScreenLabel = new Label();
		lobbyPlayersLabel = new Label();
		lobbiesLabel = new Label();
		buttonFactory = new ButtonFactory();
		buttonBox = new VBox();
		playersBox = new VBox();
		lobbiesListBox = new VBox();
		lobbies = new MultiplayerListBox();
		players = new MultiplayerListBox();
		multiClient = new MultiPlayerClient();
		setMultiplayerBackground(width, height);
//		rootAdd(multiplayerLayout);
		rootAdd(topScreenLabel);
		createButtons();
		setUpButtonBox();
		setUpPlayersBox();
		setUpLobbiesListBox();
		initializeMultiplayerHomeScreen();
		setStyleAndLayout(width, height);
	}
	
	private void setStyleAndLayout(int width, int height) {
		rootStyleAndClear("player/resources/multiplayer.css");
		topScreenLabel.getStyleClass().add("label2");
		buttonBox.setLayoutX(width / 2 - 100);
		buttonBox.setLayoutY(400);
		buttonBox.setSpacing(20);
//		buttonBox.getStyleClass().add("borders");
		playersBox.setLayoutX(width - 300);
		playersBox.setLayoutY(50);
		playersBox.setMinWidth(300);
		playersBox.setMinHeight(300);
		playersBox.getStyleClass().add("borders");
		lobbiesListBox.setLayoutX(width / 2 - 300);
		lobbiesListBox.setLayoutY(height / 2 - 200);
		lobbiesListBox.setMinWidth(600);
		lobbiesListBox.setMinHeight(400);
		lobbiesListBox.getStyleClass().add("borders");
		startGame.setLayoutX(width / 2 - 50);
		startGame.setLayoutY(height - 100);
		returnHome.setLayoutX(width - 300);
		returnHome.setLayoutY(height - 40);
		backHome.setLayoutX(width - 300);
		backHome.setLayoutY(height - 40);
		returnToLobbies.setLayoutX(width - 300);
		returnToLobbies.setLayoutY(height - 80);
		joinSelectedLobby.setLayoutX(width - 300);
		joinSelectedLobby.setLayoutY(height - 80);
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
	
	private void initializeLobbiesList() {
		addLobbiesListBox();
		addBack();
		addJoinSelectedLobby();
		lobbies.setNames(multiClient.getGameRooms());
	}
	
	//Eventually should take in parameter of lobby name
	private void initializeLobby() {
		setTopLabelForLobby();
		addPlayersBox();
		addStartGame();
		addReturn();
		addReturnToLobbies();
//		players.setNames(multiClient.getPlayerNames("Wow"));
	}
	
	private void clearMultiplayerHomeScreen() {
		removeButtonBox();
	}
	
	private void clearLobbiesList() {
		removeLobbiesListBox();
		removeBack();
		removeJoinSelectedLobby();
	}
	
	private void clearLobby() {
		removePlayersBox();
		removeStartGame();
		removeReturn();
		removeReturnToLobbies();
	}
	
	private void changeHomeToLobbiesList() {
		clearMultiplayerHomeScreen();
		initializeLobbiesList();
	}
	
	private void changeLobbiesListToLobby() {
		clearLobbiesList();
		initializeLobby();
	}
	
	private void changeHomeToLobby() {
		clearMultiplayerHomeScreen();
		initializeLobby();
	}
	
	private void changeLobbiesListToHome() {
		clearLobbiesList();
		initializeMultiplayerHomeScreen();
	}
	
	private void changeLobbyToHome() {
		clearLobby();
		initializeMultiplayerHomeScreen();
		//Exit lobby
	}
	
	private void changeLobbyToLobbiesList() {
		clearLobby();
		initializeLobbiesList();
		//Exit lobby
	}
	
	private void createLobby() {
		multiClient.createGameRoom("circularMonkey.voog");
		changeHomeToLobby();
		promptForLobbyName();
		//createLobby + set lobby name
		//joinLobby
		//Add player name to lobby
	}
	
	private void joinLobby() {
		changeHomeToLobbiesList();
		promptForPlayerName();
		//joinLobby
		//Add player name to lobby
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
		returnHome = buttonFactory.buildDefaultTextButton("Return to Multiplayer Home", e -> changeLobbyToHome());
		backHome = buttonFactory.buildDefaultTextButton("Back", e -> changeLobbiesListToHome());
		returnToLobbies = buttonFactory.buildDefaultTextButton("View Open Lobbies", e -> changeLobbyToLobbiesList());
		joinSelectedLobby = buttonFactory.buildDefaultTextButton("Join Selected Lobby", e -> changeLobbiesListToLobby());
	}
	
	private void setUpButtonBox() {
		buttonBox.getChildren().add(createGameLobby);
		buttonBox.getChildren().add(joinGameLobby);
	}
	
	private void setUpPlayersBox() {
		lobbyPlayersLabel.setText("Players");
		playersBox.getChildren().add(lobbyPlayersLabel);
		players.attach(playersBox);
	}
	
	private void setUpLobbiesListBox() {
		lobbiesLabel.setText("Lobbies");
		lobbiesListBox.getChildren().add(lobbiesLabel);
		lobbies.attach(lobbiesListBox);
	}
	
	private void addButtonBox() {
//		centerBox.getChildren().add(buttonBox);
		rootAdd(buttonBox);
	}
	
	private void removeButtonBox() {
//		centerBox.getChildren().remove(buttonBox);
		rootRemove(buttonBox);
	}

	private void addPlayersBox() {
//		rightBox.getChildren().add(playersBox);
		rootAdd(playersBox);
	}
	
	private void removePlayersBox() {
//		rightBox.getChildren().remove(playersBox);
		rootRemove(playersBox);
	}
	
	private void addStartGame() {
//		bottomBox.getChildren().add(startGame);
		rootAdd(startGame);
	}
	
	private void removeStartGame() {
//		bottomBox.getChildren().remove(startGame);
		rootRemove(startGame);
	}
	
	private void addReturn() {
//		rightBox.getChildren().add(returnHome);
		rootAdd(returnHome);
	}
	
	private void removeReturn() {
//		rightBox.getChildren().remove(returnHome);
		rootRemove(returnHome);
	}
	
	private void addBack() {
//		rightBox.getChildren().add(returnHome);
		rootAdd(backHome);
	}
	
	private void removeBack() {
//		rightBox.getChildren().remove(returnHome);
		rootRemove(backHome);
	}
	
	private void addReturnToLobbies() {
//		rightBox.getChildren().add(returnHome);
		rootAdd(returnToLobbies);
	}
	
	private void removeReturnToLobbies() {
//		rightBox.getChildren().remove(returnHome);
		rootRemove(returnToLobbies);
	}
	
	private void addLobbiesListBox() {
		rootAdd(lobbiesListBox);
	}
	
	private void removeLobbiesListBox() {
		rootRemove(lobbiesListBox);
	}
	
	private void addJoinSelectedLobby() {
		rootAdd(joinSelectedLobby);
	}
	
	private void removeJoinSelectedLobby() {
		rootRemove(joinSelectedLobby);
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
