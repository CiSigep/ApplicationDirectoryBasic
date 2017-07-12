package sample.exceptions;

public class DBAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DBAccessException()
	{
		super("There was an error in accessing the database");
	}
	
	public DBAccessException(String message){
		super(message);
	}

}
