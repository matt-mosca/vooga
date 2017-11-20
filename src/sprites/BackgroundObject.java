package sprites;

import authoring.AuthorInterface;

public class BackgroundObject extends StaticObject {
	
	public BackgroundObject(int size, AuthorInterface author, String imageString) {
		super(size, author, imageString);
		this.toBack();
	}

}
