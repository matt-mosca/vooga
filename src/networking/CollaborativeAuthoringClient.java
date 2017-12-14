package networking;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.protobuf.InvalidProtocolBufferException;

import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import networking.protocol.AuthorClient.AddElementToInventory;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorClient.CreateWaveProperties;
import networking.protocol.AuthorClient.DefineElement;
import networking.protocol.AuthorClient.DefineElementUpgrade;
import networking.protocol.AuthorClient.DeleteElementDefinition;
import networking.protocol.AuthorClient.DeleteLevel;
import networking.protocol.AuthorClient.EditWaveProperties;
import networking.protocol.AuthorClient.ExportGame;
import networking.protocol.AuthorClient.GetAllDefinedElementUpgrades;
import networking.protocol.AuthorClient.GetAuxiliaryElementConfigurationOptions;
import networking.protocol.AuthorClient.GetCurrentDefeatConditions;
import networking.protocol.AuthorClient.GetCurrentLevel;
import networking.protocol.AuthorClient.GetCurrentVictoryConditions;
import networking.protocol.AuthorClient.GetElementBaseConfigurationOptions;
import networking.protocol.AuthorClient.GetPossibleDefeatConditions;
import networking.protocol.AuthorClient.GetPossibleVictoryConditions;
import networking.protocol.AuthorClient.GetResourceEndowments;
import networking.protocol.AuthorClient.GetWaveProperties;
import networking.protocol.AuthorClient.SetLevel;
import networking.protocol.AuthorClient.SetStatusProperty;
import networking.protocol.AuthorClient.SetUnitCost;
import networking.protocol.AuthorClient.SetVictoryCondition;
import networking.protocol.AuthorClient.UpdateElementDefinition;
import networking.protocol.AuthorClient.UpdateElementProperties;
import networking.protocol.AuthorClient.Property;
import networking.protocol.AuthorClient.ResourceEndowment;
import networking.protocol.AuthorClient.SetDefeatCondition;
import networking.protocol.AuthorClient.SetGameDescription;
import networking.protocol.AuthorClient.SetGameName;
import networking.protocol.AuthorServer.AuthoringServerMessage;

public class CollaborativeAuthoringClient extends AbstractClient implements AuthoringModelController {

	private final int PORT = 9043;

	public CollaborativeAuthoringClient() {
		super();
	}

