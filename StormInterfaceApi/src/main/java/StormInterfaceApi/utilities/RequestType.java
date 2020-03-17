package StormInterfaceApi.utilities;

import java.util.HashMap;
import java.util.Map;

public enum RequestType {
	DEVICE_STATUS(1),			//Device status message
	LED_BRIGHTNESS(2),			//< set led brightness
    LOAD_NEW_TABLE(5),			//load new key code table
    KEYPAD_TYPE(7),			// set keypad type 0 - default table, 1 - alternate 2- customise
    WRITE_DEFAULT(9),			// Write defaults values from ram to flash
    RESET_TO_FACTORY_DEFAULT(10),  // reset the setting to factory default
    ENABLE_BSL(11),				//start downloader
	SET_SERIAL_NO(14);			// command to set serial number
	
	private final int value;
	RequestType(int value)
	{
		this.value = value;
	}
	public Integer value()
	{
		return this.value;
	}
	
	private static Map<Integer, RequestType> map = new HashMap<>();
	
	static
	{
		for(RequestType reqType : RequestType.values())
			map.put(reqType.value(), reqType);
	}
	
	public static RequestType valueOf(int requestTypeValue)
	{
		return map.get(requestTypeValue);
	}
}
