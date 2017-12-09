package util.protocol;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.Update;

public class ClientMessageUtils {

	private Map<Integer, ImageView> idsToImageViews = new HashMap<>();
	
	public void initializeLoadedLevel(LevelInitialized levelInitialized) {
		if (levelInitialized.hasError()) {
			// TODO - Handle error - display dialog to user
			return;
		}
		if (levelInitialized.hasSpritesAndStatus()) {
			handleSpriteUpdates(levelInitialized.getSpritesAndStatus());
		}
		if (levelInitialized.hasInventory()) {
			// Could choose to initialize here, but it appears the front end does it in
			// Toolbar.initializeInventory() with hardcoded sizes?
		}
	}

	public void handleSpriteUpdates(Update update) {
		update.getNewSpritesList().forEach(newSprite -> addNewSpriteToDisplay(newSprite));
		update.getSpriteUpdatesList().forEach(updatedSprite -> updateSpriteDisplay(updatedSprite));
		update.getSpriteDeletionsList().forEach(deletedSprite -> removeDeadSpriteFromDisplay(deletedSprite));
	}
	
	public ImageView getRepresentationFromSpriteId(int id) {
		return idsToImageViews.get(id);
	}

	private void addNewSpriteToDisplay(NewSprite newSprite) {
		ImageView imageViewForSprite = new ImageView(new Image(newSprite.getImageURL()));
		imageViewForSprite.setFitHeight(newSprite.getImageHeight());
		imageViewForSprite.setFitWidth(newSprite.getImageWidth());
		imageViewForSprite.setX(newSprite.getSpawnX());
		imageViewForSprite.setY(newSprite.getSpawnY());
		idsToImageViews.put(newSprite.getSpriteId(), imageViewForSprite);
	}

	private void updateSpriteDisplay(SpriteUpdate updatedSprite) {
		ImageView imageViewForSprite = idsToImageViews.get(updatedSprite.getSpriteId());
		imageViewForSprite.setX(updatedSprite.getNewX());
		imageViewForSprite.setY(updatedSprite.getNewY());
	}

	private void removeDeadSpriteFromDisplay(SpriteDeletion spriteDeletion) {
		idsToImageViews.remove(spriteDeletion.getSpriteId());
	}


}
