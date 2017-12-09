package exceptions;

public class VoogaException extends RuntimeException{
	
	public VoogaException(Throwable exception) {
		super(exception);
	}
	
	public VoogaException() {
		super();
		
	}
	
}
