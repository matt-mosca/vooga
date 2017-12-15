package player;

import java.util.Optional;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import display.factory.ButtonFactory;
import display.splashScreen.NotifiableDisplay;
import display.splashScreen.ScreenDisplay;
import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import networking.AbstractClient;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.Notification;
import networking.Constants;

public class MultiplayerLobby extends ScreenDisplay {
	// private static final String BACKGROUND_IMAGE = "grass_large.png";
	private static final String BACKGROUND_IMAGE = "space_background.jpeg";

	private NotifiableDisplay playDisplay;
	private Label topScreenLabel;
	private Label lobbyPlayersLabel;
	private Label lobbiesLabel;
	private Label usernameLabel;
	private Label gameStateLabel;
	private VBox buttonBox;
	private VBox playersBox;
	private VBox lobbiesListBox;
	private VBox rightBox;
	private VBox activityBox;
	private HBox usernameBox;
	private HBox notificationBox;
	private NotificationBar notificationBar;
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
	private ActivityListBox activityList;
	private AbstractClient multiClient;
	private String username;
	private String gameName;
	private String currentLobby;

	private boolean launched = false;

	// Add a way to select a lobby in the list and then press "Join Selected Lobby"
	// to join it
	// To do this, make an EventHandler- upon clicking, reset an instance variable
	// Clicking Join Selected Lobby button will then join the lobby specified by
	// that variable

	// TODO need to start the play display at some point when you are ready!

	public MultiplayerLobby(int width, int height, Paint background, Stage currentStage, NotifiableDisplay play,
			AbstractClient client) {
		super(width, height, background, currentStage);
		// multiplayerLayout = new BorderPane();
		playDisplay = play;
		topScreenLabel = new Label();
		lobbyPlayersLabel = new Label();
		lobbiesLabel = new Label();
		usernameLabel = new Label();
		gameStateLabel = new Label();
		buttonFactory = new ButtonFactory();
		buttonBox = new VBox();
		playersBox = new VBox();
		lobbiesListBox = new VBox();
		rightBox = new VBox();
		activityBox = new VBox();
		usernameBox = new HBox();
		notificationBox = new HBox();
		notificationBar = new NotificationBar(notificationBox, "");
		lobbies = new MultiplayerListBox();
		players = new MultiplayerListBox();
		activityList = new ActivityListBox();
		multiClient = client;
		System.out.println("Registering notification listener");
		registerNotificationListener();
		username = new String();
		currentLobby = new String();
		System.out.println("Retrieving game name");
		String loadedGameName = playDisplay.getGameState();
		gameName = loadedGameName == null ? Constants.NEW_GAME_NAME : loadedGameName;
		System.out.println("Setting multiplayer background");
		setMultiplayerBackground(width, height);
		// rootAdd(multiplayerLayout);
		rootAdd(topScreenLabel);
		createButtons();
		setUpButtonBox();
		setUpRightBox();
		setUpLobbiesListBox();
		setUpActivityBox();
		initializeMultiplayerHomeScreen();
		createGameStateLabel(width, height);
		setStyleAndLayout(width, height);
		//
		// multiClient.createGameRoom("circularMonkey.voog", "Noooo");
		// multiClient.joinGameRoom("Noooo", "Matt");
		// System.out.println(multiClient.getPlayerNames());
		// System.out.println(multiClient.getGameRooms());
		// multiClient.exitGameRoom();
		// System.out.println(multiClient.getGameRooms());
		// multiClient.joinGameRoom("Noooo", "Matt");
		// System.out.println(multiClient.getPlayerNames());
		// System.out.println(multiClient.getGameRooms());
	}

	public void promptForUsername() {
		TextInputDialog dialog = new TextInputDialog("Matthew");
		dialog.setTitle("Name Selection");
		dialog.setHeaderText("Enter a username.");
		dialog.setContentText("Username:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			username = result.get();
		}
		setUsername();
	}

	private void setUsername() {
		usernameLabel.setText(username);
		usernameBox.getChildren().add(usernameLabel);
	}

