package StormInterfaceApi.messageHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import StormInterfaceApi.utilities.RequestType;
import StormInterfaceApi.utilities.VersionDecoder;

public class MessageCommand {

	private Integer lastErrorCode;
	private Integer led_brightness;
	private Integer keypad_table;
	private Integer jack_status;
	private Integer hv_status;
	private byte[] keycodeTable;
	private String version;
	private String serialNo;
	
	public MessageCommand()
	{
		this.lastErrorCode = null;
		this.led_brightness = -1;
		this.keypad_table = -1;
		this.jack_status = -1;
		this.hv_status = -1;
		this.keycodeTable = new byte[20];
		this.version = "";
		this.serialNo = "";
	}
	
	public byte[] buildRequest(MessageRequest messageRequest, byte[] _responseMessage)
	{
		MessageBuildPacket messageBuildPacket = new MessageBuildPacket();
		ArrayList<Byte> dataTosend = new ArrayList<Byte>();
		try
		{
			switch(RequestType.valueOf(messageRequest.getRequestType()))
			//switch(messageRequest.getRequestType())
			{
			case LED_BRIGHTNESS:
				dataTosend.add((byte) messageRequest.getAdditionalParams()[0]);
				break;
			case LOAD_NEW_TABLE:
			case SET_SERIAL_NO:
				for(byte bytes : messageRequest.getPtrString())
					dataTosend.add(bytes);
				break;
			case KEYPAD_TYPE:
				dataTosend.add((byte) messageRequest.getAdditionalParams()[0]);
				break;
			default:
				break;
			}
			_responseMessage = messageBuildPacket.makePacket(messageRequest.getRequestType(), dataTosend, _responseMessage);
		}
		//TODO -> add exceptions with error codes
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return _responseMessage;
	}
	
	public boolean decodeMessage(MessageHeader receivedMessage) throws Exception
	{
		boolean retbool = true;
		int idx = 0;
		int fieldSize;
		//Get Error Code
		fieldSize = 1;
		System.out.println("hier");
		this.lastErrorCode = (int) receivedMessage.getMessageData()[idx];
		System.out.println("hieer2");
		if(0> this.lastErrorCode)
			throw new Exception("Inbalid Keypad Error");
		idx += fieldSize;
		//get led brightness
		System.out.println("hier3");
		System.out.println(idx);
		for(byte bytes : receivedMessage.getMessageData())
			System.out.printf("%02x \n", bytes);
		this.led_brightness = (int) receivedMessage.getMessageData()[idx];
		idx += fieldSize;
		//get inverse Mode
		System.out.println("hier4");
		this.keypad_table = (int) receivedMessage.getMessageData()[idx];
		idx += fieldSize;
		//get contrast Level
		System.out.println("hier5");
		this.jack_status = (int) receivedMessage.getMessageData()[idx];
		idx += fieldSize;
		//get backlight 
		this.hv_status = (int) receivedMessage.getMessageData()[idx];
		idx += fieldSize;
		//get keycode table
		this.keycodeTable = Arrays.copyOfRange(receivedMessage.getMessageData(), idx, idx+20);
		idx += 20;
		//get version number
		VersionDecoder versionPos = new VersionDecoder();;
		if(receivedMessage.getMessageData()[idx]!=0x56)
			versionPos.findVersionString(receivedMessage.getMessageData(), -1);
		else
			versionPos.findVersionString(Arrays.copyOfRange(receivedMessage.getMessageData(), idx, (receivedMessage.getMessageData()).length), idx);
		this.version = new String(Arrays.copyOfRange(receivedMessage.getMessageData(), versionPos.getStartPosition(),versionPos.getStartPosition() + versionPos.getEndPosition()+1),StandardCharsets.UTF_8);
		//get serial number
		idx += versionPos.getEndPosition()+1;
		this.serialNo = new String(Arrays.copyOfRange(receivedMessage.getMessageData(), idx, idx+15), StandardCharsets.UTF_8);		
		return retbool;
	}
	
	public int getLedBrigthness()
	{
		return this.led_brightness;
	}
	
	public int getKeypadTable()
	{
		return this.keypad_table;
	}
	
	public int getJackStatus()
	{
		return this.jack_status;
	}
	
	public int getHVStatus()
	{
		return this.hv_status;
	}
	
	public byte[] getKeyCodeTable()
	{
		return this.keycodeTable;
	}
	
	public String getVersion()
	{
		return this.version;
	}
	
	public String getSerialNo()
	{
		return this.serialNo;
	}
}
