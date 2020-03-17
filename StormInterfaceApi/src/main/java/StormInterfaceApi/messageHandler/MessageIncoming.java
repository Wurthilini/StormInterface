package StormInterfaceApi.messageHandler;

import java.util.Arrays;

public class MessageIncoming {
	
	enum COMMS_STATE
	{
		NEED_STX,
		NEED_ETX,
		NEED_LRC,
		STATE_QUIT
	}
	
	public enum MESSAGE_STATUS
	{
		INCOMPLETE_MESSAGE,
		GOOD_MESSAGE,
		BAD_MESSAGE,
		MESSAGE_IS_ACK,
		MESSAGE_IS_NAK,
	}
	
	COMMS_STATE commsState = COMMS_STATE.NEED_STX;
	public MESSAGE_STATUS completeStatus = MESSAGE_STATUS.INCOMPLETE_MESSAGE;
	private static final byte ACK = 0x06;
	private static final byte NAK = 0x15;
	private static final byte SC_STX = 0x02;
	private static final byte SC_ETX = 0x03;
	public byte[] accumulatedMessage;
	private byte LRC = SC_STX;
	
	@SuppressWarnings("incomplete-switch")
	public void AccumulateMessage(byte[] incommingData)
	{
		int foundLocation = 0;
		int startPosition = 0;
		int endPosition = 0;
		boolean abortLoop = false;
		int messageLength = 0;
		while(abortLoop == false && incommingData != null) //TO DO -> wenn size nicht zuende ist weitermacen
		{
			switch(commsState)
			{
			case NEED_STX:
				//Look for NAK or ACK before STX
				if(NAK == incommingData[2])
				{
					abortLoop = true;
					this.completeStatus = MESSAGE_STATUS.MESSAGE_IS_NAK;
					incommingData = null;
					break;
				}
				if(ACK == incommingData[2])
				{
					abortLoop = true;
					this.completeStatus = MESSAGE_STATUS.MESSAGE_IS_ACK;
					incommingData = null;
					break;
				}
				//STX, MSGID, MSGLEN, DATA, ETX, LRC
				// Find first instance of an STX in the string
				messageLength = incommingData[1];
				foundLocation = findSTX(incommingData);
				if(foundLocation == -1)
				{
					// There is NO STX in this data... delete it all
					incommingData = null;
					break;
				}
				// We are NOW looking for an ETX
				this.commsState = COMMS_STATE.NEED_ETX;
				startPosition = foundLocation +1;
				if(startPosition >= incommingData.length)
				{
					incommingData = null;
					break;
				}
			case NEED_ETX:
				if(incommingData[messageLength] != SC_ETX)
					endPosition = incommingData.length;
				else
				{
					endPosition = messageLength; //skip STX,ETX and LRC
					this.commsState = COMMS_STATE.NEED_LRC;
				}
				this.accumulatedMessage = Arrays.copyOfRange(incommingData, startPosition, endPosition);
				incommingData = Arrays.copyOfRange(incommingData, endPosition+1, incommingData.length);
				if(!((COMMS_STATE.NEED_LRC == this.commsState) && (incommingData != null)))
					break;
			case NEED_LRC:
				//calculate the LRC
				for(int x=0;x<this.accumulatedMessage.length;x++)
				{
					LRC ^= this.accumulatedMessage[x];
				}
				LRC ^= SC_ETX;
				if(LRC == incommingData[0])
				{
					completeStatus = MESSAGE_STATUS.GOOD_MESSAGE;
					incommingData = null;
					abortLoop = true;
				}
				else
				{
					this.completeStatus = MESSAGE_STATUS.BAD_MESSAGE;
					this.commsState = COMMS_STATE.NEED_STX;
					abortLoop = true;
				}
				break;
			}
		}		
	}
	
	private int findSTX(byte[] incommingData)
	{
		int counter = 0;
		for(byte currentByte : incommingData)
		{
			if(currentByte == SC_STX)
				return counter;
			counter++;
		}
		return -1;
	}
	
}
