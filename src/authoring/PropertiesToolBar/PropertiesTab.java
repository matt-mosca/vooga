package authoring.PropertiesToolBar;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class PropertiesTab extends Pane{
	private PropertiesToolBar myProperties;
	private ImageView myImageView;
	
	public PropertiesTab(PropertiesToolBar properties, ImageView imageView) {
		myProperties = properties;
		myImageView = imageView;
		
		AddToWaveButton myWaveAdder = new AddToWaveButton(myProperties);
		CostButton myCost = new CostButton(myProperties, myImageView);
		AddToLevelButton myLevelAdder = new AddToLevelButton(myProperties);
//		propBox.setLayoutX(100);
		
		Button backButton = new Button("Back");
		backButton.setLayoutX(350);
		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->myProperties.removeButtonPressed());
		
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		
		this.getChildren().add(myImageView);
		this.getChildren().add(backButton);
//		this.getChildren().add(propBox);
		this.getChildren().add(myWaveAdder);
		this.getChildren().add(myCost);
		this.getChildren().add(myLevelAdder);
	}

}
