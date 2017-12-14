package exceptions;

import java.util.ResourceBundle;

public class VoogaException extends Throwable {

	private final String BUNDLE_NAME = VoogaException.class.getSimpleName();

	public VoogaException(Throwable exception) {
		super(exception);
	}

	/**
	 * Get the message associated with a particular Vooga exception.
	 *
	 * @return the appropriate message for the exception type, obtained through reflection on the exception name
	 */
	public String getMessage() {
		return ResourceBundle.getBundle(BUNDLE_NAME).getString(this.getClass().getName());
	}
	
}