	@Override
	public String exportGame() throws IOException {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setExportGame(ExportGame.getDefaultInstance()).build()
				.toByteArray());
		// todo - get message
		return "";
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
		writeRequestBytes(
				AuthoringClientMessage.newBuilder()
						.setGetAuxiliaryElementConfig(GetAuxiliaryElementConfigurationOptions.newBuilder()
								.addAllBaseConfigurationChoices(baseConfigurationChoices.entrySet().stream()
										.map(entry -> Property.newBuilder().setName(entry.getKey())
												.setValue(entry.getValue()).build())
										.collect(Collectors.toList())))
						.build().toByteArray());
		return handleAuxiliaryElementConfigurationOptions(readAuthoringServerResponse());
	}

	@Override
	public void defineElement(String elementName, Map<String, Object> properties) throws IllegalArgumentException {
		writeRequestBytes(
				AuthoringClientMessage.newBuilder()
						.setDefineElement(DefineElement.newBuilder().setElementName(elementName)
								.addAllProperties(getPropertiesFromObjectMap(properties)).build())
						.build().toByteArray());
	}

	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, Object> upgradeProperties)
			throws IllegalArgumentException {
		writeRequestBytes(
				AuthoringClientMessage.newBuilder()
						.setDefineElementUpgrade(DefineElementUpgrade.newBuilder().setElementName(elementName)
								.setUpgradeLevel(upgradeLevel)
								.addAllProperties(getPropertiesFromObjectMap(upgradeProperties)).build())
						.build().toByteArray());
	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, Object> propertiesToUpdate, boolean retroactive)
			throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setUpdateElementDefinition(UpdateElementDefinition.newBuilder().setElementName(elementName)
						.addAllProperties(getPropertiesFromObjectMap(propertiesToUpdate)).setRetroactive(retroactive)
						.build())
				.build().toByteArray());
	}

	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setDeleteElementDefinition(DeleteElementDefinition.newBuilder().setElementName(elementName).build())
				.build().toByteArray());
	}

	@Override
	public void addElementToInventory(String elementName) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setAddElementToInventory(AddElementToInventory.newBuilder().setElementName(elementName).build())
				.build().toByteArray());
	}

	@Override
	public int getCurrentLevel() {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setGetCurrentLevel(GetCurrentLevel.getDefaultInstance())
				.build().toByteArray());
		return handleCurrentLevelResponse(readAuthoringServerResponse());
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, Object> propertiesToUpdate) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setUpdateElementProperties(UpdateElementProperties.newBuilder().setElementId(elementId)
						.addAllProperties(getPropertiesFromObjectMap(propertiesToUpdate)).build())
				.build().toByteArray());
	}

	@Override
	public Map<String, List<Map<String, Object>>> getAllDefinedElementUpgrades() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetAllDefinedElementUpgrades(GetAllDefinedElementUpgrades.getDefaultInstance()).build()
				.toByteArray());
		return handleAllDefinedElementUpgradesResponse(readAuthoringServerResponse());
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetResourceEndowments(GetResourceEndowments.getDefaultInstance()).build().toByteArray());
		return handleResourceEndowmentsResponse(readAuthoringServerResponse());
	}

	@Override
	public void setGameName(String gameName) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetGameName(SetGameName.newBuilder().setGameName(gameName).build()).build().toByteArray());
	}

	@Override
	public void setGameDescription(String gameDescription) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetGameDescription(SetGameDescription.newBuilder().setGameDescription(gameDescription).build())
				.build().toByteArray());
	}

	@Override
	public void setVictoryCondition(String conditionIdentifier) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetVictoryCondition(
						SetVictoryCondition.newBuilder().setConditionIdentifier(conditionIdentifier).build())
				.build().toByteArray());
	}

	@Override
	public void setDefeatCondition(String conditionIdentifier) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetDefeatCondition(
						SetDefeatCondition.newBuilder().setConditionIdentifier(conditionIdentifier).build())
				.build().toByteArray());
	}

	@Override
	public void setStatusProperty(String property, Double value) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetStatusProperty(
						SetStatusProperty.newBuilder().setPropertyName(property).setPropertyValue(value).build())
				.build().toByteArray());
	}

	@Override
	public void setResourceEndowments(Map<String, Double> resourceEndowments) {
		resourceEndowments.entrySet().forEach(entry -> setResourceEndowment(entry.getKey(), entry.getValue()));
	}

	@Override
	public void setResourceEndowment(String resourceName, double newResourceEndowment) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.addSetResourceEndowments(
						ResourceEndowment.newBuilder().setName(resourceName).setAmount(newResourceEndowment).build())
				.build().toByteArray());
	}

	@Override
	public void setUnitCost(String elementName, Map<String, Double> unitCosts) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetUnitCost(SetUnitCost.newBuilder().setElementName(elementName)
						.addAllElementCosts(unitCosts.entrySet().stream()
								.map(entry -> ResourceEndowment.newBuilder().setName(entry.getKey())
										.setAmount(entry.getValue()).build())
								.collect(Collectors.toList()))
						.build())
				.build().toByteArray());
	}

	@Override
	public int createWaveProperties(Map<String, Object> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) throws ReflectiveOperationException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setCreateWaveProperties(
						makeCreateWavePropertiesMessage(waveProperties, elementNamesToSpawn, spawningPoint))
				.build().toByteArray());
		return handleCreateWavePropertiesResponse(readAuthoringServerResponse());
	}

	@Override
	public void editWaveProperties(int waveNum, Map<String, Object> updatedProperties,
			Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) throws ReflectiveOperationException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setEditWaveProperties(EditWaveProperties.newBuilder().setWaveNum(waveNum).setEditProperties(
						makeCreateWavePropertiesMessage(updatedProperties, newElementNamesToSpawn, newSpawningPoint))
						.build())
				.build().toByteArray());
	}

	@Override
	public Map<String, Object> getWaveProperties(int waveNum) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetWaveProperties(GetWaveProperties.newBuilder().setWaveNum(waveNum).build()).build()
				.toByteArray());
		return handleWavePropertiesResponse(readAuthoringServerResponse());
	}

	@Override
	public Collection<String> getPossibleVictoryConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetPossibleVictoryConditions(GetPossibleVictoryConditions.getDefaultInstance()).build()
				.toByteArray());
		return handleConditionsResponse(readAuthoringServerResponse());
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetPossibleDefeatConditions(GetPossibleDefeatConditions.getDefaultInstance()).build()
				.toByteArray());
		return handleConditionsResponse(readAuthoringServerResponse());
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentVictoryConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetCurrentVictoryConditions(GetCurrentVictoryConditions.getDefaultInstance()).build()
				.toByteArray());
		return handleCurrentVictoryConditionsResponse(readAuthoringServerResponse());
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentDefeatConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetCurrentDefeatConditions(GetCurrentDefeatConditions.getDefaultInstance()).build().toByteArray());
		return handleCurrentDefeatConditionsResponse(readAuthoringServerResponse());
	}

	@Override
	protected int getPort() {
		return PORT;
	}

	private Map<String, List<String>> handleElementBaseConfigurationOptionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, List<String>> configOptions = new HashMap<>();
		authoringServerMessage.getElementBaseConfigurationOptionsList()
				.forEach(config -> configOptions.put(config.getConfigKey(), config.getConfigOptionsList()));
		return configOptions;
	}

	private Map<String, Class> handleAuxiliaryElementConfigurationOptions(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, Class> auxiliaryElementConfigOptions = new HashMap<>();
		authoringServerMessage.getAuxiliaryElementConfigurationOptionsList().forEach(config -> {
			try {
				auxiliaryElementConfigOptions.put(config.getConfigName(), Class.forName(config.getConfigClassName()));
			} catch (ClassNotFoundException e) {
				// TODO - Custom Exception
			}
		});
		return auxiliaryElementConfigOptions;
	}

	private int handleCurrentLevelResponse(AuthoringServerMessage authoringServerMessage) {
		if (authoringServerMessage.hasCurrentLevel()) {
			return authoringServerMessage.getCurrentLevel();
		}
		return 0;
	}

	private Map<String, List<Map<String, Object>>> handleAllDefinedElementUpgradesResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, List<Map<String, Object>>> allElementUpgradesMap = new HashMap<>();
		authoringServerMessage.getAllDefinedElementUpgradesList().forEach(upgrade -> allElementUpgradesMap
				.put(upgrade.getElementName(), upgrade.getElementUpgradesList().stream().map(elementUpgrades -> {
					Map<String, String> elementUpgradesMap = new HashMap<>();
					elementUpgrades.getItemsList().stream()
							.forEach(item -> elementUpgradesMap.put(item.getName(), item.getValue()));
					return getSerializationUtils().deserializeElementTemplate(elementUpgradesMap);
				}).collect(Collectors.toList())));
		return allElementUpgradesMap;
	}

	private Map<String, Double> handleResourceEndowmentsResponse(AuthoringServerMessage authoringServerMessage) {
		Map<String, Double> resourceEndowmentsMap = new HashMap<>();
		authoringServerMessage.getResourceEndowmentsList().stream()
				.forEach(resource -> resourceEndowmentsMap.put(resource.getName(), resource.getValue()));
		return resourceEndowmentsMap;
	}

	private int handleCreateWavePropertiesResponse(AuthoringServerMessage authoringServerMessage) {
		if (authoringServerMessage.hasCreatedWaveNumber()) {
			return authoringServerMessage.getCreatedWaveNumber();
		}
		return 0;
	}

	private Map<String, Object> handleWavePropertiesResponse(AuthoringServerMessage authoringServerMessage) {
		Map<String, String> stringifiedWaveProperties = new HashMap<>();
		authoringServerMessage.getWavePropertiesList()
				.forEach(property -> stringifiedWaveProperties.put(property.getName(), property.getValue()));
		return getSerializationUtils().deserializeElementTemplate(stringifiedWaveProperties);
	}

	private Collection<String> handleConditionsResponse(AuthoringServerMessage authoringServerMessage) {
		return authoringServerMessage.getPossibleVictoryConditionsList();
	}

	private Map<String, Collection<Integer>> handleCurrentVictoryConditionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, Collection<Integer>> victoryConditionsMap = new HashMap<>();
		authoringServerMessage.getCurrentVictoryConditionsList().forEach(condition -> victoryConditionsMap
				.put(condition.getConditionName(), condition.getLevelsUsingConditionList()));
		return victoryConditionsMap;
	}

	private Map<String, Collection<Integer>> handleCurrentDefeatConditionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, Collection<Integer>> defeatConditionsMap = new HashMap<>();
		authoringServerMessage.getCurrentDefeatConditionsList().forEach(condition -> defeatConditionsMap
				.put(condition.getConditionName(), condition.getLevelsUsingConditionList()));
		return defeatConditionsMap;
	}

	private Collection<Property> getPropertiesFromObjectMap(Map<String, Object> objectMap) {
		return objectMap.entrySet().stream()
				.map(entry -> Property.newBuilder().setName(entry.getKey())
						.setValue(getSerializationUtils().serializeElementProperty(entry.getValue())).build())
				.collect(Collectors.toList());
	}

	private CreateWaveProperties makeCreateWavePropertiesMessage(Map<String, Object> waveProperties,
			Collection<String> elementNamesToSpawn, Point2D spawningPoint) {
		Map<String, String> stringifiedWaveProperties = getSerializationUtils()
				.serializeElementTemplate(waveProperties);
		return CreateWaveProperties.newBuilder()
				.addAllWaveProperties(stringifiedWaveProperties.entrySet().stream()
						.map(entry -> Property.newBuilder().setName(entry.getKey()).setValue(entry.getValue()).build())
						.collect(Collectors.toList()))
				.build();
	}

	private AuthoringServerMessage readAuthoringServerResponse() {
		try {
			return AuthoringServerMessage.parseFrom(readResponseBytes());
		} catch (InvalidProtocolBufferException e) {
			return AuthoringServerMessage.getDefaultInstance(); // empty message
		}
	}

	@Override
	public int getLevelHealth(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLevelHealth(int health) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumLevelsForGame() {
		// TODO Auto-generated method stub
		return 0;
	}

}
