package application;

public class NotToBeCoupledException extends Exception {

	/**
	 * when trying to make two adults a couple and at least one of them is already connected with another adult as a couple.
	 */
	public NotToBeCoupledException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
