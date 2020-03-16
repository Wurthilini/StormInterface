package StormInterfaceApi.MessageHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageCommand {

	public Integer lastErrorCode; //TODO -> enum with error codes
	public Integer led_brightness;
	public Integer keypad_table;
	public Integer jack_status;
	public Integer hv_status;
	public byte[] keycodeTable;
	
	public byte[] buildRequest(MessageRequest messageRequest, byte[] _responseMessage)
	{
		MessageBuildPacket messageBuildPacket = new MessageBuildPacket();
		boolean retbool = false;
		ArrayList<Byte> dataTosend = new ArrayList<Byte>();
		try
		{
			switch(messageRequest.requestType)
			{
			case LED_BRIGHTNESS:
				dataTosend.add(messageRequest.param1);
				//dataTosend += messageRequest.param1;
				break;
			case LOAD_NEW_TABLE:
				//reinterprete 
				//c++
				//std::string str (reinterpret_cast<const char *> (newRequest->ptrString),newRequest->param1);
				//dataToSend  += str ;	
				break;
			case KEYPAD_TYPE:
				dataTosend.add(messageRequest.param1);
				break;
			default:
				break;
			}
			_responseMessage = messageBuildPacket.makePacket(messageRequest.requestType.getValue(), dataTosend, _responseMessage);
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
		int conversionOutput;
		char[] tmpBuffer = new char[50];
		int idx = 0;
		int cnt;
		int retval;
		int fieldSize;
		//Get Error Code
		fieldSize = 1;
		this.lastErrorCode = (int) receivedMessage.messageData[idx];
		if(0> this.lastErrorCode)
			throw new Exception("Inbalid Keypad Error");
		idx += fieldSize;
		//get led bitghtness
		this.led_brightness = (int) receivedMessage.messageData[idx];
		idx += fieldSize;
		//get inverse Mode
		this.keypad_table = (int) receivedMessage.messageData[idx];
		idx += fieldSize;
		//get contrast Level
		this.jack_status = (int) receivedMessage.messageData[idx];
		idx += fieldSize;
		//get backlight 
		this.hv_status = (int) receivedMessage.messageData[idx];
		idx += fieldSize;
		//get keycode table
		this.keycodeTable = Arrays.copyOfRange(receivedMessage.messageData, idx, 20);
		idx += 20;
		//TODO -> version number and serial number
		//get firmware version
		
		return retbool;
	}
	
}
