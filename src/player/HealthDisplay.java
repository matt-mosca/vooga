package player;

public class HealthDisplay extends ValueDisplay {
	private static final String IMAGE = "heart.png";
	private static final double XPOS = 600;
	private static final double YPOS = 0;
	
	public HealthDisplay() {
		super();
		setStandardDisplayImage(IMAGE);
		setStandardImageViewSize();
		setBoxPosition(XPOS, YPOS);
		addItemsWithImage();
	}
}
