package player;

public class PointsDisplay extends ValueDisplay {
	private static final String LABEL = "Points";
	private static final double XPOS = 850;
	private static final double YPOS = 0;
	
	public PointsDisplay() {
		super();
		setStandardDisplayLabel(LABEL);
		addItemsWithLabel();
	}
}
