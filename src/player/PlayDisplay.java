package player;

import engine.PlayModelController;

import javafx.stage.Stage;

/**
 * Main play display, uses file chooser.
 */
public class PlayDisplay extends AbstractPlayDisplay {

	public PlayDisplay(int width, int height, Stage stage, PlayModelController myController) {
		super(width, height, stage, myController);
		initializeGameState();
	}
	private void step() {
		Update latestUpdate = myController.update();
		if (myController.isReadyForNextLevel()) {
			hideTransitorySplashScreen();
			// animation.play();
			myController.resume();
		}
		if (myController.isLevelCleared()) {
			level++;
			animation.pause();
			myController.pause();
			launchTransitorySplashScreen();
			hud.initialize(myController.getResourceEndowments());
		} else if (myController.isLost()) {
			// launch lost screen
		} else if (myController.isWon()) {
			// launch win screen
		}
		hud.update(myController.getResourceEndowments());
		clientMessageUtils.handleSpriteUpdates(latestUpdate);
		loadSprites();
	}

	private void launchTransitorySplashScreen() {
		this.getStage().setScene(myTransitionScene);
	}

	private void hideTransitorySplashScreen() {
		this.getStage().setScene(this.getScene());
	}

	private void createGameArea(int sideLength) {
		myPlayArea = new PlayArea(myController, clientMessageUtils, sideLength, sideLength);
		myPlayArea.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.dropElement(e));
		currentElements = new ArrayList<ImageView>();
		rootAdd(myPlayArea);
	}

	private void dropElement(MouseEvent e) {
		if (selected) {
			selected = false;
			this.getScene().setCursor(Cursor.DEFAULT);
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				Point2D startLocation = new Point2D(e.getX(), e.getY());
				try {
					NewSprite newSprite = myController.placeElement(placeable.getElementName(), startLocation);
					clientMessageUtils.addNewSpriteToDisplay(newSprite);
				} catch (ReflectiveOperationException failedToPlaceElementException) {
					// todo - handle
				}
			}
		}
	}

	@Override
	public void listItemClicked(ImageView image) {
		if(!checkFunds(image)) return;
		Alert costDialog = new Alert(AlertType.CONFIRMATION);
		costDialog.setTitle("Purchase Resource");
		costDialog.setHeaderText(null);
		costDialog.setContentText("Would you like to purchase this object?");

		Optional<ButtonType> result = costDialog.showAndWait();
		if (result.get() == ButtonType.OK) {
			placeable = new StaticObject(1, this, (String) image.getUserData());
			placeable.setElementName(image.getId());
			this.getScene().setCursor(new ImageCursor(image.getImage()));
			selected = true;
		}
	}
	
	//TODO call this on click event of the static objects
	public void upgradeableClicked(ImageView image) {
		if(checkFunds(image)) return;
		Alert costDialog = new Alert(AlertType.CONFIRMATION);
		costDialog.setTitle("Upgrade Resource");
		costDialog.setHeaderText(null);
		costDialog.setContentText("Would you like to upgrade this object?");
		
		Optional<ButtonType> result = costDialog.showAndWait();
		if (result.get() == ButtonType.OK) {
			//pass in the image id to this, but make sure we're actually setting it
//			myController.upgradeElement();
		}
	}
	
	private boolean checkFunds(ImageView image) {
		try {
		Map<String, Double> unitCosts = myController.getElementCosts().get(image.getId());
		if (!hud.hasSufficientFunds(unitCosts)) {
			launchInvalidResources();
			return false;
		}
		return true;}
		catch(Exception e) {
			return false;
		}
	}

	private void launchInvalidResources() {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Resource Error!");
		error.setHeaderText(null);
		error.setContentText("You do not have the funds for this item.");
		error.show();
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}
	
	protected void changeLevel(int newLevel) {
		level = newLevel;
		try {
			clientMessageUtils.initializeLoadedLevel(myController.loadOriginalGameState(gameState, newLevel));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
