package networking;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.SpriteUpdate;
import util.path.PathList;

public class CollaborativeAuthoringClient extends AbstractClient implements AuthoringModelController  {

	private final int PORT = 9043;
	
	public CollaborativeAuthoringClient() {
		super();
	}
		
	@Override
	protected int getPort() {
		return PORT;
	}

	@Override
	public void exportGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLevel(int level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteLevel(int level) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, List<String>> getElementBaseConfigurationOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void defineElement(String elementName, Map<String, String> properties) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, String> upgradeProperties)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, String> propertiesToUpdate, boolean retroactive)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int placePathFollowingElement(String elementName, PathList pathList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addElementToInventory(String elementName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCurrentLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SpriteUpdate moveElement(int elementId, double xCoordinate, double yCoordinate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, String> propertiesToUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteElement(int elementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, List<Map<String, String>>> getAllDefinedElementUpgrades() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGameName(String gameName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameDescription(String gameDescription) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVictoryCondition(String conditionIdentifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefeatCondition(String conditionIdentifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatusProperty(String property, Double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourceEndowments(Map<String, Double> resourceEndowments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourceEndowment(String resourceName, double newResourceEndowment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUnitCost(String elementName, Map<String, Double> unitCosts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setWaveProperties(Map<String, ? extends Object> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void editWaveProperties(int waveId, Map<String, ? extends Object> updatedProperties,
			Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<String> getPossibleVictoryConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		// TODO Auto-generated method stub
		return null;
	}

}
