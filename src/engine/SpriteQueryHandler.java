package engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.behavior.movement.TrackingPoint;
import engine.game_elements.GameElement;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * Handlers sprite queries.
 * 
 * @author Ben Schwennesen
 */
public class SpriteQueryHandler {

	public Map<String, Object> getAuxiliarySpriteConstructionObjectMap(int elementPlayerId, Point2D startCoordinates,
			List<GameElement> levelGameElements) {
		GameElement gameElementToTrack = getNearestEnemySpriteToPoint(elementPlayerId, startCoordinates, levelGameElements);
		TrackingPoint targetLocation;
		if (gameElementToTrack != null)
			targetLocation = gameElementToTrack.getPositionForTracking();
		else
			targetLocation = new TrackingPoint(new SimpleDoubleProperty(0), new SimpleDoubleProperty(0));
		Point2D targetPoint = new Point2D(targetLocation.getCurrentX(), targetLocation.getCurrentY());
		Map<String, Object> auxiliarySpriteConstructionObjects = new HashMap<>();
		auxiliarySpriteConstructionObjects.put(targetLocation.getClass().getName(), targetLocation);
		auxiliarySpriteConstructionObjects.put(targetPoint.getClass().getName(), targetPoint);
		return auxiliarySpriteConstructionObjects;
	}

	private GameElement getNearestEnemySpriteToPoint(int toGenerateId, Point2D coordinates, List<GameElement> levelGameElements) {
		double nearestDistance = Double.MAX_VALUE;
		GameElement nearestGameElement = null;
		for (GameElement gameElement : levelGameElements) {
			double distanceToSprite = new Point2D(gameElement.getX(), gameElement.getY()).distance(coordinates);
			if (distanceToSprite < nearestDistance && gameElement.getPlayerId() != toGenerateId) {
				nearestDistance = distanceToSprite;
				nearestGameElement = gameElement;
			}
		}
		return nearestGameElement;
	}

}
