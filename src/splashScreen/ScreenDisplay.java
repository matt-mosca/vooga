package splashScreen;

import java.io.File;

import interfaces.ClickableInterface;
import interfaces.Droppable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

public abstract class ScreenDisplay {

	public double FRAMES_PER_SECOND = 60;
	public double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public double SECOND_DELAY = 100.0 / FRAMES_PER_SECOND;
	private Droppable droppable;
	private KeyFrame frame;
	private Timeline animation = new Timeline();
	private Scene myScene;
	private Group root = new Group();

	/**
	 * Constructor: Screen Display class
	 */

	public ScreenDisplay(int width, int height, Paint background) {
		init();
		myScene = new Scene(root, width, height, background);

	}
	
	public ScreenDisplay(int width, int height) {
		init();
		myScene = new Scene(root, width, height);

	}

	public Scene getScene() {
		return myScene;
	}
	
	public ObservableList<Node> getRootChildren() {
		return root.getChildren();
	}
	
	public void init() {
		//frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> this.step(SECOND_DELAY));
		animation.setCycleCount(Timeline.INDEFINITE);
		//animation.getKeyFrames().add(frame);
	}

	protected void rootAdd(Node object) {
		root.getChildren().add(object);
	}

	protected void rootRemove(Node object) {
		root.getChildren().remove(object);
	}
	
	protected boolean rootContain(Node object) {
		return root.getChildren().contains(object);
	}
	
	protected void rootStyleAndClear(String sheet) {
		root.getStylesheets().clear();
		root.getStylesheets().add(sheet);
	}
	
	protected void rootStyle(String sheet) {
		root.getStylesheets().add(sheet);
	}
	
	protected void centerScene() {

	}
	
	public Droppable getDroppable() {
		return droppable;
	}
	
	public void SetDroppable(Droppable drop) {
		droppable = drop;
	}
	
	public abstract void save(File saveFile);
	
	public abstract void listItemClicked(ImageView object);
}
