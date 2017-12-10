package networking;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteUpdate;
import util.io.SerializationUtils;
import util.path.PathList;

public class CollaborativeAuthoringClient extends AbstractClient implements AuthoringModelController  {

	private final int PORT = 9043;
	
	public CollaborativeAuthoringClient(SerializationUtils serializationUtils) {
		super(serializationUtils);
	}

	@Override
	public void exportGame() {

	}

	@Override
	public void setLevel(int level) {

	}

	@Override
	public void deleteLevel(int level) throws IllegalArgumentException {

	}

	@Override
	public Map<String, List<String>> getElementBaseConfigurationOptions() {
		return null;
	}

	@Override
	public Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices) {
		return null;
	}

	@Override
	public void defineElement(String elementName, Map<String, Object> properties) throws IllegalArgumentException {

	}

	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, Object> upgradeProperties) throws IllegalArgumentException {

	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, Object> propertiesToUpdate, boolean retroactive) throws IllegalArgumentException {

	}

	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {

	}

	@Override
	public void addElementToInventory(String elementName) {

	}

	@Override
	public SpriteUpdate moveElement(int elementId, double xCoordinate, double yCoordinate) {
		return null;
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, Object> propertiesToUpdate) {

	}

	@Override
	public void deleteElement(int elementId) {

	}

	@Override
	public Map<String, List<Map<String, Object>>> getAllDefinedElementUpgrades() {
		return null;
	}

	@Override
	public void setGameName(String gameName) {

	}

	@Override
	public void setGameDescription(String gameDescription) {

	}

	@Override
	public void setVictoryCondition(String conditionIdentifier) {

	}

	@Override
	public void setDefeatCondition(String conditionIdentifier) {

	}

	@Override
	public void setStatusProperty(String property, Double value) {

	}

	@Override
	public void setResourceEndowments(Map<String, Double> resourceEndowments) {

	}

	@Override
	public void setResourceEndowment(String resourceName, double newResourceEndowment) {

	}

	@Override
	public void setUnitCost(String elementName, Map<String, Double> unitCosts) {

	}

	@Override
	public int setWaveProperties(Map<String, Object> waveProperties, Collection<String> elementNamesToSpawn, Point2D spawningPoint) throws ReflectiveOperationException {
		return 0;
	}

	@Override
	public void editWaveProperties(int waveId, Map<String, Object> updatedProperties, Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) throws ReflectiveOperationException {

	}

	@Override
	public Collection<String> getPossibleVictoryConditions() {
		return null;
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		return null;
	}

	@Override
	public int getCurrentLevel() {
		return 0;
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		return null;
	}

	@Override
	protected int getPort() {
		return PORT;
	}

}
