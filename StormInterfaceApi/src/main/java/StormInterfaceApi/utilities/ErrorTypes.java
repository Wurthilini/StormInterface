package StormInterfaceApi.utilities;

import java.util.HashMap;
import java.util.Map;

public class ErrorTypes {

	public enum ErrorCodeMessageRequest
	{
		SUCCESS(1),
		UNKNOWN_ERROR(-1),
		FIRST_ERROR(-9800),
		FATAL_CODES(-9799),
		LAST_ERROR(-9798);
		
		private final int value;
		ErrorCodeMessageRequest(int value)
		{
			this.value = value;
		}
		public Integer value()
		{
			return this.value;
		}
		private static Map<Integer, ErrorCodeMessageRequest> map = new HashMap<>();
		
		static
		{
			for(ErrorCodeMessageRequest errorID : ErrorCodeMessageRequest.values())
				map.put(errorID.value(), errorID);
		}
		
		public static ErrorCodeMessageRequest valueOf(int errorCodeMessageRequest)
		{
			return map.get(errorCodeMessageRequest);
		}
	}
	
	public enum ReturnCodes
	{
		SUCCESS(0),
		NO_DATA_AVAILABLE(-1),
		NO_USB_DISPLAY_CONNECTED(-2),
		REQUEST_TIMEOUT(-3),
		HID_FAILED(-4),
		STARTUP_FAILED(-10000),
		LCD_FUNCTION_UNKNOWN(-9999),
		TOO_MUCH_DATA(-9998),
		PROC_MSG_UNKNOWN_MESSAGE(-9995),
		PROC_MSG_IGNORED_MESSAGE(-9994),
		GETM_MESSAGE_TOO_LONG(-9993),
		SENDACK_WRITE_FAILED(-9992),
		SEND_ACK_DATA_WROTE_ZERO(-9991),
		PKEYPRESS_INVALID_MESSAGE(-9990),
		PDEVICESTATUS_INVALID_MESSAGE(-9989),
		SENDHOSTBYTE_WRITE_FAILED(-9988),
		SENDHOSTBYTE_DATA_WROTE_ZERO(-9987),
		STATUS_FAILED(-9986),
		DEVICE_INFO_STRUCTURE_NULL(-9985),
		FATAL_CODES_END(-9984),
		UNKNOWN_ERROR_CODE(-9983);
		
		private final int value;
		ReturnCodes(int value)
		{
			this.value = value;
		}
		public Integer value()
		{
			return this.value;
		}
		private static Map<Integer, ReturnCodes> map = new HashMap<>();
		
		static
		{
			for(ReturnCodes returnCode : ReturnCodes.values())
				map.put(returnCode.value(), returnCode);
		}
		
		public static String valueOf(int returnCode)
		{
			return map.get(returnCode).toString();
		}
		
	}
	
}
