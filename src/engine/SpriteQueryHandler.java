package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;

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
		GameElement gameElementToTrack = getNearestEnemy(elementPlayerId, startCoordinates, levelGameElements);
		TrackingPoint targetLocation;
		if (gameElementToTrack != null)
			targetLocation = gameElementToTrack.getPositionForTracking();
		else
			targetLocation = new TrackingPoint(new SimpleDoubleProperty(0), new SimpleDoubleProperty(0));
		Map<String, Object> auxiliarySpriteConstructionObjects = new HashMap<>();
		auxiliarySpriteConstructionObjects.put("targetLocation", targetLocation);
		auxiliarySpriteConstructionObjects.put("startPoint", startCoordinates);
		return auxiliarySpriteConstructionObjects;
	}

	public Map<String, Object> getAuxiliarySpriteConstructionObjectMap(Point2D startCoordinates,GameElement gameElementToTrack) {
		TrackingPoint targetLocation;
		System.out.println("Getting Auxiliary Sprite Constructor");
		if (gameElementToTrack != null)
			targetLocation = gameElementToTrack.getPositionForTracking();
		else
			targetLocation = new TrackingPoint(new SimpleDoubleProperty(0), new SimpleDoubleProperty(0));
		Point2D targetPoint = new Point2D(targetLocation.getCurrentX(), targetLocation.getCurrentY());
		Map<String, Object> auxiliarySpriteConstructionObjects = new HashMap<>();
		//auxiliarySpriteConstructionObjects.put(targetLocation.getClass().getName(), targetLocation);
		//auxiliarySpriteConstructionObjects.put(targetPoint.getClass().getName(), targetPoint);
		auxiliarySpriteConstructionObjects.put("targetLocation", targetLocation);
		auxiliarySpriteConstructionObjects.put("startPoint", startCoordinates);
		
		System.out.println(auxiliarySpriteConstructionObjects.toString());
		System.out.println("Got Auxillary Sprite Constructor");
		return auxiliarySpriteConstructionObjects;
	}

	public GameElement getNearestEnemy(int toGenerateId, Point2D coordinates, List<GameElement> levelGameElements) {
		double nearestDistance = Double.MAX_VALUE;
		GameElement nearestGameElement = null;
		for (GameElement gameElement : levelGameElements) {
			double distanceToSprite = new Point2D(gameElement.getX(), gameElement.getY()).distance(coordinates);
			if ((distanceToSprite < nearestDistance )&& gameElement.getPlayerId() != toGenerateId) {
				nearestDistance = distanceToSprite;
				nearestGameElement = gameElement;
			}
		}
		return nearestGameElement;
	}
	
	public List<GameElement> getAllElementsWithinRange(int toGenerateId, Point2D coordinates,
													   List<GameElement> levelGameElements, 
													   double rangeRadius){
		ArrayList<GameElement> targetElements = new ArrayList<>();
		
		for(GameElement gameElement : levelGameElements) {
			if(getDistance(coordinates,new Point2D(gameElement.getX(),gameElement.getY()))<rangeRadius
												&& gameElement.getPlayerId() != toGenerateId) {
				targetElements.add(gameElement);
			}
		}
		return targetElements;
	}
	
	private double getDistance(Point2D start, Point2D end) {
		return start.distance(end);
	}
}
