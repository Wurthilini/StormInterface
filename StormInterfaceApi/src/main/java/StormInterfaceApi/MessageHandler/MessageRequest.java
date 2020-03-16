package StormInterfaceApi.MessageHandler;

public class MessageRequest {

	public REQUEST_TYPE requestType;
	
	public enum ERROR_CODE
	{
		SUCCESS(1),
		UNKNOWN_ERROR(-1),
		FIRST_ERROR(-9800);
		public final int value;
		
		private ERROR_CODE(int value)
		{
			this.value = value;
		}
		public int getValue()
		{
			return value;
		}
		
	} 
	
	public enum REQUEST_TYPE
	{
		DEVICE_STATUS(1),			//Device status message
		LED_BRIGHTNESS(2),			//< set led brightness
	    LOAD_NEW_TABLE(5),			//load new key code table
	    KEYPAD_TYPE(7),			// set keypad type 0 - default table, 1 - alternate 2- customise
	    WRITE_DEFAULT(9),			// Write defaults values from ram to flash
	    RESET_TO_FACTORY_DEFAULT(10),  // reset the setting to factory default
	    ENABLE_BSL(11),				//start downloader
		SET_SERIAL_NO(14);			// command to set serial number
		private final int value;
		
		private REQUEST_TYPE(int value)
		{
			this.value = value;
		}
		public int getValue()
		{
			return value;
		}
	}
	
	//command parameters
	public byte param1;
	public int param2;
	public int param3;
	public int param4;
	public int param5;
	public int param6;
	public int param7;
	public int param8;
	public byte[] ptrString;
	public String paramStr;
	
}
