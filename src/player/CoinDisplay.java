package player;

public class CoinDisplay extends ValueDisplay {
	private static final String IMAGE = "coins.png";
	private static final double XPOS = 700;
	private static final double YPOS = 0;
	
	public CoinDisplay() {
		super();
		setStandardDisplayImage(IMAGE);
		setStandardImageViewSize();
		setBoxPosition(XPOS, YPOS);
		addItemsWithImage();
	}
}
