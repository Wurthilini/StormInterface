package StormInterfaceApi;


import java.util.ArrayList;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;


import StormInterfaceApi.deviceManager.PortListener;
import StormInterfaceApi.messageHandler.MessageCommand;
import StormInterfaceApi.messageHandler.MessageHeader;
import StormInterfaceApi.messageHandler.MessageIncoming;
import StormInterfaceApi.messageHandler.MessageRequest;
import StormInterfaceApi.utilities.CustomisedCodeTable;
import StormInterfaceApi.utilities.DeviceInfo;
import StormInterfaceApi.utilities.MessageID;
import StormInterfaceApi.utilities.RequestType;
import StormInterfaceApi.utilities.StormInterfaceException;
import StormInterfaceApi.utilities.UsbKeyCodes;
import StormInterfaceApi.utilities.ErrorTypes.ReturnCodes;
import propertiesConfig.CustomisedTableCodeConfig;
import propertiesConfig.PropertiesConfig;
import propertiesConfig.PropertiesConfigReader;

public class StormCommunicationManager{
	protected static final Integer STORM_VENDOR_ID = 0x2047;
	protected static final Integer STORM_PRODUCT_ID = 0x9d0;
	protected static final Integer STORM_VENDOR_ID_AUDIO = 0x0D8c;
	protected static final Integer STORM_PRODUCT_ID_AUDIO = 0x0170;
	protected static final String STORM_SERIAL_NUMBER = null;
	private static final byte CONFIGURATION_INTERFACE = 0x01;
	private static final byte KEYMAT_REPORT_ID = 0x3f;
	private PropertiesConfig propertiesConfig;
	protected static HidDevice hidDevice;
	protected CustomisedCodeTable customisedCodeTable;
	private DeviceInfo deviceInfo = new DeviceInfo();
	private ArrayList<MessageRequest> messageRequests = new ArrayList<MessageRequest>();
	private ArrayList<byte[]> stormResponse = new ArrayList<byte[]>();
	private ArrayList<MessageIncoming> messageArray = new ArrayList<MessageIncoming>();
	private static final Integer PACKET_LENGTH = 64;
	
	public StormCommunicationManager() throws Exception
	{
		this.propertiesConfig = new PropertiesConfigReader().readPropertiesConfig();
	}
	
	public void initialiseStormUSBDevice() throws Exception
	{
		boolean retbool = false;
		HidDevice hidDevice = null;
		// Configure to use custom specification
		HidServices hidServices = HidManager.getHidServices();
		hidServices.start();
		hidDevice = init(hidServices, hidDevice);
		retbool = (hidDevice != null);
		StormCommunicationManager.hidDevice = hidDevice;
		if(!retbool)
		{
			System.out.printf("Waiting for Storm Device to get plugged in.%n");
			PortListener portListener = new PortListener(2);
			StormCommunicationManager.hidDevice = portListener.getHidDevice();
		}
	}
	
	public boolean getDeviceStatus(DeviceInfo deviceInformation) throws Exception
	{
		boolean retbool = false, sendMessageSuccess = false, readMessageSuccess = false;
		MessageRequest newRequest = new MessageRequest();
		this.deviceInfo = deviceInformation;
		newRequest.setRequestType(RequestType.DEVICE_STATUS.value());
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	public boolean setLedLevel() throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		int ledlevel = 0;
		for(int i=0;i<=10;i++)
		{
			ledlevel = i;
			if(i == 10)
				ledlevel = this.propertiesConfig.getLedLevelConfig().getLedLevel();
			MessageRequest newRequest = new MessageRequest();
			newRequest.setRequestType(RequestType.LED_BRIGHTNESS.value());
			newRequest.setParam1((byte) ledlevel);
			messageRequests.add(newRequest);
			sendMessageSuccess = SendMessageRequest(newRequest);
			if(sendMessageSuccess)
				readMessageSuccess = readStormResponse();
			else
				readMessageSuccess = false;
			retbool = readMessageSuccess ? true : false;
		}
		return retbool;
	}
	
