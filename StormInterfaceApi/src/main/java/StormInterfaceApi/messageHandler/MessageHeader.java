package StormInterfaceApi.messageHandler;

import java.util.ArrayList;
import java.util.Arrays;

import StormInterfaceApi.utilities.MessageID;

public class MessageHeader{
	
	private final Integer MAX_MESSAGE_DATA = 1024;
	private int messageID;
	private int dataLength;
	private byte[] messageData = new byte[MAX_MESSAGE_DATA];
	
	public boolean decodeMessage(MessageIncoming incomingMessage)
	{
		boolean retbool = true;
		switch(incomingMessage.completeStatus)
		{
		case MESSAGE_IS_ACK:
			this.messageID = MessageID.MID_ACK.value();
			break;
		case MESSAGE_IS_NAK:
			this.messageID = MessageID.MID_NAK.value();
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
			this.messageID = -1;
			dataLength		= 0;
			if(rawLength < 3)
				throw new Exception("message is too short");
			//parse messageID
			fieldSize = 2;
			retval = ASCIIHexToBinary(Arrays.copyOfRange(rawMessage, idx, fieldSize), fieldSize, conversionOutput);
			if(retval < 0)
				throw new Exception("Retrieved Invalid Message ID");
			this.messageID = conversionOutput.get(0);
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
	
	public int getMessageID()
	{
		return this.messageID;
	}
	
	public byte[] getMessageData()
	{
		return this.messageData;
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
