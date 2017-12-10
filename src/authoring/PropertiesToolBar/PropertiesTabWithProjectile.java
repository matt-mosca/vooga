package authoring.PropertiesToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PropertiesTabWithProjectile extends PropertiesTab{
	private PropertiesToolBar myProperties;
	private ProjectileSlot projectileSlot;
	private AuthoringController myController;
	private ImageView myImageView;
	
	public PropertiesTabWithProjectile(PropertiesToolBar properties, ImageView imageView, AuthoringController author) {
		super(properties, imageView);
		myProperties = properties;
		myController = author;
		myImageView = imageView;
		
		Label projectileLabel = new Label("Click to\nChoose a\nprojectile");
		projectileLabel.setLayoutY(90);
		projectileSlot = new ProjectileSlot(this, imageView);
		HBox imageBackground = new HBox();
		imageBackground.setStyle("-fx-background-color: white");
		imageBackground.getChildren().add(imageView);
		if (myController.getAllDefinedTemplateProperties().get(imageView.getId()).get("Projectile Type Name") != null) {
			addProjectileImage();
		}
		this.getChildren().add(imageBackground);
		this.getChildren().add(projectileLabel);
		this.getChildren().add(projectileSlot);
	}

	private void addProjectileImage() {
		String projectileName = myController.getAllDefinedTemplateProperties().get(myImageView.getId()).get("Projectile Type Name");
		String url = myController.getAllDefinedTemplateProperties().get(projectileName).get("imageUrl");
		Double height =  Double.parseDouble(myController.getAllDefinedTemplateProperties().get(projectileName).get("imageHeight"));
		Double width =  Double.parseDouble(myController.getAllDefinedTemplateProperties().get(projectileName).get("imageWidth"));
		ImageView projectile = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(url)));
		projectile.setFitWidth(width);
		projectile.setFitHeight(height);
		resize(projectileSlot.getPrefHeight(), projectile);
		projectileSlot.getChildren().add(projectile);
	}
	
	protected void newProjectileList(ImageView imageView) {
		ScrollPane projectilesWindow = new ScrollPane();
		ListView<SpriteImage> projectilesView = new ListView<SpriteImage>();
		if (myProperties.getAvailableProjectiles().isEmpty()) {
			Label emptyLabel = new Label("You have no projectiles\nin your inventory");
//			this.getChildren().remove(propBox);
			emptyLabel.setLayoutX(100);
			this.getChildren().add(emptyLabel);
		} else {
			List<SpriteImage> cloneList = new ArrayList<>();
			for (SpriteImage s : myProperties.getAvailableProjectiles()) {
				cloneList.add(s.clone());
			}
			ObservableList<SpriteImage> items =FXCollections.observableArrayList(cloneList);
	        projectilesView.setItems(items);
	        projectilesView.getSelectionModel();
	        projectilesWindow.setContent(projectilesView);
	        projectilesWindow.setLayoutX(100);
	        projectilesWindow.setPrefHeight(250);
	        projectilesView.setOnMouseClicked(e->projectileSelected(imageView,
	        		projectilesView.getSelectionModel().getSelectedItem().clone()));
//	        this.getChildren().remove(myPropertiesBox);
	        this.getChildren().add(projectilesWindow);
		}
	}
	
	
	private void projectileSelected(ImageView imageView, ImageView projectile) {
		projectileSlot.getChildren().removeAll(projectileSlot.getChildren());
		projectileSlot.getChildren().add(projectile);
		Map<String, String> newProperties = new HashMap<>();
		newProperties.put("Projectile Type Name", projectile.getId());
		myController.updateElementDefinition(imageView.getId(), newProperties, true);
	}
	
	private void resize(double displaySize, ImageView imageView) {
		double spriteWidth = this.getBoundsInLocal().getWidth();
		double spriteHeight = this.getBoundsInLocal().getHeight();
		double maxDimension = Math.max(spriteWidth, spriteHeight);
		double scaleValue = maxDimension / displaySize;
		imageView.setFitWidth(spriteWidth / scaleValue);
		imageView.setFitHeight(spriteHeight / scaleValue);
	}
}
