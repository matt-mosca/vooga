package authoring;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AuthoringGameProjectile {
	public static final String DEFAULT_PROJ = "bullet.gif";
	public static final double DEFAULT_WIDTH = 5;
	public static final double DEFAULT_HEIGHT = 5;
	public static final double DEFAULT_DAMAGE = 10;
	
	ImageView projImage;
	double width;
	double height;
	double damage;
	
	public AuthoringGameProjectile() {
		String imageName = DEFAULT_PROJ;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		projImage = new ImageView(image);
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		damage = DEFAULT_DAMAGE;
		
	}
	
	public AuthoringGameProjectile(Image image) {
		projImage = new ImageView(image);
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		damage = DEFAULT_DAMAGE;
	}
	
	public AuthoringGameProjectile(Image image, double w, double h, double d) {
		projImage = new ImageView(image);
		width = w;
		height = h;
		damage = d;
	}
}
