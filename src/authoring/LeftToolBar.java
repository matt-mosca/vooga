package authoring;

import java.util.ArrayList;
import java.util.List;

import GUI.TurProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Creates a re-runable HBox for old commands
 * 
 * @author Matt
 */
public class LeftToolBar extends ScrollPane {
	private static final int WIDTH = 300;
	private List<Rectangle> myList;
	private ListView<Rectangle> myListView;
	private AuthorInterface myAuthor;
	private Rectangle myrec1;
	private Rectangle myrec2;
	private Rectangle myrec3;
	private Rectangle myrec4;
	
	public LeftToolBar(AuthorInterface author) {
		myAuthor = author;
        myList = new ArrayList<Rectangle>();
        myrec1 = createRectangle(100, 100, Color.YELLOW);
        myrec2 = createRectangle(150, 150, Color.BLUE);
        myrec3 = createRectangle(50, 50, Color.RED);
        myrec4 = createRectangle(100, 400, Color.BLACK);
        myList.add(myrec1);
        myList.add(myrec2);
        myList.add(myrec3);
        myList.add(myrec4);
        ObservableList<Rectangle> items = FXCollections.observableArrayList(myList);
        myListView = new ListView<Rectangle>();
        myListView.setItems(items);
        this.setContent(myListView);
	}
	
	private Rectangle createRectangle(double width, double height, Paint color) {
		Rectangle tempRec = new Rectangle(width, height, color);
        tempRec.addEventHandler(MouseEvent.MOUSE_CLICKED, e->myAuthor.clicked(tempRec));
		return tempRec;
	}

//	private void drag(MouseEvent e, Rectangle myrec) {
//		Rectangle newRectangle = new Rectangle(myrec.getX(), myrec.getY());
//		newRectangle.setX(e.getSceneX());
//		myrec.setY(e.getSceneY());
//	}

//	public void init() {
//		myCommandHistoryBox = new ScrollPane();
//		commandHistoryView = new ListView<Button>();
//		commandHistory = new ArrayList<Button>();
//		ObservableList<Button> items =FXCollections.observableArrayList(commandHistory);
//        commandHistoryView.setItems(items);
//        commandHistoryView.getSelectionModel();
//        myCommandHistoryBox.setContent(commandHistoryView);
//	}
	
//	public void addCommandToHistoryBox(String command) {
//		Button button = new Button(command);
//		button.addEventHandler(MouseEvent.MOUSE_CLICKED, e->System.out.println(command));
//		commandHistory.add(button);
//		button.setStyle(  "-fx-border-color: transparent; -fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
//		ObservableList<Button> items =FXCollections.observableArrayList(commandHistory);
//        commandHistoryView.setItems(items);
//	}
	
}