	private void setStyleAndLayout(int width, int height) {
		rootStyleAndClear("player/resources/multiplayer.css");
		topScreenLabel.getStyleClass().add("label2");
		buttonBox.setLayoutX(width / 2 - 100);
		buttonBox.setLayoutY(400);
		buttonBox.setSpacing(20);
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
		rightBox.setLayoutX(width - 300);
		rightBox.setLayoutY(20);
		rightBox.setSpacing(40);
		usernameBox.getStyleClass().add("borders");
		usernameBox.setMinWidth(300);
		usernameBox.setSpacing(10);
		lobbiesListBox.getStyleClass().add("borders");
		activityBox.setLayoutY(100);
		activityBox.setMinWidth(300);
		activityBox.setMinHeight(300);
		activityBox.getStyleClass().add("borders");
		notificationBox.setLayoutX(350);
		rootAdd(notificationBox);
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
		addActivityBox();
		activityList.setNames(multiClient);
	}

	private void initializeLobbiesList() {
		addLobbiesListBox();
		addBack();
		addJoinSelectedLobby();
		System.out.println(multiClient.getGameRooms());
		lobbies.setNames(multiClient.getGameRooms());
	}

	private void initializeLobby() {
		setTopLabelForLobby();
		addPlayersBox();
		addStartGame();
		addReturn();
		addReturnToLobbies();
		System.out.println(multiClient.getPlayerNames());
		System.out.println(multiClient.getGameRooms());
		players.setNames(multiClient.getPlayerNames());
	}

	private void clearMultiplayerHomeScreen() {
		removeButtonBox();
		removeActivityBox();
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
		System.out.println("EXITING LOBBY");
		//multiClient.exitGameRoom();
		initializeMultiplayerHomeScreen();
	}

	private void changeLobbyToLobbiesList() {
		clearLobby();
		System.out.println("EXITING LOBBY");
		//multiClient.exitGameRoom();
		initializeLobbiesList();
	}

	private void createLobby() {
		promptForLobbyName();
		String createdLobbyName = multiClient.createGameRoom(gameName, currentLobby);
		multiClient.joinGameRoom(createdLobbyName, username);
		changeHomeToLobby();
	}

	private void joinLobby() {
		changeHomeToLobbiesList();
	}

	private void joinSelectedLobby() {
		String selectedLobby = getSelectedLobby();
		System.out.println(selectedLobby);
		if (selectedLobby != null)
			currentLobby = selectedLobby;
		multiClient.joinGameRoom(currentLobby, username);
		changeLobbiesListToLobby();
	}

	// getSelectionModel.getSelectedItem

	private String getSelectedLobby() {
		return lobbies.getListView().getSelectionModel().getSelectedItem();
	}

	// Call this method when user wants to create new lobby- to name the lobby
	// This method should pull up prompt box
	private void promptForLobbyName() {
		TextInputDialog dialog = new TextInputDialog("Cool lobby");
		dialog.setTitle("Lobby Name");
		dialog.setHeaderText("Enter a lobby name.");
		dialog.setContentText("Lobby name:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			currentLobby = result.get();
		}
	}

	// This method or an external method would be called when "START GAME" is
	// pressed (serves as action for button)
	private void startGame() {
		LevelInitialized newLevelData = multiClient.launchGameRoom();
		launched = true;
		System.out.println("Launched game room!");
		getStage().setScene(playDisplay.getScene());
		System.out.println("Starting play display!");
		playDisplay.startDisplay(newLevelData);
		// multiClient.launchGameRoom(currentLobby);
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
		joinSelectedLobby = buttonFactory.buildDefaultTextButton("Join Selected Lobby", e -> joinSelectedLobby());
	}

	private void createGameStateLabel(int width, int height) {
		// VBox gameStateBox = new VBox();
		// gameStateBox.getStyleClass().add("borders");
		// gameLabel
		// gameStateBox.getChildren().add(gameStateLabel);
		// gameStateBox.setLayoutY(height - 80);
		gameStateLabel.setText("Game:  " + gameName);
		gameStateLabel.setLayoutY(height - 40);
		rootAdd(gameStateLabel);
	}

	private void setUpButtonBox() {
		buttonBox.getChildren().add(createGameLobby);
		buttonBox.getChildren().add(joinGameLobby);
	}

	private void setUpRightBox() {
		String imageName = "person_silhouette.png";
		Image personImage = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		ImageView person = new ImageView(personImage);
		person.setFitWidth(30);
		person.setFitHeight(30);
		usernameBox.getChildren().add(person);
		usernameBox.getStyleClass().add("borders");
		rightBox.getChildren().add(usernameBox);
		lobbyPlayersLabel.setText("Players");
		playersBox.getChildren().add(lobbyPlayersLabel);
		players.attach(playersBox);
		rootAdd(rightBox);
	}

