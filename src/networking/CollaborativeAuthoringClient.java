package networking;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.SpriteUpdate;
import util.io.SerializationUtils;

public class CollaborativeAuthoringClient extends AbstractClient implements AuthoringModelController  {

	private final int PORT = 9043;
	
	
	public CollaborativeAuthoringClient(SerializationUtils serializationUtils) {
		super(serializationUtils);
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
	public void defineElement(String elementName, Map<String, Object> properties) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, Object> upgradeProperties)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateElementDefinition(String elementName, Map<String, Object> propertiesToUpdate, boolean retroactive)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
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
	public void updateElementProperties(int elementId, Map<String, Object> propertiesToUpdate) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteElement(int elementId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Map<String, List<Map<String, Object>>> getAllDefinedElementUpgrades() {
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
	public int createWaveProperties(Map<String, Object> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) throws ReflectiveOperationException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void editWaveProperties(int waveId, Map<String, Object> updatedProperties,
			Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) throws ReflectiveOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getWaveProperties(int waveNum) {
		// TODO Auto-generated method stub
		return null;
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


	@Override
	protected int getPort() {
		return PORT;
	}

}
