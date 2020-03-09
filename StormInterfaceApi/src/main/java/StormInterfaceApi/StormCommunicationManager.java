package StormInterfaceApi;

import java.util.ArrayList;

import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;

import StormInterfaceApi.MessageHandler.MessageCommand;
import StormInterfaceApi.MessageHandler.MessageRequest;

public class StormCommunicationManager {
	private static final Integer STORM_VENDOR_ID = 0x2047;
	private static final Integer STORM_PRODUCT_ID = 0x9d0;
	private static final String STORM_SERIAL_NUMBER = null;
	private static final byte CONFIGURATION_INTERFACE = 0x01;
	public static HidDevice hidDevice = null;
	private ArrayList<MessageRequest> messageRequest = new ArrayList<MessageRequest>();
	
	boolean InitialiseStormUSBDevice() throws HidException
	{
		boolean retbool = false;
		System.out.printf("vid %02x pid %02x \n", STORM_VENDOR_ID, STORM_PRODUCT_ID);
		try
		{
		    // Configure to use custom specification
		    HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
		    hidServicesSpecification.setAutoShutdown(true);
		    hidServicesSpecification.setScanInterval(500);
		    hidServicesSpecification.setPauseInterval(5000);
		    hidServicesSpecification.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);
		    HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
		    HidServicesListener servicesListener = null;
		    hidServices.addHidServicesListener(servicesListener);
		    hidServices.start();
		    // Open the device device by Vendor ID and Product ID with wildcard serial number
		    this.hidDevice = hidServices.getHidDevice(STORM_VENDOR_ID, STORM_PRODUCT_ID, STORM_SERIAL_NUMBER, (byte) CONFIGURATION_INTERFACE);
		    if(hidDevice!=null)
		    	retbool = true;
		}
		finally
		{
			return retbool;
		}
	}
	
	boolean GetDeviceStatus(DEVICE_INFO deviceInfo, int timeToWait) throws StormDeviceNotInitialised
	{
		boolean initialised = false;
		boolean retbool = false;
		MessageRequest newRequest = new MessageRequest();
		long lastStatus;
		newRequest.requestType = newRequest.requestType.DEVICE_STATUS;
		if(hidDevice == null)
		{
			initialised = InitialiseStormUSBDevice();
			if(!initialised)
				throw new StormDeviceNotInitialised("storm device may not be connected");
		}
		messageRequest.add(newRequest);
		retbool = SendMessageRequest(newRequest);
		return true;
	}
	
	boolean SendMessageRequest(MessageRequest request)
	{
		boolean retval = false;
		for(MessageRequest currentMessageRequest : messageRequest)
		{
			messageRequest.remove(0);
			switch (currentMessageRequest.requestType)
			{
			case DEVICE_STATUS:
				MessageCommand commandRequest = new MessageCommand();
				String fullPacket = null;
				commandRequest.BuildRequest(currentMessageRequest, fullPacket);
			}
		}
		return retval;
	}
	
	private class DEVICE_INFO
	{
		byte led_brightness;
		byte keypad_table;
		byte jack_status;
		byte HV_status;
		byte[] keyCode = new byte[20];
		String version;
		String serialNumber;
	}
	
	public class StormDeviceNotInitialised extends Exception
	{
		public StormDeviceNotInitialised(String message)
		{
			super(message);
		}
	}
	
}
