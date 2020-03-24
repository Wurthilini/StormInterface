package StormInterfaceApi;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;
import org.hid4java.event.HidServicesEvent;

import StormInterfaceApi.messageHandler.MessageCommand;
import StormInterfaceApi.messageHandler.MessageHeader;
import StormInterfaceApi.messageHandler.MessageIncoming;
import StormInterfaceApi.messageHandler.MessageRequest;
import StormInterfaceApi.utilities.DeviceInfo;
import StormInterfaceApi.utilities.MessageID;
import StormInterfaceApi.utilities.RequestType;
import StormInterfaceApi.utilities.StormInterfaceException;
import StormInterfaceApi.utilities.ErrorTypes.ReturnCodes;

public class StormCommunicationManager implements HidServicesListener{
	private static final Integer STORM_VENDOR_ID = 0x2047;
	private static final Integer STORM_PRODUCT_ID = 0x9d0;
	private static final String STORM_SERIAL_NUMBER = null;
	private static final byte CONFIGURATION_INTERFACE = 0x01;
	private static final byte KEYMAT_REPORT_ID = 0x3f;
	private HidDevice hidDevice = null;
	private HidServices hidServices = null;
	private DeviceInfo deviceInfo = new DeviceInfo();
	private ArrayList<MessageRequest> messageRequests = new ArrayList<MessageRequest>();
	private ArrayList<byte[]> stormResponse = new ArrayList<byte[]>();
	private ArrayList<MessageIncoming> messageArray = new ArrayList<MessageIncoming>();
	private static final Integer PACKET_LENGTH = 64;
	
	public StormCommunicationManager()
	{
		
	}
	
	public void initialiseStormUSBDevice() throws StormInterfaceException
	{
		boolean retbool = false;
		// Configure to use custom specification
		HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
		hidServicesSpecification.setAutoShutdown(true);
		hidServicesSpecification.setScanInterval(500);
		hidServicesSpecification.setPauseInterval(5000);
		hidServicesSpecification.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);
		this.hidServices = HidManager.getHidServices(hidServicesSpecification);
        this.hidServices.addHidServicesListener(this);
		this.hidServices.start();
		retbool = init();
		if(!retbool)
		{
			System.out.printf("Waiting for Storm Device to get plugged in.%n");
			sleepUninterruptibly(2);
		}
	}
	
    public void hidDeviceAttached(HidServicesEvent event) {

        //System.out.println("Device attached: " + event);
        if(event.getHidDevice().isVidPidSerial(STORM_VENDOR_ID, STORM_PRODUCT_ID, STORM_SERIAL_NUMBER))
        	init();
    }

    public void hidDeviceDetached(HidServicesEvent event) {
        //System.err.println("Device detached: " + event);
    }

    public void hidFailure(HidServicesEvent event) {
        //System.err.println("HID failure: " + event);
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
	
	public boolean setLedLevel(int ledLevel) throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		MessageRequest newRequest = new MessageRequest();
		newRequest.setRequestType(RequestType.LED_BRIGHTNESS.value());
		newRequest.setParam1((byte) ledLevel);
		messageRequests.add(newRequest);
		sendMessageSuccess = SendMessageRequest(newRequest);
		if(sendMessageSuccess)
			readMessageSuccess = readStormResponse();
		else
			readMessageSuccess = false;
		retbool = readMessageSuccess ? true : false;
		return retbool;
	}
	
	public boolean setKeypadTable(int keycodeTable) throws Exception
	{
		boolean retbool = true, sendMessageSuccess = false, readMessageSuccess = false;
		if(keycodeTable>2)
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
	
	public boolean setSerialNumber(String serialNumber) throws Exception
	{
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
	
	private boolean init()
	{
		// Open the device device by Vendor ID and Product ID with wildcard serial number
		this.hidDevice = this.hidServices.getHidDevice(STORM_VENDOR_ID, STORM_PRODUCT_ID, STORM_SERIAL_NUMBER, (byte) CONFIGURATION_INTERFACE);
		boolean retbool = this.hidDevice == null ? false : true;
		return retbool;
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
				int status = hidDevice.write(fullPacket, PACKET_LENGTH, KEYMAT_REPORT_ID);
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
		    	/*System.out.print("[");
		    	for(byte currentbyte : data)
		    		System.out.printf(" %x",currentbyte);
		    	System.out.print("]");
		    	System.out.println();*/
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
	
    public void sleepUninterruptibly(long sleepFor) {
    	TimeUnit unit = TimeUnit.SECONDS;
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepFor);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                	if(this.hidDevice!=null)
                		return;
                	else
                		NANOSECONDS.sleep(unit.toNanos(sleepFor));
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                //Thread.currentThread().interrupt();
            }
        }
    }
}