	private void setUpActivityBox() {
		Label activityLabel = new Label("Active Players");
		activityBox.getChildren().add(activityLabel);
		activityList.attach(activityBox);
		activityList.setNames(multiClient);
	}

	private void setUpLobbiesListBox() {
		lobbiesLabel.setText("Lobbies");
		lobbiesListBox.getChildren().add(lobbiesLabel);
		lobbies.attach(lobbiesListBox);
	}

	private void addButtonBox() {
		// centerBox.getChildren().add(buttonBox);
		rootAdd(buttonBox);
	}

	private void removeButtonBox() {
		// centerBox.getChildren().remove(buttonBox);
		rootRemove(buttonBox);
	}

	private void addPlayersBox() {
		rightBox.getChildren().add(playersBox);
		// rootAdd(playersBox);
	}

	private void removePlayersBox() {
		rightBox.getChildren().remove(playersBox);
		// rootRemove(playersBox);
	}

	private void addStartGame() {
		// bottomBox.getChildren().add(startGame);
		rootAdd(startGame);
	}

	private void removeStartGame() {
		// bottomBox.getChildren().remove(startGame);
		rootRemove(startGame);
	}

	private void addReturn() {
		// rightBox.getChildren().add(returnHome);
		rootAdd(returnHome);
	}

	private void removeReturn() {
		// rightBox.getChildren().remove(returnHome);
		rootRemove(returnHome);
	}

	private void addBack() {
		// rightBox.getChildren().add(returnHome);
		rootAdd(backHome);
	}

	private void removeBack() {
		// rightBox.getChildren().remove(returnHome);
		rootRemove(backHome);
	}

	private void addReturnToLobbies() {
		// rightBox.getChildren().add(returnHome);
		rootAdd(returnToLobbies);
	}

	private void removeReturnToLobbies() {
		// rightBox.getChildren().remove(returnHome);
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

	private void registerNotificationListener() {
		multiClient.registerNotificationListener(notification -> processNotification(notification));
	}

	private void processNotification(Change<? extends Message> notificationMessage) {
		Platform.runLater(() -> {
			System.out.println("Processing notification in lobby!");
			while (notificationMessage.next()) {
				notificationMessage.getAddedSubList().stream().forEach(message -> {
					try {
						Notification notification = Notification.parseFrom(message.toByteArray());						
						if (notification.hasPlayerJoined()) {
							String nameOfJoinedPlayer = notification.getPlayerJoined().getUserName();
							handleUserJoinedRoom(nameOfJoinedPlayer);
						}
						if (notification.hasPlayerExited()) {
							String nameOfExitedPlayer = notification.getPlayerExited().getUserName();
							handleUserExitedRoom(nameOfExitedPlayer);
						}
						if (notification.hasLevelInitialized() && !launched) {
							LevelInitialized levelData = notification.getLevelInitialized();
							handleGameLaunched(levelData);
						}
						if (launched) {
							System.out.println("In-game notification!");
							playDisplay.receiveNotification(message.toByteArray());
						}
					} catch (InvalidProtocolBufferException e) {
					}
				});
			}
		});
	}

	private void handleUserJoinedRoom(String userName) {
		if(!userName.equals("bot")) {
			notificationBar.show();
			notificationBar.setText("User " + userName + " joined!");
		}
		System.out.println("User " + userName + " joined!");
		players.setNames(multiClient.getPlayerNames());
	}

	private void handleUserExitedRoom(String userName) {
		if(!userName.equals("bot")) {
			notificationBar.setText("User " + userName + " exited!");
			notificationBar.show();
		}
		System.out.println("User " + userName + "exited!");
		players.setNames(multiClient.getPlayerNames());
	}

	private void handleGameLaunched(LevelInitialized levelData) {
		launched = true;
		getStage().setScene(playDisplay.getScene());
		playDisplay.startDisplay(levelData);
	}

	private void addActivityBox() {
		rootAdd(activityBox);
	}

	private void removeActivityBox() {
		rootRemove(activityBox);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void listItemClicked(MouseEvent e, ImageView object) {
		// TODO Auto-generated method stub

	}

}
