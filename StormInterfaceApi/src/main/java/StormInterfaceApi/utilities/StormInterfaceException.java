package StormInterfaceApi.utilities;

public class StormInterfaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StormInterfaceException(String errorCode)
	{
		super(errorCode);
	}
}
