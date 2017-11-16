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
		setImageviewProperties();
	}
	
	public AuthoringGameTower(Image image) {
		towerImage = new ImageView(image);
		xPosition = 0;
		yPosition = 0;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		perSecondFiringRate = DEFAULT_FIRING_RATE;
		setImageviewProperties();
	}
	
	public AuthoringGameTower(Image image, double xPos, double yPos, double w, double h, double rate) {
		towerImage = new ImageView(image);
		xPosition = xPos;
		yPosition = yPos;
		width = w;
		height = h;
		perSecondFiringRate = rate;
		setImageviewProperties();
	}
	
	private void setImageviewProperties() {
		towerImage.setFitWidth(width);
		towerImage.setFitHeight(height);
		towerImage.setX(xPosition);
		towerImage.setY(yPosition);
	}
	
	public void setPosition(double x, double y) {
		xPosition = x;
		yPosition = y;
	}
	
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}
	
	public void setFiringRate(double rate) {
		perSecondFiringRate = rate;
	}
	
	public void setImage(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		towerImage.setImage(image);
	}
}
