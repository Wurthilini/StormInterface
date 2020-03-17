package StormInterfaceApi.utilities;

import java.util.HashMap;
import java.util.Map;

public enum MessageID {

	MID_DEVICE_STATUS(1),         	///Device status message
	MID_LED_BRIGHTNESS(2),      		//< set led brightness
	MID_RESERVED_1(3),					///MID_RESERVED_6
	MID_RESERVED_2(4),				// MID_RESERVED_6
	MID_LOAD_NEW_TABLE(5),				//load new key code table
	MID_RESERVED_3(6),              // MID_RESERVED_6
	MID_KEYPAD_TYPE(7),				// set keypad type
	MID_RESERVED_4(8),					//notMID_RESERVED_6
	MID_WRITE_DEFAULT(9),			    // Write defaults values from ram to flash
	MID_RESET_TO_FACTORY_DEFAULT(10),    // reset the setting to factory default
	MID_RESERVED_5(11),				//MID_RESERVED_6
	MID_ENABLE_BSL(12),				//start downloader
	MID_RESERVED_6(13),			//MID_RESERVED_6
	MID_SET_SERIAL_NO(14),				// command to set serial number
	MID_ACK(15),
	MID_NAK(16),
	MID_EXTRA(17);
	
	private final int value;
	MessageID(int value)
	{
		this.value = value;
	}
	public Integer value()
	{
		return this.value;
	}
	private static Map<Integer, MessageID> map = new HashMap<>();
	
	static
	{
		for(MessageID messageID : MessageID.values())
			map.put(messageID.value(), messageID);
	}
	
	public static MessageID valueOf(int messageIDvalue)
	{
		return map.get(messageIDvalue);
	}
}
