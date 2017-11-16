package authoring;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AuthoringGameTower {
	public static final String DEFAULT_TOWER = "tower.gif";
	public static final double DEFAULT_WIDTH = 20;
	public static final double DEFAULT_HEIGHT = 20;
	public static final double DEFAULT_FIRING_RATE = 3;
	
	ImageView towerImage;
	double xPosition;
	double yPosition;
	double width;
	double height;
	double perSecondFiringRate;
	
	public AuthoringGameTower() {
		String imageName = DEFAULT_TOWER;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		towerImage = new ImageView(image);
		xPosition = 0;
		yPosition = 0;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		perSecondFiringRate = DEFAULT_FIRING_RATE;
		
//		String imageName = DEFAULT_TURTLE;
//		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
//		turtleImage = new ImageView(image);
//		turtleImage.setFitWidth(DEFAULT_WIDTH);
//		turtleImage.setFitHeight(DEFAULT_HEIGHT);
//		xCoordinateOnRegion = xCoord;
//		yCoordinateOnRegion = yCoord;
//		turtleImage.setX(xCoord);
//		turtleImage.setY(yCoord);
	}
	
	public AuthoringGameTower(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		towerImage = new ImageView(image);
		xPosition = 0;
		yPosition = 0;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		perSecondFiringRate = DEFAULT_FIRING_RATE;
	}
	
	public AuthoringGameTower(String imageName, double xPos, double yPos, double w, double h, double rate) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		towerImage = new ImageView(image);
		xPosition = xPos;
		yPosition = yPos;
		width = w;
		height = h;
		perSecondFiringRate = rate;
	}
}
