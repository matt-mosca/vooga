package authoring.PropertiesToolBar;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class PropertiesPane extends TabPane{
//    private AddToLevelButton myLevelAdder;
    private ImageView myImageView;
    private PropertiesToolBar myProperties;
	
	public PropertiesPane(PropertiesToolBar properties, ImageView imageView, PropertiesBox propBox) {
		myImageView = imageView;
		myProperties = properties;
		createInitialTab();
		
	}
	
	private void createInitialTab() {
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
