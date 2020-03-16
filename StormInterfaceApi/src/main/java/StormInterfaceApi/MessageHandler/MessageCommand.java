package StormInterfaceApi.MessageHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageCommand {

	public Integer lastErrorCode; //TODO -> enum with error codes
	public Integer led_brightness;
	public Integer keypad_table;
	public Integer jack_status;
	public Integer hv_status;
	public byte[] keycodeTable;
	public String version;
	public String serialNo;
	
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
				break;
			case LOAD_NEW_TABLE:
			case SET_SERIAL_NO:
				for(byte bytes : messageRequest.ptrString)
					dataTosend.add(bytes);
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
		this.keycodeTable = Arrays.copyOfRange(receivedMessage.messageData, idx, idx+20);
		idx += 20;
		//get version number
		int newIdx;
		VersionPosition vPos;
		if(receivedMessage.messageData[idx]!=0x56)
		{
			vPos = findV(receivedMessage.messageData, -1);
			newIdx = vPos.getStart();
		}
		else
			vPos = findV(Arrays.copyOfRange(receivedMessage.messageData, idx, receivedMessage.messageData.length), idx);
		this.version = new String(Arrays.copyOfRange(receivedMessage.messageData, vPos.getStart(),vPos.getStart() + vPos.getEnd()+1),StandardCharsets.UTF_8);
		//get serial number
		idx += vPos.getEnd()+1;
		this.serialNo = new String(Arrays.copyOfRange(receivedMessage.messageData, idx, idx+15), StandardCharsets.UTF_8);		
		return retbool;
	}
	
	VersionPosition findV(byte[] messageData, int Vstart)
	{
		if(Vstart!=-1)
		{
			int counter = 0;
			for(byte bytes : messageData)
			{
				if(bytes == 0x2e)
				{
					VersionPosition Vpos = new VersionPosition(Vstart, counter+1);
					return Vpos;
				}
				counter++;
			}
		}
		else
		{
			for(byte bytes : messageData)
			{
				int counter = 0;
				if(bytes == 0x56)
				{
					Vstart = counter;
					return findV(Arrays.copyOfRange(messageData, Vstart, messageData.length), Vstart);
				}
				counter++;
			}
		}
		return null;
	}
	
	private class VersionPosition
	{
		private int start;
		private int end;
		
		public VersionPosition(int start, int end)
		{
			this.start = start;
			this.end = end;
		}
		
		public int getStart()
		{
			return this.start;
		}
		
		public int getEnd()
		{
			return this.end;
		}
	}
}
