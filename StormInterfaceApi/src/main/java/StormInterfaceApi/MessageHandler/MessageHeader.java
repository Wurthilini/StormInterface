package StormInterfaceApi.MessageHandler;

import java.util.ArrayList;
import java.util.Arrays;

import StormInterfaceApi.StormCommunicationManager;

public class MessageHeader{
	
	public enum MESSAGE_ID
	{
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
		private MESSAGE_ID(int value)
		{
			this.value = value;
		}
		public int getValue()
		{
			return this.value;
		}
	}
	private final Integer MAX_MESSAGE_DATA = 1024;
	public MESSAGE_ID messageID;
	private int messageVersion;
	private int dataLength;
	public byte[] messageData = new byte[MAX_MESSAGE_DATA];
	
	public boolean decodeMessage(MessageIncoming incomingMessage)
	{
		boolean retbool = true;
		switch(incomingMessage.completeStatus)
		{
		case MESSAGE_IS_ACK:
			this.messageID = MESSAGE_ID.MID_ACK;
			break;
		case MESSAGE_IS_NAK:
			this.messageID = MESSAGE_ID.MID_NAK;
			break;
		default:
			retbool = decodeMessage(incomingMessage.accumulatedMessage, incomingMessage.accumulatedMessage.length);
		}
		return retbool;
	}
	
	public boolean decodeMessage(byte[] rawMessage, int rawLength)
	{
		boolean retbool = true;
		int idx = 0;
		int retval = 0;
		ArrayList<Character> conversionOutput = new ArrayList<Character>();
		int fieldSize;
		try
		{
			messageVersion	= 0;
			messageID		= null;
			dataLength		= 0;
			if(rawLength < 3)
				throw new Exception("message is too short");
			//parse messageID
			fieldSize = 2;
			retval = ASCIIHexToBinary(Arrays.copyOfRange(rawMessage, idx, fieldSize), fieldSize, conversionOutput);
			if(retval < 0)
				throw new Exception("Retrieved Invalid Message ID");
			//this.messageID = conversionOutput.get(0);
			this.messageID = MESSAGE_ID.values()[conversionOutput.get(0)-1];
			conversionOutput.clear();
			//parse messageLength
			idx += fieldSize;
			retval = ASCIIHexToBinary(Arrays.copyOfRange(rawMessage, idx, idx+fieldSize), fieldSize, conversionOutput);
			if(retval < 0)
				throw new Exception("Retrieved Invalid Message Length");
			this.dataLength = conversionOutput.get(0);
			//Copy Data into buffer
			idx += fieldSize;
			this.messageData = Arrays.copyOfRange(rawMessage, idx, idx+this.dataLength);
		}
		catch(Exception e)
		{
			System.out.println("Decode Message failed");
		}
		return retbool;
	}
	
	private int ASCIIHexToBinary(byte[] asciiIn, int len, ArrayList<Character> out)
	{
		  int cntIn;
		  int cntOut = 0;
		  char Nybble = 0;
		  char workIn = 0;
		  char workOut = 0;
		  cntOut = 0;
		  for(cntIn=0; cntIn<len; cntIn++)
		  {
			  workIn = (char) asciiIn[cntIn];
			  if(workIn <= '9')
				  Nybble = (char) (workIn - '0');
			  else if(workIn <= 'F')
				  Nybble = (char) ((workIn - 'A') + 10);
			  else if(workIn <= 'f')
				  Nybble = (char) ((workIn - 'a') + 10);
			  else 
				  return 0; //Invalid value
			  if((cntIn & 1)==0)
				  workOut = (char) (Nybble<<4);
			  else
			  {
				  workOut = (char) (workOut | Nybble);
				  out.add(workOut);
				  cntOut++;
			  }
		  }
		  return cntOut;
	}
	
}
