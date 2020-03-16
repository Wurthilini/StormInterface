package StormInterfaceApi;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;

import StormInterfaceApi.MessageHandler.MessageCommand;
import StormInterfaceApi.MessageHandler.MessageHeader;
import StormInterfaceApi.MessageHandler.MessageHeader.MESSAGE_ID;
import StormInterfaceApi.MessageHandler.MessageIncoming;
import StormInterfaceApi.MessageHandler.MessageRequest;

public class StormCommunicationManager extends StormCommunication{
	private static final Integer STORM_VENDOR_ID = 0x2047;
	private static final Integer STORM_PRODUCT_ID = 0x9d0;
	private static final String STORM_SERIAL_NUMBER = null;
	private static final byte CONFIGURATION_INTERFACE = 0x01;
	private static final byte KEYMAT_REPORT_ID = 0x3f;
	public static HidDevice hidDevice = null;
	public DEVICE_INFO deviceInfo;
	private ArrayList<MessageRequest> messageRequests = new ArrayList<MessageRequest>();
	private ArrayList<byte[]> stormResponse = new ArrayList<byte[]>();
	private ArrayList<MessageIncoming> messageArray = new ArrayList<MessageIncoming>();
	private static final Integer PACKET_LENGTH = 64;
	
	boolean initialiseStormUSBDevice() throws HidException
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
		    return retbool;
		}
		catch(Exception e)
		{
			System.out.println("Unable to initialise StormDevice");
		}
		return retbool;
	}
	
	DEVICE_INFO getDeviceStatus() throws Exception
	{
		boolean initialised = false, retbool = false, sendMessageSuccess = false, readMessageSuccess = false;
		MessageRequest newRequest = new MessageRequest();
		this.deviceInfo = new DEVICE_INFO();
		newRequest.requestType = newRequest.requestType.DEVICE_STATUS;
		if(hidDevice == null)
		{
			initialised = initialiseStormUSBDevice();
			if(!initialised)
				throw new Exception("storm device may not be connected");
		}
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
		{
			readMessageSuccess = readStormResponse();
		}
		else
			 System.err.println(hidDevice.getLastErrorMessage());
		return this.deviceInfo;
	}
	
	boolean setLedLevel(int ledLevel) throws Exception
	{
		boolean initialised = false, retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		MessageRequest newRequest = new MessageRequest();
		newRequest.requestType = newRequest.requestType.LED_BRIGHTNESS;
		newRequest.param1 = (byte) ledLevel;
		if(hidDevice == null)
		{
			initialised = initialiseStormUSBDevice();
			if(!initialised)
				throw new Exception("storm device may not be connected");
		}
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
		{
			System.err.println(hidDevice.getLastErrorMessage());
			retbool = false;
		}
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	boolean SendMessageRequest(MessageRequest request) throws InterruptedException
	{
		boolean retval = true;
		//create Message
		for(MessageRequest currentMessageRequest : messageRequests)
		{
			MessageCommand commandRequest = new MessageCommand();
			byte[] fullPacket = new byte[PACKET_LENGTH-1];
			fullPacket = commandRequest.buildRequest(currentMessageRequest, fullPacket);
			int status = hidDevice.write(fullPacket, PACKET_LENGTH, KEYMAT_REPORT_ID);
			if(status < 0)
				retval = false;
		}
		this.messageRequests.clear();
		return retval;
	}
	
	boolean readStormResponse() throws Exception
	{
		int val;
		boolean moreData = true, retval = true;
		this.stormResponse.clear();
		while(moreData)
		{
			byte[] data = new byte[PACKET_LENGTH];
		    // This method will now block for 500ms or until data is read
		    val = hidDevice.read(data, 500);
		    switch(val)
		    {
		    case -1:
		          System.err.println(hidDevice.getLastErrorMessage());
		          break;
		    case 0:
		          moreData = false;
		          break;
		    default:
		    	System.out.print("[");
		    	for(byte currentbyte : data)
		    		System.out.printf(" %x",currentbyte);
		    	System.out.print("]");
		    	System.out.println();
		    	this.stormResponse.add(data);
		    	break;
		    }
		}
		getMessageStatus(); //only saves messages that have a required structure
		for(MessageIncoming newMessage : this.messageArray)
		{
			MessageHeader receivedMessage = new MessageHeader();
			if(receivedMessage.decodeMessage(newMessage))
			{
				retval = ProcessCommand(receivedMessage);
				if(!(retval))
				{
					System.out.printf("Message %s failed \n", receivedMessage.messageID);
					retval = false;
				}
			}
		}
		return retval;
	}
	
	boolean getMessageStatus()
	{
		MessageIncoming accumMessage = new MessageIncoming();
		for(byte[] response : this.stormResponse)
			accumMessage.AccumulateMessage(response);
			switch(accumMessage.completeStatus)
			{
			case BAD_MESSAGE:
				//delete bad message
				accumMessage = new MessageIncoming();
				break;
			case GOOD_MESSAGE:
				this.messageArray.add(accumMessage);
				accumMessage = new MessageIncoming();
				break;
			case INCOMPLETE_MESSAGE:
				break;
			default: //ACK OR NAK
				messageArray.add(accumMessage);
				accumMessage = new MessageIncoming();
				break;
			}
		return true; 
	}
	
	public boolean ProcessCommand(MessageHeader receivedMessage) throws Exception
	{
		boolean retbool = true;
		//What type of message?
		switch(receivedMessage.messageID)
		{
		case MID_ACK:
		case MID_NAK:
			break;
		case MID_DEVICE_STATUS:
			retbool = processDeviceStatus(receivedMessage);
			break;
		case MID_LED_BRIGHTNESS:
		case MID_LOAD_NEW_TABLE:
		case MID_KEYPAD_TYPE:
		case MID_WRITE_DEFAULT:
		case MID_RESET_TO_FACTORY_DEFAULT:
		case MID_SET_SERIAL_NO:
		case MID_ENABLE_BSL:
			if(!(receivedMessage.messageData[0]==0x00))
				retbool = false;
			break;
		default:
			throw new Exception("Unknown Message");
		}
		return retbool;
	}
	
	public boolean processDeviceStatus(MessageHeader receivedMessage) throws Exception
	{
		boolean retbool = true;
		MessageCommand commandStatus = new MessageCommand();
		retbool = commandStatus.decodeMessage(receivedMessage);
		this.deviceInfo.led_brightness = commandStatus.led_brightness;
		this.deviceInfo.keypad_table = commandStatus.keypad_table;
		this.deviceInfo.jack_status = commandStatus.jack_status;
		this.deviceInfo.HV_status = commandStatus.hv_status;
		this.deviceInfo.keyCode = commandStatus.keycodeTable;
		return retbool;
	}
	
	public static class DEVICE_INFO
	{
		int led_brightness;
		int keypad_table;
		int jack_status;
		int HV_status;
		byte[] keyCode = new byte[20];
		String version;
		String serialNumber;
	}
}