	public boolean assignKeypadTable() throws Exception
	{
		switch(this.propertiesConfig.getKeypadTableConfig().getKeypadTableID())
		{
		case 0:
			customisedCodeTable = new CustomisedCodeTable(0);
			break;
		case 1:
			customisedCodeTable = new CustomisedCodeTable(1);
			break;
		case 2:
			customisedCodeTable = new CustomisedCodeTable(2);
			for(CustomisedTableCodeConfig currentTableCodeConfig : this.propertiesConfig.getCustomisedTableCodeConfig())
			{
				byte usbUsageCode;
				byte modifierCode = 0;
				boolean modifierCodeEmpty = true;
				try
				{
					usbUsageCode = UsbKeyCodes.valueOf(currentTableCodeConfig.getUsbUsageName()).value();
				}
				catch(Exception e)
				{
					throw new StormInterfaceException("Unknown usbUsageCode in " + currentTableCodeConfig.getID() + " with Name " + currentTableCodeConfig.getUsbUsageName());
				}
				if(!currentTableCodeConfig.getModifierName().isEmpty())
				{
					try
					{
						modifierCode = UsbKeyCodes.valueOf(currentTableCodeConfig.getModifierName()).value();
					}
					catch(Exception e)
					{
						throw new StormInterfaceException("Unknown ModifierName in " + currentTableCodeConfig.getID() + " with Name " + currentTableCodeConfig.getModifierName());
					}
					modifierCodeEmpty = false;
				}
				switch(currentTableCodeConfig.getID())
				{
				case "Volume":
						if(modifierCodeEmpty)
							customisedCodeTable.setVolume(usbUsageCode);
						else
							customisedCodeTable.setVolume(usbUsageCode, modifierCode);
						break;
				case "Left":
					if(modifierCodeEmpty)
						customisedCodeTable.setLeft(usbUsageCode);
					else
						customisedCodeTable.setLeft(usbUsageCode, modifierCode);
					break;
				case "Right":
					if(modifierCodeEmpty)
						customisedCodeTable.setRight(usbUsageCode);
					else
						customisedCodeTable.setRight(usbUsageCode, modifierCode);
					break;
				case "Down":
					if(modifierCodeEmpty)
						customisedCodeTable.setDown(usbUsageCode);
					else
						customisedCodeTable.setDown(usbUsageCode, modifierCode);
					break;
				case "Up":
					if(modifierCodeEmpty)
						customisedCodeTable.setUp(usbUsageCode);
					else
						customisedCodeTable.setUp(usbUsageCode, modifierCode);
					break;
				case "Enter":
					if(modifierCodeEmpty)
						customisedCodeTable.setEnter(usbUsageCode);
					else
						customisedCodeTable.setEnter(usbUsageCode, modifierCode);
					break;
				case "Horizontal":
					if(modifierCodeEmpty)
						customisedCodeTable.setHorizontal(usbUsageCode);
					else
						customisedCodeTable.setHorizontal(usbUsageCode, modifierCode);
					break;
				case "Vertical":
					if(modifierCodeEmpty)
						customisedCodeTable.setVertical(usbUsageCode);
					else
						customisedCodeTable.setVertical(usbUsageCode, modifierCode);
					break;
				case "JackIn":
					if(modifierCodeEmpty)
						customisedCodeTable.setJackIn(usbUsageCode);
					else
						customisedCodeTable.setJackIn(usbUsageCode, modifierCode);
					break;
				case "JackOut":
					if(modifierCodeEmpty)
						customisedCodeTable.setJackOut(usbUsageCode);
					else
						customisedCodeTable.setJackOut(usbUsageCode, modifierCode);
					break;
				default:
					throw new StormInterfaceException("Unknown CustomisedTableCode ID " + currentTableCodeConfig.getID());
				}
			}
			customisedCodeTable.loadCodeTable();
		}
		return true;
	}
	
