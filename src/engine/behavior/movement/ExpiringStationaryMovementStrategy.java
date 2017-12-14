package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

public class ExpiringStationaryMovementStrategy extends StationaryMovementStrategy{
	int expirationTime;
	int expirationTimer;
	public ExpiringStationaryMovementStrategy(@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
	@ElementProperty(value = "expirationTime", isTemplateProperty = true) int expirationTime) {
		super(startPoint);
		this.expirationTime = expirationTime;
		this.expirationTimer = 0;
	}
	
	public boolean targetReached() {
		return (expirationTimer >= expirationTime);
	}
	
	public Point2D move() {
		updateTimer();
		return getCurrentCoordinates();
	}
	
	private void updateTimer() {
		expirationTimer++;
	 }
}
