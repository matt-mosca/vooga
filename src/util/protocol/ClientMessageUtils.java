package util.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

	public Collection<Integer> getCurrentSpriteIds() {
		return idsToImageViews.keySet();
	}
	
	public void handleSpriteUpdates(Update update) {
		update.getNewSpritesList().forEach(newSprite -> addNewSpriteToDisplay(newSprite));
		update.getSpriteUpdatesList().forEach(updatedSprite -> updateSpriteDisplay(updatedSprite));
		update.getSpriteDeletionsList().forEach(deletedSprite -> removeDeadSpriteFromDisplay(deletedSprite));
		System.out.println("Size of image map: " + idsToImageViews.keySet().size());
	}
	
	public ImageView getRepresentationFromSpriteId(int id) {
		return idsToImageViews.get(id);
	}
	
	public int addNewSpriteToDisplay(NewSprite newSprite) {
		ImageView imageViewForSprite = new ImageView(new Image(newSprite.getImageURL()));
		imageViewForSprite.setFitHeight(newSprite.getImageHeight());
		imageViewForSprite.setFitWidth(newSprite.getImageWidth());
		imageViewForSprite.setX(newSprite.getSpawnX());
		imageViewForSprite.setY(newSprite.getSpawnY());
		int spriteId = newSprite.getSpriteId();
		idsToImageViews.put(spriteId, imageViewForSprite);
		return spriteId;
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
