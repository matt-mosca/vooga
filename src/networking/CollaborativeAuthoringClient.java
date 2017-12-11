package networking;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;

import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorClient.DeleteLevel;
import networking.protocol.AuthorClient.ExportGame;
import networking.protocol.AuthorClient.GetAuxiliaryElementConfigurationOptions;
import networking.protocol.AuthorClient.GetElementBaseConfigurationOptions;
import networking.protocol.AuthorClient.SetLevel;
import networking.protocol.AuthorServer.AuthoringServerMessage;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.SpriteUpdate;
import util.io.SerializationUtils;

public class CollaborativeAuthoringClient extends AbstractClient implements AuthoringModelController {

	private final int PORT = 9043;

	public CollaborativeAuthoringClient() {
		super();
	}

	@Override
	public void exportGame() {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setExportGame(ExportGame.getDefaultInstance()).build()
				.toByteArray());
	}

	@Override
	public void setLevel(int level) {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setSetLevel(SetLevel.newBuilder().setLevel(level)).build()
				.toByteArray());
	}

	@Override
	public void deleteLevel(int level) throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setDeleteLevel(DeleteLevel.newBuilder().setLevel(level))
				.build().toByteArray());
	}

	@Override
	public Map<String, List<String>> getElementBaseConfigurationOptions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetElementBaseConfig(GetElementBaseConfigurationOptions.getDefaultInstance()).build()
				.toByteArray());
		return handleElementBaseConfigurationOptionsResponse(readAuthoringServerResponse());
	}

	@Override
	public Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices) {
		//writeRequestBytes(AuthoringClientMessage.newBuilder().setGetAuxiliaryElementConfig(GetAuxiliaryElementConfigurationOptions.newBuilder().addAllBaseConfigurationChoices(baseConfigurationChoices.entrySet().map(entry -> Property.newBuilder.))))
		return new HashMap<>();
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
	public void editWaveProperties(int waveNum, Map<String, Object> updatedProperties,
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
	public Map<String, Collection<Integer>> getCurrentVictoryConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentDefeatConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPort() {
		return PORT;
	}

	Map<String, List<String>> handleElementBaseConfigurationOptionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, List<String>> configOptions = new HashMap<>();
		authoringServerMessage.getElementBaseConfigurationOptionsList()
				.forEach(upgrade -> configOptions.put(upgrade.getConfigKey(), upgrade.getConfigOptionsList()));
		return configOptions;
	}

	private AuthoringServerMessage readAuthoringServerResponse() {
		try {
			return AuthoringServerMessage.parseFrom(readResponseBytes());
		} catch (InvalidProtocolBufferException e) {
			return AuthoringServerMessage.getDefaultInstance(); // empty message
		}
	}

}
