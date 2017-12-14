package authoring.spriteTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.interfaces.TestingInterface;
import javafx.scene.control.Button;

public class SpriteTesterButton extends Button {
	
	private static String DEFAULT_IMAGE_URL = "https://users.cs.duke.edu/~rcd/images/rcd.jpg\"";

	
	public SpriteTesterButton(TestingInterface tester) {
		this.setLayoutY(700);
		this.setText("PlayGame");
		this.setOnAction(e -> {
			Map<String, Object> fun = new HashMap<>();
			fun.put("frequency", 50);
			fun.put("number", 100);
			/*
			 * for (int i=0; i<defaults.size(); i++) { fun.put((String)
			 * defaults.keySet().toArray()[i], (String) defaults.entrySet().toArray()[i]); }
			 */
			fun.put("Collision effects", "Invulnerable to collision damage");
			fun.put("Collided-with effects", "Do nothing to colliding objects");
			fun.put("Move an object", "Object will stay at desired location");
			fun.put("Firing Behavior", "Shoot periodically");
			fun.put("Path of game element image", DEFAULT_IMAGE_URL);
			fun.put("Width", 45.0);
			fun.put("Height", 45.0);
			fun.put("Numerical \"team\" association", 0);
			fun.put("Health points", 50);
			fun.put("Damage dealt to colliding objects", 20);
			fun.put("Speed of movement", 5);
			fun.put("Start angle of rotation", 0);
			fun.put("Radius of circular movement", 10);
			fun.put("Center y-coordinate", 0);
			fun.put("Center x-coordinate", 0);
			fun.put("Target y-coordinate", 0);
			fun.put("Target x-coordinate", 0);
			fun.put("Projectile Type Name", "Tank");
			fun.put("Attack period", 10);
			List<String> sprites = new ArrayList<>();
			sprites.add("Tank");
			tester.createTesterLevel(fun, sprites);

		});
	}


}
