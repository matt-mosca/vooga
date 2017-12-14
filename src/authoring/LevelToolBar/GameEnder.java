package authoring.LevelToolBar;

import engine.authoring_engine.AuthoringController;
import javafx.scene.layout.VBox;

public class GameEnder extends VBox{
	private AuthoringController myController;
	private GameEnderConditions conditions;
	private HealthSelector health;
	private PointSelector points;
	
	
	public GameEnder(AuthoringController controller) {
		myController = controller; 
		conditions = new GameEnderConditions(controller);
		health = new HealthSelector(controller);
		points = new PointSelector(controller);
		this.getChildren().addAll(conditions, health, points);
		this.setPrefWidth(300);
		this.setSpacing(100);
		}
	
	public void update() {
		conditions.update();
	}
	
	public void setRecorder(GameEnderRecorder r) {
		conditions.setRecorder(r);
	}
	
}
