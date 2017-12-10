package util.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import engine.game_elements.GameElement;
import networking.protocol.PlayerServer.ElementCost;
import networking.protocol.PlayerServer.Inventory;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Resource;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.StatusUpdate;
import networking.protocol.PlayerServer.TemplateProperties;
import networking.protocol.PlayerServer.TemplateProperty;
import networking.protocol.PlayerServer.Update;

public class ServerMessageUtils {

	public ServerMessageUtils() {
		// TODO Auto-generated constructor stub
	}

	public LevelInitialized packageState(Map<Integer, GameElement> levelSprites, Collection<String> inventory, Map<String, Double> resourceEndowments, int currentLevel) {
		return LevelInitialized.newBuilder()
				.setSpritesAndStatus(packageUpdates(levelSprites, new HashMap<>(),
						new HashMap<>(), false, false, false, false, resourceEndowments, currentLevel))
				.setInventory(packageInventory(inventory)).build();
	}

	public Update packageStatusUpdate(boolean levelCleared, boolean isWon, boolean isLost, boolean inPlay, int currentLevel) {
		return Update.newBuilder().setStatusUpdates(getStatusUpdate(levelCleared, isWon, isLost, inPlay, currentLevel)).build();
	}

	public Inventory packageInventory(Collection<String> inventory) {
		Inventory.Builder inventoryBuilder = Inventory.newBuilder();
		inventory.forEach(template -> inventoryBuilder.addTemplates(template));
		return inventoryBuilder.build();
	}

	public Collection<TemplateProperties> packageAllTemplateProperties(
			Map<String, Map<String, String>> templateProperties) {
		return packageAllMessages(templateProperties.keySet(),
				templateName -> packageTemplateProperties(templateName, templateProperties.get(templateName)));
	}

	public Collection<ElementCost> packageAllElementCosts(Map<String, Map<String, Double>> elementCosts) {
		return packageAllMessages(elementCosts.keySet(),
				elementName -> packageElementCosts(elementName, elementCosts.get(elementName)));
	}

	private <R> Collection<R> packageAllMessages(Collection<String> messageDataSupplier,
			Function<String, R> messagePackager) {
		return messageDataSupplier.stream().map(messageData -> messagePackager.apply(messageData))
				.collect(Collectors.toSet());
	}

	public TemplateProperties packageTemplateProperties(String templateName,
			Map<String, String> templatePropertiesMap) {
		TemplateProperties.Builder templatePropertiesBuilder = TemplateProperties.newBuilder();
		templatePropertiesMap.keySet()
				.forEach(templateProperty -> templatePropertiesBuilder.addProperty(TemplateProperty.newBuilder()
						.setName(templateProperty).setValue(templatePropertiesMap.get(templateProperty)).build()));
		return templatePropertiesBuilder.build();
	}

	public ElementCost packageElementCosts(String elementName, Map<String, Double> elementCosts) {
		ElementCost.Builder elementCostBuilder = ElementCost.newBuilder();
		elementCostBuilder.setElementName(elementName);
		elementCosts.keySet().forEach(resourceName -> elementCostBuilder.addCosts(
				Resource.newBuilder().setName(resourceName).setAmount(elementCosts.get(resourceName)).build()));
		return elementCostBuilder.build();
	}

	public Update packageUpdates(Map<Integer, GameElement> newSprites, Map<Integer, GameElement> updatedSprites,
			Map<Integer, GameElement> deletedSprites, boolean levelCleared, boolean isWon, boolean isLost,
			boolean inPlay, Map<String, Double> resourceEndowments, int currentLevel) {
		Update.Builder updateBuilder = Update.newBuilder();
		// Sprite Creations
		updateBuilder.addAllNewSprites(packageNewSprites(newSprites));
		// Sprite Updates
		updateBuilder.addAllSpriteUpdates(packageUpdatedSprites(updatedSprites));
		// Sprite Deletions
		updateBuilder.addAllSpriteDeletions(packageDeletedSprites(deletedSprites));
		// Status Updates
		updateBuilder.setStatusUpdates(getStatusUpdate(levelCleared, isWon, isLost, inPlay, currentLevel));
		// Resources - Just send all resources in update for now
		ResourceUpdate.Builder resourceUpdateBuilder = ResourceUpdate.newBuilder();
		resourceEndowments.keySet().forEach(resourceName -> resourceUpdateBuilder.addResources(
				Resource.newBuilder().setName(resourceName).setAmount(resourceEndowments.get(resourceName)).build()));
		return updateBuilder.setResourceUpdates(resourceUpdateBuilder.build()).build();
	}

	private StatusUpdate getStatusUpdate(boolean levelCleared, boolean isWon, boolean isLost, boolean inPlay, int currentLevel) {
		// Just always send status update for now
		return StatusUpdate.newBuilder().setLevelCleared(levelCleared).setIsWon(isWon).setIsLost(isLost)
				.setInPlay(inPlay).setCurrentLevel(currentLevel).build();
	}

	public Collection<NewSprite> packageNewSprites(Map<Integer, GameElement> newSprites) {
		return packageSprites(newSprites, (newSprite, newSpriteId) -> packageNewSprite(newSprite, newSpriteId));
	}

	private Collection<SpriteUpdate> packageUpdatedSprites(Map<Integer, GameElement> updatedSprites) {
		return packageSprites(updatedSprites,
				(updatedSprite, updatedSpriteId) -> packageUpdatedSprite(updatedSprite, updatedSpriteId));
	}

	private Collection<SpriteDeletion> packageDeletedSprites(Map<Integer, GameElement> deadSprites) {
		return packageSprites(deadSprites,
				(deadSprite, deadSpriteId) -> packageDeletedSprite(deadSprite, deadSpriteId));
	}

	private <R> Collection<R> packageSprites(Map<Integer, GameElement> spriteMap,
			BiFunction<GameElement, Integer, R> spriteFunction) {
		return spriteMap.entrySet().stream()
				.map(spriteEntry -> spriteFunction.apply(spriteEntry.getValue(), spriteEntry.getKey()))
				.collect(Collectors.toList());
	}

	public NewSprite packageNewSprite(GameElement newSprite, int spriteId) {
		return NewSprite.newBuilder().setSpriteId(spriteId).setImageURL(newSprite.getImageUrl())
				.setImageHeight(newSprite.getGraphicalRepresentation().getFitHeight())
				.setImageWidth(newSprite.getGraphicalRepresentation().getFitWidth()).setSpawnX(newSprite.getX())
				.setSpawnY(newSprite.getY()).build();
	}

	private SpriteUpdate packageUpdatedSprite(GameElement spriteToUpdate, int spriteId) {
		return SpriteUpdate.newBuilder().setSpriteId(spriteId).setNewX(spriteToUpdate.getX())
				.setNewY(spriteToUpdate.getY()).build();
	}

	private SpriteDeletion packageDeletedSprite(GameElement spriteToDelete, int spriteId) {
		return SpriteDeletion.newBuilder().setSpriteId(spriteId).build();
	}

}
