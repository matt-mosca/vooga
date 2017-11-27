package authoring.customize;

import interfaces.CustomizeInterface;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorChanger extends ColorPicker {
	
	private static final int Y_POS = 500;
	private static final int WIDTH = 200;
	private static final String PROMPT_TEXT = "Choose a background color";
	
	public ColorChanger(CustomizeInterface customize) {
		this.setPrefWidth(WIDTH);
		this.setLayoutY(Y_POS);
		this.setPromptText(PROMPT_TEXT);
		this.setOnAction((ActionEvent e)-> customize.changeColor(toRGBCode(this.getValue())));
	}
	
	//https://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color
	private String toRGBCode(Color color){
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
}
