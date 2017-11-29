package player;

public class CoinDisplay extends ValueDisplay {
	private static final String IMAGE = "coins.png";
	
	public CoinDisplay() {
		super();
		setStandardDisplayImage(IMAGE);
		setStandardImageViewSize();
		setBoxPosition(400, 0);
	}
}