	public boolean setKeypadTable(int keycodeTable) throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		if(keycodeTable>2 || keycodeTable<0)
			throw new StormInterfaceException("Unknown KeypadType");
		MessageRequest newRequest = new MessageRequest();
		newRequest.setRequestType(RequestType.KEYPAD_TYPE.value());
		newRequest.setParam1((byte) keycodeTable);
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	public boolean loadCodeTable(byte[] keyCodes, int keyCodeLen) throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		if(keyCodes.length!=20)
			throw new StormInterfaceException("loadCodeTable requieres 20 keyCodes");
		MessageRequest newRequest = new MessageRequest();
		newRequest.setRequestType(RequestType.LOAD_NEW_TABLE.value());
		newRequest.setParam1((byte) keyCodeLen);
		newRequest.setPtrString(keyCodes);
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	public boolean writeDefaultToFlash() throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		MessageRequest newRequest = new MessageRequest();
		newRequest.setRequestType(RequestType.WRITE_DEFAULT.value());
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	public boolean resetToFactoryDefault() throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		MessageRequest newRequest = new MessageRequest();
		newRequest.setRequestType(RequestType.RESET_TO_FACTORY_DEFAULT.value());
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	public boolean setSerialNumber() throws Exception
	{
		String serialNumber = this.propertiesConfig.getSerialNumberConfig().getSerialNumberID();
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		if(serialNumber.isEmpty())
			throw new StormInterfaceException("No SerialNumber to set");
		MessageRequest newRequest = new MessageRequest();
		newRequest.setRequestType(RequestType.SET_SERIAL_NO.value());
		newRequest.setPtrString(serialNumber.getBytes());
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	protected HidDevice init(HidServices hidServices, HidDevice hidDevice)
	{
		// Open the device device by Vendor ID and Product ID with wildcard serial number
		hidDevice = hidServices.getHidDevice(STORM_VENDOR_ID, STORM_PRODUCT_ID, STORM_SERIAL_NUMBER, (byte) CONFIGURATION_INTERFACE);
		return hidDevice;
	}
	
	private boolean SendMessageRequest(MessageRequest request) throws InterruptedException
	{
		boolean retval = true;
		//create Message
		for(MessageRequest currentMessageRequest : messageRequests)
		{
			MessageCommand commandRequest = new MessageCommand();
			byte[] fullPacket = new byte[PACKET_LENGTH-1];
			fullPacket = commandRequest.buildRequest(currentMessageRequest, fullPacket);
			try {
				int status = StormCommunicationManager.hidDevice.write(fullPacket, PACKET_LENGTH, KEYMAT_REPORT_ID);
				if(status < 0)
					throw new StormInterfaceException(ReturnCodes.valueOf(-4));
			} catch (Exception e) {
				e.printStackTrace();
				this.messageRequests.clear();
				return false;
			}
		}
		this.messageRequests.clear();
		return retval;
	}
	
	private boolean readStormResponse() throws Exception
	{
		int val;
		boolean moreData = true, retval = true;
		this.stormResponse.clear();
		while(moreData)
		{
			byte[] data = new byte[PACKET_LENGTH];
		    // This method will now block for 500ms or until data is read
		    val = StormCommunicationManager.hidDevice.read(data, 500);
		    switch(val)
		    {
		    case -1:
		          System.err.println(StormCommunicationManager.hidDevice.getLastErrorMessage());
		          break;
		    case 0:
		          moreData = false;
		          break;
		    default:
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
					System.out.printf("Message %s failed \n", receivedMessage.getMessageID());
					retval = false;
				}
			}
		}
		return retval;
	}
	
	private boolean getMessageStatus()
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
	
	private boolean ProcessCommand(MessageHeader receivedMessage) throws Exception
	{
		boolean retbool = true;
		//What type of message?
		switch(MessageID.valueOf(receivedMessage.getMessageID()))
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
			if(!(receivedMessage.getMessageData()[0]==0x00))
				retbool = false;
			break;
		default:
			throw new Exception("Unknown Message");
		}
		return retbool;
	}
	
	private boolean processDeviceStatus(MessageHeader receivedMessage) throws Exception
	{
		boolean retbool = true;
		MessageCommand commandStatus = new MessageCommand();
		retbool = commandStatus.decodeMessage(receivedMessage);
		this.deviceInfo.setLedBrightness(commandStatus.getLedBrigthness());
		this.deviceInfo.setKeypadTable(commandStatus.getKeypadTable());
		this.deviceInfo.setJackStatus(commandStatus.getJackStatus());
		this.deviceInfo.setHvStatus(commandStatus.getHVStatus());
		this.deviceInfo.setKeyCode(commandStatus.getKeyCodeTable());
		this.deviceInfo.setVersion(commandStatus.getVersion());
		this.deviceInfo.setSerialNumber(commandStatus.getSerialNo());
		return retbool;
	}
}
