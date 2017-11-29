package sprites;

import javafx.scene.image.Image;
import splashScreen.ScreenDisplay;

public class StaticObject extends InteractiveObject{
	
	private static final int CELL_SIZE = 40;
	private int objectSize;
	private int realSize;
	private String myImageString;
	
	public StaticObject(int size, ScreenDisplay display, String imageString) {
		super(display);
		myImageString = imageString;
		setSize(size);
		Image image;
		try {
			image = new Image(getClass().getClassLoader().getResourceAsStream(imageString));
		}catch(NullPointerException e) {
			image = new Image(imageString);
		}
		this.setImage(image);
		objectSize = size;
		
	}

	private void setSize(int size) {
		realSize = size * CELL_SIZE;
		this.setFitWidth(realSize);
		this.setFitHeight(realSize);
	}
	
	public double getHeight() {
		return this.getFitHeight();
	}
	
	public double getWidth() {
		return this.getFitWidth();
	}
	
	public String getImageString() {
		return myImageString;
	}
	
	@Override
	public int getSize() {
		return objectSize;
	}
	
	public void incrementSize() {
		objectSize++;
		setSize(objectSize);
	}
	
	public void decrementSize() {
		if (objectSize > 1) {
			objectSize--;
			setSize(objectSize);
		}
	}
}
