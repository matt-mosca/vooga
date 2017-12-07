package authoring.rightToolBar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authoring.EditDisplay;
import display.tabs.AddSpriteImageTab;
import display.tabs.AddTab;
import display.tabs.InventoryTab;
import display.tabs.NewProjectileTab;
import display.tabs.NewSpriteTab;
import display.tabs.NewTowerTab;
import display.tabs.NewTroopTab;
import display.tabs.SimpleTab;
import engine.authoring_engine.AuthoringController;
import display.factory.TabFactory;
import display.interfaces.CreationInterface;
import display.interfaces.PropertiesInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import display.splashScreen.ScreenDisplay;
import display.toolbars.ToolBar;
 
public class RightToolBar extends ToolBar implements PropertiesInterface {
	
	private TabFactory tabMaker;
	private TabPane topTabPane;
	private TabPane bottomTabPane;
	private AddTab addTab;
	private NewSpriteTab newTower;
	private NewSpriteTab newTroop;
	private NewSpriteTab newProjectile;
	private SimpleTab inventoryTower;
	private SimpleTab inventoryTroop;
	private SimpleTab inventoryProjectile;
	private ScreenDisplay myDisplay;
	private AddNewButton myNewButton;
	private PropertiesBox myPropertiesBox;
	private Pane propertiesPane;
	private Label projectileLabel;
	private HBox projectileSlot;
	private Button deleteButton;
	private ReturnButton retB;
	private CreationInterface created;
	private AuthoringController myController;
	private Map<String, String> basePropertyMap;
	private EditDisplay display;
    private AddToWaveButton myWaveAdder;

	private List<SpriteImage> availableProjectiles;

	private final int X_LAYOUT = 680;
	private final int Y_LAYOUT = 30;

	
	public RightToolBar(EditDisplay display, AuthoringController controller) {
		this.display = display;
		myDisplay = display;
		retB = new ReturnButton(display);
		myController = controller;
		availableProjectiles = new ArrayList<>();
		newTower = new NewTowerTab(display);   
	    newTroop = new NewTroopTab(display); 
	    newProjectile = new NewProjectileTab(display); 
	    tabMaker = new TabFactory();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    topTabPane.setPrefHeight(250);
	    bottomTabPane.setPrefHeight(250);
	    
        this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		this.setSpacing(20);
	    createAndAddTabs();
	    
	    
	    myNewButton = new AddNewButton(created);
        this.getChildren().add(topTabPane);
        this.getChildren().add(bottomTabPane);
        this.getChildren().add(retB);
        
        
        newTower.attach(topTabPane.getTabs().get(0));
        newTroop.attach(topTabPane.getTabs().get(1));
        newProjectile.attach(topTabPane.getTabs().get(2));
        
        basePropertyMap = new HashMap<String, String>();
        initializeInventory(myController, bottomTabPane);
    }
	
