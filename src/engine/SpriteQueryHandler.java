package engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.behavior.movement.TrackingPoint;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import sprites.Sprite;

/**
 * Handlers sprite queries.
 * 
 * @author Ben Schwennesen
 */
public class SpriteQueryHandler {

	public Map<String, Object> getAuxiliarySpriteConstructionObjectMap(int elementPlayerId, Point2D startCoordinates,
			List<Sprite> levelSprites) {
		Sprite spriteToTrack = getNearestEnemySpriteToPoint(elementPlayerId, startCoordinates, levelSprites);
		TrackingPoint targetLocation;
		if (spriteToTrack != null)
			targetLocation = spriteToTrack.getPositionForTracking();
		else
			targetLocation = new TrackingPoint(new SimpleDoubleProperty(0), new SimpleDoubleProperty(0));
		Point2D targetPoint = new Point2D(targetLocation.getCurrentX(), targetLocation.getCurrentY());
		Map<String, Object> auxiliarySpriteConstructionObjects = new HashMap<>();
		auxiliarySpriteConstructionObjects.put(targetLocation.getClass().getName(), targetLocation);
		auxiliarySpriteConstructionObjects.put(targetPoint.getClass().getName(), targetPoint);
		return auxiliarySpriteConstructionObjects;
	}

	private Sprite getNearestEnemySpriteToPoint(int toGenerateId, Point2D coordinates, List<Sprite> levelSprites) {
		double nearestDistance = Double.MAX_VALUE;
		Sprite nearestSprite = null;
		for (Sprite sprite : levelSprites) {
			double distanceToSprite = new Point2D(sprite.getX(), sprite.getY()).distance(coordinates);
			if (distanceToSprite < nearestDistance && sprite.getPlayerId() != toGenerateId) {
				nearestDistance = distanceToSprite;
				nearestSprite = sprite;
			}
		}
		return nearestSprite;
	}

}
