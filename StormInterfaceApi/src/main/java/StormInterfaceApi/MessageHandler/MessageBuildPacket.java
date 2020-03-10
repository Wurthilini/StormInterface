package StormInterfaceApi.MessageHandler;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class MessageBuildPacket {
	
	private static final byte SC_STX = 0x02;
	private static final byte SC_ETX = 0x03;
	private static final byte USB_REPORT_LEN = 64;
	
	public boolean makePacket(int requestType, String dataTosend, byte[] _responseMessage)
	{
		//byte[] _responseMessage = new byte[USB_REPORT_LEN];
		boolean retbool = true;
		byte LRC = SC_STX;
		String messageID = null;
		String messageLength = null;
		byte usbLen;
		char[] convString = {(char) (requestType & 0xff)}; 
		messageID = binaryToASCIIHex(convString);
		int dataTosendLength = dataTosend == null ? 0 : dataTosend.length();
		char[] convStringSecond = {(char) (dataTosendLength & 0xff)};
		messageLength = binaryToASCIIHex(convStringSecond);
		//_responseMessage[0] = KEYMAT_REPORT_ID; -> is set in hid_write function
		_responseMessage[0] = USB_REPORT_LEN;
		_responseMessage[1] = SC_STX;
		_responseMessage[2] = (byte) messageID.toCharArray()[0];
		_responseMessage[3] = (byte) messageID.toCharArray()[1];
		_responseMessage[4] = (byte) messageLength.toCharArray()[0];
		_responseMessage[5] = (byte) messageLength.toCharArray()[1];
		int currentIndex = 6;
		for(char charIndataTosend : dataTosend.toCharArray())
		{
			_responseMessage[currentIndex] = (byte) charIndataTosend;
			currentIndex++;
		}
		_responseMessage[currentIndex] = SC_ETX;
		currentIndex++;
		//calculate LRC
		//skip header, start with SC_STX
		int LRCIndex = 1;
		LRC = _responseMessage[LRCIndex];
		while(LRCIndex < currentIndex)
		{
			LRC ^= _responseMessage[LRCIndex];
			LRCIndex++;
		}
		
		return retbool;
	}
	
	private String binaryToASCIIHex(char[] binaryData)
	{
		char binaryByte;
		char workNybble;
		String ASCIIData = "";
		for(char binaryChar : binaryData)
		{
			binaryByte = binaryChar;
			//Do upper nybble first
			workNybble = (char) (binaryByte>>4);
			if(workNybble<10)
				workNybble += '0';
			else
				workNybble += 'A'-10;
			ASCIIData += workNybble;
			//Do lower nybble now
			workNybble = (char) (binaryByte & 0x0f);
			if(workNybble <10)
				workNybble += '0';
			else
				workNybble += 'A' -10;
			ASCIIData += workNybble;
		}
		return ASCIIData;
	}
}