	@Override
	protected void createAndAddTabs() {
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Tower", "TowerImage", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Troop", "TroopImage", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Projectile", "ProjectileImage", topTabPane));
		addTab = new AddSpriteImageTab(null , topTabPane);
		topTabPane.getTabs().add(tabMaker.buildTab("Add Image", null, addTab, topTabPane));
		
		inventoryTower = new InventoryTab(myDisplay, new ArrayList<>(), this);
		inventoryTroop = new InventoryTab(myDisplay, new ArrayList<>(), this);
		inventoryProjectile = new InventoryTab(myDisplay, new ArrayList<>(), this);
		bottomTabPane.getTabs().add(tabMaker.buildTab("Towers", "TowerImage", inventoryTower, bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTab("Troops", "TroopImage", inventoryTroop, bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTab("Projectiles", "ProjectileImage", inventoryProjectile, bottomTabPane));
		makeTabsUnclosable(topTabPane);
		makeTabsUnclosable(bottomTabPane);
	}
	
	@Override
	public void imageSelected(SpriteImage myImageView) {
		myPropertiesBox = new PropertiesBox(myDisplay.getDroppable(), myImageView, myController);
		if (myImageView instanceof TowerImage) inventoryTower.addItem(myImageView.clone());
		if (myImageView instanceof TroopImage) inventoryTroop.addItem(myImageView.clone());
		if (myImageView instanceof ProjectileImage) {
			inventoryProjectile.addItem(myImageView.clone());
			availableProjectiles.add(myImageView);
		}
	}

	@Override
	public void clicked(ImageView imageView) {	
		myPropertiesBox = new PropertiesBox(myDisplay.getDroppable(), imageView, myController);
		String tabType = myController.getAllDefinedTemplateProperties().get(imageView.getId()).get("tabName");
		if (tabType.equals("Towers")) {
			newPaneWithProjectileSlot(imageView);
		}else {
			newPane(imageView);
		}
	}
	private void newPropertiesPane() {
		propertiesPane = new Pane();
		myWaveAdder = new AddToWaveButton(this);
		deleteButton = new Button("Back");
	}
	private void newPaneWithProjectileSlot(ImageView imageView) {
		/**
		 * Awful code atm, it'll be refactored dw, just trying to get it all to work <3
		 */
		projectileLabel = new Label("Click to\nChoose a\nprojectile");
		projectileLabel.setLayoutY(90);
		projectileSlot = new HBox();
		projectileSlot.setPrefWidth(50);
		projectileSlot.setPrefHeight(50);
		projectileSlot.setLayoutY(170);
		projectileSlot.setStyle("-fx-background-color: white");
		projectileSlot.addEventHandler(MouseEvent.MOUSE_CLICKED, e->newProjectilesWindow((TowerImage) imageView));
		propertiesPane = new Pane();
	    myWaveAdder = new AddToWaveButton(this);
		deleteButton = new Button("Back");
		deleteButton.setLayoutX(370);
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		myPropertiesBox.setLayoutX(100);
		deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->removeButtonPressed());
		HBox imageBackground = new HBox();
		imageBackground.setStyle("-fx-background-color: white");
		imageBackground.getChildren().add(clone(imageView));
		if (myController.getAllDefinedTemplateProperties().get(imageView.getId()).get("Projectile Type Name") != null) {
			ProjectileImage projectile = new ProjectileImage(myDisplay, myController.getAllDefinedTemplateProperties().get(imageView.getId()).get("Projectile Type Name"));
			projectile.resize(projectileSlot.getPrefHeight());
			projectileSlot.getChildren().add(projectile);
		}
		propertiesPane.getChildren().add(imageBackground);
		propertiesPane.getChildren().add(deleteButton);
		propertiesPane.getChildren().add(myPropertiesBox);
		propertiesPane.getChildren().add(projectileLabel);
		propertiesPane.getChildren().add(projectileSlot);
		propertiesPane.getChildren().add(myWaveAdder);
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(propertiesPane);
		this.getChildren().add(bottomTabPane);
	}
	
	private void newProjectilesWindow(ImageView myTowerImage) {
		ScrollPane projectilesWindow = new ScrollPane();
		ListView<SpriteImage> projectilesView = new ListView<SpriteImage>();
		if (availableProjectiles.isEmpty()) {
			Label emptyLabel = new Label("You have no projectiles\nin your inventory");
			propertiesPane.getChildren().remove(myPropertiesBox);
			emptyLabel.setLayoutX(100);
			propertiesPane.getChildren().add(emptyLabel);
		} else {
			List<SpriteImage> cloneList = new ArrayList<>();
			for (SpriteImage s : availableProjectiles) {
				cloneList.add(s.clone());
			}
			ObservableList<SpriteImage> items =FXCollections.observableArrayList(cloneList);
	        projectilesView.setItems(items);
	        projectilesView.getSelectionModel();
	        projectilesWindow.setContent(projectilesView);
	        projectilesWindow.setLayoutX(100);
	        projectilesWindow.setPrefHeight(250);
	        projectilesView.setOnMouseClicked(e->projectileSelected(myTowerImage,
	        		projectilesView.getSelectionModel().getSelectedItem().clone()));
        propertiesPane.getChildren().remove(myPropertiesBox);
        propertiesPane.getChildren().add(projectilesWindow);
		}
	}
	
	private void projectileSelected(ImageView imageView, ImageView projectile) {
		projectileSlot.getChildren().removeAll(projectileSlot.getChildren());
		projectileSlot.getChildren().add(projectile);
		Map<String, String> newProperties = new HashMap<>();
		newProperties.put("Projectile Type Name", projectile.getId());
		myController.updateElementDefinition(imageView.getId(), newProperties, true);
		
	}

	private void newPane(ImageView imageView) {
//		myPropertiesBox = new PropertiesBox(created, imageView);
		propertiesPane = new Pane();
		myWaveAdder = new AddToWaveButton(this);
		Button deleteButton = new Button("Back");
		deleteButton.setLayoutX(350);
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->removeButtonPressed());
		propertiesPane.getChildren().add(clone(imageView));
		propertiesPane.getChildren().add(deleteButton);
		propertiesPane.getChildren().add(myPropertiesBox);
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(propertiesPane);
		this.getChildren().add(bottomTabPane);
		propertiesPane.getChildren().add(myWaveAdder);
	}
	
	private void removeButtonPressed() {
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(topTabPane);
		this.getChildren().add(bottomTabPane);
		this.getChildren().add(retB);
	}
	
	public void addToMap(String property, String value) {
		basePropertyMap.put(property, value);
	}
	
	@Override
	public void addToWave() {
		int maxLevel = display.getMaxLevel();
		Stage waveStage = new Stage();
		waveStage.setTitle("CheckBox Experiment 1");
        VBox myVBox = new VBox();

        for (int i = 1; i <= maxLevel; i++) {
        	CheckBox myCheckBox = new CheckBox(Integer.toString(i));
        	myVBox.getChildren().add(myCheckBox);
        }
        TextField amountField = new TextField();
        amountField.setPromptText("How many of this Sprite?");
        myVBox.getChildren().add(amountField);
        Button submitButton = new Button("Submit");
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->submitToWaves(myVBox, waveStage, Integer.valueOf(amountField.getText())));
        myVBox.getChildren().add(submitButton);
        Scene scene = new Scene(myVBox, 200, 50 + 20*maxLevel);
        waveStage.setScene(scene);
        waveStage.show();
	}
	
	private void submitToWaves(VBox myVBox, Stage waveStage, Integer integer) {
		for (Node n : myVBox.getChildren()) {
			if (n instanceof CheckBox) {
				CheckBox c = (CheckBox) n;
				if (c.isSelected()) {
					for (int i = 0; i < integer; i++) {
						display.addToBottomToolBar(Integer.valueOf(c.getText()), clone(myPropertiesBox.getCurrSprite()));
					}
				}
			}
		}
		waveStage.hide();
	}
	
	private ImageView clone(ImageView imageView) {
		ImageView cloneImage = new ImageView(imageView.getImage());
		cloneImage.setFitHeight(imageView.getFitHeight());
		cloneImage.setFitWidth(imageView.getFitWidth());
		return cloneImage;
	}
} 